/*
 * Close the opened graph if exists.
 * The openedGraphWindow variable is defined in the caqsPortal.js file.
 * Because of the iframe organisation, access to this variable is made using parent
 * accessor.
 */
function closeExistingWindow() {
    if (parent.parent.openedGraphWindow != undefined) {
        parent.parent.openedGraphWindow.close();
    }
}

Ext.ux.CaqsBottomUpSynthese = Ext.extend(Ext.Panel, {
    hideMode:               'offsets',
    layout:                 'anchor',
    border:                 false,
    gridStore:              undefined,
    grid:                   undefined,
    gridPageSize:           6,
    pieChartUrl:            requestContextPath + '/BottomUpRepartitionSelect.do?',
    numberFormatConfig:     '#.00',
    pieChartWidth:          300,
    isEA:                   false,
    filterComponent:        undefined,
    volumetryPanel:         undefined,
    allEltsButton:          undefined,
    allProbButton:          undefined,
    maxLine:                100,
    maxLines:               100,
    onlyProbMsg:            undefined,
    noErrorSpan:            undefined,
    imgCounter:             0,
    volumetryPanelSize:     490,
    columnsPanels:          undefined,
    autoWidth:              true,
    autoHeight:             true,
    //gridHeight:             200,
    typeChart:              'obj',
    mask:                   undefined,
    pictures:               undefined,
    maps:                   undefined,
    noErrorPanel:           undefined,
    switchChartLink:        undefined,
    isFiltered:             false,
    autoScroll:             true,
    style:                  'margin-left: 5px;',
    searchByNamePanel:      undefined,
    gridWidth:              400,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsBottomUpSynthese.superclass.constructor.apply(this, arguments);
    },

    pieChartRenderer: function(response) {
        if(response.responseText!='') {
            var json = Ext.util.JSON.decode(response.responseText);
            var vals = json.datas.split('!!!!');
            this.pictures[this.typeChart] = vals[0];
            this.maps[this.typeChart] = vals[1];
            this.changePicture(this.pictures[this.typeChart], this.maps[this.typeChart]);
        }
    },

    changePicture: function(filename, mapObj) {
        var img = document.getElementById('bottomUpSynthesePieChartImg');
        if(img!=undefined) {
            img.setAttribute("usemap", '#'+filename);
            var src = './displaychart?filename='+filename;
            img.src = src;
        }
        var map = document.getElementById('bottomUpSynthesePieChartImgMap');
        if(map!=undefined) {
            var newspan = document.createElement("span");
            newspan.setAttribute("id","bottomUpSynthesePieChartImgSimuleMapSpan");
            newspan.innerHTML = mapObj;
            var oldspan = document.getElementById("bottomUpSynthesePieChartImgSimuleMapSpan");
            if(oldspan!=undefined) {
                map.removeChild(oldspan);
            }
            map.appendChild(newspan);
        }
    },

    switchChart: function() {
        this.imgCounter++;
        var linkTxt = null;
        if((this.imgCounter%2)==0) {
            this.typeChart = 'obj';
            linkTxt = getI18nResource("caqs.bottomup.changeChart")+'&nbsp;'+getI18nResource("caqs.critere");
        } else {
            this.typeChart = 'crit';
            linkTxt = getI18nResource("caqs.bottomup.changeChart")+'&nbsp;'+getI18nResource("caqs.objectif");
        }
        this.switchChartLink.setText(linkTxt);
        if(this.pictures[this.typeChart] != null) {
            this.changePicture(this.pictures[this.typeChart], this.maps[this.typeChart]);
        }else {
            this.reloadPieChart();
        }
    },

    reloadPieChart: function() {
        var div = Ext.getCmp('bottomUpSyntheseColumn1');
        if(div != null) {
            var height = this.columnsPanels.getInnerHeight() - 40;
            var piecharturl = this.pieChartUrl + 'width='+(div.getInnerWidth()-20);
            piecharturl += "&height="+height;
            piecharturl += '&nb='+this.imgCounter;
            Ext.Ajax.request({
                url:        piecharturl,
                success:    this.pieChartRenderer,
                scope:      this,
                params: {
                    type:   this.typeChart
                }
            });
        }
    },

    fillVolumetry: function(json) {
        var firstFieldSet = new Ext.form.FieldSet ({
            title:      getI18nResource("caqs.bottomup.elements"),
            style:      "margin-top:5px;margin-left:5px;",
            anchor:     '98% 50%',
            layout:     'anchor',
            id:         'bottomUpSynthesisVolumetryFieldSet1',
            items : [
                {
                    layout:         'table',
                    border:         false,
                    anchor:         '100% 100%',
                    layoutConfig: {
                        columns:    3
                    },
                    defaults: {
                        border: false
                    },
                    items: [
                        {
                            html: 	getI18nResource("caqs.bottomup.nbrejet") + ' : ',
                            cls:	'normalFont',
                            width:	330
                        }
                        ,{
                            html:       json.volumetryNbRejets,
                            cls:        'normalFont',
                            width:      50,
                            style:      'text-align:right;'
                        }
                        ,{
                            html:       '&nbsp;',
                            width:      60
                        }
                        ,{
                            html:       getI18nResource("caqs.bottomup.reserve") + ' : ',
                            cls:        'normalFont',
                            width:	330
                        }
                        ,{
                            html:       json.volumetryNbReserve,
                            cls:        'normalFont',
                            width:      50,
                            style:      'text-align:right;'
                        }
                        ,{
                            html:       '&nbsp;',
                            width:      60
                        }
                        ,{
                            html:       getI18nResource("caqs.bottomup.accepte") + ' : ',
                            cls:        'normalFont',
                            width:	330
                        }
                        ,{
                            html:       json.volumetryNbAccepte,
                            cls:        'normalFont',
                            width:      50,
                            style:      'text-align:right;'
                        }
                        ,{
                            html:       '&nbsp;',
                            width:      60
                        }
                        ,{
                            html:       getI18nResource("caqs.bottomup.ameliore") + ' : ',
                            cls:        'normalFont',
                            width:	330
                        }
                        ,{
                            html:       '&nbsp;',
                            width:      50
                        }
                        ,{
                            html:       json.volumetryNbAmeliore+" %",
                            cls:        'normalFont',
                            width:      60,
                            style:      'width:60;text-align:right;'
                        }
                    ]
                 }
                ]
        });
        var secondFieldSet = new Ext.form.FieldSet ({
            title:      getI18nResource("caqs.bottomup.aameliorer"),
            style:      "margin-left:5px;",
            anchor:     '98% 40%',
            layout:     'anchor',
            id:         'bottomUpSynthesisVolumetryFieldSet2',
            items : [
                {
                    layout:         'table',
                    border:         false,
                    anchor:         '100% 100%',
                    layoutConfig: {
                        columns: 3
                    },
                    defaults: {
                        border: false
                    },
                    items: [
                        {
                            html: 	getI18nResource("caqs.bottomup.amelioration1") + ' : ',
                            cls:	'normalFont',
                            style:	'width:330;'
                        }
                        ,{
                            html: 	json.volumetryNbAmelioration1,
                            cls:	'normalFont',
                            style:	'width:50; text-align:right;'
                        }
                        ,{
                            html: 	json.volumetryPctAmelioration1+" %",
                            cls:	'normalFont',
                            style:	'width:60; text-align:right;'
                        }
                        ,{
                            html: 	getI18nResource("caqs.bottomup.amelioration2") + ' : ',
                            cls:	'normalFont',
                            style:	'width:330;'
                        }
                        ,{
                            html: 	json.volumetryNbAmelioration2,
                            cls:	'normalFont',
                            style:	'width:50; text-align:right;'
                        }
                        ,{
                            html: 	json.volumetryPctAmelioration2+" %",
                            cls:	'normalFont',
                            style:	'width:60; text-align:right;'
                        }
                        ,{
                            html: 	getI18nResource("caqs.bottomup.totalameliorations") + ' : ',
                            cls:	'normalFont',
                            style:	'width:330;'
                        }
                        ,{
                            html: 	json.volumetryTotalAmelioration,
                            cls:	'normalFont',
                            style:	'width:50; text-align:right;'
                        }
                        ,{
                            html: 	'&nbsp;',
                            cls:	'normalFont',
                            style:	'width:60; text-align:right;'
                        }
                    ]
                 }
                ]
        });
        var f = this.volumetryPanel.getComponent('bottomUpSynthesisVolumetryFieldSet1');
        if(f!=undefined) {
            this.volumetryPanel.remove(f);
        }
        f = this.volumetryPanel.getComponent('bottomUpSynthesisVolumetryFieldSet2');
        if(f!=undefined) {
            this.volumetryPanel.remove(f);
        }
        this.volumetryPanel.add(firstFieldSet);
        this.volumetryPanel.add(secondFieldSet);
        this.volumetryPanel.doLayout();
    },

    getGridStore: function(json) {
        var retour = ['eltDesc', 'eltId', 'eltLib', 'eltHasSource', 'eltIdBline',
            'note1', 'note2', 'note3', 'note4'];
        if(json && json.additionnalMetrics) {
            for(var i=0; i<json.additionnalMetrics.length; i++) {
                retour[retour.length] = 'idGridCol'+json.additionnalMetrics[i].id;
            }
        }
        var newGridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url:    requestContextPath + '/BottomUpSyntheseListAjax.do'
            }),

            reader: new Ext.data.JsonReader({
                    root:           'elements',
                    totalProperty:  'totalCount',
                    id:             'eltId',
                    fields:         retour
                }),
                remoteSort: true
            });
        newGridStore.on('load', function(s, records, option) {
            if(this.noErrorSpan!=undefined) {
                this.noErrorSpan.setVisible((records.length==0));
            }
            if(records.length > 0) {
                this.noErrorPanel.setVisible(false);
                this.grid.setVisible(true);
                if(records.length < this.maxLine) {
                    //au chargement, nous avons moins de 100 elements
                    this.grid.getBottomToolbar().setVisible(false);
                } else {
                    this.grid.getBottomToolbar().setVisible(true);
                }
            } else {
                this.noErrorPanel.setVisible(true);
                this.grid.setVisible(false);
            }
        }, this);
        return newGridStore;
    },

    getColumnHeaders: function(json) {
        var columnArray = [
            {
                id:         'elt',
                header:     getI18nResource("caqs.bottomup.nom"),
                sortable:   true,
                dataIndex:  'elt',
                width:      200,
                renderer:   this.eltDetailRenderer
            },
            {
                header:     getI18nResource("caqs.bottomup.nbde", 1),
                width:      70,
                sortable:   true,
                dataIndex:  'note1',
                align:      'right'
            },
            {
                header:    getI18nResource("caqs.bottomup.nbde", 2),
                width:      70,
                sortable:   true,
                align:      'right',
                dataIndex:  'note2'
            },
            {
                header:     getI18nResource("caqs.bottomup.nbde", 3),
                width:      70,
                sortable:   true,
                align:      'right',
                dataIndex:  'note3'
            },
            {
                header:     getI18nResource("caqs.bottomup.nbde", 4),
                width:      70,
                sortable:   true,
                align:      'right',
                dataIndex:  'note4'
            }
        ];
        //this.gridWidth = 480;
        if(json!=undefined && json.additionnalMetrics != undefined) {
            for(var i=0; i<json.additionnalMetrics.length; i++) {
                var elt = json.additionnalMetrics[i];
                columnArray[columnArray.length] = {
                    header: elt.lib,
                    width: 70,
                    align:'center',
                    dataIndex: 'idGridCol'+elt.id
                };
                //this.gridWidth += 70;
            }
        }
        return new Ext.grid.ColumnModel(columnArray);
    },

    reconfigureGrid: function(json) {
        this.gridStore = this.getGridStore(json);
        var newcolumnmodel = this.getColumnHeaders(json);
        this.grid.reconfigure(this.gridStore, newcolumnmodel);
        //this.grid.setWidth(this.gridWidth);
        this.grid.getView().fitColumns();
    },

    filterDatas: function(filter, teltFilter) {
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        this.isFiltered = true;
        Ext.Ajax.request({
            url: requestContextPath+'/BottomUpVolumetry.do',
            params: {
                filter:     filter,
                teltFilter: teltFilter
            },
            scope: this,
            success: function(response) {
                if(response!=undefined && response.responseText!=undefined) {
                    var json = Ext.util.JSON.decode(response.responseText);
                    if(json!=undefined) {
                        this.fillVolumetry(json);
                        this.reconfigureGrid(json);
                        this.loadGridData('ok', this.maxLine, filter, teltFilter);
                        this.allProbButton.setVisible(false);
                        if(this.isEA) {
                            this.reloadPieChart();
                        }
                        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
                    }
                }
            }
        });
        //Caqs.Portal.setCurrentScreen('bottomup_synthese');
    },

    initComponent : function(){
        Ext.ux.CaqsBottomUpSynthese.superclass.initComponent.call(this);

        this.pictures = new Array();
        this.maps = new Array();

        var items = new Array();

        var openImageInPopup = new Ext.ux.ComponentImage({
            id:     'bottomUpOpenPopupImg',
            style:  'cursor:pointer; width:16px; height:16px;',
            anchor: '100% 100%',
            src:    requestContextPath+'/images/application_double.gif'
        });
        openImageInPopup.on('click', function(c) {
            this.openChartPopup();
        }, this);

        if(this.isEA) {
            this.volumetryPanel = new Ext.Panel({
                title:          getI18nResource("caqs.bottomup.volumetrie"),
                height:         230,
                style:          'margin-top:5px;',
                layout:         'anchor'
            });

            this.searchByNamePanel = new Ext.ux.CaqsAjaxSearchByNamePanel({
                id:                     'bottomUpSyntheseSearchPanel',
                parentPanelElement:     this
            });
            this.switchChartLink = new Ext.LinkButton({
                text:       getI18nResource("caqs.bottomup.changeChart")+'&nbsp;'+getI18nResource("caqs.critere")
            });
            this.switchChartLink.on('click', this.switchChart, this);
            this.columnsPanels = new Ext.Panel({
                layout:         'column',
                anchor:         '95%',
                //height:         '60%',
                border:         false,
                layoutConfig:   {scrollOffset:25},
                items:      [
                    {//colonne 1
                        id:		'bottomUpSyntheseColumn0',
                        width:          this.volumetryPanelSize,
                        autoScroll:     true,
                        border:		false,
                        layout:         'row-fit',
                        items:          [
                                this.searchByNamePanel
                                , this.volumetryPanel
                        ]
                    },{//colonne 2
                        id:		'bottomUpSyntheseColumn1',
                        border:		false,
                        columnWidth:    .95,
                        style:          'text-align: center;',
                        items:  [
                            {
                                border: false,
                                html:   '<div id="bottomUpSynthesePieChartImgMap"></div>' +
                                    '<IMG id="bottomUpSynthesePieChartImg" />'
                            }
                            , this.switchChartLink
                        ],
                        listeners:      {
                                        'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                                            this.pictures['obj'] = null;
                                            this.pictures['crit'] = null;
                                            if(this.isFiltered) {
                                                this.pieChartWidth = adjWidth;
                                                this.reloadPieChart();
                                            }
                                        },
                                        scope:  this
                        }
                    },{//colonne 3
                        id:		'bottomUpSyntheseColumn2',
                        border:		false,
                        columnWidth:    .05,
                        layout:         'anchor',
                        items:          openImageInPopup
                    }
                ]
            });
        } else {
            this.volumetryPanel = new Ext.Panel({
                title:          getI18nResource("caqs.bottomup.volumetrie"),
                height:         230,
                style:          'margin-top:5px;',
                layout:         'anchor',
                anchor:         '95%'
            });

            this.columnsPanels = this.volumetryPanel;
        }

        items[items.length] = this.columnsPanels;

        this.allEltsButton = new Ext.Button({
            text:	getI18nResource("caqs.bottomup.displayall"),
            scope:      this,
            name:	'caqs.admin.delete',
            icon: 	requestContextPath + '/images/arrow_refresh.gif',
            cls: 	'x-btn-text-icon',
            handler: 	this.showAllElts
        });

        this.allProbButton = new Ext.Button({
            text:	getI18nResource("caqs.bottomup.displayfirst", this.maxLines),
            name:	'caqs.admin.delete',
            icon: 	requestContextPath + '/images/arrow_refresh.gif',
            cls: 	'x-btn-text-icon',
            scope:      this,
            handler: 	this.showProbElts
        });

        var title = getI18nResource("caqs.bottomup.labelrepartition");
        title += '&nbsp;&nbsp;&nbsp;' + '<IMG id="gridExcelIcon" style="cursor: pointer;"';
        title += ' onClick="PopupCentrer(\'ExportAsCsv.do?sessionAttributeName=syntheseBottomUp\')"';
        title += ' src="'+requestContextPath+'/images/csv-small.gif" border=0 />';
      	addTooltip('gridExcelIcon', '', getI18nResource("caqs.telecharger.csv"));

        this.onlyProbMsg = new Ext.Toolbar.TextItem({
            text:			'<span style="font-style: italic; font-weight: bold;">'+
                getI18nResource("caqs.critere.displayfirstmessage", 100)+'</span>'
        });

        this.gridStore = this.getGridStore(null);

        var gridAnchor = '95%';
        if(this.isEA) {
            gridAnchor = '95%';
        }
        this.grid = new Ext.grid.GridPanel({
            id:                 this.id+'bottomUpSynthesisElementsGrid',
            store:              this.gridStore,
            cm:                 this.getColumnHeaders(null),
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:	false,
            title:		title,
            frame:		false,
            loadMask: 		true,
            collapsible:        false,
            iconCls:            'icon-grid',
            autoExpandColumn:   'elt',
            height:             300,
            //width:              this.gridWidth,
            viewConfig: {
                    forceFit:   true
            },
            style:              'margin-right: 20px; margin-top: 10px;',
            anchor:             gridAnchor,
            bbar: [
                                this.allEltsButton,
                                this.allProbButton,
                                this.onlyProbMsg
            ]
        });
        items[items.length] = this.grid;

        this.grid.getView().on('refresh', function() {
            putTooltips();
        }, this);

        this.noErrorPanel = new Ext.Panel({
            border:     false,
            style:      'margin-top: 5px;',
            html:       '<span id="noErrorSpan" class="normalFont" style="font-weight: bold; clear: both;">'+
                            getI18nResource("caqs.bottomup.noerror") + '</span>'
        });
        items[items.length] = this.noErrorPanel;

        Ext.apply(this, Ext.apply(this.initialConfig, {
            items:  items
        }));
        Ext.ux.CaqsBottomUpSynthese.superclass.initComponent.apply(this, arguments);
    },

    openChartPopup: function() {
        PopupCentrer("RepartitionSelect.do?width=600&height=400&type="+this.typeChart,600, 400,"menubar=no,statusbar=no,scrollbars=no,resizable=yes");
    },

    refresh: function() {
        if(this.isEA) {
            this.searchByNamePanel.refresh();
        } else {
            this.filterDatas('%', 'ALL');
        }
        Caqs.Portal.setCurrentScreen('bottomup');
    },

    loadGridData: function(onlyProbElts, limit, filter, teltFilter) {
        this.gridStore.load( {
            params: {
                start:		0,
                limit:		limit,
                onlyProb:	onlyProbElts,
                filter:		filter,
                typeElt:	teltFilter,
                sort:		null
            }
        }); 
    },

    eltDetailRenderer: function(val, cell, record) {
        var retour = '';

        cell.attr = 'ext:qmaxForcedWidth=900 ext:qtip="<img src=\''+requestContextPath+'/images/info.gif\' style=\'vertical-align:middle\'>&nbsp;&nbsp;'
                + record.data.eltDesc +'"';

        addTooltip('bottomUpDetail'+record.data.eltId, '',
                "<img src='"+requestContextPath+"/images/info.gif' style='vertical-align:middle'>&nbsp;&nbsp;"
                    + record.data.eltDesc,
                    0, 'auto', 900);

        retour = '<A id="bottomUpDetail'+record.data.eltId
            +'" style="cursor:pointer;" onClick=\'Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().loadBottomUpDetail("'+record.data.eltDesc+'", "'+record.data.eltId+'")\' >';
        retour += record.data.eltLib + '</A>';

        if(record.data.eltHasSource == true) {
            addTooltip('imgLoupe'+record.data.eltId, '',
                    "<img src='"+requestContextPath+"/images/help.gif' />&nbsp;&nbsp;&nbsp;&nbsp;"+getI18nResource("caqs.critere.srcfileaccess"));
            retour += '<a href="RetrieveSourceFile.do?id_elt='+record.data.eltId+'" target="_blank">';
            retour += '<IMG id="imgLoupe'+record.data.eltId+'" src="'+requestContextPath+'/images/loupe.gif" border="0">';
            retour += '</a>';
            addTooltip('imgImpactAnalysis'+record.data.eltId, '',
                    "<img src='"+requestContextPath+"/images/help.gif' />&nbsp;&nbsp;&nbsp;&nbsp;"+getI18nResource("caqs.critere.callgraphaccess"));
            // Because the applet should exit when closing, the window cannot be replaced just opening a new one in the same Window.
            // Need to close the existing one, wait a little bit and open a new one.
            retour += '<a style="cursor:pointer;" onClick="closeExistingWindow();setTimeout(\'parent.parent.openedGraphWindow=window.open(\\\'impactgraph.jsp?id_elt='+record.data.eltId+'&id_bline='+record.data.eltIdBline+'&desc_elt='+record.data.eltDesc+'\\\', \\\'impactgraph\\\')\', 750);">';
            retour += '<IMG id="imgImpactAnalysis'+record.data.eltId+'" src="'+requestContextPath+'/images/graph.gif" border="0" />';
            retour += '</a>';
        }
        return retour;
    },

    showAllElts: function() {
        this.loadGridData('ko', -1, this.searchByNamePanel.getFilter(), this.searchByNamePanel.getTeltFilter());
        this.allEltsButton.setVisible(false);
        this.allProbButton.setVisible(true);
        this.onlyProbMsg.setVisible(false);
    },

    showProbElts: function() {
        this.loadGridData('ok', this.maxLine, this.searchByNamePanel.getFilter(), this.searchByNamePanel.getTeltFilter());
        this.allEltsButton.setVisible(true);
        this.allProbButton.setVisible(false);
        this.onlyProbMsg.setVisible(true);
    }
});
