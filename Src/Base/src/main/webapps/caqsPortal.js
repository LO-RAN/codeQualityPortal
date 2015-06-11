Ext.ux.CaqsPortal = function(config) {
    // call parent constructor
    Ext.ux.CaqsPortal.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsPortal constructor


Ext.extend(Ext.ux.CaqsPortal, Ext.Viewport, {
    layout:             'border',
    messagesPanel:      undefined,
    changeActivityMenu:  undefined,
    gestionQualitePanel:undefined,
    dashboardPanel:      undefined,
    administrationPanel:undefined,
    architecturePanel:  undefined,
    justificationIFrame:undefined,
    labellisationIFrame:undefined,
    modelEditor:        undefined,
    activeCmpt:         undefined,
    border:             true,
    messagesZone:       undefined,
    userPreferencesPanel:undefined,
    dashboardDefaultDomain: undefined,
    pages:              undefined,
    startingPage:       undefined,
    aboutWnd:           undefined,
    portalContainerPanel:undefined,
    locationTitle:      undefined,
    portalToolbar:      undefined,
    activityPanel:      undefined,
    menu:               undefined,
    activitiesButton:   undefined,
    currentScreenId:    undefined,
    dashboardSeeConnectionTimeline:     false,
    dashboardSeeGlobalBaselineTimeline: false,
    locationField:      undefined,
    portalNorthPanelHeight: 60,
    documentations:     undefined,
    cardArray:          undefined,

    getMessagesPanel: function() {
        return this.messagesPanel;
    },

    getGestionQualiteActivity: function() {
        return this.gestionQualitePanel;
    },

    getAdministrationActivity: function() {
        return this.administrationPanel;
    },

    setCurrentScreenId: function(id) {
        this.currentScreenId = id;
    },

    initAdministration: function(){
        this.administrationPanel = new Ext.ux.AdministrationPanel();
        this.pages['administration'] = this.goToAdministration;
    },

    initJustification: function() {
        this.justificationIFrame = new Ext.ux.ManagedIframePanel({
            id:         'justificationPanel',
            name:       'justificationPanel',
            loadMask:	true,
            autoCreate:	true,
            border: 	false,
            width:		'100%',
            height:		630,
            nocache:	true
        });
         this.pages['justification'] = this.goToJustification;
         this.justificationIFrame.on('show', function() {
             this.justificationIFrame.setSrc(requestContextPath + '/justification.do');
         }, this);
    },

    initLabelling: function() {
        this.labellisationIFrame = new Ext.ux.ManagedIframePanel({
            id:         'labellisationPanel',
            loadMask:	true,
            autoCreate:	true,
            border: 	false,
            width:		'100%',
            height:		630,
            nocache:	true
        });
        this.pages['labellisation'] = this.goToLabellisation;
        this.labellisationIFrame.on('show', function() {
             this.labellisationIFrame.setSrc('labellisation.do');
         }, this);
    },

    initdashboard: function() {
        this.dashboardPanel = new Ext.ux.CaqsDashboard({
            displayConnectionTimeplot:  this.dashboardSeeConnectionTimeline && canAccessFunction("DASHBOARD_CONNECTION_TIMELINE"),
            displayGlobalBaselineTimeplot:this.dashboardSeeGlobalBaselineTimeline,
            dashboardDefaultDomain: this.dashboardDefaultDomain
        });
        this.pages['dashboard'] = this.goToDashboard;
    },

    initUserPreferences: function() {
        this.userPreferencesPanel = new Ext.ux.CaqsPreferencesPanel({
            id:         'userPreferencesPanel',
            dashboardAccessConnectionCheck: canAccessFunction("DASHBOARD_CONNECTION_TIMELINE"),
            dashboardDefaultDomain: this.dashboardDefaultDomain
        });
        this.pages['userPreferences'] = this.goToPreferences;
    },

    initQuality: function() {
        this.gestionQualitePanel = new Ext.ux.GestionQualitePanel({});
        this.pages['quality'] = this.goToQualityPage;
    },

    initArchitecture: function() {
        this.architecturePanel = new Ext.ux.ArchitecturePanel({});
        this.pages['architecture'] = this.goToArchitecture;
    },

    initModelEditor: function() {
        this.modelEditor = new Ext.ux.CaqsQualityModelsDataEditorPanel({
            id:         'modelEditorPanel'
        });
        this.pages['modelEditor'] = this.goToModelEditor;
    },

    initPortalToolbar: function() {
        var menuItems = new Array();
        menuItems[menuItems.length] = new Ext.menu.Item({
                    icon:       requestContextPath + '/images/homepage.gif',
                    text:       getI18nResource("caqsPortal.dashboard"),
                    handler:    this.goToDashboard,
                    scope:      this
                });
        menuItems[menuItems.length] = new Ext.menu.Item({
                    icon:       requestContextPath + '/images/wheel.gif',
                    text:       getI18nResource("caqsPortal.quality"),
                    handler:    this.goToQualityPage,
                    scope:      this
                });
        if(canAccessFunction("ADMINISTRATION_ACCESS") || isUserInRole("ADMINISTRATOR")) {
            menuItems[menuItems.length] = new Ext.menu.Item({
                        icon:       requestContextPath + '/images/wrench.gif',
                        text:       getI18nResource("caqsPortal.admin"),
                        handler:    this.goToAdministration,
                        scope:      this
                    });
        }
        if(canAccessFunction("ARCHITECTURE_ACCESS")) {
            menuItems[menuItems.length] = new Ext.menu.Item({
                        icon:       requestContextPath + '/images/brick.gif',
                        text:       getI18nResource("caqsPortal.architecture"),
                        handler:    this.goToArchitecture,
                        scope:      this
                    });
        }
        menuItems[menuItems.length] = new Ext.menu.Item({
                    icon:       requestContextPath + '/images/tick.gif',
                    text:       getI18nResource("caqsPortal.justif"),
                    handler:    this.goToJustification,
                    scope:      this
                });
        menuItems[menuItems.length] = new Ext.menu.Item({
                    icon:       requestContextPath + '/images/award_star_gold_2.gif',
                    text:       getI18nResource("caqsPortal.label"),
                    handler:    this.goToLabellisation,
                    scope:      this
                });
        if(canAccessFunction("Model_Editor")) {
            menuItems[menuItems.length] = new Ext.menu.Item({
                        icon:       requestContextPath + '/images/cog_edit.gif',
                        text:       getI18nResource("caqs.admin.qualimero"),
                        handler:    this.goToModelEditor,
                        scope:      this
                    });
        }
        menuItems[menuItems.length] = new Ext.menu.Item({
                    icon:       requestContextPath + '/images/user.gif',
                    text:       getI18nResource("caqsPortal.userPreference"),
                    handler:    this.goToPreferences,
                    scope:      this
                });
        menuItems[menuItems.length] = new Ext.menu.Item({
                    icon:       requestContextPath + '/images/help.gif',
                    text:       getI18nResource("caqsPortal.help"),
                    handler:    this.helpAboutThisPage,
                    scope:      this
                });
        var documentationsArray = new Array();
        if(this.documentations != null) {
            for(i=0; i<this.documentations.length; i++) {
                documentationsArray[documentationsArray.length] = new Ext.menu.Item({
                                text:           this.documentations[i].name,
                                handler:    function(b) {
                                    window.open(requestContextPath + '/../CAQSdoc/'+b.text,'_blank')
                                },
                                scope:          this
                            });
            }
        }
        if(documentationsArray.length>0) {
            menuItems[menuItems.length] = {
                        icon:       requestContextPath + '/images/page_word.gif',
                        text:       getI18nResource("caqsPortal.documentation"),
                        menu:       documentationsArray
                    };
        }
        menuItems[menuItems.length] = new Ext.menu.Item({
                    icon:       requestContextPath + '/images/information.gif',
                    text:       getI18nResource("caqsPortal.about"),
                    handler:    this.about,
                    scope:      this
                });
        menuItems[menuItems.length] = new Ext.menu.Item({
                    icon:       requestContextPath + '/images/disconnect.gif',
                    text:       getI18nResource("caqsPortal.logout"),
                    handler:    this.logout,
                    scope:      this
                });
        this.menu = new Ext.menu.Menu({
            items: menuItems
        });

        this.activitiesButton = new Ext.Button({
            id:         'activitiesButtonId',
            handler:    this.showMenu,
            scope:      this,
            cellId:     'activityBtnCell',
            text:       '&nbsp;&nbsp;&nbsp;'+getI18nResource("caqsPortal.activity"),
            cls:		"x-btn-text-icon",
            icon:       requestContextPath+'/images/miniLogoCac.gif',
            cellVAlign: 'bottom',
            rowspan:    2
        });

        this.locationField = new Ext.ux.form.StaticTextField({
            id:                 'locationFieldStaticTextField',
            cellId:             'locationFieldCell',
            parentClassName:    'smallerStaticField',
            smallLine:          true,
            htmlEncode:         false,
            value:              ''
        });

        var locationPanel = new Ext.Panel({
            border:     false,
            cellId:     'locationFieldCell',
            cellAlign:  'right',
            cellStyle:  'width: 80%;',
            cellVAlign: 'bottom',
            items: [
                this.locationField
            ]
        });

        var welcomePanel = new Ext.Panel({
            colspan:    2,
            border:     false,
            cellId:     'welcomeCellId',
            cellAlign:  'right',
            style:      'margin-bottom: 3px;',
            html:       '<span class="normalText" style="font-weight: bold;">'+getI18nResource("caqs.header.connectionString") + userName + '</span>'
        });

        var headerRightPanel = new Ext.Panel({
            id:         'headerRightPanel',
            header:     false,
            frame:      true,
            style:      'margin-right: 5px;',
            height:     this.portalNorthPanelHeight - 5,
            layout:     'table',
            cellId:     'headerRightPanelCellId',
            cellStyle:  'width: 30%;',
            layoutConfig: {
                columns:    2
            },
            items: [
                welcomePanel
                , locationPanel
                , this.activitiesButton
            ]
        });

        var caqsLogo = new Ext.ux.ComponentImage({
            id:   'caqsPortalLogo',
            cellStyle:  'width: 350px;',
            src: requestContextPath+'/images/CAQS_logo.gif'
        });


        var nbColumns = 2;
        //var customHeaderTitle = getI18nResourceOrNull('caqs.customHeaderTitle');
        //var customHeaderImg = getI18nResourceOrNull('caqs.customHeaderImg');
        if((customHeaderLeft != null && customHeaderLeft != '') || (customHeaderRight!=null && customHeaderRight!='')) {
            nbColumns = 3;
        }
        var portalToolbarItemsArray = new Array();
        portalToolbarItemsArray[portalToolbarItemsArray.length] = caqsLogo;
        if(nbColumns == 3) {
            var customheaderItems = new Array();
            if(customHeaderLeft != null && customHeaderLeft != '' != null) {
                customheaderItems[customheaderItems.length] = {
                    border:     false,
                    html:       customHeaderLeft
                }
            }
            if(customHeaderRight!=null && customHeaderRight!='' != null) {
                customheaderItems[customheaderItems.length] = {
                    border:     false,
                    html:       customHeaderRight
                }
            }
            portalToolbarItemsArray[portalToolbarItemsArray.length] = new Ext.Panel({
                bodyStyle:  'padding-left: 5px; ',
                style:      'margin-left: 5px; text-align:center; margin-right: 5px; ',
                layout:     'table',
                height:     this.portalNorthPanelHeight - 5,
                frame:      false,
                layoutConfig:   {
                    columns:    customheaderItems.length
                },
                items:      customheaderItems
            });
        }
        portalToolbarItemsArray[portalToolbarItemsArray.length] = headerRightPanel;

        this.portalToolbar = new Ext.Panel({
            id:         'caqsPortalToolbar',
            border:     false,
            layout:     'table',
            layoutConfig:   {
                columns:    nbColumns
            },
            height:     this.portalNorthPanelHeight,
            region:     'north',
            items:      portalToolbarItemsArray
        });
    },

    showMenu: function() {
        if(this.menu.isVisible()) {
            this.menu.hide();
        } else {
            this.menu.show('activitiesButtonId');
        }
    },

    initComponent : function(){
        var items = undefined;
        this.pages = new Array();
        this.initAdministration();
        this.initJustification();
        this.initLabelling();
        this.initdashboard();
        this.initUserPreferences();
        this.initQuality();
        this.initArchitecture();
        this.initModelEditor();
        this.initPortalToolbar();

        this.cardArray = [
            this.dashboardPanel,
            this.gestionQualitePanel,
            this.administrationPanel,
            this.architecturePanel,
            this.justificationIFrame,
            this.labellisationIFrame,
            this.modelEditor,
            this.userPreferencesPanel
        ]

        this.aboutWnd = new Ext.ux.AboutWindow();

        if(this.startingPage == undefined) {
            this.startingPage = 'quality';
        }

        this.messagesPanel = new Ext.ux.MessagesPanel({
            messagesZone:       this.messagesZone,
            parentContainer:    this
        });

        //border pour la zone de message au nord
        var portalContainerPanelLayout = 'border';
        var portalContainerItem = undefined;
        var portalContainerPanelId = undefined;
        if(this.messagesZone == 'south') {
            portalContainerPanelId = 'activityPanelId';
            portalContainerPanelLayout = 'card';
            portalContainerItem = this.cardArray;
        } else {
            portalContainerPanelId = 'portalContainerPanel';
            this.activityPanel = new Ext.Panel({
                region:     'center',
                id:         'activityPanelId',
                layout:     'card',
                layoutConfig: {
                    //deferredRender: true
                },
                autoScroll: true,
                border:     false,
                items:      this.cardArray
            });
            portalContainerItem = [
                this.messagesPanel,
                this.activityPanel
            ];
        }

        this.portalContainerPanel = new Ext.Panel({
            id:             portalContainerPanelId,
            layout:         portalContainerPanelLayout,
            layoutConfig: {
                //deferredRender: true
            },
            region:         'center',
            border:         false,
            autoScroll:     true,
            items:          portalContainerItem
        });

        if(this.messagesZone == 'south') {
            this.activityPanel = this.portalContainerPanel;
            items = [
                    this.messagesPanel,
                    this.portalToolbar,
                    this.portalContainerPanel
            ]
        } else {
            //la zone de messages est au nord, donc contenue dans portalContainerPanel
            items = [
                    this.portalToolbar,
                    this.portalContainerPanel
            ]
        }

        Ext.apply(this, Ext.apply(this.initialConfig, {
            items:      items
        }));
        Ext.ux.CaqsPortal.superclass.initComponent.call(this);
    },

    helpAboutThisPage: function() {
        var helpPage = requestContextPath + '/help/';
        helpPage += this.currentScreenId + '_' + languageCode + '.pdf';
        var hauteur = 480;
        var largeur = 640;
        var top=(screen.height-hauteur)/2;
        var left=(screen.width-largeur)/2;
        window.open(helpPage,"","menubar=no,statusbar=no,scrollbars=yes,resizable=yes,top="+top+",left="+left+",width="+largeur+",height="+hauteur);
    },

    goToDashboard: function() {
        if(this.dashboardPanel != this.activeCmpt) {
            this.dashboardPanel.refresh();
            this.showPage(0, getI18nResource("caqsPortal.dashboard"));
        }
    },

    goToQualityPage: function() {
        if(this.gestionQualitePanel != this.activeCmpt) {
            this.gestionQualitePanel.refresh();
            this.showPage(1, getI18nResource("caqsPortal.quality"));
        }
    },

    goToAdministration: function() {
        if(this.administrationPanel != this.activeCmpt) {
            this.showPage(2, getI18nResource("caqsPortal.admin"));
        }
    },

    goToArchitecture: function() {
        if(this.architecturePanel != this.activeCmpt) {
            this.architecturePanel.refresh();
            this.showPage(3, getI18nResource("caqsPortal.architecture"));
        }
    },

    goToJustification: function() {
        if(this.justificationIFrame != this.activeCmpt) {
            this.showPage(4, getI18nResource("caqsPortal.justif"));
        }
    },

    goToLabellisation: function() {
        if(this.labellisationIFrame != this.activeCmpt) {
            this.showPage(5, getI18nResource("caqsPortal.label"));
        }
    },

    goToModelEditor: function() {
        if(this.modelEditor != this.activeCmpt) {
            this.showPage(6, getI18nResource("caqs.admin.qualimero"));
        }
    },

    goToPreferences: function() {
        if(this.userPreferencesPanel != this.activeCmpt) {
            this.showPage(7, getI18nResource("caqsPortal.userPreference"));
        }
    },

    logout: function() {
        logoutFunction();
    },

    about: function() {
        this.aboutWnd.show(this);
    },

    setLocationTitle: function() {
        this.locationField.setValue(this.locationTitle+'&nbsp;>>');
    },

    showPage: function(newActivePanelIndex, place) {
        var l = this.activityPanel.getLayout();
        l.setActiveItem(newActivePanelIndex);
        var activeItem = this.cardArray[newActivePanelIndex];
        if(activeItem!=null && activeItem.lazyLoad!=null) {
            activeItem.lazyLoad();
        }
        this.locationTitle = place;
        this.setLocationTitle();
    },

    goToSelectedFavorite: function(pIdElt, pIdPrj) {
        this.goToQualityPage();
        this.gestionQualitePanel.treesPanel.goToSelectedFavorite(pIdElt, pIdPrj);
    },

    start: function() {
        this.pages[this.startingPage].call(this);
        this.setLocationTitle();
    }
});
