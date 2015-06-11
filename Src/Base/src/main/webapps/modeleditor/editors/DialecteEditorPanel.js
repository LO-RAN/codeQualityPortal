Ext.ux.CaqsDialecteEditorPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    title:              getI18nResource('caqs.modeleditor.dialecteEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/ModelEditorRetrieveDialectInfos.do',
    saveUrl:            requestContextPath + '/ModelEditorSaveDialectInfos.do',
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
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.lib')
                        , maxLength:    32
                        , name: 'lib'
                    }
                    , {
                        columnNumber: 1
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.desc')
                        , maxLength:    128
                        , name: 'desc'
                    }
                    , {
                        name: 'idLangage'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.langage')
                        , comboBoxStoreUrl: requestContextPath + '/ModelEditorLanguagesCB.do'
                        , type: 'combobox'
                        , columnNumber: 1
                    }
    ],
    additionalAssociationsGridStore: undefined,
    additionalAssociationsGrid:     undefined,
    eaFS:                       undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsDialecteEditorPanel.superclass.constructor.apply(this, arguments);
    },

    specificDataManagement: function(json) {
        var disable = (json.nbEAs && json.nbEAs>0);
        this.deleteBtn.setDisabled(disable);
        var msg = (disable) ? getI18nResource('caqs.modeleditor.dialecteEdition.noDeletionPossible') : null;
        this.setInfoMessage(msg);
        this.eaFS.collapse();
    },

    putAdditionalParamsToSave: function(params) {
        //to be overloaded if necessary
    },

    createAdditionalFormItems: function() {
        //tableau d'affichage des eas utilisant ce modele
        var cm = new Ext.grid.ColumnModel([
            {
               header: 		getI18nResource("caqs.EA"),
               dataIndex: 	'eaLib',
               id:              'eaLib',
               sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.projet"),
                dataIndex:	'projectLib',
                id:             'projectLib',
                //groupRenderer:   this.criterionGroupRenderer,
                sortable: 	true
            }
        ]);
        this.additionalAssociationsGridStore = new Ext.data.GroupingStore({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveEAsForDialecte.do'
            }),
            reader: new Ext.data.JsonReader({
                root:           'elements',
                totalProperty:  'totalCount',
                fields: [ 'eaId', 'eaLib', 'projectId', 'projectLib' ]
            }),
            remoteSort: false,
            groupField: 'projectLib',
            sortInfo:           {
                field:          'eaLib',
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
            autoExpandColumn:   'eaLib',
            view:               new Ext.grid.GroupingView({
                                    forceFit:		true,
				    groupTextTpl:
                                        '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "'+getI18nResource('caqs.EAs')+'" : "'+getI18nResource('caqs.EA')+'"]})'
				})
        });
        this.eaFS = new Ext.form.FieldSet({
            title:      getI18nResource('caqs.modeleditor.dialecteEdition.associatedElements.title'),
            style:      'margin-top: 10px;',
            collapsed:  true,
            collapsible:true,
            autoHeight: true,
            items:      this.additionalAssociationsGrid
        })
        this.eaFS.on('expand', function() {
            this.additionalAssociationsGridStore.baseParams = {
                idDialecte:     this.elementId
            };
            this.additionalAssociationsGridStore.load();
        }, this, {

        });
        this.formItems[this.formItems.length] = this.eaFS;
    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsDialecteEditorPanel.superclass.initComponent.apply(this, arguments);
    }

});

