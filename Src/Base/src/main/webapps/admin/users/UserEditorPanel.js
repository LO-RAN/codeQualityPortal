Ext.ux.CaqsUserEditorPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    title:              getI18nResource('caqs.admin.userEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/RetrieveUserInfos.do',
    saveUrl:            requestContextPath + '/SaveUserInfos.do',
    fieldsDefinitions: [
                    {
                        name:           'id'
                        , fieldLabel:   getI18nResource('caqs.admin.user.id')
                        , maxLength:    32
                        , type:         'id'
                    }
                    , {
                        name: 'lastname'
                        , maxLength:    128
                        , fieldLabel:   getI18nResource('caqs.admin.user.lastname')
                    }
                    , {
                        name: 'firstname'
                        , maxLength:    128
                        , fieldLabel:   getI18nResource('caqs.admin.user.firstname')
                    }
                    , {
                        name: 'email'
                        , maxLength:    128
                        , fieldLabel:   getI18nResource('caqs.admin.user.email')
                    }
                    , {
                        name: 'password'
                        , maxLength:    128
                        , fieldLabel:   getI18nResource('caqs.admin.user.password')
                        , type:         'password'

                    }
    ],


    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsUserEditorPanel.superclass.constructor.apply(this, arguments);
    },

    getMultirolelistStore: function() {
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

    specificDataManagement: function(json) {
        this.deleteBtn.setDisabled(json.isConnected);

        var i=this.findById('admin'+this.telt+'multirolelist');
        i.loadDatas.call(i, json.availableRolesCollection, json.userRolesCollection);
        //i.setEditionMode(json.id=='', json.lib);

    },

    putAdditionalParamsToSave: function(params) {
        var i=this.findById('admin'+this.telt+'multirolelist');
        params['roles'] = i.getValue.call(i);
    },

    createAdditionalFormItems: function() {


        this.multirolelist = new Ext.ux.ItemSelector({
            id:             'admin'+this.telt+'multirolelist',
            anchor:         '100%',
            xtype:          "itemselector",
            name:           'admin'+this.telt+"multirolelist",
            hideNavIcons:   false,
            drawUpIcon:     false,
            drawDownIcon:   false,
            drawTopIcon:    false,
            drawBotIcon:    false,
            fieldLabel:     getI18nResource("caqs.admin.user.roles"),
            dataFields:     ["id", "lib"],
            fromStore:      this.getMultirolelistStore(),
            toStore:        this.getMultirolelistStore(),
            msWidth:        250,
            msHeight:       200,
            valueField:     "id",
            displayField:   "lib",
            imagePath:      requestContextPath + "/ext/plugins/multiselect",
            toLegend:       getI18nResource("caqs.admin.user.userRoles"),
            fromLegend:     getI18nResource("caqs.admin.user.availableRoles"),
            fromTBar:[{
                text:   getI18nResource("caqs.admin.user.addAllRoles"),
                handler:function(){
                    var i=this.findById('admin'+this.telt+'multirolelist');
                    i.selectAll.call(i);
                },
                scope:  this
            }],
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
                text:   getI18nResource("caqs.admin.user.removeAllRoles"),
                handler:function(){
                    var i=this.findById('admin'+this.telt+'multirolelist');
                    i.reset.call(i);
                },
                scope:  this
            }]
        });



        this.formItems[this.formItems.length] = this.multirolelist;

    },

   initComponent : function(){
        Ext.ux.CaqsUserEditorPanel.superclass.initComponent.apply(this, arguments);
    }

});

