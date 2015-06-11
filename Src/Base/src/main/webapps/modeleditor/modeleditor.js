Ext.ux.CaqsQualityModelsDataEditorPanel = Ext.extend(Ext.TabPanel, {
    id:                 'caqsModelEditor',
    activeTab:		0,
    border:             false,
    hideMode:           'offsets',
    titlecollapse:      true,
    hideCollapseTool:   true,
    layoutOnTabChange:	true,
    cls:                'mask-below-menu',

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsQualityModelsDataEditorPanel.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        Ext.ux.CaqsQualityModelsDataEditorPanel.superclass.initComponent.call(this);
        var config = {
            items:
                [
                    {
                        id:     'models',
                        title:  getI18nResource('caqs.modeleditor.models.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsModelPanel()
                    }
                    ,{
                        id:     'goals',
                        title:  getI18nResource('caqs.modeleditor.goals.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsGoalPanel()
                    }
                    ,{
                        id:     'criterions',
                        title:  getI18nResource('caqs.modeleditor.criterions.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsCriterePanel()
                    }
                    ,{
                        id:     'metrics',
                        title:  getI18nResource('caqs.modeleditor.metrics.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsMetriquePanel()
                    }
                    ,{
                        id:     'tools',
                        title:  getI18nResource('caqs.modeleditor.tools.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsToolPanel()
                    }
                    ,{
                        id:     'elementTypes',
                        title:  getI18nResource('caqs.modeleditor.elementTypes.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsElementTypePanel()
                    }
                    ,{
                        id:     'langues',
                        title:  getI18nResource('caqs.modeleditor.langues.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsLanguePanel()
                    }
                    ,{
                        id:     'apUnits',
                        title:  getI18nResource('caqs.modeleditor.apUnits.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsActionPlanUnitPanel()
                    }
                    ,{
                        id:     'dialects',
                        title:  getI18nResource('caqs.modeleditor.dialects.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsDialectePanel()
                    }
                    ,{
                        id:     'languages',
                        title:  getI18nResource('caqs.modeleditor.languages.title'),
                        layout: 'fit',
                        items:  new Ext.ux.CaqsLanguagePanel()
                    }
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsQualityModelsDataEditorPanel.superclass.initComponent.call(this);
        /*
        Ext.EventManager.onWindowResize(function(){
            
        }, this);*/
    },
    
    lazyLoad: function() {
        Caqs.Portal.setCurrentScreen('modeleditor');
    },

    onRender : function(cmpt){
        Ext.ux.CaqsQualityModelsDataEditorPanel.superclass.onRender.call(this, cmpt);
        
    }

});

