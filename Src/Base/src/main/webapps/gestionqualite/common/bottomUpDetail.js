Ext.ux.CaqsBottomUpDetailWindow = function(config) {
    // call parent constructor
    Ext.ux.CaqsBottomUpDetailWindow.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor

Ext.extend(Ext.ux.CaqsBottomUpDetailWindow, Ext.Window, {
    modal: 		true,
    maximizable: 	true,
    resizable:		true,
    layout:		'fit',
    minimizable: 	false,
    width:		810,
    height:		330,
    shadow:		false,
    plain:		true,
    x:			20,
    y:			20,
    bottomUpDetailStore:undefined,
    gridDetail:         undefined,
    idEltToLoad:        undefined,
    tendanceFilterVar:  undefined,
    previousIdBline:    undefined,

    noteDetailRenderer: function(val, cell, record) {
        var retour = '<span class="';
        if(record.data.formattedNote=='') {
            record.data.formattedNote = "&nbsp;-&nbsp;";
        }
        if(record.data.note<3 && record.data.note>0) {
            if(record.data.hasJust==true) {
                retour += 'TopLineBad'+record.data.justStatus+'">';
                retour += (record.data.justNote>0) ? record.data.formattedJustNote : record.data.formattedNote;
                var title = '';
                if(record.data.justStatus=='DEMAND') {
                    title = '<img src="'+requestContextPath+'/images/info.gif" />&nbsp;&nbsp;'+getI18nResource("caqs.facteursynthese.demandecours");
                    retour += '<IMG id="just'+record.data.id+'" src="'+requestContextPath+'/images/encours.gif" />';
                } else if(record.data.justStatus == 'REJET') {
                    title = '<img src="'+requestContextPath+'/images/info.gif" />&nbsp;&nbsp;'+getI18nResource("caqs.facteursynthese.demanderejetee");
                    retour += '<IMG id="just'+record.data.id+'" src="'+requestContextPath+'/images/delete.gif" />';
                } else if(record.data.justStatus == 'VALID') {
                    title = '<img src="'+requestContextPath+'/images/info.gif" />&nbsp;&nbsp;'+getI18nResource("caqs.facteursynthese.accepte");
                    retour += '<IMG id="just'+record.data.id+'" src="'+requestContextPath+'/images/tick.gif" />';
                }
                if(record.data.justDesc!='') {
                    addTooltip( 'just'+record.data.id, title, record.data.justDesc);
                }
            } else {
                retour += 'TopLineBad">' + record.data.formattedNote;
            }
        } else {
            retour += 'TopLine">' + record.data.formattedNote;
        }
        retour += '</span>';
        return retour;
    },

    tendanceDetailRenderer: function(val, cell, record) {
        cell.attr = 'ext:qtip="'+record.data.tendanceMsg+'"';
        var retour = '<IMG id="imgTrend'+record.data.id+'" src="'+requestContextPath+'/images/note_'+record.data.tendanceLabel+'.gif" />';
        addTooltip('imgTrend'+record.data.id, '', record.data.tendanceMsg);
        return retour;
    },

    loadBottomUpDetail: function(title, idElt, tendanceFilterToLoad, previousIdBline) {
        this.setTitle(title);
        if(tendanceFilterToLoad != undefined) {
            this.tendanceFilterVar = tendanceFilterToLoad;
        }
        this.idEltToLoad = idElt;
        this.previousIdBline = previousIdBline;
        this.show();
    },
    
    initComponent : function(){
        this.bottomUpDetailStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/BottomUpDetailList.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 		'criteres',
                totalProperty: 	'totalCount',
                id: 		'id',
                fields: [
                    'id', 'lib', 'desc', 'compl', 'note', 'formattedNote', 'formattedJustNote',
                    'tendance', 'hasJust', 'tendanceMsg', 'tendanceLabel', 'justNote',
                    'critBOldNote', 'justDesc', 'justStatus', 'justId', 'objectifs'
                ]
            }),
            remoteSort: true
        });
        this.bottomUpDetailStore.setDefaultSort('note', 'asc');

        var myTemplate = new Ext.XTemplate(
            '<p><b>'+getI18nResource("caqs.facteursynthese.desc")+':</b> {desc}</p>',
            '<tpl if="compl != \'\'">',
            '<br /><p><b>'+getI18nResource("caqs.facteursynthese.compl")+':</b> {compl}</p>',
            '</tpl>'
        );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });
        this.gridDetail = new Ext.grid.GridPanel({
            store: 		this.bottomUpDetailStore,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:	false,
            loadMask: 		true,
            frame:		false,
            cm: new Ext.grid.ColumnModel([
                expander,
                {
                    header: 	getI18nResource("caqs.bottomupdetail.critere"),
                    width: 	400,
                    sortable: 	true,
                    dataIndex: 	'lib'
                },
                {
                    header: 	getI18nResource("caqs.bottomupdetail.note"),
                    width: 	80,
                    sortable: 	true,
                    renderer: 	this.noteDetailRenderer,
                    dataIndex: 	'note',
                    align:	'center'
                },
                {
                    header: 	getI18nResource("caqs.bottomupdetail.tendance"),
                    width: 	80,
                    sortable: 	true,
                    align:	'center',
                    dataIndex: 	'tendance',
                    tooltip:	getI18nResource("caqs.bottomupdetail.tendance.tooltip"),
                    renderer: 	this.tendanceDetailRenderer
                },
                {
                    header: 	getI18nResource("caqs.objectif"),
                    width: 	170,
                    sortable: 	true,
                    align:	'center',
                    dataIndex:	'objectifs'
                }
            ]),
            collapsible: false,
            plugins: expander,
            iconCls: 'icon-grid',
            listeners:      {
                'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                    this.gridDetail.getView().fitColumns();
                },
                scope:  this
            }
        });
        this.gridDetail.getView().on('refresh', function() {
            putTooltips();
        });
        var config = {
            items:      [
                this.gridDetail
            ]
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsBottomUpDetailWindow.superclass.initComponent.apply(this, arguments);
        this.on('beforeclose', this.onBeforeClose, this);
        this.on('show', this.refresh, this);
    },

    refresh: function() {
        this.bottomUpDetailStore.load({
            params: {
                id_elt:    	this.idEltToLoad,
                tendanceFilter:	this.tendanceFilterVar,
                idPreviousBline: this.previousIdBline
            }
        });
    },

    onBeforeClose: function(wnd) {
        wnd.hide();
        return false;
    }
});