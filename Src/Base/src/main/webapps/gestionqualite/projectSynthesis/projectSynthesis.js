Ext.ux.CaqsProjectSynthesisPanel = function(config) {
    Ext.ux.CaqsProjectSynthesisPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsProjectSynthesisPanel constructor


Ext.extend(Ext.ux.CaqsProjectSynthesisPanel, Ext.Panel, {
    tabPanel:           undefined,
    layout:             'fit',
    recomputeButton:    undefined,
    labelButton:        undefined,
    idLabel:            undefined,
    labelisationWnd:    undefined,
    gestionQualite:     undefined,
    projectId:          undefined,
    idBline:            undefined,
    projectActionPlan:  undefined,
    projectSynthesis:   undefined,
    justRefreshed:      false,

    createToolBar: function() {
        this.recomputeButton = new Ext.Toolbar.Button({
            text:	getI18nResource("caqs.synthese.recalcul"),
            icon: 	requestContextPath + '/images/cog.gif',
            handler:	function() {
                Caqs.Computation.askLaunchRecalcul(window)
            },
            scope:      this,
            hidden:     !canAccessFunction("Calculation"),
            tooltip: 	getI18nResource("caqs.synthese.toolbar.recalculTooltip"),
            cls: 	'x-btn-text-icon',
            style:      'margin-left: 10px;'
        });
        this.labelButton = new Ext.Button({
            id:         'projectLabellisationButton',
            text:	getI18nResource("caqs.synthese.labelliser"),
            handler:    function() {
                            Caqs.Labellisation.launchLabellisation(this);
                        },
            icon:	requestContextPath + '/images/award_star_gold_2.gif',
            scope:      this,
            hidden:     !canAccessFunction("LABEL"),
            cls: 	'x-btn-text-icon'
        });
        var retour = new Ext.Toolbar({
            items: [
            this.labelButton,
            this.recomputeButton
            ]
        });
        return retour;
    },

    updateToolbar: function(json) {
        if(json.syntheseLabelId != null && json.syntheseLabelId != '') {
            this.idLabel = json.syntheseLabelId;
            if("DEMAND" == json.syntheseLabelStatus && canAccessFunction("LABEL")) {
                this.labelButton.setText(getI18nResource("caqs.synthese.labellisationdemandee"));
                this.labelButton.setTooltip(getI18nResource("caqs.synthese.demandecours") + ' : ' +
                                json.syntheseLabelDesc);
                this.labelButton.setIcon(requestContextPath+'/images/encours.gif');
                this.labelButton.setDisabled(false);
            } else {
                var labellisationMsg = '';
                var labellisationTooltip = json.syntheseLabelDesc;
                var labellisationIcon = '';
                if("DEMAND" == json.syntheseLabelStatus) {
                    labellisationMsg = getI18nResource("caqs.synthese.labellisationdemandee");
                    labellisationIcon = requestContextPath+'/images/encours.gif';
                } else if("VALID" == json.syntheseLabelStatus) {
                    labellisationMsg = getI18nResource("caqs.synthese.labellisee");
                    labellisationIcon = requestContextPath+'/images/tick.gif';
                } else if("VALID_RES" == json.syntheseLabelStatus) {
                    labellisationMsg = getI18nResource("caqs.synthese.labellisationreserve");
                    labellisationIcon = requestContextPath+'/images/tick.gif';
                } else if("REJET" == json.syntheseLabelStatus) {
                    labellisationMsg = getI18nResource("caqs.synthese.labellisationrejetee");
                    labellisationIcon = requestContextPath+'/images/delete.gif';
                }
                this.labelButton.setText(labellisationMsg);
                this.labelButton.setTooltip(labellisationTooltip);
                this.labelButton.setIcon(labellisationIcon);
                this.labelButton.setDisabled(true);
            }
        } else {
            this.labelButton.setText(getI18nResource("caqs.synthese.labelliser"));
            this.labelButton.setTooltip('');
            this.labelButton.setIcon(requestContextPath + '/images/award_star_gold_2.gif');
            this.labelButton.setDisabled(false);
        }
    },

    displayActionPlanTab: function(display) {
        var apTab = Ext.getCmp('projectSynthesisActionPlanTab');
        if(apTab) {
            apTab.setDisabled(!display);
        }
    },

    initComponent : function(){
        Ext.ux.CaqsProjectSynthesisPanel.superclass.initComponent.call(this);

        var tabs = new Array();
        this.projectSynthesis = new Ext.ux.CaqsGlobalProjectSynthesisPanel({
            id:     'projectSynthesisGlobalSynthesis'
        });
        tabs[tabs.length] = {
            title:      getI18nResource("caqs.domainsynthese.treemap.title"),
            border:     false,
            layout:     'fit',
            items:      this.projectSynthesis
        };
        tabs[tabs.length] ={
            title:      getI18nResource("caqs.mainFrame.title.bottomup"),
            border:     false,
            layout:     'fit',
            autoScroll: true,
            items:      new Ext.ux.CaqsBottomUpSynthese({
                isEA:   false,
                id:     'projectSynthesisBottomUp'
            })
        };
        tabs[tabs.length] = {
            title:      getI18nResource("caqs.domainsynthese.synthese"),
            border:     false,
            layout:     'fit',
            items:      new Ext.ux.CaqsProjectSynthesisVolumetryPanel({
                parentPanel:        this,
                id:                 'projectSynthesisVolumetry'
            })
        };
        tabs[tabs.length] = {
            title:      getI18nResource("caqs.domainsynthese.stats"),
            border:     false,
            layout:     'fit',
            items:      new Ext.ux.CaqsProjectSynthesisRepartitionChart({
                idFac:      'ALL_FACTORS',
                id:         'projectSynthesisRepartition'
            })
        };
        this.projectActionPlan = new Ext.ux.CaqsProjectActionPlan();
        tabs[tabs.length] = {
            id:         'projectSynthesisActionPlanTab',
            title:      getI18nResource("caqs.mainFrame.title.actionPlan"),
            border:     false,
            layout:     'fit',
            items:      this.projectActionPlan
        };

        this.tabPanel = new Ext.TabPanel({
            id:                 'projectSynthesisTabs',
            activeTab:          0,
            layoutOnTabChange:  true,
            border:             false,
            bodyStyle:          'padding-top:5px; padding-left:5px; padding-right: 5px; padding-bottom: 5px;',
            hideMode:           'offsets',
            items:              tabs,
            listeners: {
                beforetabchange:this.showPage,
                scope:          this
            }
        });

        var config = {
            items:  [
            this.tabPanel
            ],
            tbar:   this.createToolBar()
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsProjectSynthesisPanel.superclass.initComponent.call(this);
    },

    refresh: function(projectId, idBline) {
        Caqs.Portal.setCurrentScreen('project_synthesis');
        this.idBline = idBline;
        this.projectId = projectId;
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        Ext.Ajax.request({
                url:	requestContextPath+'/ProjectSynthesis.do',
                scope:      this,
                params:    {
                    projectId: this.projectId,
                    idBline: this.idBline
                },
                success: function(response) {
                    if(response.responseText!='' && response.responseText!='[]') {
                        var json = Ext.util.JSON.decode(response.responseText);
                        if(json.noProjectSynthesis) {
                            this.gestionQualite.setProjectNotAnalysed();
                        } else {
                            this.updateSynthesisState(json);
                            this.justRefreshed = true;
                            this.projectSynthesis.refresh();
                            this.tabPanel.setActiveTab(0);

                        }
                    }
                    Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
                }
            });
    },

    updateSynthesisState: function(json) {
        this.updateToolbar(json);
        this.displayActionPlanTab(json.actionPlanTab);
        this.projectActionPlan.refreshState(json);
        //this.removeMask();
    },

    showPage: function(tabPanel, newTab) {
        if(this.projectId!=null && this.idBline!=null && newTab!=null && !this.justRefreshed) {
            var item = newTab.getComponent(0);
            if(item!=null && item.refresh!=null) {
                item.refresh();
            }
        }
        this.justRefreshed = false;
    }
});