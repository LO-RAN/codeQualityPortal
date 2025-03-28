Ext.ux.CaqsToolEditorPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    title:              getI18nResource('caqs.modeleditor.toolEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/ModelEditorRetrieveToolInfos.do',
    saveUrl:            requestContextPath + '/ModelEditorSaveToolInfos.do',
    fieldsDefinitions: [
                    {
                        name: 'id'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.id')
                        , maxLength:    40
                        , type: 'id'
                    }
                    , {
                        name:   'i18n'
                        , type: 'i18n'
                    }
    ],
    additionalMetricsGrid:      undefined,
    additionalMetricsGridStore: undefined,
    additionalMetricsGridPageSize: 10,
    metricsFS:  undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsToolEditorPanel.superclass.constructor.apply(this, arguments);
    },

    specificDataManagement: function(json) {
        var disable = (json.nbMetricsAssociated && json.nbMetricsAssociated>0)
            || (json.nbModelsAssociated && json.nbModelsAssociated>0);
        this.deleteBtn.setDisabled(disable);
        var msg = (disable) ? getI18nResource('caqs.modeleditor.toolEdition.noDeletionPossible') : null;
        this.setInfoMessage(msg);
        this.metricsFS.collapse();
    },

    putAdditionalParamsToSave: function(params) {
        //to be overloaded if necessary
    },

    createAdditionalFormItems: function() {
        var cm = new Ext.grid.ColumnModel([
            {
               header: 		getI18nResource("caqs.modeleditor.grid.id"),
               dataIndex: 	'id',
               id:              'id',
               sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.modeleditor.grid.lib"),
                dataIndex:	'lib',
                sortable: 	true
            }
            ,{
                header:         getI18nResource("caqs.modeleditor.grid.desc"),
                sortable:	true,
                dataIndex:	'desc'
            }
        ]);
        this.additionalMetricsGridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/ModelEditorRetrieveToolsAssociatedMetrics.do'
            }),
            reader: new Ext.data.JsonReader({
                root:           'metrics',
                totalProperty:  'totalCount',
                id:             'metricId',
                fields: [ 'id', 'lib', 'desc' ]
            }),
            remoteSort: true
        });
        this.additionalMetricsGridStore.setDefaultSort('id', 'asc');

        this.additionalMetricsGrid = new Ext.grid.EditorGridPanel({
            height:		260,
            header:		false,
            store: 		this.additionalMetricsGridStore,
            cm: 		cm,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableColumnResize:	true,
            enableHdMenu:	false,
            clicksToEdit:	1,
            trackMouseOver:	true,
            frame:		true,
            sm: 		new Ext.grid.RowSelectionModel({selectRow:Ext.emptyFn}),
            style:              'margin-top: 5px;',
            loadMask: 		true,
            autoExpandColumn:   'metricLib',
            viewConfig: {
                forceFit: true
            },
            bbar: new Ext.PagingToolbar({
                pageSize: 	this.additionalMetricsGridPageSize,
                store: 		this.additionalMetricsGridStore,
                displayInfo: 	true,
                displayMsg: 	'{0} - {1} / {2}'
            })
        });
        this.metricsFS = new Ext.form.FieldSet({
            title:      getI18nResource('caqs.modeleditor.toolEdition.associatedMetrics.title'),
            style:      'margin-top: 10px;',
            collapsed:  true,
            collapsible:true,
            autoHeight: true,
            items:      this.additionalMetricsGrid
        })
        this.metricsFS.on('expand', function() {
            this.additionalMetricsGridStore.baseParams = {
                toolId:     this.elementId
            };
            this.additionalMetricsGridStore.load({
                params:{
                    start: 0,
                    limit: this.additionalMetricsGridPageSize
                }
            });
        }, this, {
            
        })
        this.formItems[this.formItems.length] = this.metricsFS;
    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsToolEditorPanel.superclass.initComponent.apply(this, arguments);
    }

});

