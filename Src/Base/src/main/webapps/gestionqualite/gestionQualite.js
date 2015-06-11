Ext.ux.GestionQualitePanel = function(config) {
    // call parent constructor
    Ext.ux.GestionQualitePanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.GestionQualitePanel, Ext.ux.CommonLayoutPanel, {
    domainSynthesisPanel:           undefined,
    projectSynthesisPanel:          undefined,
    eaSynthesisPanel:               undefined,
    bottomUpDetailWnd:              undefined,
    selectedElementId:              undefined,
    selectedBaselineId:             undefined,
    selectedBaselineLib:            undefined,
    nowComputingPanel:              undefined,
    nowComputingMask:               undefined,
    noAnalysedProjects:             undefined,
    projectNotAnalysed:             undefined,

    loadBottomUpDetail: function(title, idElt, tendanceFilterToLoad, previousIdBline) {
        if(this.bottomUpDetailWnd==undefined) {
            this.bottomUpDetailWnd = new Ext.ux.CaqsBottomUpDetailWindow();
        }
        this.bottomUpDetailWnd.loadBottomUpDetail(title, idElt, tendanceFilterToLoad, previousIdBline);
    },

    setCardPanelItems: function() {
        this.cardPanelItems[this.cardPanelItems.length] = this.eaSynthesisPanel;
        this.cardPanelItems[this.cardPanelItems.length] = this.nowComputingPanel;
        this.cardPanelItems[this.cardPanelItems.length] = this.domainSynthesisPanel;
        this.cardPanelItems[this.cardPanelItems.length] = this.noAnalysedProjects;
        this.cardPanelItems[this.cardPanelItems.length] = this.projectSynthesisPanel;
        this.cardPanelItems[this.cardPanelItems.length] = this.projectNotAnalysed;
    },

    setNowComputingMask: function() {
        var l = this.cardPanel.getLayout();
        l.setActiveItem(3);
        if(!this.nowComputingMask) {
            this.nowComputingMask = new Ext.LoadMask(this.nowComputingPanel.getEl(), {
                msg: getI18nResource('caqs.nowcomputing.body')
            });
        }
        this.nowComputingMask.show();
    },

    setNoAnalysedProjects: function() {
        var l = this.cardPanel.getLayout();
        l.setActiveItem(5);
    },

    setProjectNotAnalysed: function() {
        var l = this.cardPanel.getLayout();
        l.setActiveItem(7);
    },

    manageActionForElementType: function(telt) {
        var retour = false;
        var l = this.cardPanel.getLayout();
        if(this.nowComputingMask) {
            this.nowComputingMask.hide();
        }
        if(Caqs.Computation.isComputing(this.selectedElementId)) {
            this.setNowComputingMask();
            retour = true;
        } else {
            if(TYPE_DOMAIN == telt || TYPE_ENTRYPOINT == telt) {
                Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
                l.setActiveItem(4);
                this.domainSynthesisPanel.refresh(this.selectedElementId);
                retour = true;
            } else if(TYPE_EA == telt) {
                Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
                l.setActiveItem(2);
                this.eaSynthesisPanel.refresh(this.selectedElementId, this.selectedBaselineId, this.selectedBaselineLib);
                retour = true;
            } else if((TYPE_PRJ == telt) || (TYPE_SSP==telt)) {
                Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
                l.setActiveItem(6);
                this.projectSynthesisPanel.refresh(this.selectedElementId, this.selectedBaselineId, this.selectedBaselineLib);
                retour = true;
            } else if("SELECT_ELEMENT"===telt) {
                this.setSelectElement();
                retour = true;
            }
        }
        if(!retour) {
            l.setActiveItem(0);
        }
        return retour;
    },

    initComponent : function(){
        this.eaSynthesisPanel = new Ext.ux.CaqsMainFrame({});
        this.domainSynthesisPanel = new Ext.ux.CaqsDomainSynthesis({
            gestionQualite: this
        });
        this.projectSynthesisPanel = new Ext.ux.CaqsProjectSynthesisPanel({
            gestionQualite: this
        });
        this.nowComputingPanel = new Ext.Panel({
            border: true
        });
        this.noAnalysedProjects = new Ext.Panel({
            autoHeigt:              true,
            border:                 true,
            items: {
                fieldLabel: 		'',
                htmlEncode:		false,
                value:      		'<img src="'+requestContextPath+'/images/warning.gif" /><span class="normalText">&nbsp;'
                    +getI18nResource("caqs.domainsynthese.noproject")+'</span>',
                labelSeparator:		'',
                xtype :			'statictextfield'
            }
        });
        this.projectNotAnalysed = new Ext.Panel({
            autoHeigt:              true,
            border:                 true,
            items: {
                fieldLabel: 		'',
                htmlEncode:		false,
                value:      		'<img src="'+requestContextPath+'/images/warning.gif" /><span class="normalText">&nbsp;'
                    +getI18nResource("caqs.projectsynthese.noresults")+'</span>',
                labelSeparator:		'',
                xtype :			'statictextfield'
            }
        });
        Ext.apply(this, Ext.apply(this.initialConfig, {
            region:                 'center',
            id:                     'gestionQualitePanel',
            projectTreeLoaderURL:   requestContextPath + '/ConsultProjectsTree.do',
            //projectTreeRootURL:     'selectElementOnProjectTree();',
            projectsTreeLoaderURL:  requestContextPath + '/ConsultProjectsTree.do?projectsTree=true',
            displayInPopup:         false,
            domainAreClickable:     true,

            createURL:              function(id_bline, id_pro, id_elt, telt, blineLib) {
                this.selectedBaselineId = id_bline;
                this.selectedElementId = id_elt;
                this.selectedBaselineLib = blineLib;
                return requestContextPath + '/ElementSelect.do?id_bline='+id_bline+
                '&id_pro='+id_pro+'&id_elt='+id_elt;
            },
            specificWork: function() {
            },
            specificProjectTreeWork: function(tree) {
            }
        }));
        Ext.ux.GestionQualitePanel.superclass.initComponent.apply(this, arguments);
    },

    refresh: function() {
        Ext.ux.GestionQualitePanel.superclass.refresh.apply(this, arguments);
        Caqs.Portal.getCaqsPortal().setCurrentScreenId('gestion_qualite');
    }
});
