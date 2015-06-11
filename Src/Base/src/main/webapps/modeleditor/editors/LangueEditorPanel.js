Ext.ux.CaqsLangueEditorPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    title:              getI18nResource('caqs.modeleditor.langueEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/ModelEditorRetrieveLangueInfos.do',
    saveUrl:            requestContextPath + '/ModelEditorSaveLangueInfos.do',
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

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsLangueEditorPanel.superclass.constructor.apply(this, arguments);
    },

    specificDataManagement: function(json) {
        var disable = (json.nbTexts && json.nbTexts>0);
        this.deleteBtn.setDisabled(disable);
        var msg = (disable) ? getI18nResource('caqs.modeleditor.langueEdition.noDeletionPossible') : null;
        this.setInfoMessage(msg);
    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsLangueEditorPanel.superclass.initComponent.apply(this, arguments);
    }

});

