Ext.ux.CaqsDomainSynthesis = function(config) {
    // call parent constructor
    Ext.ux.CaqsDomainSynthesis.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsBottomUpSynthese constructor


Ext.extend(Ext.ux.CaqsDomainSynthesis, Ext.Panel, {
    border:             true,
    layout:             'fit',
    generateReportButton:undefined,
    nbexcel:            0,
    domainId:           undefined,
    tabsPanel:          undefined,
    globalSynthesis:    undefined,
    volumetryPanel:     undefined,
    statsPanel:         undefined,
    gestionQualite:     undefined,
    
    refresh: function(domainId) {
        Caqs.Portal.setCurrentScreen('domainSynthesis');
        this.domainId = domainId;
        if(this.domainId!=null) {
            Ext.Ajax.request({
                url:	requestContextPath+'/DomainSynthesis.do',
                scope:      this,
                params:    {
                    domainId: this.domainId
                },
                success: function(response) {
                    if(response.responseText!='' && response.responseText!='[]') {
                        var json = Ext.util.JSON.decode(response.responseText);
                        if(json.numberOfProjects>0) {
                            this.tabsPanel.setActiveTab(0);
                        } else {
                            this.gestionQualite.setNoAnalysedProjects();
                        }
                    }
                    Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
                }
            });
        }
    },

    createToolBar: function() {
        this.generateReportButton = new Ext.Toolbar.Button({
            text:	getI18nResource("caqs.domainsynthesis.generatesynthesis"),
            icon: 	requestContextPath + '/images/page_white_excel.gif',
            handler:	function() {
                this.nbexcel++;
                location.href = requestContextPath + '/GenerateDomainSynthesisReport.do?domainId='+this.domainId+'&nb='+this.nbexcel;
            },
            scope:   this,
            tooltip: getI18nResource("caqs.domainsynthesis.generatesynthesis.tooltip"),
            cls:     'x-btn-text-icon',
            style:   'margin-left: 10px;'
        });
        var retour = new Ext.Toolbar({
            items: [
            this.generateReportButton
            ]
        });
        return retour;
    },

    showPage: function(tabPanel, newTab, currentTab) {
        if(this.domainId!=null) {
            newTab.getComponent(0).refresh(this.domainId);
        }
    },

    initComponent : function(){
        Ext.ux.CaqsDomainSynthesis.superclass.initComponent.call(this);
        this.globalSynthesis = new Ext.ux.CaqsGlobalDomainSynthesisPanel();
        this.volumetryPanel = new Ext.ux.CaqsDomainSynthesisVolumetryPanel();
        this.statsPanel = new Ext.ux.CaqsDomainSynthesisRepartitionChart();

        this.tabsPanel = new Ext.TabPanel({
            activeTab:		0,
            layoutOnTabChange:	true,
            border:		false,
            bodyStyle:		'padding-top:5px; padding-left:5px; padding-right: 5px; padding-bottom: 5px;',
            hideMode:           'offsets',
            items:[
                {
                    title: 		getI18nResource("caqs.domainsynthese.treemap.title"),
                    border: 		false,
                    layout:             'fit',
                    items:              this.globalSynthesis
                },
                {
                    title: 		getI18nResource("caqs.domainsynthese.synthese"),
                    border: 		false,
                    autoHeight:         true,
                    layout:             'fit',
                    items:              this.volumetryPanel
                },
                {
                    title: 		getI18nResource("caqs.domainsynthese.stats"),
                    border: 		false,
                    layout:             'fit',
                    items:              this.statsPanel
                }
            ],
            listeners: {
                beforetabchange:    this.showPage,
                scope:              this
            }
        });
        var config = {
            tbar:   this.createToolBar()
            ,
            items:  [
            this.tabsPanel
            ]
        };

        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsDomainSynthesis.superclass.initComponent.apply(this, arguments);
    }
});