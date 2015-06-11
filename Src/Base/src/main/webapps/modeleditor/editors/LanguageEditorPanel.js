Ext.ux.CaqsLanguageEditorPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    title:              getI18nResource('caqs.modeleditor.languageEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/ModelEditorRetrieveLanguageInfos.do',
    saveUrl:            requestContextPath + '/ModelEditorSaveLanguageInfos.do',
    fieldsDefinitions: [
                    {
                        name:           'id'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.id')
                        , maxLength:    32
                        , type:         'id'
                    }
                    , {
                        name: 'lib'
                        , maxLength:    32
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.lib')
                    }
                    , {
                        name: 'desc'
                        , maxLength:    128
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.desc')
                    }
    ],
    additionalAssociationsGridStore: undefined,
    additionalAssociationsGrid:     undefined,
    dialectFS:                       undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsLanguageEditorPanel.superclass.constructor.apply(this, arguments);
    },

    specificDataManagement: function(json) {
        var disable = (json.nbDialects && json.nbDialects>0);
        this.deleteBtn.setDisabled(disable);
        var msg = (disable) ? getI18nResource('caqs.modeleditor.languageEdition.noDeletionPossible') : null;
        this.setInfoMessage(msg);
        this.dialectFS.collapse();
    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsLanguageEditorPanel.superclass.initComponent.apply(this, arguments);
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
                url: requestContextPath+'/ModelEditorRetrieveLanguagesAssociatedDialects.do'
            }),
            reader: new Ext.data.JsonReader({
                root:           'dialects',
                totalProperty:  'totalCount',
                id:             'id',
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
        this.dialectFS = new Ext.form.FieldSet({
            title:      getI18nResource('caqs.modeleditor.languageEdition.associatedDialects.title'),
            style:      'margin-top: 10px;',
            collapsed:  true,
            collapsible:true,
            autoHeight: true,
            items:      this.additionalMetricsGrid
        })
        this.dialectFS.on('expand', function() {
            this.additionalMetricsGridStore.baseParams = {
                languageId:     this.elementId
            };
            this.additionalMetricsGridStore.load({
                params:{
                    start: 0,
                    limit: this.additionalMetricsGridPageSize
                }
            });
        }, this, {

        })
        this.formItems[this.formItems.length] = this.dialectFS;
    }

});

