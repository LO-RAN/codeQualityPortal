Ext.ux.CaqsAdminProjectTree = function(config) {
    // call parent constructor
    Ext.ux.CaqsAdminProjectTree.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsDomainSynthesisRepartitionChart constructor


Ext.extend(Ext.ux.CaqsAdminProjectTree, Ext.Panel, {
    projectsAdmin:              undefined,
    treePanel:                  undefined,
    contextMenu:                undefined,
    selectedTreeNodeForMenu:    undefined,
    createLinkWindow:           undefined,
    symLinkVariable:            undefined,
    id:                         'caqsAdminProjectTreeWest-panel',
    split:                      true,
    width:                      200,
    minSize:                    175,
    maxSize:                    400,
    layout:                     'anchor',
    layoutConfig:{
        animate:true
    },

    initializeCreateLinkWindow: function() {
        var treeloader = new Ext.tree.TreeLoader({
            dataUrl: requestContextPath+'/AdminProjectTree.do'
        });
        treeloader.on("beforeload", function(treeLoader, node) {
            treeloader.baseParams.id_elt = node.attributes.realId;
        }, this);

        // set the root node
        var rootNode = new Ext.tree.AsyncTreeNode({
            text:       '<i>'+getI18nResource("caqs.allProject.racine")+'</i>',
            draggable:  false, // disable root node dragging
            droppable:  true,
            telt:       "ENTRYPOINT",
            id:         'ENTRYPOINT',
            realId:     'ENTRYPOINT',
            iconCls:    'entrypoint'
        });
        var createLinkTreePanel = new Ext.tree.TreePanel({
            animate:		true,
            border:		false,
            loader: 		treeloader,
            root:		rootNode,
            height:		300,
            autoScroll:		true,
            containerScroll: 	true,
            anchor:		'100%',
            enableDD:		false,
            parentScope:        this
        });
        // add a tree sorter in folder mode
        new Ext.tree.TreeSorter(createLinkTreePanel, {
            folderSort:true,
            sortType: function(n) {
                return n.attributes.telt+n.text;
            }
        });

        var validateButton = new Ext.Button({
            handler:    this.createLink,
            text:       getI18nResource("caqs.admin.adminProjects.selectElementForLink"),
            disabled:   true,
            scope:      createLinkTreePanel
        });

        this.createLinkWindow = new Ext.Window({
            modal:          true,
            maximizable:    true,
            resizable:      true,
            layout:         'fit',
            minimizable:    false,
            width:          550,
            autoHeight:     true,
            shadow:         false,
            plain:          true,
            x:              20,
            y:              20,
            items:	[
            createLinkTreePanel
            ],
            buttons: [
            validateButton,
            new Ext.Button({
                text:       getI18nResource('"caqs.annuler"'),
                handler:    function(){
                    this.createLinkWindow.hide();
                },
                scope:      this
            })
            ]
        });
        this.createLinkWindow.on('beforeclose', function(wnd) {
            this.createLinkWindow.hide();
            return false;
        }, this);
        createLinkTreePanel.on('click', function(node, event) {
            var disabled = true;

            if(node.attributes.telt == 'DOMAIN') {
                disabled = false;
            } else {
                disabled = true;
            }

            if(!disabled) {
                var selectedParentLinkNode = node;
                var selectedChildLinkNode = this.parentScope.selectedTreeNodeForMenu;

                //le parent est-il deja ancetre du fils ?
                var tmpNode = selectedChildLinkNode;
                while(tmpNode!=undefined && !disabled) {
                    if(tmpNode.attributes.id == selectedParentLinkNode.attributes.id) {
                        disabled = true;
                    }
                    tmpNode = tmpNode.parentNode;
                }
                if(disabled == false) {
                    //le fils est-il deja ancetre du parent ?
                    tmpNode = selectedParentLinkNode;
                    while(tmpNode!=undefined && !disabled) {
                        if(tmpNode.attributes.id == selectedChildLinkNode.attributes.id) {
                            disabled = true;
                        }
                        tmpNode = tmpNode.parentNode;
                    }
                }
            }
            validateButton.setDisabled(disabled);
            return true;
        }, createLinkTreePanel);
        this.createLinkWindow.on('show', function() {
            if(!rootNode.isExpanded()) {
                rootNode.expand(false, /*no anim*/ false);
            } else {
                rootNode.reload();
            }
        }, this);
    },

    createLinkTreeNode: function() {
        if(this.symLinkVariable != undefined) {
            this.addNodeToTree(this.symLinkVariable.childId+'-link',
                this.symLinkVariable.desc,
                this.symLinkVariable.telt,
                this.symLinkVariable.cls,
                this.symLinkVariable.fatherId,
                'S'
                )

            this.symLinkVariable = undefined;
        }
    },

    deleteLinkTreeNode: function() {
        if(this.symLinkVariable != undefined) {
            var n = this.treePanel.getNodeById(this.symLinkVariable.childId);
            if(n != undefined) {
                n.select();
                this.deleteNodeFromTree();
            }
            this.symLinkVariable = undefined;
        }
    },

    deleteSymLink: function() {
        var selectedLinkNode = this.selectedTreeNodeForMenu;
        var selectedParentLinkNode = this.selectedTreeNodeForMenu.parentNode;
        this.symLinkVariable = {
            childId:    selectedLinkNode.attributes.id
        };

        Ext.Ajax.request({
            url:		requestContextPath+'/ElementManageSymbolicLink.do',
            params: {
                new_father_id:  selectedParentLinkNode.attributes.realId,
                new_child_id:   selectedLinkNode.attributes.realId,
                action:         'delete'
            },
            scope:      this,
            success:	this.deleteLinkTreeNode
        });
    },
    
    createLink: function() {
        var selectedParentLinkNode = this.getSelectionModel().getSelectedNode();
        var selectedChildLinkNode = this.parentScope.selectedTreeNodeForMenu;
        this.parentScope.symLinkVariable = {
            childId:    selectedChildLinkNode.attributes.realId,
            fatherId:   selectedParentLinkNode.attributes.realId,
            desc:       selectedChildLinkNode.attributes.text,
            telt:       selectedChildLinkNode.attributes.telt,
            cls:        selectedChildLinkNode.attributes.iconCls+'-link'
        };

        Ext.Ajax.request({
            url:		requestContextPath+'/ElementManageSymbolicLink.do',
            params: {
                new_father_id:  selectedParentLinkNode.attributes.realId,
                new_child_id:   selectedChildLinkNode.attributes.realId,
                action:         'create'
            },
            scope:      this.parentScope,
            success:	this.createLinkTreeNode
        });
        this.parentScope.createLinkWindow.hide();
    },

    showCreateLinkWindow: function() {
        if(this.createLinkWindow == undefined) {
            this.initializeCreateLinkWindow();
        }
        var title = getI18nResource("caqs.admin.adminProjects.addLinkWindowTitle");
        title += '&nbsp;'+this.treePanel.getSelectionModel().getSelectedNode().attributes.text;
        this.createLinkWindow.setTitle(title);
        this.createLinkWindow.show(this);
    },

    showContextMenu: function(node) {
        this.selectedTreeNodeForMenu = node;

        //on affiche/cache les options selon le type
        //ENTRYPOINT
        if(node.attributes.telt == 'ENTRYPOINT' && caqsUserRights["ALL_PROJECT_ADMIN"]) {
            var item = this.contextMenu.items.get("deleteItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("addChildDOMAINItem");
            item.setVisible(true);
            var subitem = item.menu.items.get("addDomain");
            subitem.setVisible(caqsUserRights["ADMIN_DOMAIN_MODIFICATION"]);
            item = this.contextMenu.items.get("addChildPROJECTItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("addChildSSPItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("createLink");
            item.setVisible(false);
            item = this.contextMenu.items.get("deleteLink");
            item.setVisible(false);
        } else if(node.attributes.telt == 'DOMAIN') {
            var item = this.contextMenu.items.get("deleteItem");
            item.setVisible(caqsUserRights["ADMIN_DOMAIN_MODIFICATION"] && (node.attributes.linkType != 'S'));
            item = this.contextMenu.items.get("addChildDOMAINItem");
            item.setVisible(caqsUserRights["ADMIN_DOMAIN_MODIFICATION"] && (node.attributes.linkType != 'S'));
            item = this.contextMenu.items.get("addChildPROJECTItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("addChildSSPItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("createLink");
            item.setVisible((node.attributes.linkType != 'S'));
            item = this.contextMenu.items.get("deleteLink");
            item.setVisible((node.attributes.linkType == 'S'));
        } else if(node.attributes.telt == 'PRJ') {
            var item = this.contextMenu.items.get("deleteItem");
            item.setVisible((node.attributes.linkType != 'S'));
            item = this.contextMenu.items.get("addChildDOMAINItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("addChildPROJECTItem");
            item.setVisible((node.attributes.linkType != 'S'));
            item = this.contextMenu.items.get("addChildSSPItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("createLink");
            item.setVisible((node.attributes.linkType != 'S'));
            item = this.contextMenu.items.get("deleteLink");
            item.setVisible((node.attributes.linkType == 'S'));
        } else if(node.attributes.telt == 'SSP') {
            var item = this.contextMenu.items.get("deleteItem");
            item.setVisible(true);
            item = this.contextMenu.items.get("addChildDOMAINItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("addChildPROJECTItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("addChildSSPItem");
            item.setVisible(true);
            item = this.contextMenu.items.get("createLink");
            item.setVisible(false);
            item = this.contextMenu.items.get("deleteLink");
            item.setVisible(false);
        } else if(node.attributes.telt == 'EA') {
            var item = this.contextMenu.items.get("deleteItem");
            item.setVisible(true);
            item = this.contextMenu.items.get("addChildDOMAINItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("addChildPROJECTItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("addChildSSPItem");
            item.setVisible(false);
            item = this.contextMenu.items.get("createLink");
            item.setVisible(false);
            item = this.contextMenu.items.get("deleteLink");
            item.setVisible(false);
        }
        var show = true;
        if(node.attributes.telt == 'DOMAIN' && !caqsUserRights["ADMIN_DOMAIN_MODIFICATION"]) {
            show = false;
        }
        if(show) {
            this.treePanel.getSelectionModel().select(node);
            this.contextMenu.show(node.ui.getAnchor());
        }
    },

    deleteNodeFromTree: function() {
        var selectedNode = this.selectedTreeNodeForMenu;
        var parentNode = selectedNode.parentNode;
        if(selectedNode!==null && parentNode!==null) {
            this.treePanel.selectPath(parentNode.getPath());
            selectedNode.remove();
            this.clickOnTree(parentNode.attributes.realId)
        }
    },

    deleteNodeElement: function() {
        var thisScope = this;
        Ext.MessageBox.confirm(this.selectedTreeNodeForMenu.attributes.text,
            getI18nResource('caqs.admin.confirmDeleteElt'),
            function(result) {
                thisScope.contextMenu.hide();
                if(result=='yes') {
                    Ext.Ajax.request({
                        url:		requestContextPath+'/ElementAdminDelete.do',
                        params: {
                            id_elt:     thisScope.selectedTreeNodeForMenu.attributes.realId,
                            id_telt:    thisScope.selectedTreeNodeForMenu.attributes.telt
                        },
                        scope:      thisScope,
                        success:	this.deleteNodeFromTree
                    });
                }
            }, this);  
    },

    addChild : function(item, event) {
        this.projectsAdmin.editElement(item.telt, null, this.selectedTreeNodeForMenu.attributes.realId);
    },

    setContextMenu: function(){
        this.selectedTreeNodeForMenu = undefined;
        this.contextMenu = new Ext.menu.Menu('adminProjectTreeContextMenu');
        this.contextMenu.add(
            new Ext.menu.Item({//add child for domain and entrypoint
                id:         'addChildDOMAINItem',
                text:       getI18nResource('caqs.admin.addElt'),
                icon:       requestContextPath+'/images/add.gif',
                menu:       new Ext.menu.Menu({
                    id:     'adminProjectTreeContextMenuDomain',
                    items:  [
                    new Ext.menu.Item({
                        id:         'addDomain',
                        text:       TYPE_DOMAIN_LIB,
                        telt:       'DOMAIN',
                        from:       'DOMAIN',
                        icon:       requestContextPath+'/images/customExt/domain.gif',
                        handler:    this.addChild,
                        scope:      this
                    }),
                    new Ext.menu.Item({
                        id:         'addProject',
                        text:       TYPE_PRJ_LIB,
                        from:       'DOMAIN',
                        telt:       'PRJ',
                        icon:       requestContextPath+'/images/customExt/prj.gif',
                        handler:    this.addChild,
                        scope:      this
                    })
                    ]
                }),
                scope:      this
            }),
            new Ext.menu.Item({//add child for project
                id:         'addChildPROJECTItem',
                text:       getI18nResource('caqs.admin.addElt'),
                icon:       requestContextPath+'/images/add.gif',
                menu:       new Ext.menu.Menu({
                    id:     'adminProjectTreeContextMenuProject',
                    items:  [
                    new Ext.menu.Item({
                        id:         'addEA',
                        text:       TYPE_EA_LIB,
                        telt:       'EA',
                        from:       'PRJ',
                        icon:       requestContextPath+'/images/customExt/ea.gif',
                        handler:    this.addChild,
                        scope:      this
                    }),
                    new Ext.menu.Item({
                        id:         'addSSP',
                        text:       TYPE_SSP_LIB,
                        telt:       'SSP',
                        from:       'PRJ',
                        icon:       requestContextPath+'/images/customExt/ssp.gif',
                        handler:    this.addChild,
                        scope:      this
                    })
                    ]
                }),
                scope:      this
            }),
            new Ext.menu.Item({//add child for subproject
                id:         'addChildSSPItem',
                text:       getI18nResource('caqs.admin.addElt'),
                icon:       requestContextPath+'/images/add.gif',
                menu:       new Ext.menu.Menu({
                    id:     'adminProjectTreeContextMenuSSP',
                    items:  [
                    new Ext.menu.Item({
                        id:         'addEA',
                        text:       TYPE_EA_LIB,
                        telt:       'EA',
                        from:       'SSP',
                        icon:       requestContextPath+'/images/customExt/ea.gif',
                        handler:    this.addChild,
                        scope:      this
                    }),
                    new Ext.menu.Item({
                        id:         'addSSP',
                        text:       TYPE_SSP_LIB,
                        telt:       'SSP',
                        from:       'SSP',
                        icon:       requestContextPath+'/images/customExt/ssp.gif',
                        handler:    this.addChild,
                        scope:      this
                    })
                    ]
                }),
                scope:      this
            }),
            new Ext.menu.Item({//suppress element for all but entrypoint
                id:         'deleteItem',
                text:       getI18nResource('caqs.perimer'),
                icon:       requestContextPath+'/images/delete.gif',
                handler:    this.deleteNodeElement,
                scope:      this
            }),
            new Ext.menu.Item({//create link for all but entrypoint
                id:         'createLink',
                text:       getI18nResource("caqs.admin.adminProjects.createLink"),
                icon:       requestContextPath+'/images/link_add.gif',
                handler:    this.showCreateLinkWindow,
                scope:      this
            }),
            new Ext.menu.Item({//create link for all but entrypoint
                id:         'deleteLink',
                text:       getI18nResource("caqs.admin.adminProjects.deleteLink"),
                icon:       requestContextPath+'/images/link_delete.gif',
                handler:    this.deleteSymLink,
                scope:      this
            })
            );
        this.treePanel.on('contextmenu', this.showContextMenu, this);
    },

    reloadRoot: function() {
        this.treePanel.getRootNode().reload();
        if(this.treePanel.isVisible()) {
            this.treePanel.getRootNode().select();
        }
        if(!this.treePanel.getRootNode().isExpanded()) {
            this.treePanel.getRootNode().expand();
        } else {
            this.clickOnTree('ENTRYPOINT');
        }
    },

    treePanelNodeDrop: function(dragOverEvent) {
        var dropNode = dragOverEvent.dropNode;
        Ext.Ajax.request({
            url:    requestContextPath + '/AdminProjectTreeDD.do',
            scope:  this,
            params: {
                'source': dragOverEvent.dropNode.attributes.realId,
                'target': dragOverEvent.target.attributes.realId
            },
            success:    function() {
                this.clickOnTree(dropNode.attributes.realId);
            }
        });
    },

    treePanelNodeDragOver: function(dragOverEvent) {
        var retour = false;
        var targetTelt = dragOverEvent.target.attributes.telt;
        var sourceTelt = dragOverEvent.dropNode.attributes.telt;
        if(sourceTelt==='DOMAIN') {
            if((targetTelt==='DOMAIN' || targetTelt==='ENTRYPOINT')) {
                retour = true;
            }
        } else if(sourceTelt==='PRJ') {
            if((targetTelt==='DOMAIN' || targetTelt==='ENTRYPOINT')) {
                retour = true;
            }
        } else if(sourceTelt==='SSP') {
            if((targetTelt==='PRJ' || targetTelt==='SSP')) {
                retour = true;
            }
        } else if(sourceTelt==='EA') {
            if((targetTelt==='PRJ' || targetTelt==='SSP')) {
                retour = true;
            }
        }

        if(sourceTelt==='EA' || sourceTelt==='SSP') {
            var idPrjSrc = '';
            var idPrjTarget = '';

            var tmpNode = dragOverEvent.dropNode.parentNode;
            while(tmpNode!=undefined && tmpNode.attributes.telt!='PRJ') {
                tmpNode = tmpNode.parentNode;
            }
            if(tmpNode!=undefined) {
                idPrjSrc = tmpNode.attributes.realId;
            }

            tmpNode = (dragOverEvent.target.attributes.telt!='PRJ') ? dragOverEvent.target.parentNode : dragOverEvent.target;
            while(tmpNode!=undefined && tmpNode.attributes.telt!='PRJ') {
                tmpNode = tmpNode.parentNode;
            }
            if(tmpNode!=undefined) {
                idPrjTarget = tmpNode.attributes.realId;
            }

            if(idPrjSrc!==idPrjTarget || tmpNode==undefined) {
                retour = false;
            }
        }
        return retour;
    },

    initComponent : function(){
        Ext.ux.CaqsAdminProjectTree.superclass.initComponent.call(this);
        var treeloader = new Ext.tree.TreeLoader({
            dataUrl: requestContextPath+'/AdminProjectTree.do'
        });
        treeloader.on("beforeload", function(treeLoader, node) {
            treeloader.baseParams.id_elt = node.attributes.realId;
        }, this);

        // set the root node
        var rootNode = new Ext.tree.AsyncTreeNode({
            text:       '<i>'+getI18nResource("caqs.allProject.racine")+'</i>',
            draggable:  false, // disable root node dragging
            droppable:  true,
            telt:       "ENTRYPOINT",
            id:         'ENTRYPOINT',
            realId:     'ENTRYPOINT',
            iconCls:    'entrypoint',
            ddScroll:   true
        });

        this.treePanel = new Ext.tree.TreePanel({
            animate:	true,
            border:	false,
            loader: 	treeloader,
            root:	rootNode,
            //height:	550,
            anchor:     '100% 100%',
            autoScroll:	true,
            //containerScroll: 	true,
            //anchor:				'100%',
            ddAppendOnly:true,
            ddScroll:   true,
            enableDD:	caqsUserRights["ADMIN_DRAGDROP"]
        });
        this.treePanel.on('nodedragover', this.treePanelNodeDragOver, this);
        this.treePanel.on('nodedrop', this.treePanelNodeDrop, this);
        this.treePanel.on('beforeappend', this.onBeforeTreeAppend, this);
        this.treePanel.on('beforeclick', function(node, event) {
            if(node.attributes.linkType != 'S') {
                this.clickOnTree(node.attributes.realId);
            }
            return false;
        }, this);
        // add a tree sorter in folder mode
        new Ext.tree.TreeSorter(this.treePanel, {
            folderSort:true,
            sortType: function(n) {
                return n.attributes.telt+n.text;
            }
        });
        
        this.add(this.treePanel);
        this.setContextMenu();
    },

    onBeforeTreeAppend: function( tree, parent, node) {
        var id = node.attributes.id;
        var cpt = 1;
        var tmpId = id;
        while( tree.getNodeById(tmpId) != undefined) {
            tmpId = node.attributes.id + cpt;
            cpt++;
        }
        node.attributes.id = tmpId;
        node.id = tmpId;
    },

    clickOnTree: function(id) {
        if(this.treePanel.isVisible()) {
            var n = this.treePanel.getNodeById(id);
            if(n != undefined) {
                this.projectsAdmin.editElement(n.attributes.telt, id);
                n.select();
            }
        }
    },

    modifyNodeText: function(id, newlib, newtooltip) {
        var selectednode = this.treePanel.getNodeById(id);
        selectednode.setText(newlib);
        selectednode.updateqtip(newtooltip);
    },

    addNodeToTree: function(id, lib, desc, telt, cls, fatherId, type) {
        var selectednode = this.treePanel.getNodeById(fatherId);
        type = (type!=undefined) ? type : 'T';
        var newNode = new Ext.tree.TreeNode({
            id      : id,
            realId  : id,
            text    : lib,
            leaf    : (telt==='EA') || type == 'S',
            telt    : telt,
            iconCls : cls,
            linkType: type,
            qtip:     desc
        });
        if(selectednode!==null) {
            newNode = selectednode.appendChild(newNode);
            newNode.ensureVisible();
            this.treePanel.getSelectionModel().select(newNode);
            this.clickOnTree(id);
        }
    }
});