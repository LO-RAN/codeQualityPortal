Ext.ux.CaqsGoalEditorPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    title:              getI18nResource('caqs.modeleditor.goalEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/ModelEditorRetrieveGoalInfos.do',
    saveUrl:            requestContextPath + '/ModelEditorSaveGoalInfos.do',
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
    additionalAssociationsGrid: undefined,
    additionalAssociationsGridStore: undefined,
    modelFS: undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsGoalEditorPanel.superclass.constructor.apply(this, arguments);
    },

    specificDataManagement: function(json) {
        var disable = (json.nbModelsAssociated && json.nbModelsAssociated>0);
        this.deleteBtn.setDisabled(disable);
        var msg = (disable) ? getI18nResource('caqs.modeleditor.goalEdition.noDeletionPossible') : null;
        this.setInfoMessage(msg);
        this.modelFS.collapse();
    },

    putAdditionalParamsToSave: function(params) {
        //to be overloaded if necessary
    },

    modelLibRenderer: function(v, attr, record) {
        return record.data.modelLib + ' ('+record.data.modelId+')';
    },

    createAdditionalFormItems: function() {
        var cm = new Ext.grid.ColumnModel([
            {
               header: 		getI18nResource("caqs.modeleditor.models.title"),
               dataIndex: 	'modelLib',
               id:              'modelLib',
               renderer:        this.modelLibRenderer,
               sortable: 	true
            }
        ]);
        this.additionalAssociationsGridStore = new Ext.data.GroupingStore({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveModelsForGoal.do'
            }),
            reader: new Ext.data.JsonReader({
                root:           'elements',
                totalProperty:  'totalCount',
                fields: [ 'modelId', 'modelLib' ]
            }),
            remoteSort:         false,
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
            viewConfig: {
                forceFit:       true
            }
        });

        this.modelFS = new Ext.form.FieldSet({
            title:      getI18nResource('caqs.modeleditor.goalEdition.associatedElements.title'),
            style:      'margin-top: 10px;',
            collapsed:  true,
            collapsible:true,
            autoHeight: true,
            items:      this.additionalAssociationsGrid
        })

        this.modelFS.on('expand', function() {
            this.additionalAssociationsGridStore.baseParams = {
                idGoal:     this.elementId
            };
            this.additionalAssociationsGridStore.load();
        }, this, {

        })

        this.formItems[this.formItems.length] = this.modelFS;
    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsGoalEditorPanel.superclass.initComponent.apply(this, arguments);
    }

});

