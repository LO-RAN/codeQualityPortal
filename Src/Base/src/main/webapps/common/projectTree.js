
Ext.ux.CaqsProjectsTreePanel = function(config) {
    // call parent constructor
    Ext.ux.CaqsProjectsTreePanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsProjectsTreePanel constructor


Ext.extend(Ext.ux.CaqsProjectsTreePanel, Ext.Panel, {
    racineProjetsLabel:         '<i>'+getI18nResource("caqs.allProject.racine")+'</i>',
    border:                     true,
    selectedBaselineId:         '',
    selectedBaselineLib:        undefined,
    selectedProjectId:          undefined,
    selectedElementType:        undefined,
    projectsTreeSelectedProjectId: undefined,
    selectedProjectElementId:   undefined,
    baselineStore:              undefined,
    baselineCB:                 undefined,
    treePanel:                  undefined,
    comboboxPanel:              undefined,
    projectTree:                undefined,
    backToProjectsTreeButton:   undefined,
    backToProjectsTreeLabel:    undefined,
    //pas de var car globale
    centerDivIframe:            undefined,
    selectedElementId:          undefined,
    //le texte a afficher a cote du bouton backToProjectTree
    prjPathText:                undefined,
    prjLibText:                 undefined,
    prjDesctext:                undefined,
    eltIdToSelect:              undefined,
    elementToSelectPath:        undefined,
    projectTreeloader:          undefined,
    goToIdPrj:                  undefined,
    commonLayoutContainer:      undefined,
    layout:                     'anchor',
    isRendered:                 false,
    lastUrl:                    undefined,
    lastForceNoDisplayInPopup:  false,

    //affiche la page unavailable
    showBlankPage: function() {
        if(!this.commonLayoutContainer.manageActionForElementType("SELECT_ELEMENT")) {
            this.showPage(requestContextPath + '/unavailable.jsp', true, null);
        }
    },

    refresh: function() {
        this.backToProjectsTree();
        if(this.isRendered) {
            this.treePanel.getRootNode().reload();
        }
    },

    getNodeByRealId: function(tree, realId) {
        for(i in tree.nodeHash) {
            if(tree.nodeHash[i].attributes.realId == realId) {
                return tree.nodeHash[i];
            }
        }
        return null;
    },

    goToSelectedFavoriteManageResponse: function(response) {
		var json = Ext.util.JSON.decode(response.responseText);;
        var split = json.datas;
		if(split!=null) {
            split = split.split('|');
			this.removeProjectTree();
			var idEltPrj = split[0];
            this.prjLibText = split[1];
            this.prjDescText = split[2];
            this.prjPathText = split[3];
            this.elementToSelectPath = split[4];
			var node = this.getNodeByRealId(this.treePanel, idEltPrj);
			if(node != null) {
				this.treePanel.selectPath(node.getPath());
			}
			this.clickOnTree('', idEltPrj, TYPE_PRJ, this.goToIdPrj);
		}
	},

	//appellee quand on clique dans le dashboard sur un element
	goToSelectedFavorite: function(pIdElt, pIdPrj) {
		this.goToIdPrj = pIdPrj;
		this.eltIdToSelect = pIdElt;
		Ext.Ajax.request({
			success: 	this.goToSelectedFavoriteManageResponse,
            scope:      this,
	   		url: 		requestContextPath+'/ManageEltFavorites.do',
	   		params: {
                action:         'GO_TO_FAVORITE_ACTION',
	   			idPro:			pIdPrj,
	   			idEltToSelect:	pIdElt
	   		}
   		});
	},

    //gestion de la combobox
    //assignation en parametre de l'id du projet avant r�cup�ration des baselines
    beforeLoadingBaselines: function(store, options) {
        store.baseParams.id_pro = this.selectedProjectId;
        this.baselineCB.setRawValue('');
    },

    //affichage de la combo si au moins une baseline
    afterLoadingBaselines: function(store, records, options) {
        this.comboboxPanel.setVisible(records.length>0);
        this.treePanel.setVisible(records.length==0);
        if(records.length>0) {
            var mynode = this.getNodeByRealId(this.treePanel, this.projectsTreeSelectedProjectId);
            var path = undefined;
            if(mynode!=undefined) {
                path = mynode.getPath('text');
                path = path.substring(this.racineProjetsLabel.length+1);//+1 car le chemin est /<i>racine</i>/...
            } else {
                //depuis le dashboard
                path = this.prjPathText;
            }

            if(path != undefined) {
                var smallTxt = path;
                if(smallTxt.length>27) {
                    var diff = smallTxt.length - 27;
                    smallTxt = smallTxt.substring(diff,smallTxt.length);
                    smallTxt = '...'+smallTxt;
                }
                this.setTitle(getI18nResource("caqs.projectDetail"));
                this.backToProjectsTreeLabel.setValue(smallTxt);
            }

            this.prjPathText = null;

            this.selectedBaselineId = records[0].data.idBaseline;
            this.selectedBaselineLib = records[0].data.libBaseline;
            this.selectedElementId = undefined;
            this.baselineCB.setValue(this.selectedBaselineId);
            var cmpt = Ext.get(this.commonLayoutContainer.id+'idBaselineCB');
            cmpt.set({qtitle: getI18nResource("caqs.baselineList.lastUpdate")});
            cmpt.set({qtip:records[0].data.lastDMaj});
            cmpt.next('img').set({qtitle: getI18nResource("caqs.baselineList.lastUpdate")});
            cmpt.next('img').set({qtip:records[0].data.lastDMaj});
            this.createProjectTree(this.selectedProjectId);
        }
    },

    removeProjectTree: function() {
        if(this.projectTree!=undefined) {
            this.remove(this.projectTree, true);
            this.projectTree = undefined;
        }
    },

    onchangeCB: function(field, record, index) {
        var v = record.data.idBaseline;
        if(v!='' && v!== this.selectedBaselineId) {
            var cmpt = Ext.get(this.commonLayoutContainer.id+'idBaselineCB');
            cmpt.set({qtitle: getI18nResource("caqs.baselineList.lastUpdate")});
            cmpt.set({qtip: record.data.lastDMaj});
            cmpt.next('img').set({qtitle: getI18nResource("caqs.baselineList.lastUpdate")});
            cmpt.next('img').set({qtip: record.data.lastDMaj});

            this.selectedBaselineId = v;
            this.selectedBaselineLib = record.data.libBaseline;
            this.removeProjectTree();
            this.createProjectTree(this.selectedProjectId);
            if(this.selectedElementId!=undefined && this.commonLayoutContainer.rootURL!=undefined) {
                this.showPage(this.commonLayoutContainer.rootURL, true, 'ENTRYPOINT');
            }
            this.selectedElementId = undefined;
            this.showBlankPage();
        }
    },

    backToProjectsTree: function(button, e) {
        this.setTitle(getI18nResource("caqs.projectList"));
        this.comboboxPanel.setVisible(false);
        this.treePanel.setVisible(true);
        this.selectedElementId = undefined;
        this.removeProjectTree();
        this.selectedProjectId = undefined;
        this.showBlankPage();
        var treePanelSelectedNode = this.treePanel.getSelectionModel().getSelectedNode();
        if(treePanelSelectedNode != null) {
            this.treePanel.getSelectionModel().unselect(treePanelSelectedNode);
        }
    },
    //fin de la gestion de la combobox

    reselectElementOnProjectTree: function() {
        this.showPage(this.lastUrl, this.lastForceNoDisplayInPopup, this.selectedElementType);
    },

    showPage: function(url, forceNoDisplayInPopup, telt) {
        this.lastUrl = url;
        this.lastForceNoDisplayInPopup = forceNoDisplayInPopup;
        var managed = this.commonLayoutContainer.manageActionForElementType(telt);
        if(!managed) {
            if(this.commonLayoutContainer.displayInPopup===false || (forceNoDisplayInPopup==undefined) ||
                (forceNoDisplayInPopup!=undefined && forceNoDisplayInPopup===true)) {
                var centerdiv = this.commonLayoutContainer.iframePanel;
                if(centerdiv!=undefined) {
                    if(this.centerDivIframe==undefined) {
                        this.centerDivIframe = new Ext.ux.ManagedIframePanel({
                            id:             this.commonLayoutContainer.id+'centerDivIframe',
                            name:           this.commonLayoutContainer.id+'centerDivIframe',
                            defaultSrc:     url,
                            autoCreate:     true,
                            width:          '100%',
                            height:         800,
                            border:         false
                        });
                        centerdiv.add(this.centerDivIframe);
                        centerdiv.doLayout();
                    } else {
                        this.centerDivIframe.setSrc(url);
                    }
                }

            } else {
                window.open(url,"","");
            }
        }
    },

    //click sur l'arbre des domaines/projets
    clickOnTree: function(id, realId, telt, id_pro) {
        var n = this.treePanel.getNodeById(id);
        if(n != undefined) {
            n.select();
        }
        if(realId=='ENTRYPOINT') {
            this.selectedProjectId = undefined;
            var url = '';
            if(this.commonLayoutContainer.domainAreClickable) {
                url = requestContextPath + '/DomainSynthesis.do?idDomain=' + realId;
                this.commonLayoutContainer.createURL(null, null, realId,
                    TYPE_DOMAIN, null);
            } else if(this.commonLayoutContainer.rootURL != undefined) {
                url = this.commonLayoutContainer.rootURL;
            }
            this.showPage(url, true, TYPE_ENTRYPOINT);
        } else {
            this.selectedProjectElementId = realId;
            this.selectedElementId = undefined;

            if(telt == TYPE_DOMAIN && this.commonLayoutContainer.domainAreClickable) {
                this.selectedProjectId = undefined;
                url = requestContextPath + '/DomainSynthesis.do?idDomain=' + realId;
                this.commonLayoutContainer.createURL(null, null, realId,
                    TYPE_DOMAIN, null);
                this.showPage(url, true, telt);
            } else if(telt == TYPE_PRJ) {
                this.showBlankPage();
                this.selectedProjectId = id_pro;
                this.projectsTreeSelectedProjectId = realId;
                this.selectedElementType = telt;
                this.baselineCB.store.load({
                    add:false
                });
            }
        }
    },

    //fonction de selection des elements pour une arborescence projet
    selectElementOnProjectTree: function(id, isLink, forceNoDisplayInPopup) {
        if(id==undefined) {
            id = this.selectedProjectElementId;
        }
        this.selectedElementId = id;
        var href = this.commonLayoutContainer.createURL(this.selectedBaselineId,
                        this.selectedProjectId, id, this.selectedElementType, this.selectedBaselineLib);
        if(isLink) {
            this.showPage(href, forceNoDisplayInPopup, this.selectedElementType);
        }
    },

    initComponent : function(){
        //construction de l'arbre des projets
        this.projectTreeloader = new Ext.tree.TreeLoader({
          dataUrl:      this.commonLayoutContainer.projectTreeLoaderURL
        });
        this.projectTreeloader.on("beforeload", function(projectTreeloader, node) {
                projectTreeloader.baseParams.id_elt = node.attributes.realId;
                projectTreeloader.baseParams.completeId = node.attributes.id;
                projectTreeloader.baseParams.id_pro = this.selectedProjectId;
                projectTreeloader.baseParams.id_bline = this.selectedBaselineId;
            }, this);

        var treeloader = new Ext.tree.TreeLoader({
              dataUrl:  this.commonLayoutContainer.projectsTreeLoaderURL
            });
        treeloader.on("beforeload", function(treeLoader, node) {
            treeloader.baseParams.id_elt = node.attributes.realId;
            treeloader.baseParams.completeId = node.attributes.id;
        }, this);

        var rootNode = new Ext.tree.AsyncTreeNode({
                                    text:       this.racineProjetsLabel,
                                    draggable:  false, // disable root node dragging
                                    attributes: "ENTRYPOINT",
                                    iconCls:    'entrypoint',
                                    id:         this.commonLayoutContainer.id+'ENTRYPOINT',
                                    realId:     'ENTRYPOINT'
                                });
        this.treePanel = new Ext.tree.TreePanel({
            animate:			true,
            border:				false,
            autoScroll:			true,
            containerScroll: 	true,
            draggable:			false,
            loader: 			treeloader,
            anchor:				'100% 100%',
            root:				rootNode
        });
        treeloader.on('load', function(obj, node, response) {
            
        }, this);
        this.treePanel.on('beforeclick', this.setNodeClickFunctionProjectsTree, this);
        /*rootNode.on('expand', function() {
             // render the tree
            this.clickOnTree('ENTRYPOINT', 'ENTRYPOINT', 'ROOT');
        }, this, {single: true});*/
        // add a tree sorter in folder mode
        new Ext.tree.TreeSorter(this.treePanel, {
            folderSort: true,
            sortType: function(n) {
                return n.attributes.telt+n.text;
            }
        });

        this.baselineStore = new Ext.ux.CaqsJsonStore({
            url: requestContextPath + '/BaselineListAjax.do',
            fields: ['idBaseline', 'libBaseline', 'lastDMaj']
        });

        this.baselineStore.addListener('beforeload', this.beforeLoadingBaselines, this);
        this.baselineStore.addListener('load', this.afterLoadingBaselines, this);

        this.baselineCB = new Ext.form.ComboBox({
            name:       	this.commonLayoutContainer.id+'idBaselineCB',
            id:         	this.commonLayoutContainer.id+'idBaselineCB',
            displayField:	'libBaseline',
            valueField: 	'idBaseline',
            hiddenName: 	'idBaseline',
            editable:		false,
            store:		this.baselineStore,
            triggerAction:	'all',
            autocomplete:	false,
            anchor:			'99%',
            mode:      		'local'
        });
       this.baselineCB.addListener('select', this.onchangeCB, this);

       this.backToProjectsTreeButton = new Ext.Button({
            icon:       requestContextPath + '/images/arrow_left.gif',
            scope:      this,
            handler:    this.backToProjectsTree
       });

       this.backToProjectsTreeLabel = new Ext.ux.form.StaticTextField({
            value: 	'',
            id: 	this.commonLayoutContainer.id+'backToProjectsTreeLabel',
            border:	false,
            height:	25,
            style:      'font-size: 14px;',
            name: 	'backToProjectsTreeLabel'
       });
       this.backToProjectsTreeLabel.setHtmlEncode(false);

       this.comboboxPanel = new Ext.Panel({
            border:         false,
            bodyStyle:      'padding-left:5px;',
            anchor:         '95% 15%',
            hideMode:       'offsets',
            items : [
                {
                    xtype:      'panel',
                    layout:     'table',
                    border:     false,
                    style:      'margin-top:10px;margin-bottom:10px;',
                    layoutConfig: {
                        columns : 2
                    },
                    items :[
                        this.backToProjectsTreeButton,
                        this.backToProjectsTreeLabel
                    ]
                },
                this.baselineCB
            ]
        });

        var config = {
            region:     'west',
            title:	getI18nResource("caqs.projectList"),
            split:	true,
            width: 	200,
            minSize: 	175,
            maxSize: 	400,
            layout:	'anchor',
            collapsible: true,
            items:       [
                            this.treePanel,
                            this.comboboxPanel
            ]
        }; // eo config object

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsProjectsTreePanel.superclass.initComponent.apply(this, arguments);
    },

    getProjectTreeSelectedNode: function() {
        var retour = null;
        if(this.projectTree) {
            retour = this.projectTree.getSelectionModel().getSelectedNode();
        }
        return retour;
    },

    //creation de l'arbre du projet
    createProjectTree: function() {
        this.projectTree = new Ext.tree.TreePanel({
                        animate:		true,
                        autoScroll:		true,
                        containerScroll: 	true,
                        border:			false,
                        loader:			this.projectTreeloader,
                        draggable:		false,
                        style:			'margin-top:10px;',
                        width:			this.getSize().width,
                        anchor:			'100% 83%'/*,
                        height:			495*/
                 });
        // add a tree sorter in folder mode
        new Ext.tree.TreeSorter(this.projectTree, {folderSort:true});

        //recuperation du projet selectionne
        var newRootNodeText = '';
        var newRootNodeQtip = '';
        var selectedNode = this.treePanel.getSelectionModel().getSelectedNode();
        var isLink = true;
        if(selectedNode!=null && selectedNode.realId!='ENTRYPOINT') {
            newRootNodeText = selectedNode.text;
            newRootNodeQtip = selectedNode.attributes.qtip;
            isLink = selectedNode.attributes.isLink;
        } else {
            //s'il n'y a pas d'element selectionne, on vient du dashboard
            newRootNodeText = this.prjLibText;
            newRootNodeQtip = this.prjDescText;
        }
        this.prjLibText = null;
        this.prjDesctext = null;

        var projectTreePanelRoot = new Ext.tree.AsyncTreeNode({
            text: 		newRootNodeText,
            qtip:		newRootNodeQtip,
            iconCls:	'prj',
            draggable:	false, // disable root node dragging
            telt:		TYPE_PRJ,
            id: 		this.selectedProjectElementId,
            realId:     this.selectedProjectElementId,
            attributes: this.selectedProjectId,
            isLink:     isLink,
            expanded:	true
        });
        projectTreePanelRoot.on('append', this.projectTreeNodeAppended, this);
        this.projectTree.setRootNode(projectTreePanelRoot);
        this.projectTree.on('beforeclick', this.setNodeClickFunctionProjectTree, this);
        this.commonLayoutContainer.specificProjectTreeWork(this.projectTree);
        this.add(this.projectTree);
        this.doLayout();
    },

    //fonction appelee quand un des noeud d'une arborescence projet a un noeud fils ajoute.
    projectTreeNodeAppended: function(tree, parent, node, index) {
        node.on('append', this.projectTreeNodeAppended, this);
        if(this.elementToSelectPath!=undefined) {
            //nous sommes ici par un click sur un favori
            //nous devons derouler l'arborescence pour arriver au bon element et le selectionner
            var cuts = this.elementToSelectPath.split('/');
            if(cuts!=undefined && cuts.length>0) {
                //on teste si le parent est bien le noeud a afficher
                //c'est a dire si c'est le projet qui est mis en favoris
                if(parent.attributes.realId == cuts[cuts.length-1]) {
                    //nous y sommes
                    parent.select();
                    this.selectElementOnProjectTree(parent.attributes.realId, true);
                } else {
                    if(node.attributes.realId == cuts[0] && node.attributes.linkType != 'S') {
                        //ce noeud fait parti de l'arborescence
                        if(cuts.length==1) {
                            //c'est le noeud a selectionner
                            parent.on("expand", function(){
                                tree.getSelectionModel().select(node, null, true);
                            }, this, {single: true});
                            this.selectElementOnProjectTree(node.attributes.realId, true);
                        } else {
                            //on continue au niveau inferieur
                            var first = true;
                            this.elementToSelectPath = '';
                            for(var i=1; i<cuts.length; i++) {
                                if(!first) {
                                    this.elementToSelectPath += '/';
                                }
                                this.elementToSelectPath += cuts[i];
                                first = false;
                            }
                            node.expand(false, false);
                        }
                    }
                }
            }
        }
    },

    setNodeClickFunctionProjectTree: function(node, event) {
        this.selectedElementType = node.attributes.telt;
        this.selectElementOnProjectTree(node.attributes.realId, node.attributes.isLink);
        return true;
    },

    setNodeClickFunctionProjectsTree: function(node, event) {
        this.clickOnTree(node.attributes.id, node.attributes.realId,
            node.attributes.telt, node.attributes.idPro);
        return false;
    },

    onRender : function(ct, position){
        Ext.ux.CaqsProjectsTreePanel.superclass.onRender.call(this, ct, position);
        this.comboboxPanel.setVisible(false);
        this.treePanel.getRootNode().expand(false, false);
        this.isRendered = true;
    }
    
});