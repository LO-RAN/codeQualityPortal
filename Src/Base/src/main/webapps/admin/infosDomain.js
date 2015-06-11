Ext.ux.CaqsInfosDomain = function(config) {
    // call parent constructor
    Ext.ux.CaqsInfosDomain.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsInfosDomain, Ext.ux.CaqsCommonInfosPanel, {
    from:                       undefined,
    fatherId:                   undefined,
    name:                       undefined,
    canModifyDomain:            undefined,
    telt:                       'DOMAIN',
    teltLib:                    TYPE_DOMAIN_LIB,

    initComponent : function(){
        Ext.ux.CaqsInfosDomain.superclass.initComponent.call(this);

    	    var fromStore=Ext.ux.CaqsInfosDomain.superclass.getMultiuserlistStore.call();
    	    var toStore=Ext.ux.CaqsInfosDomain.superclass.getMultiuserlistStore.call();
    	    
        this.canModifyDomain = canAccessFunction("ADMIN_DOMAIN_MODIFICATION");
        var fromTBARMultiUserList = undefined;
        var toTBARMultiUserList = undefined;
        if(this.canModifyDomain) {
            fromTBARMultiUserList = [{
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
            ];
            toTBARMultiUserList = [{
                    text:   getI18nResource("caqs.admin.removeAllUsers"),
                    handler:function(){
                        var i=this.findById('admin'+this.telt+'multiuserlist');
                        i.reset.call(i);
                    },
                    scope:  this
                },
                ' ',
                new Ext.ux.SearchSelectorField({
                    store:  toStore,
                    paramName: 'search',
                    ctCls: 	'fill-spacer'
                })
            ];
        }

        this.multiuserlist = new Ext.ux.ItemSelector({
            anchor:         '100%',
            id:             'admin'+this.telt+"multiuserlist",
            name:           'admin'+this.telt+"multiuserlist",
            hideNavIcons:   !this.canModifyDomain,
            readOnly:       !this.canModifyDomain,
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
            fromTBar:       fromTBARMultiUserList,
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
            },
            toTBar:         toTBARMultiUserList
        });

        var mainFormItemsColumn1 = [
        {
            name:       'typeAction',
            id:         'typeAction',
            value:      '',
            xtype:      'hidden'
        },{
            name:       'adminDOMAINid',
            id:         'adminDOMAINid',
            xtype:      'hidden'
        }
        ];
        var mainFormItemsColumn2 = [
        {
            xtype:      'textfield',
            fieldLabel: getI18nResource("caqs.infosCOMMON.description"),
            name:       'adminDOMAINdesc',
            id:         'adminDOMAINdesc',
            width:      290,
            disabled:   !this.canModifyDomain,
            maxLength : 128,
            allowBlank: true
        }
        ];
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            name:       'adminDOMAINfrom',
            id:         'adminDOMAINfrom',
            value:      this.from,
            xtype:      'hidden'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] ={
            name:       'adminDOMAINfatherId',
            id:         'adminDOMAINfatherId',
            value:      this.fatherId,
            xtype:      'hidden'
        }
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            xtype:          'textfield',
            disabled:       !this.canModifyDomain,
            fieldLabel:     getI18nResource("caqs.infosCOMMON.nom")+' *',
            name:           'adminDOMAINlib',
            id:             'adminDOMAINlib',
            width:          220,
            validateOnBlur: true,
            validator:      verifSyntaxeName,
            maxLength :     64,
            allowBlank:     false
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            fieldLabel: getI18nResource("caqs.infosCOMMON.dateCreation"),
            name:       'adminDOMAINdinst',
            id:         'adminDOMAINdinst',
            xtype :     'statictextfield'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            name:       'adminDOMAINidPro',
            id:         'adminDOMAINidPro',
            value:      this.idPro,
            xtype:      'hidden'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            xtype     : 'numberfield',
            fieldLabel: getI18nResource("caqs.infosCOMMON.poids")+' * ',
            width:      220,
            name:       'adminDOMAINpoids',
            id:         'adminDOMAINpoids',
            allowDecimals:false,
            disabled:       !this.canModifyDomain,
            minValue:   1,
            allowBlank : false
        }
        mainFormItemsColumn2[mainFormItemsColumn2.length] = {
            fieldLabel: getI18nResource("caqs.infosCOMMON.dateModif"),
            name:       'adminDOMAINdmaj',
            id:         'adminDOMAINdmaj',
            xtype :     'statictextfield'
        }
        mainFormItemsColumn2[mainFormItemsColumn2.length] = {
            //rien à droite du poids
            xtype:'panel',
            height:27,
            border:false,
            html:'&nbsp;'
        }

        var mainFormButtons = [
            this.updateButton
            , this.applyChildrenButton
            , this.annulerButton
        ];
        
        var config = {
            defaults: {
                anchor: '100%'
            },
            items: [
            {
                layout:'column',
                border:false,
                items: [
                {//colonne 1
                    columnWidth:.5,
                    layout: 'form',
                    border:false,
                    items : mainFormItemsColumn1
                },{//colonne 2
                    columnWidth:.5,
                    layout: 'form',
                    border:false,
                    items : mainFormItemsColumn2
                }
                ]
            }, {
                xtype:          'fieldset',
                title:          getI18nResource("caqs.admin.common.info"),
                defaultType:    'textfield',
                collapsed:      true,
                checkboxToggle: true,
                autoHeight:     true,
                id:             'adminDOMAINinfoPanel',
                name:           'adminDOMAINinfoPanel',
                items : [
                {
                    fieldLabel: getI18nResource("caqs.admin.common.info1"),
                    name:       'adminDOMAINinfo1',
                    id:         'adminDOMAINinfo1',
                    disabled:   !this.canModifyDomain,
                    width:	720,
                    maxLength : 512
                },{
                    fieldLabel: getI18nResource("caqs.admin.common.info2"),
                    maxLength : 512,
                    width:	720,
                    disabled:   !this.canModifyDomain,
                    name:       'adminDOMAINinfo2',
                    id:         'adminDOMAINinfo2'
                }
                ]
            }, {
                layout:     'form',
                border:     false,
                items:      this.multiuserlist
            }
            ],//fin definition des panels

            buttons: mainFormButtons
        };

        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsInfosDomain.superclass.initComponent.call(this);
    },

    refresh : function(idElt, fatherId){
        if(!this.canModifyDomain) {
            this.stopMonitoring();
        } else {
            this.startMonitoring();
        }
        this.updateButton.setDisabled(this.canModifyDomain);
        this.applyChildrenButton.setDisabled(!this.canModifyDomain);
        
        this.idElt = idElt;
        this.fatherId = fatherId;
        this.startRefresh();
    },

    refreshDatas: function(json) {
        if(json!=null) {
            this.refreshCommonData(json, this);
        }
    },

    loadDatasFromJSON: function(json) {
        this.refreshDatas(json);
    }
});