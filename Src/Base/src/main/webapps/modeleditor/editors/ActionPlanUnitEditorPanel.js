Ext.ux.CaqsActionPlanUnitEditorPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    title:              getI18nResource('caqs.modeleditor.actionPlanUnitEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/ModelEditorRetrieveActionPlanUnitInfos.do',
    saveUrl:            requestContextPath + '/ModelEditorSaveActionPlanUnitInfos.do',
    fieldsDefinitions: [
                    {
                        name: 'id'
                        , maxLength:    64
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.id')
                        , columnNumber: 1
                        , type: 'id'
                    }
                    , {
                        name: 'nbApu'
                        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.nbApu')
                        , maxLength:    40
                        , columnNumber: 2
                        , allowDecimals : false
                        , type: 'number'
                    }
                    , {
                        name:   'i18n'
                        , type: 'i18n'
                    }
    ],
    additionalAssociationsGrid: undefined,
    additionalAssociationsGridStore: undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsActionPlanUnitEditorPanel.superclass.constructor.apply(this, arguments);
    },

    specificDataManagement: function(json) {
    },

    modelLibRenderer: function(v, attr, record) {
        return record.data.modelLib + ' ('+record.data.modelId+')';
    },

    createAdditionalFormItems: function() {

    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsActionPlanUnitEditorPanel.superclass.initComponent.apply(this, arguments);
    }

});

