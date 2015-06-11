Ext.ux.CaqsMainFrame = function(config) {
    // call parent constructor
    Ext.ux.CaqsMainFrame.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsBottomUpSynthese constructor


Ext.extend(Ext.ux.CaqsMainFrame, Ext.Panel, {
    layout:             'fit',
    border:             true,
    qualitySectionsTabs:undefined,
    synthesisTabs:      undefined,
    bottomUp:           undefined,
    evolution:          undefined,
    actionPlan:         undefined,
    previousHasActionPlan: false,
    currentBaselineId: null,
    idElt:              null,
    currentBaselineLib: null,
    realBaselineItem:   undefined,
    realBaselineSep:    undefined,
    recomputeBtn:       undefined,
    recomputeSep:       undefined,
    labelButton:        undefined,
    scatterplotBtn:     undefined,
    generateReportItem: undefined,
    retrieveReportItem: undefined,
    reportMenu:         undefined,
    dataExportBtn:      undefined,
    dataMenu:           undefined,
    uploadBtn:          undefined,
    tabsArray:          undefined,
    facteurSynthese:    undefined,
    topDownPanel:       undefined,

    refresh: function(idElt, idBline, blineLib) {
        this.idElt = idElt;
        if(this.idElt!=null) {
            this.currentBaselineId = idBline;
            this.currentBaselineLib = blineLib;
            Ext.Ajax.request({
                url:	requestContextPath+'/LoadMainFrame.do',
                scope:      this,
                params:    {
                    idElt: this.idElt,
                    idBline: this.currentBaselineId
                },
                success:	function(response) {
                    if(response.responseText!='' && response.responseText!='[]') {
                        var json = Ext.util.JSON.decode(response.responseText);
                        this.currentBaselineId = json.realBline;
                        this.updateToolbar(json);
                        this.updateQualitySectionTabs(json);
                        this.updateSynthesisTabs(json);
                        this.qualitySectionsTabs.setActiveTab(0);
                    }
                }
            });
        }
    },

    getReport: function() {
        location.href = requestContextPath+'/DownloadReport.do';
    },


    setIdLabel: function(i) {
        this.idLabel = i;
    },

    setLabelStatus: function(labelId, labelStatus, labelDesc) {
        this.idLabel = labelId;
        this.labellisationStatus = labelStatus;
        this.labellisationDesc = labelDesc;
        if(this.idLabel != '' && this.idLabel != undefined) {
            if("DEMAND" == this.labellisationStatus && canAccessFunction("LABEL")) {
                this.labelButton.setText(getI18nResource("caqs.synthese.labellisationdemandee"));
                this.labelButton.setIcon(requestContextPath+'/images/encours.gif');
                this.labelButton.setDisabled(false);
                this.labelButton.setVisible(true);
                this.labelButton.setTooltip(getI18nResource("caqs.synthese.demandecours") + ' : ' +
                    this.labellisationDesc);
            } else {
                var labellisationMsg = '';
                var labellisationTooltip = this.labellisationDesc;
                var labellisationIcon = '';
                if("DEMAND" == this.labellisationStatus) {
                    labellisationMsg = getI18nResource("caqs.synthese.labellisationdemandee");
                    labellisationIcon = requestContextPath+'/images/encours.gif';
                } else if("VALID" == this.labellisationStatus) {
                    labellisationMsg = getI18nResource("caqs.synthese.labellisee");
                    labellisationIcon = requestContextPath+'/images/tick.gif';
                } else if("VALID_RES" == this.labellisationStatus) {
                    labellisationMsg = getI18nResource("caqs.synthese.labellisationreserve");
                    labellisationIcon = requestContextPath+'/images/tick.gif';
                } else if("REJET" == this.labellisationStatus) {
                    labellisationMsg = getI18nResource("caqs.synthese.labellisationrejetee");
                    labellisationIcon = requestContextPath+'/images/delete.gif';
                }
                this.labelButton.setText(labellisationMsg);
                this.labelButton.setIcon(labellisationIcon);
                this.labelButton.setDisabled(true);
                this.labelButton.setVisible(true);
                this.labelButton.setTooltip(labellisationTooltip);
            }
        }else {
            this.labelButton.setText(getI18nResource("caqs.synthese.labelliser"));
            this.labelButton.setIcon(requestContextPath + '/images/award_star_gold_2.gif');
            this.labelButton.setDisabled(false);
            this.labelButton.setVisible(canAccessFunction("LABEL"));
            this.labelButton.setTooltip(null);
        }
    },

    launchReportGeneration: function() {
        this.retrieveReportItem.setDisabled(true);
        if(this.generateReportItem!=undefined) {
            this.generateReportItem.setDisabled(true);
        }

        var url = requestContextPath + '/LaunchReportGeneration.do?forceRegeneration=true';

        Ext.Ajax.request({
            url:	url,
            timeout: 	1800000,//30 minutes
            scope:      this/*,
            success: 	this.reportGenerated*/
        });

        window.setTimeout(function(){
            Caqs.Messages.checkMessages();
        }, 2000);
    },

    updateReportMenu: function(idElt, generatingReport, reportAvailable) {
        if(idElt==this.idElt) {
            this.generateReportItem.setDisabled(generatingReport);
            this.retrieveReportItem.setDisabled(!reportAvailable);
        }
    },

    updateToolbar: function(json) {
        if(canAccessFunction("Report")) {
            this.updateReportMenu(this.idElt, json.generatingReport, json.reportAvailable);
            this.reportMenu.setVisible(true);
        } else {
            this.reportMenu.setVisible(false);
        }

        /******************************** export des donnees **************************************/
        this.dataExportBtn.setVisible(canAccessFunction("Data"));

        /******************************** upload des donnees **************************************/
        this.uploadBtn.setVisible(canAccessFunction("Upload"));
        /**** recalcul necessaire ??? ***/
        if(json.needsRecompute) {
            this.recomputeButton.setTooltip('<img src="'+requestContextPath+
                '/images/warning.gif" /><span class="normalFont" style="font-weight: bold; font-style: italic;">&nbsp;'+
                getI18nResource("caqs.message.needsRecompute")+'</span>');
            this.recomputeButton.setIcon(requestContextPath+'/images/warning.gif');
        } else {
            this.recomputeButton.setTooltip(getI18nResource("caqs.synthese.toolbar.recalculTooltip"));
            this.recomputeButton.setIcon(requestContextPath+'/images/cog.gif');
        }
        //this.recomputeButton.setVisible(json.needsRecompute);
        //this.recomputeSep.setVisible(json.needsRecompute);
        /** labellisation **/
        this.setLabelStatus(json.syntheseLabelId, json.syntheseLabelStatus, json.syntheseLabelDesc);

        if(json.notDisplayedBaseline) {
            var html = '<img src="'+requestContextPath+'/images/info.gif" />&nbsp;<span style="font-weight: bold;">'+
            getI18nResource('caqs.currentBaseline.lib')+'&nbsp;'+json.currentBaselineLib+'</span>';
            this.realBaselineItem.setText(html);
        }
        this.realBaselineItem.setVisible(json.notDisplayedBaseline);
        this.realBaselineSep.setVisible(json.notDisplayedBaseline);
    },

    createToolBar: function() {
        this.labelButton = new Ext.Button({
            id:         'labellisationButton',
            text:	'',
            icon:       '',
            handler:	function() {
                Caqs.Labellisation.launchLabellisation(this);
            },
            scope:      this,
            cls: 	'x-btn-text-icon',
            tooltip: 	''
        });
        this.scatterplotBtn = new Ext.Toolbar.Button({
            text:	getI18nResource("caqs.synthese.scatterplot"),
            icon: 	requestContextPath + '/images/map.gif',
            cls: 	'x-btn-text-icon',
            width:      80,
            scope:      this,
            handler:	function() {
                PopupCentrer('ScatterPlotPrepareAction.do',
                    900, 770, 'menubar=no,status=1, resizable=yes');
            },
            tooltip: 	getI18nResource("caqs.synthese.toolbar.scatterplotTooltip")
        });

        this.retrieveReportItem = new Ext.menu.Item({
            text: 	getI18nResource("caqs.synthese.recupReport"),
            icon: 	requestContextPath + '/images/page_word.gif',
            handler:	this.getReport,
            scope:      this,
            cls: 	'x-btn-text-icon'
        });
        this.generateReportItem = new Ext.menu.Item({
            text: 	getI18nResource("caqs.gestionqualite.reportMgmt.generateReport"),
            icon: 	requestContextPath + '/images/arrow_refresh.gif',
            cls: 	'x-btn-text-icon',
            scope:      this,
            handler:	this.launchReportGeneration
        });
        this.reportMenu = new Ext.MenuButton({
            text: 	getI18nResource("caqs.synthese.toolbar.reportManagement"),
            cls: 	'x-btn-text',
            width:      130,
            menu : {
                items :	[
                this.retrieveReportItem
                , this.generateReportItem
                ]
            },
            listeners: {
                scope: this.reportMenu,
                'click': function() {
                    this.showMenu();
                }
            }
        });

        /******************************** export des donnees **************************************/
        this.dataExportBtn = new Ext.menu.Item({
            text:	getI18nResource("caqs.synthese.donneesbrutes"),
            icon: 	requestContextPath + '/images/page_excel.gif',
            cls: 	'x-btn-text-icon',
            width:      80,
            handler:	function() {
                parent.location.href = 'RetrieveMetricsAsCsv.do';
            },
            tooltip: 	getI18nResource("caqs.synthese.toolbar.exportDataTooltip")
        });

        /******************************** upload des donnees **************************************/
        this.uploadBtn = new Ext.menu.Item({
            text:	getI18nResource("caqs.synthese.upload"),
            icon: 	requestContextPath + '/images/database_add.gif',
            cls: 	'x-btn-text-icon',
            width:      80,
            handler:	function() {
                PopupCentrer('UploadDataPre.do',560,190,'menubar=no,status=1, resizable=yes');
            },
            tooltip: 	getI18nResource("caqs.synthese.toolbar.uploadTooltip")
        });
        this.dataMenu = new Ext.MenuButton({
            text: 	getI18nResource("caqs.synthese.toolbar.dataManagement"),
            cls: 	'x-btn-text',
            width:      130,
            menu : {
                items :	[
                this.dataExportBtn
                , this.uploadBtn
                ]
            },
            listeners: {
                scope: this.dataMenu,
                'click': function() {
                    this.showMenu();
                }
            }
        });

        /******************************* bouton recalcul ***********/
        this.recomputeButton = new Ext.Button({
            text:	getI18nResource("caqs.synthese.recalcul"),
            icon: 	requestContextPath + '/images/cog.gif',
            handler:	function() {
                Caqs.Computation.askLaunchRecalcul(window.parent)
            },
            scope:      this,
            disabled:   !canAccessFunction("Calculation"),
            tooltip: 	'<img src="'+requestContextPath+
            '/images/warning.gif" /><span class="normalFont" style="font-weight: bold; font-style: italic;">&nbsp;'+
            getI18nResource("caqs.message.needsRecompute")+'</span>',
            cls: 	'x-btn-text-icon',
            style:      'margin-left: 10px;'
        });
        this.recomputeSep = new Ext.Toolbar.Separator();

        /*** message pour indiquer la veritable baseline ***/
        this.realBaselineItem = new Ext.Toolbar.TextItem({
            text:       '&nbsp;'
        });
        this.realBaselineSep = new Ext.Toolbar.Separator();
        var retour = new Ext.Toolbar({
            items: [
            this.realBaselineItem
            , this.realBaselineSep
            , this.labelButton
            , '-'
            , this.scatterplotBtn
            , '-'
            , this.reportMenu
            , '-'
            , this.dataMenu
            , this.recomputeSep
            , this.recomputeButton
            ]
        });
        return retour;
    },

    createMainFrame: function() {
        this.topDownPanel = new Ext.ux.CaqsTopDown({
            region:     'center'
        });

        this.bottomUp = new Ext.ux.CaqsBottomUpSynthese({
            isEA:                   true
        });
        this.evolution = new Ext.ux.CaqsEvolutionsPanel();
        this.actionPlan = new Ext.ux.CaqsEAActionPlan({
            concurrentModification:     false
        });
    },

    updateQualitySectionTabs: function(json) {
        var evolTab = Ext.getCmp('eaEvolution');
        if(evolTab) {
            evolTab.setDisabled(!json.evolutionTab);
        }
        var apTab = Ext.getCmp('eaActionPlan');
        if(apTab) {
            apTab.setDisabled(!json.actionsPlanTab);
        }
    },

    createQualitySectionTabs: function() {
        this.qualitySectionsTabs = new Ext.ux.ClickableTabPanel({
            style:              'margin-top: 5px;',
            layoutOnTabChange:  true,
            minTabWidth:        115,
            enableTabScroll:    true,
            activeTab:          -1,
            plain:              true,
            border:             false,
            defaults: {
                autoScroll:     true,
                style:          'padding-top:5px;',
                border:         false
            },
            items:              [
                {
                    title: 	getI18nResource('caqs.mainFrame.title.synthese'),
                    id:		'topDown',
                    layout:     'border',
                    items: [
                            this.synthesisTabs
                            , this.topDownPanel
                        ]
                }
                , {
                    title: 	getI18nResource('caqs.mainFrame.title.bottomup'),
                    id:		'bottomUp',
                    layout:     'fit',
                    items:      [
                        this.bottomUp
                    ]
                }
                , {
                    title:  getI18nResource('caqs.mainFrame.title.evolution'),
                    id:     'eaEvolution',
                    layout:     'fit',
                    items:      [
                        this.evolution
                    ]
                }
                , {
                    title:      getI18nResource('caqs.mainFrame.title.actionPlan'),
                    id:		'eaActionPlan',
                    layout:     'fit',
                    items:      [
                        this.actionPlan
                    ]
                }
            ],
            listeners: {
                tabchange:    this.showQualityManagementPage,
                scope:        this
            }
        });
    },

    showQualityManagementPage: function(tabPanel, newTab, currentTab) {
        if(this.idElt!=null) {
            if(newTab.id=='bottomUp') {
                this.bottomUp.refresh();
            } else if(newTab.id=='eaEvolution') {
                this.evolution.refresh(this.idElt, this.currentBaselineId, this.currentBaselineLib, this.previousHasActionPlan);
            } else if(newTab.id=='eaActionPlan') {
                this.actionPlan.refresh();
            } else if(newTab.id == 'topDown') {
                this.synthesisTabs.setActiveTab(0);
            }
        }
    },

    updateSynthesisTabs: function(json) {
        this.removeAllTabs();
        this.tabsArray = new Array();
        for(var i=0; i < json.goals.length; i++) {
            var tabIndex = 4+i;
            var g = json.goals[i];
            var desc = '<img src="images/info.gif">&nbsp;&nbsp;<B>'+g.lib+'</B><BR />'+g.desc;
            var obj = {
                id:         'topdownSynthesisTab'+g.id,
                goalId:     g.id,
                sectionId:  tabIndex,
                title :     g.lib,
                tabTip:     desc
            };
            this.synthesisTabs.add(obj);
            this.tabsArray[this.tabsArray.length] = obj;
        }
    },
    
    removeAllTabs: function() {
        for(var i=0; i<this.tabsArray.length; i++) {
            var goal = this.tabsArray[i];
            if(goal!=null) {
                var elt = this.synthesisTabs.getComponent(goal.id);
                this.synthesisTabs.remove(elt);
            }
        }
        this.tabsArray.length = 0;
    },

    createSynthesisTabs: function() {
        this.tabsArray = new Array();
        this.synthesisTabs = new Ext.ux.ClickableTabPanel({
            region:             'north',
            layoutOnTabChange:  true,
            minTabWidth: 	115,
            enableTabScroll:	true,
            plain:		true,
            border:		false,
            height:             30,
            defaults: {
                autoScroll:	true,
                border:		false
            },
            items:              {
                id:     'topDownSynthesis',
                title:  getI18nResource("caqs.synthese.title")
            },
            listeners: {
                tabchange:      this.topDownShowPage,
                render:        function() {
                    if(this.synthesisTabs && this.synthesisTabs.bwrap) {
                        this.synthesisTabs.bwrap.remove();
                    }
                },
                scope:          this
            }
        });
    },

    topDownShowPage: function(tabPanel, newTab, currentTab) {
        if(this.idElt!=null) {
            if(newTab.id=='topDownSynthesis') {
                this.topDownPanel.setTopDownSynthesis();
            } else {
                this.topDownPanel.setGoal(newTab.goalId);
            }
        }
    },

    initComponent : function(){
        Ext.ux.CaqsMainFrame.superclass.initComponent.call(this);
        this.createMainFrame();
        this.createSynthesisTabs();
        this.createQualitySectionTabs();
        var config = {
            tbar:   this.createToolBar()
            ,
            items:  this.qualitySectionsTabs
        };

        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsMainFrame.superclass.initComponent.apply(this, arguments);
    }
});