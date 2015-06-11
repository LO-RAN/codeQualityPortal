Ext.ux.CaqsModelEditorAddMetricToCriterionWindow = function(config) {
    Ext.ux.CaqsModelEditorAddMetricToCriterionWindow.superclass.constructor.call(this, config);
};


Ext.extend(Ext.ux.CaqsModelEditorAddMetricToCriterionWindow, Ext.Window, {
    layout:             'fit',
    maximizable:        true,
    modelId:            undefined,
    critId:             undefined,
    bodyBorder:         false,
    addGoalBtn:         undefined,
    closeBtn:           undefined,
    parentPanel:        undefined,
    grid:               undefined,
    gridStore:          undefined,
    toolComboStore:     undefined,
    toolCombo:          undefined,
    height:             500,
    width:              700,
    modal:              true,
    gridPageSize:       14,

    setId: function(idUsa, idCrit) {
        this.modelId = idUsa;
        this.critId = idCrit;
    },

    addMetric: function(idMet) {
        Ext.Ajax.request({
            url:	requestContextPath + '/ManageRegle.do',
            success:	this.refreshGridDatas,
            params:     {
                critId:     this.critId,
                modelId:    this.modelId,
                idMet:      idMet,
                action:     'save'
            },
            scope:      this
        });
    },

    refreshMetricsGrid: function() {
        this.gridStore.load({
            params:     {
                start:                      0,
                limit:                      this.gridPageSize
            }
        });
    },

    onchangeToolCB: function(field, record, index) {
        //this.toolId = record.data.id;
        this.refreshMetricsGrid();
    },

    initComponent : function(){
        this.toolComboStore = new Ext.ux.CaqsJsonStore({
            url: requestContextPath + '/ModelEditorToolsCB.do',
            fields: ['id', 'lib']
        });
        this.toolComboStore.on('load', function(store, records, options) {
            if(records.length>0) {
                var cb = Ext.getCmp(this.id + 'addMetricToCriterionWndToolCombo');
                if(cb) {
                    cb.setValue(records[0].data.id);
                }
                //this.toolId = records[0].data.id;
                this.grid.setHeaderFilter('toolId', records[0].data.id);
                this.gridStore.load({
                    params:     {
                        start:      0,
                        limit:      this.gridPageSize/*,
                        toolId:     records[0].data.id*/
                    }
                });
            }            
        }, this);
        this.toolComboStore.on('beforeload', function(store, options) {
            store.baseParams.addAllElement = false;
            store.baseParams.modelId = this.modelId;
        }, this);

        this.toolCombo = {
            xtype:              'combo',
            fieldLabel: 	getI18nResource('caqs.modeleditor.modelEdition.model.addMetricWnd.selectTool'),
            name:       	'addMetricToCriterionWndToolCombo',
            id:         	this.id + 'addMetricToCriterionWndToolCombo',
            store:      	this.toolComboStore,
            displayField:	'lib',
            valueField: 	'id',
            hiddenName: 	'toolId',
            width:		220,
            editable:		false,
            forceSelection:	true,
            triggerAction:	'all',
            autocomplete:	false,
            filterName:         'toolId'
        };
        //this.toolCombo.on('select', this.onchangeToolCB, this);

        this.gridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveMetricsRegardingCriterion.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 		'elements',
                totalProperty: 	'totalCount',
                fields: [
                    'id', 'lib', 'desc', 'compl', 'tool'
                ]
            }),
            remoteSort: true
        });
        this.gridStore.setDefaultSort('id', 'asc');
        this.gridStore.on('beforeload', function(store, options) {
            store.baseParams.modelId = this.modelId;
            store.baseParams.critId = this.critId;
            //store.baseParams.toolId = this.toolId;
            store.baseParams.associated = false;
        }, this);

        var myTemplate = new Ext.XTemplate(
               '<p><b>'+getI18nResource('caqs.modeleditor.grid.desc')+':</b> {desc}</p>',
                '<tpl if="compl != \'\'">',
                '<br /><p><b>'+getI18nResource('caqs.modeleditor.grid.compl')+':</b> {compl}</p>',
                '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });


        var action = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:[
                {
                    qtip:      getI18nResource('caqs.modeleditor.modelEdition.metricAdd.add'),
                    iconCls:   'icon-addgoal'
                }
            ]
        });

        action.on({
            scope:  this,
            action: function(grid, record, action, row, col) {
                this.addMetric(record.data.id);
            }
        }, this);

        // create the Grid
        this.grid = new Ext.grid.GridPanel({
            store:              this.gridStore,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:       false,
            frame:		false,
            loadMask:           true,
            style:              'margin-left: 5px;',
            cm: new Ext.grid.ColumnModel([
                expander
                , {
                    id:         'id',
                    header:     getI18nResource('caqs.modeleditor.grid.id'),
                    width:      50,
                    sortable:   true,
                    dataIndex:  'id',
                    filter: {xtype: 'textfield', filterName: 'filterId'}
                }
                , {
                    id:         'lib',
                    header:     getI18nResource('caqs.modeleditor.grid.lib'),
                    sortable:   true,
                    dataIndex:  'lib',
                    filter: {xtype: 'textfield', filterName: 'filterLib'}
                }
                , {
                    id:         'tool',
                    header:     getI18nResource('caqs.modeleditor.grid.tool'),
                    sortable:   false,
                    dataIndex:  'tool',
                    filter:     this.toolCombo
                }
                , action
                ]),
            collapsible: 	false,
            iconCls: 		'icon-grid',
            autoExpandColumn:   'lib',
            viewConfig: {
                forceFit: true
            },
            plugins: 		[
                expander
                , action
                , new Ext.ux.grid.GridHeaderFilters()
            ],
            bbar:               new Ext.PagingToolbar({
                                    pageSize: 		this.gridPageSize,
                                    store: 		this.gridStore,
                                    displayInfo: 	true,
                                    displayMsg: 	'{0} - {1} / {2}'
                                })
        });

        var btn = new Ext.Button({
            text:	getI18nResource("caqs.close"),
            icon: 	requestContextPath + '/images/cross.gif',
            cls: 	'x-btn-text-icon',
            width:      80,
            handler:    this.close,
            scope:      this
        });
        this.on('beforeclose', function(wnd) {
            this.hide();
            return false;
        });
        this.on('show', function(wnd) {
            this.toolComboStore.load();
        });

        var config = {
            title:          getI18nResource('caqs.modeleditor.modelEdition.model.addMetricWnd.title'),
            items:          [
                this.grid
            ],
            buttons: [
                btn
            ]
        }
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelEditorAddMetricToCriterionWindow.superclass.initComponent.apply(this, arguments);
    },

    refreshGridDatas: function() {
        this.parentPanel.refreshGridDatas();
        this.refreshMetricsGrid();
    }
});