Ext.ux.CaqsProjectsAdminPanel = function(config) {
    Ext.ux.CaqsProjectsAdminPanel.superclass.constructor.call(this, config);
};

Ext.extend(Ext.ux.CaqsProjectsAdminPanel, Ext.Panel, {
    layout:         'border',
    header:         false,
    border:         false,
    projectTree:    undefined,
    editInfosPanel: undefined,
    editPRJ:        undefined,
    editSSP:        undefined,
    editEA:         undefined,
    editDOMAIN:     undefined,
    editROOT:       undefined,
    activeElt:      undefined,

    modifyCurrentNodeLabel: function(eltId, newLabel, newTooltip) {
        this.projectTree.modifyNodeText(eltId, newLabel,newTooltip);
    },

    editElement: function(telt, id, fatherId) {
        var activeItem = 0;
        this.activeElt = null;
        switch(telt) {
            case 'ENTRYPOINT':
                activeItem = 0;
                this.activeElt = this.editROOT;
                break;
            case 'DOMAIN':
                activeItem = 1;
                this.activeElt = this.editDOMAIN;
                break;
            case 'PRJ':
                activeItem = 2;
                this.activeElt = this.editPRJ;
                break;
            case 'SSP':
                activeItem = 3;
                this.activeElt = this.editSSP;
                break;
            case 'EA':
                activeItem = 4;
                this.activeElt = this.editEA;
                break;
        }
        this.editInfosPanel.getLayout().setActiveItem(activeItem);
        this.activeElt.refresh(id, fatherId);
    },
    
    initComponent : function(){
        Ext.ux.CaqsProjectsAdminPanel.superclass.initComponent.apply(this, arguments);
        this.projectTree = new Ext.ux.CaqsAdminProjectTree({
            region:         'west',
            projectsAdmin:  this
        });
        if(caqsUserRights["IMPORT_EXPORT"]) {
            this.editROOT = new Ext.Panel({
                refresh: function() {
                    this.findById('uploadPrjForm').raz();
                },
                items: new Ext.ux.CaqsUploadFileForm({
                                id:             'uploadPrjForm',
                                style:          'margin-top: 5px; margin-left: 5px',
                                title:          getI18nResource("caqs.import.title"),
                                target:         'Project',
                                uploadURL:      requestContextPath + '/ImportProject.do',
                                uploadWaitMsg:  getI18nResource('caqs.importupload.upload')
                            })
            });
        } else {
            this.editROOT = new Ext.Panel({
                html:       '&nbsp;',
                refresh:    function() {}
            });
        }
        this.editDOMAIN = new Ext.ux.CaqsInfosDomain({
            projectsAdmin: this
        });
        this.editEA = new Ext.ux.CaqsInfosEA({
            projectsAdmin: this
        });
        this.editPRJ = new Ext.ux.CaqsInfosPRJ({
            projectsAdmin: this
        });
        this.editSSP = new Ext.ux.CaqsInfosSSP({
            projectsAdmin: this
        });
        this.editInfosPanel = new Ext.Panel({
            layout:         'card',
            region:         'center',
            activeItem:     0,
            border:         false,
            items:          [
                this.editROOT
                , this.editDOMAIN
                , this.editPRJ
                , this.editSSP
                , this.editEA
            ]
        });

        var config = {
            items: [
            this.projectTree,
            this.editInfosPanel
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsProjectsAdminPanel.superclass.initComponent.apply(this, arguments);
    },

    refresh: function() {
        this.projectTree.reloadRoot();
        this.editElement('ENTRYPOINT');
        Caqs.Portal.getCaqsPortal().setCurrentScreenId('adminProjects');
    }
});
