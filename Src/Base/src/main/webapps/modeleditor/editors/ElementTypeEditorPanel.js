Ext.ux.CaqsElementTypeEditorPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    title:              getI18nResource('caqs.modeleditor.elementTypeEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/ModelEditorRetrieveElementTypeInfos.do',
    saveUrl:            requestContextPath + '/ModelEditorSaveElementTypeInfos.do',
    fieldsDefinitions: [
                    {
                        name: 'id'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.id')
                        , maxLength:    32
                        , columnNumber: 1
                        , type: 'id'
                    }
                    , {
                        columnNumber: 2
                        , type: 'blank'
                    }
                    , {
                        name:   'hasSource'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.hasSource')
                        , columnNumber: 1
                        , type: 'checkbox'
                    }
                    , {
                        name:   'isFile'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.isFile')
                        , columnNumber: 2
                        , type: 'checkbox'
                    }
                    , {
                        name:   'dinstallation'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.dinst')
                        , columnNumber: 1
                        , type: 'staticfield'
                    }
                    , {
                        name:   'dmaj'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.dmaj')
                        , columnNumber: 2
                        , type: 'staticfield'
                    }
                    , {
                        name:   'dapplication'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.dapplication')
                        , columnNumber: 1
                        , type: 'date'
                    }
                    , {
                        name:   'dperemption'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.dperemption')
                        , columnNumber: 2
                        , type: 'date'
                    }
                    , {
                        name:   'i18n'
                        , type: 'i18n'
                    }
    ],
    additionalAssociationsGridStore:    undefined,
    additionalAssociationsGrid:         undefined,
    metricsFS:          undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsElementTypeEditorPanel.superclass.constructor.apply(this, arguments);
    },

    specificDataManagement: function(json) {
        var disable = (json.nbCriterionsAssociated && json.nbCriterionsAssociated>0);
        this.deleteBtn.setDisabled(disable);
        var msg = (disable) ? getI18nResource('caqs.modeleditor.elementTypeEdition.noDeletionPossible') : null;
        this.setInfoMessage(msg);
        this.metricsFS.collapse();
    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsElementTypeEditorPanel.superclass.initComponent.apply(this, arguments);
    },

    createAdditionalFormItems: function() {
        var cm = new Ext.grid.ColumnModel([
            {
               header: 		getI18nResource("caqs.modeleditor.models.title"),
               dataIndex: 	'modelLib',
               id:              'modelLib',
               sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.modeleditor.criterions.title"),
                dataIndex:	'criterionLib',
                id:             'criterionLib',
                //groupRenderer:   this.criterionGroupRenderer,
                sortable: 	true
            }
        ]);
        this.additionalAssociationsGridStore = new Ext.data.GroupingStore({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveModelsCriterionsForElementType.do'
            }),
            reader: new Ext.data.JsonReader({
                root:           'elements',
                totalProperty:  'totalCount',
                fields: [ 'modelId', 'modelLib', 'criterionId', 'criterionLib' ]
            }),
            remoteSort: false,
            groupField: 'modelLib',
            sortInfo:           {
                field:          'modelLib',
                direction:      'ASC'
            }
        });

        this.additionalAssociationsGrid = new Ext.grid.GridPanel({
            height:		260,
            header:		false,
            store: 		this.additionalAssociationsGridStore,
            cm: 		cm,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableColumnResize:	true,
            enableHdMenu:	true,
            trackMouseOver:	true,
            frame:		true,
            sm: 		new Ext.grid.RowSelectionModel({selectRow:Ext.emptyFn}),
            style:              'margin-top: 5px;',
            loadMask: 		true,
            autoExpandColumn:   'modelLib',
            view:               new Ext.grid.GroupingView({
                                    forceFit:		true,
				    groupTextTpl:
                                        '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "'+getI18nResource('caqs.associations')+'" : "'+getI18nResource('caqs.association')+'"]})'
				})
        });
        this.metricsFS = new Ext.form.FieldSet({
            title:      getI18nResource('caqs.modeleditor.elementTypeEdition.associatedElements.title'),
            style:      'margin-top: 10px;',
            collapsed:  true,
            collapsible:true,
            autoHeight: true,
            items:      this.additionalAssociationsGrid
        })
        this.metricsFS.on('expand', function() {
            this.additionalAssociationsGridStore.baseParams = {
                idTelt:     this.elementId
            };
            this.additionalAssociationsGridStore.load();
        }, this, {

        })
        this.formItems[this.formItems.length] = this.metricsFS;
    }

});

