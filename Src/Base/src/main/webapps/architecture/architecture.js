Ext.ux.ArchitecturePanel = function(config) {
    // call parent constructor
    Ext.ux.ArchitecturePanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.ArchitecturePanel, Ext.ux.CommonLayoutPanel, {

    manageActionForElementType: function(telt) {
        if(TYPE_DOMAIN == telt || TYPE_ENTRYPOINT == telt) {

        } else {

        }
        return false;
    },

    initComponent : function(){
        Ext.apply(this, Ext.apply(this.initialConfig, {
            hideMode:               'offsets',
            region:                 'center',
            id:                     'architecturePanel',
            projectTreeLoaderURL:   requestContextPath + '/ArchitectureProjectsTree.do',
            //projectTreeRootURL:     'showPage(\''+requestContextPath+'/common/unavailable.jsp\', true);',
            projectsTreeLoaderURL:  requestContextPath + '/ArchitectureProjectsTree.do?projectsTree=true',
            displayInPopup:         true,
            rootURL:                requestContextPath + '/common/unavailable.jsp',
            domainAreClickable:     false,
            menuC:                  undefined,
            selectedTreeNodeForMenu:undefined,

            createURL:              function(id_bline, id_pro, id_elt, telt) {
                    var retour = '';
                    if(telt == TYPE_EA) {
                        retour = requestContextPath + '/architecture/architectureApplet.jsp?id_bline='+id_bline+
                            '&id_pro='+id_pro+'&id_elt='+id_elt;
                    } else {
                        retour = this.rootURL;
                    }
                    return retour;
            },

            openApplet: function() {
                this.menuC.hide();
                this.treesPanel.selectedElementType = TYPE_EA;
                this.treesPanel.selectElementOnProjectTree(
                                    this.selectedTreeNodeForMenu.attributes.realId,
                        this.selectedTreeNodeForMenu.attributes.isLink,
                                        true);
            },

            openAppletInPopup: function() {
                this.menuC.hide();
                this.treesPanel.selectedElementType = TYPE_EA;
                this.treesPanel.selectElementOnProjectTree(
                                        this.selectedTreeNodeForMenu.attributes.realId,
                        this.selectedTreeNodeForMenu.attributes.isLink,
                                        false);
            },

            menuShow: function(node) {
                if(node.attributes.telt == TYPE_EA) {
                    this.selectedTreeNodeForMenu = node;
                    this.menuC.show(node.ui.getEl());
                }
            },

            specificWork: function() {
            },
            specificProjectTreeWork: function(tree) {
                this.selectedTreeNodeForMenu = undefined;
                this.menuC = new Ext.menu.Menu('mainContext');
                this.menuC.add(
                    new Ext.menu.Item({
                            text: 		getI18nResource("architecture.menu.open"),
                            icon:		requestContextPath + '/images/application.gif',
                            handler: 	this.openApplet,
                            scope:      this
                        }),
                    new Ext.menu.Item({
                            text:       getI18nResource("architecture.menu.openPopup"),
                            icon:		requestContextPath + '/images/application_double.gif',
                            handler:    this.openAppletInPopup,
                            scope:      this
                        })
                );
                tree.on('contextmenu', this.menuShow, this);
            }
        }));
        Ext.ux.ArchitecturePanel.superclass.initComponent.apply(this, arguments);
    },

    onRender : function(ct, position){
        Ext.ux.ArchitecturePanel.superclass.onRender.call(this, ct, position);
    }
});

