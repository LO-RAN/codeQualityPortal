Ext.ux.CaqsElementsEvolutionsWindow = function(config) {
    // call parent constructor
    Ext.ux.CaqsElementsEvolutionsWindow.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsElementsEvolutionsWindow, Ext.Window, {
    id:                 'caqsElementsEvolutionWnd',
    width:              700,
    height:             400,
    layout:             'fit',
    modal:              true,
    maximizable:        true,
    resizable:          true,
    minimizable:        false,
    plain:              true,
    shadow:             false,
    grid:               undefined,
    gridStore:          undefined,
    gridPageSize:       9,
    searchPanel:        undefined,
    pieChart:           undefined,
    idElt:              undefined,
    currentIdBline:     undefined,
    previousIdBline:    undefined,
    target:             undefined,
    pieChartWidth:      300,
    searchByNamePanel:  undefined,
    pieChartFilename:   undefined,
    dataLoaded:         false,

    renderName: function(val, p, record) {
	p.attr = "ext:qmaxForcedWidth=900 ext:qtip='<img src=\""+requestContextPath+"/images/info.gif\" />&nbsp;&nbsp;" + record.data.desc +"'";
	addTooltip('tooltipDesc'+record.data.id, '',
			'<img src="'+requestContextPath+'/images/info.gif" />&nbsp;&nbsp;'+record.data.desc,
			0, 'auto', 900);

	var result = "<A id='tooltipDesc"+record.data.id+"' style='cursor:pointer;' "+
	     " onClick=\'Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().loadBottomUpDetail(\""+record.data.desc+"\", \""+record.data.id+"\", \""+record.data.target+"\", \""+this.grid.caqsContainerElement.previousIdBline+"\");\' >";
	result += record.data.lib;
	result += '</A>';
	result += '&nbsp;<a href="RetrieveSourceFile.do?id_elt=' + record.data.id
		 + '&tendance='+record.data.target+'" target="_blank">'
		 + '<IMG id=\"retrieveSource'+record.data.id+'" src="'+requestContextPath+'/images/loupe.gif" border="0">'
		 + '</a>';
	addTooltip('retrieveSource'+record.data.id, '',
			'<img src="'+requestContextPath+'/images/help.gif" />&nbsp;&nbsp;'+getI18nResource("caqs.critere.srcfileaccess"));
	return result;
    },

    generatePieChart: function() {
        Ext.Ajax.request({
            url: requestContextPath+'/ElementsEvolutionsPieChart.do',
            params: {
                'idElt':            this.idElt,
                'idBline':          this.currentIdBline,
                'idPreviousBline':  this.previousIdBline,
                'width':            this.pieChartWidth,
                'filter':           this.searchByNamePanel.getFilter(),
                'typeElt':          this.searchByNamePanel.getTeltFilter(),
                'target':           this.target
            },
            scope:      this,
            success: function(response) {
                if(response!=null && response.responseText!='') {
                    var json = Ext.util.JSON.decode(response.responseText);
                    this.changePicture(json.piechartFileName, json.piechartImageMap);
                }
            }
        });
    },

    openChartPopup: function() {
        PopupCentrer("EvolutionRepartitionChartSelect.do?width=600&height=400&target="+this.target+"&idElt="+this.idElt
            +"&idBline="+this.currentIdBline+"&idPreviousBline="+this.previousIdBline,600, 400,"menubar=no,statusbar=no,scrollbars=no,resizable=yes");
    },

    initComponent : function(){
        this.searchByNamePanel = new Ext.ux.CaqsAjaxSearchByNamePanel({
            id:                     'evolutionsElementsEvolutionsWndSearchPanel',
            style:                  'margin-bottom: 5px;',
            parentPanelElement:     this
        });
        
        this.gridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveElementsEvolutionGridList.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 'elements',
                totalProperty: 'totalCount',
                piechartFileName: 'piechartFileName',
                piechartImageMap: 'piechartImageMap',
                refreshPieChart: 'refreshPieChart',
                id: 'id',
                fields: [ 'id', 'lib', 'desc', 'target' ]
            }),
            remoteSort: true
        });
        this.gridStore.setDefaultSort('lib', 'asc');
        this.gridStore.on('load', function(store, records, options) {
            this.dataLoaded = true;
            this.generatePieChart();
        }, this);
        this.gridStore.on('beforeload', function(store, options) {
            store.baseParams.target = this.target;
            store.baseParams.idElt = this.idElt;
            store.baseParams.idBline = this.currentIdBline;
            store.baseParams.filter = this.searchByNamePanel.getFilter();
            store.baseParams.typeElt = this.searchByNamePanel.getTeltFilter();
            store.baseParams.idPreviousBline = this.previousIdBline;
        }, this);

        this.grid = new Ext.grid.GridPanel({
            height:		300,
            store: 		this.gridStore,
            cm: 		new Ext.grid.ColumnModel([{
               id: 		'lib',
               header: 		getI18nResource('caqs.bottomup.nom'),
               dataIndex:	'lib',
               sortable:	true,
               width:           200,
               renderer: 	this.renderName
            }]),
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableColumnResize:	false,
            enableHdMenu:	false,
            trackMouseOver:	true,
            sm:                 new Ext.grid.RowSelectionModel({selectRow:Ext.emptyFn}),
            loadMask:           true,
            bbar: new Ext.PagingToolbar({
                pageSize: 	this.gridPageSize,
                store: 		this.gridStore,
                displayInfo: 	true,
                displayMsg: 	'{0} - {1} / {2}'
            }),
            autoExpandColumn:   'lib',
            caqsContainerElement: this,
            viewConfig: {
                    forceFit:   true
            }/*,
            listeners:      {
                'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                    //this.grid.getView().fitColumns();
                    this.grid.getView().refresh();
                },
                scope:  this
            }*/
        });
        this.grid.getView().on('refresh', function() {
            putTooltips();
	});

        var openImageInPopup = new Ext.ux.ComponentImage({
            id:     'bottomUpOpenPopupImg',
            style:  'cursor:pointer; width:16px; height:16px;',
            anchor: '100% 100%',
            src:    requestContextPath+'/images/application_double.gif'
        });
        openImageInPopup.on('click', function(c) {
            this.openChartPopup();
        }, this);

        var containerPanel = new Ext.Panel({
            layout: 'column',
            border: false,
            defaults: {
                border: false
            },
            items: [
                {
                    columnWidth:    0.98
                    , layout:       'fit'
                    , items:        this.searchByNamePanel
                }
                , {
                    columnWidth:    0.47
                    , layout:       'fit'
                    , items:        this.grid
                }, {//colonne 1
                    id:                 'caqsElementsEvoutionsWndPieChartColumn'
                    , columnWidth:      .47
                    , border:           false
                    , layout:           'fit'
                    , items:  [
                    {
                        border:     false,
                        autoHeight: true,
                        html:       '<div id="caqsElementsEvoutionsWndPieChartMap"></div><IMG id="caqsElementsEvoutionsWndPieChartImg" />'
                    }
                    ],
                    listeners:      {
                        'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                            if(this.dataLoaded) {
                                this.pieChartWidth = adjWidth;
                                this.generatePieChart();
                            }
                        },
                        scope:  this
                    }
                },{//colonne 3
                    id:             'caqsElementsEvoutionsWndPieMaximizeColumn',
                    border:         false,
                    columnWidth:    .05,
                    layout:         'anchor',
                    items:          openImageInPopup
                }
            ]
        })

        var config = {
            items:      containerPanel
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsElementsEvolutionsWindow.superclass.initComponent.apply(this, arguments);
    },

    updatePicture: function() {
        var img = document.getElementById('caqsElementsEvoutionsWndPieChartImg');
        if(img!=undefined) {
            img.setAttribute("usemap", '#'+this.pieChartFilename);
            var src = './displaychart?filename='+this.pieChartFilename;
            img.src = src;
        }
    },
    
    changePicture: function(filename, mapObj) {
        this.pieChartFilename = filename;
        this.updatePicture();
        var map = document.getElementById('caqsElementsEvoutionsWndPieChartMap');
        if(map!=undefined) {
            var newspan = document.createElement("span");
            newspan.setAttribute("id","caqsElementsEvoutionsWndPieChartMapSpan");
            newspan.innerHTML = mapObj;
            var oldspan = document.getElementById("caqsElementsEvoutionsWndPieChartMapSpan");
            if(oldspan!=undefined) {
                map.removeChild(oldspan);
            }
            map.appendChild(newspan);
        }
    },

    filterDatas: function(filter, teltFilter) {
        this.gridStore.load({
            params:{
                start:0,
                limit: this.gridPageSize,
                fromSession: false
            }
        });
    },

    refresh: function(idElt, idBline, previousIdBline, target) {
        this.idElt = idElt;
        this.currentIdBline = idBline;
        this.previousIdBline = previousIdBline;
        this.target = target;
        this.searchByNamePanel.refresh();
        this.show();
    }
});