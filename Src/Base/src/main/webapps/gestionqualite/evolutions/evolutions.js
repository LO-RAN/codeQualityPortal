Ext.ux.CaqsEvolutionsPanel = function(config) {
    // call parent constructor
    Ext.ux.CaqsEvolutionsPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsEvolutionsPanel constructor


Ext.extend(Ext.ux.CaqsEvolutionsPanel, Ext.Panel, {
    id:                 'caqsEvolutionsPanel',
    layout:             'border',
    tabPanel:           undefined,
    border:             false,
    hideMode:           'offsets',
    titlecollapse:      true,
    hideCollapseTool:   true,
    layoutOnTabChange:	true,
    hasPreviousActionsPlan: false,
    goalsCriterionsPanel: undefined,
    elementsEvolutionsPanel: undefined,
    actionsPlanEvolutionsPanel: undefined,
    volumetryEvolutionsPanel: undefined,
    previousBaselineId:   null,
    currentBaselineId:    null,
    previousBaselineLib:  null,
    currentBaselineLib:   null,
    idElt:              null,
    previousBlinesStore:undefined,
    previousBlinesCB:   undefined,
    currentActiveView:  undefined,
    fromRefresh:        false,

    tabChangeFn: function(panel, newTab, currentTab) {
        if(((currentTab != newTab)||(this.fromRefresh)) && this.idElt!=null){
            this.fromRefresh = false;
            if(this.goalsCriterionsPanel!=undefined && newTab.id=='caqsEvolutionGoalCriterionsTab') {
                this.currentActiveView = this.goalsCriterionsPanel;
            } else if(this.elementsEvolutionsPanel!=undefined && newTab.id=='caqsEvolutionsElementsTab') {
                this.currentActiveView = this.elementsEvolutionsPanel;
            } else if(this.actionsPlanEvolutionsPanel!=undefined && newTab.id=='caqsEvolutionsActionsPlanTab') {
                this.currentActiveView = this.actionsPlanEvolutionsPanel;
            } else if(this.volumetryEvolutionsPanel!=undefined && newTab.id=='caqsEvolutionsVolumetryTab') {
                this.currentActiveView = this.volumetryEvolutionsPanel;
            }
            if(this.currentActiveView!=null && this.idElt!=null) {
                this.currentActiveView.doLayout();
                this.currentActiveView.refresh(this.idElt, this.previousBaselineId, this.previousBaselineLib, this.currentBaselineId, this.currentBaselineLib);
            }
        }
    },

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsEvolutionsPanel.superclass.constructor.apply(this, arguments);
    },

    afterLoadingBlineList: function(store, records, options) {
        this.previousBlinesCB.setValue(records[0].data.id);
        this.updateEvolutionSection(records[0].data.id, records[0].data.lib,
            records[0].data.nb_crit);
    },

    updateEvolutionSection: function(id, lib, nbCrit)  {
        this.previousBaselineId = id;
        this.previousBaselineLib = lib;
        if(nbCrit>0) {
            this.setActionPlanEvolTabDisabled(false);
        } else {
            var switchTab = false;
            if(this.currentActiveView == this.actionsPlanEvolutionsPanel) {
                switchTab = true;
                this.currentActiveView = this.goalsCriterionsPanel;
            }
            this.setActionPlanEvolTabDisabled(true);
            if(switchTab) {
                this.tabPanel.setActiveTab(0);
            }
        }
        if(this.currentActiveView==null) {
            this.fromRefresh = false;
            this.currentActiveView = this.goalsCriterionsPanel;
            //this.tabPanel.setActiveTab(0);
            //this.goalsCriterionsPanel.refresh(this.idElt, this.previousBaselineId, this.previousBaselineLib, this.currentBaselineId, this.currentBaselineLib);
        }
        this.currentActiveView.refresh(this.idElt, this.previousBaselineId, this.previousBaselineLib, this.currentBaselineId, this.currentBaselineLib);
    },

    onChangeBline: function(field, newvalue, oldvalue) {
        this.updateEvolutionSection(newvalue.data.id, newvalue.data.lib,
            newvalue.data.nb_crit);
    },

    initComponent : function(){
        Ext.ux.CaqsEvolutionsPanel.superclass.initComponent.call(this);

        this.previousBlinesStore = new Ext.ux.CaqsJsonStore({
            url: requestContextPath + '/EvolutionRetrievePreviousBaselinesList.do',
            fields: ['id', 'lib', 'nb_crit']
        });
        this.previousBlinesStore.addListener('load', this.afterLoadingBlineList, this);


        this.previousBlinesCB = new Ext.form.ComboBox({
            name:       	'evolutionsSelectPreviousBlineListCB',
            id:         	'evolutionsSelectPreviousBlineListCB',
            fieldLabel:         getI18nResource('caqs.evolution.selectPreviousBaseline'),
            displayField:	'lib',
            valueField: 	'id',
            hiddenName: 	'id',
            editable:		false,
            store:		this.previousBlinesStore,
            triggerAction:	'all',
            autocomplete:	false,
            mode:      		'local',
            width:              500
        });
        this.previousBlinesCB.addListener('select', this.onChangeBline, this);

        var blineListPanel = new Ext.form.FormPanel({
            region:      'north',
            border:       true,
            frame:        true,
            style:        'margin-left: 5px; margin-bottom: 5px;',
            height:       40,
            labelWidth:   200,
            items:        this.previousBlinesCB
        });

        this.goalsCriterionsPanel = new Ext.ux.CaqsGoalsCriterionsEvolutionsPanel();
        this.elementsEvolutionsPanel = new Ext.ux.CaqsElementsEvolutionsPanel();
        this.actionsPlanEvolutionsPanel = new Ext.ux.CaqsActionsPlanEvolutionsPanel();
        this.volumetryEvolutionsPanel = new Ext.ux.CaqsVolumetryEvolutionsPanel();
        this.tabPanel = new Ext.TabPanel({
            region:     'center',
            activeTab:  0,
            border:     false,
            defaults: {
                border: false
            },
            items:      [
            {
                id:     'caqsEvolutionGoalCriterionsTab',
                title:  getI18nResource("caqs.evolution.criterionsGoalsTitle"),
                layout: 'anchor',
                autoScroll: true,
                items:  this.goalsCriterionsPanel
            }
            , {
                id:     'caqsEvolutionsVolumetryTab',
                title:  getI18nResource("caqs.evolution.volumetryTitle"),
                layout: 'fit',
                items:  this.volumetryEvolutionsPanel
            }
            ,{
                id:     'caqsEvolutionsElementsTab',
                title:  getI18nResource("caqs.evolution.elementsTitle"),
                layout: 'fit',
                items:  this.elementsEvolutionsPanel
            }
            , {
                id:     'caqsEvolutionsActionsPlanTab',
                title:  getI18nResource("caqs.evolution.actionsPlanTitle"),
                layout: 'anchor',
                autoScroll: true,
                items:  this.actionsPlanEvolutionsPanel
            }
            ]
        })

        var config = {
            items:  [
            blineListPanel
            , this.tabPanel
            ]
        };

        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsEvolutionsPanel.superclass.initComponent.call(this);
        this.tabPanel.on('tabchange', this.tabChangeFn, this);
    },

    setActionPlanEvolTabDisabled: function(disabled) {
        var evolTab = Ext.getCmp('caqsEvolutionsActionsPlanTab');
        if(evolTab) {
            evolTab.setDisabled(disabled);
        }
    },

    refresh: function(idElt, currentBaselineId, currentBaselineLib, hasPreviousActionsPlan) {
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        this.idElt = idElt;
        this.currentBaselineId = currentBaselineId;
        this.currentBaselineLib = currentBaselineLib;
        this.hasPreviousActionsPlan = hasPreviousActionsPlan;
        this.setActionPlanEvolTabDisabled(!this.hasPreviousActionsPlan);
        this.fromRefresh = true;
        this.previousBlinesStore.load({
            params: {
                currentBaselineId:  this.currentBaselineId,
                idElt:              this.idElt
            }
        });
        Caqs.Portal.getCaqsPortal().setCurrentScreenId('evolutions');
    }
});

