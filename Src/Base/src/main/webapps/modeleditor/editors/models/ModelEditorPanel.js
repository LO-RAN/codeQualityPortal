Ext.ux.CaqsModelEditorPanel = Ext.extend(Ext.Panel, {
    layout:             'border',
    border:             false,
    navigator:          undefined,
    centerPanel:        undefined,
    hideMode:           'offsets',
    titlecollapse:      true,
    hideCollapseTool:   true,
    panels:             undefined,
    editedModelId:      undefined,
    infoPanel:          undefined,
    goBackButton:       undefined,

    createPanels: function() {
        this.panels = [
            new Ext.ux.CaqsModelEditorDefinitionPanel({
                parentSearchScreen:     this.parentSearchScreen
            })
            , new Ext.ux.CaqsModelEditorAssociatedToolsGridPanel()
            , new Ext.ux.CaqsModelEditorGoalsCriterionsPanel()
        ];
    },

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsModelEditorPanel.superclass.constructor.apply(this, arguments);
    },

    specificDataManagement: function(json) {

    },

    putAdditionalParamsToSave: function(params) {
    //to be overloaded if necessary
    },

    createAdditionalFormItems: function() {

    },

    goBackFn: function() {
        this.parentSearchScreen.finishEdition();
    },

    setInfoMessage: function(modelLib) {
        if(modelLib == null) {
            var spanDoc = document.getElementById(this.id+'ModelEditorModelEditingGoBackInfoPanel');
            if(spanDoc != null) {
                spanDoc.innerHTML = '&nbsp;&nbsp;'+getI18nResource('caqs.modeleditor.modelEdition.creatingModel', modelLib);
            }
        } else {
            this.infoPanel.setVisible(true);
            var spanDoc = document.getElementById(this.id+'ModelEditorModelEditingGoBackInfoPanel');
            if(spanDoc != null) {
                spanDoc.innerHTML = '&nbsp;&nbsp;'+getI18nResource('caqs.modeleditor.modelEdition.editingModel', modelLib);
            }
        }
    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        this.goBackButton = new Ext.Button({
            text:           getI18nResource('caqs.modeleditor.modelEdition.model.goBack'),
            tooltip:        getI18nResource('caqs.modeleditor.modelEdition.model.goBack.tooltip'),
            handler:        this.goBackFn,
            cls:            'x-btn-text-icon',
            icon:           requestContextPath+'/images/arrow_left.gif',
            scope:          this
        });
        this.infoPanel = new Ext.Panel({
            border:     false,
            frame:      true,
            anchor:     '95%',
            style:      'margin-bottom: 10px; margin-right: 5px; text-align:center;',
            region:     'north',
            layout:     'table',
            layoutConfig:{
                columns:2
            },
            items:       [
                this.goBackButton
                , {
                    border:  false,
                    html:    '<span style="font-weight: bold;" id="'+this.id+'ModelEditorModelEditingGoBackInfoPanel"></span>'
                }
            ]
        });
        this.panels = new Array();
        this.createPanels();
        this.navigator = new Ext.Panel({
            layout:         'accordion',
            region:         'west',
            width:          200,
            border:         false,
            layoutConfig:   {
                animate:    true
            },
            defaults:  {
                listeners: {
                    scope:  this,
                    'expand': function(panel) {
                        this.switchComponent(panel.index);
                    }
                }
            },
            items:
            [
                {
                    id:     'modelDef',
                    index:  0,
                    title:  getI18nResource('caqs.modeleditor.modelEdition.model'),
                    html:   getI18nResource('caqs.modeleditor.modelEdition.model.def')
                }
                ,{
                    id:     'associatedTools',
                    index:  1,
                    title:  getI18nResource('caqs.modeleditor.modelEdition.tools'),
                    html:   getI18nResource('caqs.modeleditor.modelEdition.tools.def')
                }
                ,{
                    id:     'associatedGoals',
                    index:  2,
                    title:  getI18nResource('caqs.modeleditor.modelEdition.goals'),
                    html:   getI18nResource('caqs.modeleditor.modelEdition.goals.def')
                }
            ]
        });

        this.centerPanel = new Ext.Panel({
            layout:         'card',
            region:         'center',
            activeItem:     0,
            items:          this.panels
        });

        var config = {
            items:  [
                this.infoPanel,
                this.navigator,
                this.centerPanel
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelEditorPanel.superclass.initComponent.apply(this, arguments);
    },

    setId: function(id) {
        this.editedModelId = id;
        this.setInfoMessage(id);
        this.switchComponent(0);
        this.navigator.findById('modelDef').expand(false);
    },


    switchComponent: function(index) {
        this.panels[index].setId(this.editedModelId);
        this.centerPanel.getLayout().setActiveItem(index);
    }

});

