Ext.ux.CaqsAdministrationPanel = Ext.extend(Ext.TabPanel, {
    id:                 'caqsAdministration',
    activeTab:		0,
    border:             false,
    titlecollapse:      true,
    hideCollapseTool:   true,
    layoutOnTabChange:	true,
    accessImportExport: false,
    projectsPanel:      undefined,
    trash:              undefined,
    traductionIFrame:   undefined,
    userRightsGrid:     undefined,

    lazyLoad: function() {
        this.setActiveTab(0);
        if(this.projectsPanel!=undefined) {
            this.projectsPanel.refresh();
        }
    },
    
    beforeTabChangeFn: function(panel, newTab, currentTab) {
        if(currentTab != newTab && currentTab!=0){
            if(this.projectsPanel!=undefined && newTab.id=='projects') {
                this.projectsPanel.refresh();
            } else if(newTab.id=='trash') {
                this.trash.refreshGrid();
            } else if(this.traductionIFrame!=undefined && newTab.id=='traductions') {
                Caqs.Portal.setCurrentScreen('traductions');
                if(this.traductionIFrame.getFrameWindow()!=null) {
                    this.traductionIFrame.setSrc('InternationalizationUploadPre.do');
                }
            } else if(newTab.id=='userRights') {
                this.userRightsGrid.refreshGrid();
            }
        }
    },

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsAdministrationPanel.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        Ext.ux.CaqsAdministrationPanel.superclass.initComponent.call(this);

        this.trash = new Ext.ux.CaqsTrashGridPanel();

        this.projectsPanel = new Ext.ux.CaqsProjectsAdminPanel();
        this.traductionIFrame = new Ext.ux.ManagedIframePanel({
            defaultSrc:	'InternationalizationUploadPre.do',
            loadMask:	true,
            autoCreate:	true,
            border: 	false,
            width:	'100%',
            height:	630,
            nocache:	true
        });

        var userRightsGridPanel = new Ext.ux.CaqsUserRightsGridPanel({
            reader:         userRightsGridReader,
            checkColumns:   userRightsGridCheckColumns
        });

        this.userRightsGrid = new Ext.Panel({
            border:     false,
            header:     false,
            layout:     'anchor',
            items: [
            new Ext.Panel({
                anchor:     '98%',
                border:      true,
                frame:       true,
                style:       'margin-top: 5px; margin-left: 5px; margin-right: 5px;',
                items:       [
                {
                    border:  false,
                    html:    '<img src="'+requestContextPath+'/images/information.gif" />&nbsp;&nbsp;'+getI18nResource("caqs.userRights.restart")
                }
                ]
            }),
            userRightsGridPanel
            ],
            refreshGrid: function() {
                if(userRightsGridPanel) {
                    userRightsGridPanel.refreshGrid();
                }
            }
        });

        this.usersGrid = new Ext.ux.CaqsUserPanel();

        var tabs = [
        {
            id:     'projects',
            title:  getI18nResource("caqs.admin.projets"),
            layout: 'fit',
            items:  this.projectsPanel
        }
        ,{
            id:     'trash',
            title:  getI18nResource("caqs.trash"),
            layout: 'fit',
            items:  this.trash
        }
        ];
        if(canAccessFunction("TRANSLATION")) {
            tabs[tabs.length] = {
                id:     'traductions',
                title:  getI18nResource("caqs.traduction.title"),
                layout: 'fit',
                items:  this.traductionIFrame
            };
        }
        if(canAccessFunction("USER_ADMIN_ACCESS") || isUserInRole("ADMINISTRATOR")) {
            tabs[tabs.length] = {
                id:     'userRights',
                title:  getI18nResource("caqs.admin.userRights"),
                layout: 'fit',
                items:  this.userRightsGrid
            };
            tabs[tabs.length] = {
                id:     'users',
                title:  getI18nResource("caqs.admin.users"),
                layout: 'fit',
                items:  this.usersGrid
            };
        }

        var config = {
            items:  tabs
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsAdministrationPanel.superclass.initComponent.call(this);
        this.on('beforetabchange', this.beforeTabChangeFn, this);
    },

    refresh: function() {
        
    }
});

