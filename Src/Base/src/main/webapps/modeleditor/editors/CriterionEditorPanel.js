Ext.ux.CaqsCriterionEditorPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    title:              getI18nResource('caqs.modeleditor.criterionEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/ModelEditorRetrieveCriterionInfos.do',
    saveUrl:            requestContextPath + '/ModelEditorSaveCriterionInfos.do',
    fieldsDefinitions: [
                    {
                        name: 'id'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.id')
                        , maxLength:    40
                        , columnNumber: 1
                        , type: 'id'
                    }
                    , {
                        columnNumber: 2
                        , type: 'blank'
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
    additionalAssociationsGrid:    undefined,
    additionalAssociationsGridStore:undefined,
    modelsFS:           undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsCriterionEditorPanel.superclass.constructor.apply(this, arguments);
    },

    specificDataManagement: function(json) {
        var disable = (json.nbModelsAssociated && json.nbModelsAssociated>0);
        this.deleteBtn.setDisabled(disable);
        var msg = (disable) ? getI18nResource('caqs.modeleditor.criterionEdition.noDeletionPossible') : null;
        this.setInfoMessage(msg);
        this.modelsFS.collapse();
    },

    putAdditionalParamsToSave: function(params) {
        //to be overloaded if necessary
    },

    modelAssociatedRenderer: function(v, p, record) {
        return record.data.modelLib += ' ('+record.data.modelId+')';
    },

    createAdditionalFormItems: function() {
        var cm = new Ext.grid.ColumnModel([
            {
               header: 		getI18nResource("caqs.modeleditor.models.title"),
               dataIndex: 	'modelLib',
               id:              'modelLib',
               groupRenderer:   this.modelAssociatedRenderer,
               sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.modeleditor.goals.title"),
                dataIndex:	'goalLib',
                id:             'goalLib',
                //groupRenderer:   this.criterionGroupRenderer,
                sortable: 	true
            }
        ]);
        this.additionalAssociationsGridStore = new Ext.data.GroupingStore({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveModelsGoalsForCriterion.do'
            }),
            reader: new Ext.data.JsonReader({
                root:           'elements',
                totalProperty:  'totalCount',
                fields: [ 'modelId', 'modelLib', 'goalId', 'goalLib' ]
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
        this.modelsFS = new Ext.form.FieldSet({
            title:      getI18nResource('caqs.modeleditor.criterionEdition.associatedElements.title'),
            style:      'margin-top: 10px;',
            collapsed:  true,
            collapsible:true,
            autoHeight: true,
            items:      this.additionalAssociationsGrid
        })
        this.modelsFS.on('expand', function() {
            this.additionalAssociationsGridStore.baseParams = {
                idCrit:     this.elementId
            };
            this.additionalAssociationsGridStore.load();
        }, this, {

        })
        this.formItems[this.formItems.length] = this.modelsFS;
    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsCriterionEditorPanel.superclass.initComponent.apply(this, arguments);
    }

});

