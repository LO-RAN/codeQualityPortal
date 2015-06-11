Ext.ux.CaqsCommonInfosPanel = function(config) {
    // call parent constructor
    Ext.ux.CaqsCommonInfosPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsCommonInfosPanel, Ext.form.FormPanel, {
    projectsAdmin:          undefined,
    hideMode:               'offsets',
    from:                   undefined,
    fatherId:               undefined,
    border:                 true,
    updateButton:           undefined,
    applyChildrenButton:    undefined,
    annulerButton:          undefined,
    multiuserlist:          undefined,
    autoScroll:             true,
    telt:                   undefined,
    idPro:                  undefined,
    libElt:                 undefined,
    idElt:                  undefined,
    title:                  '&nbsp;',
    teltLib:                undefined,
    labelWidth:             140,
    bodyStyle:              'padding:5px 5px 0',
    monitorValid:           true,

    startRefresh: function() {
        Caqs.Portal.getCaqsPortal().getAdministrationActivity().showMask();

        var params = null;
        if(this.idElt != null) {
            params = {
                'id_elt': this.idElt
            };
        } else {
            params = {
              fatherId:     this.fatherId,
              idTelt:       this.telt
            };
        }

        Ext.Ajax.request({
            url:	requestContextPath + '/RetrieveElementData.do',
            params:     params,
            scope:      this,
            success: function(response) {
                Caqs.Portal.getCaqsPortal().getAdministrationActivity().hideMask();
                if(response!=null && response.responseText!='[]') {
                    this.loadDatasFromJSON(Ext.util.JSON.decode(response.responseText));
                    this.findById('admin'+this.telt+'lib').focus();
                }
            }
        });
    },

    setCreateInfos: function(from, fatherId) {
        this.from = from;
        this.fatherId = fatherId;
    },

    setEditionMode: function(create, lib) {
        if(create) {
            this.setTitle(getI18nResource("caqs.addProject.addProject", this.teltLib));
        } else {
            this.setTitle(getI18nResource("caqs.infosCOMMON.propProj", this.teltLib, lib));
        }
        this.findById('admin'+this.telt+'dinst').setVisible(!create);
        this.findById('admin'+this.telt+'dmaj').setVisible(!create);
        this.applyChildrenButton.setVisible(!create);
    },

    champsok: function(){
        /*var eltName = this.mainForm.getForm().findField("lib").getValue();
        eltName = eltName.trim();
        eltName = eltName.toUpperCase();
        this.mainForm.getForm().findField("lib").setValue(eltName);
        return true;*/
    },

    afterSave: function(response) {
        Caqs.Portal.getCaqsPortal().getAdministrationActivity().hideMask();
        if(response!=null && response.responseText!='[]') {
            var json = Ext.util.JSON.decode(response.responseText);
            if(json.beenUpdated == 'true') {
                //on modifie le nom dans l'arborescence
                this.projectsAdmin.modifyCurrentNodeLabel(
                    this.findById('admin'+this.telt+'id').getValue(),
                    this.findById('admin'+this.telt+'lib').getValue(),
                    this.findById('admin'+this.telt+'desc').getValue());
            } else if(json.beenCreated == 'true') {
                var cls = '';
                if('EA'===this.telt) {
                        cls = "ea";
                } else if('DOMAIN'===this.telt) {
                        cls = "domain";
                } else {
                        cls = "prj";
                }
                this.projectsAdmin.projectTree.addNodeToTree(json.id,
						this.findById('admin'+this.telt+'lib').getValue(),
                                                this.findById('admin'+this.telt+'desc').getValue(),
						this.telt, cls, json.fatherId);
            }

        }
    },

    additionnalSave: function(obj) {
      //to be overriden
    },

    saveDatas: function(action) {
        var i=this.findById('admin'+this.telt+'multiuserlist');
        var libElt = this.findById('admin'+this.telt+'lib');
        libElt.setValue(libElt.getValue().toUpperCase());

        var obj = {
            id:     this.findById('admin'+this.telt+'id').getValue(),
            desc:   this.findById('admin'+this.telt+'desc').getValue(),
            lib:    libElt.getValue(),
            idPro:  this.findById('admin'+this.telt+'idPro').getValue(),
            weight: this.findById('admin'+this.telt+'poids').getValue(),
            info1:  this.findById('admin'+this.telt+'info1').getValue(),
            info2:  this.findById('admin'+this.telt+'info2').getValue(),
            users:  i.getValue.call(i),
            telt:   this.telt,
            fatherId:this.fatherId
        };
        this.additionnalSave(obj);
        Caqs.Portal.getCaqsPortal().getAdministrationActivity().showMask();
        Ext.Ajax.request({
            url:    requestContextPath + '/SaveElementData.do',
            params: {
                'element':      Ext.util.JSON.encode(obj),
                'typeAction':   action
            },
            scope:  this,
            success: this.afterSave
        });
    },

    updateFunction: function() {
        this.saveDatas(2);
    },

    applyChildrenFunction: function() {
        this.saveDatas(5);
    },

    annulerAction: function() {
        this.startRefresh();
    },

    getMultiuserlistStore: function() {
        return new Ext.data.Store({
            reader: new Ext.data.JsonReader({
                root: 'rows',
                totalProperty: 'totalCount',
                id: 'id',
                fields: [
                'id', 'lib'
                ]
            })
        });
    },

    initComponent : function(){
        var fromStore=this.getMultiuserlistStore();
        var toStore=this.getMultiuserlistStore();
        
        Ext.ux.CaqsCommonInfosPanel.superclass.initComponent.apply(this, arguments);
        this.updateButton = new Ext.Button({
            text:       getI18nResource("caqs.update"),
            name:       'caqs.admin.update',
            cls:	'x-btn-text-icon',
            icon:	requestContextPath+'/images/database_save.gif',
            formBind:   true,
            handler:    this.updateFunction,
            scope:      this
        });
        this.applyChildrenButton = new Ext.Button({
            text:       getI18nResource("caqs.applyChildren"),
            name:       'caqs.admin.applyChildren',
            formBind:   true,
            handler:    this.applyChildrenFunction,
            scope:      this
        });
        this.annulerButton = new Ext.Button({
            text:       getI18nResource("caqs.annuler"),
            name:       'caqs.admin.cancel',
            handler:    this.annulerAction,
            scope:      this
        });

        this.multiuserlist = new Ext.ux.ItemSelector({
            id:             'admin'+this.telt+'multiuserlist',
            anchor:         '100%',
            xtype:          "itemselector",
            name:           'admin'+this.telt+"multiuserlist",
            hideNavIcons:   false,
            drawUpIcon:     false,
            drawDownIcon:   false,
            drawTopIcon:    false,
            drawBotIcon:    false,
            fieldLabel:     getI18nResource("caqs.infosPRJ.usersAssoc"),
            dataFields:     ["id", "lib"],
            fromStore:      fromStore,
            toStore:        toStore,
            msWidth:        250,
            msHeight:       200,
            valueField:     "id",
            displayField:   "lib",
            imagePath:      requestContextPath + "/ext/plugins/multiselect",
            toLegend:       getI18nResource("caqs.infosDOMAIN.projectUsers"),
            fromLegend:     getI18nResource("caqs.infosDOMAIN.portalUsers"),
            fromTBar:[{
                text:   getI18nResource("caqs.admin.addAllUsers"),
                handler:function(){
                    var i=this.findById('admin'+this.telt+'multiuserlist');
                    i.selectAll.call(i);
                },
                scope:  this
            },
            ' ',
            new Ext.ux.SearchSelectorField({
                store: fromStore,
                paramName: 'search',
                ctCls: 'fill-spacer'
            })
            ],
            listeners: {
                render: function() {
                    //auto adjust width of search box
                    var container_obj = this.el.query('.fill-spacer')[0];
                    if (container_obj) {
                        container_obj.firstChild.style.width = 'auto';
                        var input_obj = container_obj.firstChild.firstChild;
                        input_obj.style.width = container_obj.offsetWidth - 18;
                    }
                }
            }
            ,
            toTBar:[{
                text:   getI18nResource("caqs.admin.removeAllUsers"),
                handler:function(){
                    var i=this.findById('admin'+this.telt+'multiuserlist');
                    i.reset.call(i);
                },
                scope:  this
            },
            ' ',
            new Ext.ux.SearchSelectorField({
                store:      toStore,
                paramName: 'search',
                ctCls: 	'fill-spacer'
            })
            ]
        });
    },

    refreshCommonData: function(json, scope) {
        scope.idPro = json.idPro;
        scope.libElt = json.lib;
        scope.findById('admin'+this.telt+'id').setValue(json.id);
        scope.findById('admin'+this.telt+'desc').setValue(json.desc);
        scope.findById('admin'+this.telt+'lib').setValue(json.lib);
        if(json.dinst) {
            scope.findById('admin'+this.telt+'dinst').setValue(json.dinst);
        }
        scope.findById('admin'+this.telt+'idPro').setValue(json.idPro);
        if(json.dmaj) {
            scope.findById('admin'+this.telt+'dmaj').setValue(json.dmaj);
        }
        scope.findById('admin'+this.telt+'poids').setValue(json.weight);
        scope.findById('admin'+this.telt+'info1').setValue(json.info1);
        scope.findById('admin'+this.telt+'info2').setValue(json.info2);
        if(!((json.info1!=null && json.info1.length>1) || (json.info2!=null && json.info2.length>1))) {
            scope.findById('admin'+this.telt+'infoPanel').collapse();
        } else {
            scope.findById('admin'+this.telt+'infoPanel').expand();
        }
        var i=this.findById('admin'+this.telt+'multiuserlist');
        i.loadDatas.call(i, json.fatherUserCollection, json.userCollection);
        this.setEditionMode(json.id=='', json.lib);
    }

});