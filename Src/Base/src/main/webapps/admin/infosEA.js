Ext.ux.CaqsInfosEA = function(config) {
    // call parent constructor
    Ext.ux.CaqsInfosEA.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsInfosEA, Ext.ux.CaqsCommonInfosPanel, {
    purgeBaselineWnd:   undefined,
    from:               undefined,
    fatherId:           undefined,
    analysisWindow:     undefined,
    name:               undefined,
    modelStore:         undefined,
    modelCB:            undefined,
    dialecteStore:      undefined,
    dialecteCB:         undefined,
    configurationFileField:undefined,
    telt:               'EA',
    teltLib:            TYPE_EA_LIB,
    

    displayConfigurationFileField: function(show) {
        if(show) {
            this.configurationFileField.show();
        } else {
            this.configurationFileField.hide();
        }
    },

    onchangeCB: function(field, record, index) {
        this.displayConfigurationFileField(
               this.dialecteCB.getValue().toLowerCase().match('vb_')=='vb_'
            || this.dialecteCB.getValue().toLowerCase().match('cs_')=='cs_'
        );
    },

    initComponent : function(){
        Ext.ux.CaqsInfosEA.superclass.initComponent.apply(this, arguments);

        this.configurationFileField = new Ext.form.TextField({
            xtype:      'textfield',
            fieldLabel: getI18nResource("caqs.infosEA.projectFilePath"),
            name:       'adminEAprojectFilePath',
            id:         'adminEAprojectFilePath',
            maxLength : 128,
            width:	220
        });

        this.dialecteStore= new Ext.ux.CaqsJsonStore({
            url: requestContextPath + '/RetrieveAllDialectsList.do',
            fields: ['id', 'lib']
        });

        this.dialecteCB = new Ext.form.ComboBox({
            fieldLabel:         getI18nResource("caqs.infosEA.dialecte")+' *',
            disableKeyFilter:   true,
            emptyText:          getI18nResource("caqs.admin.selectDialecte"),
            name:       	'adminEAidDialecteCB',
            id:         	'adminEAidDialecteCB',
            displayField:	'lib',
            valueField: 	'id',
            hiddenName: 	'adminEAidDialecte',
            editable:		false,
            store:		this.dialecteStore,
            triggerAction:	'all',
            autocomplete:	false,
            width:              220,
            mode:      		'remote'
        });
       this.dialecteCB.on('select', this.onchangeCB, this);

       this.modelStore= new Ext.ux.CaqsJsonStore({
            url: requestContextPath + '/RetrieveAllModelsList.do',
            fields: ['id', 'lib']
        });

        this.modelCB = new Ext.form.ComboBox({
            fieldLabel:         getI18nResource("caqs.infosEA.modeleQual")+' *',
            disableKeyFilter:   true,
            emptyText:          getI18nResource("caqs.admin.selectModele"),
            name:       	'adminEAidModeleCB',
            id:         	'adminEAidModeleCB',
            displayField:	'lib',
            valueField: 	'id',
            hiddenName: 	'adminEAidUsage',
            editable:		false,
            store:		this.modelStore,
            triggerAction:	'all',
            autocomplete:	false,
            width:              220,
            mode:      		'remote'
        });

        var mainFormItemsColumn1 = [
        {
            name:       'typeAction',
            id:         'typeAction',
            value:      '',
            xtype:      'hidden'
        },{
            name:       'adminEAid',
            id:         'adminEAid',
            xtype:      'hidden'
        }
        ];
        var mainFormItemsColumn2 = [
        {
            xtype:      'textfield',
            fieldLabel: getI18nResource("caqs.infosCOMMON.description"),
            name:       'adminEAdesc',
            id:         'adminEAdesc',
            width:      290,
            maxLength : 128,
            allowBlank: true
        }
        ];
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            name:       'adminEAfrom',
            id:         'adminEAfrom',
            value:      this.from,
            xtype:      'hidden'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] ={
            name:       'adminEAfatherId',
            id:         'adminEAfatherId',
            value:      this.fatherId,
            xtype:      'hidden'
        }
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            xtype:          'textfield',
            fieldLabel:     getI18nResource("caqs.infosCOMMON.nom")+' *',
            name:           'adminEAlib',
            id:             'adminEAlib',
            width:          220,
            validateOnBlur: true,
            validator:      verifSyntaxeName,
            maxLength :     64,
            allowBlank:     false
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            fieldLabel: getI18nResource("caqs.infosCOMMON.dateCreation"),
            name:       'adminEAdinst',
            id:         'adminEAdinst',
            xtype :     'statictextfield'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            name:       'adminEAidPro',
            id:         'adminEAidPro',
            value:      this.idPro,
            xtype:      'hidden'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            xtype:      'textfield',
            fieldLabel: getI18nResource('caqs.infosEA.libraries'),
            width:      220,
            name:       'adminEAlibraries',
            id:         'adminEAlibraries',
            maxLength : 128
        };
        mainFormItemsColumn2[mainFormItemsColumn2.length] = {
            fieldLabel: getI18nResource("caqs.infosCOMMON.dateModif"),
            name:       'adminEAdmaj',
            id:         'adminEAdmaj',
            xtype :     'statictextfield'
        }
        mainFormItemsColumn2[mainFormItemsColumn2.length] = {
            //rien à droite du poids
            xtype:'panel',
            height:27,
            border:false,
            html:'&nbsp;'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = this.modelCB;
        mainFormItemsColumn2[mainFormItemsColumn2.length] = this.dialecteCB;
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            xtype     : 'numberfield',
            fieldLabel: getI18nResource("caqs.infosCOMMON.poids")+' * ',
            width:      220,
            name:       'adminEApoids',
            id:         'adminEApoids',
            allowDecimals:false,
            minValue:   1,
            allowBlank : false
        };
        mainFormItemsColumn2[mainFormItemsColumn2.length] = this.configurationFileField;

        var mainFormButtons = [
        this.updateButton
        , this.annulerButton
        ];

        var config = {
            defaults: {
                anchor:  '100%'
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

            }, new Ext.form.FieldSet ({
                title:      getI18nResource('caqs.admin.infosEA.sourceManagment'),
                autoHeight: true,
                items : [
                {
                    anchor: '90%',
                    layout: 'table',
                    layoutConfig: {
                      columns: 2
                    },
                    border:false,
                    items: [
                    {
                        border:false,
                        layout: 'form',
                        items : [
                        {
                            xtype:		'textfield',
                            fieldLabel: 	getI18nResource('caqs.infosEA.source')+' * ',
                            name:       	'adminEAsourceDir',
                            id:         	'adminEAsourceDir',
                            invalidText:	getI18nResource('caqs.admin.badField'),
                            validateOnBlur:	true,
                            width:		210,
                            validator:          verifSyntaxeValue,
                            maxLength : 	128,
                            allowBlank : 	false
                        }
                        ]
                    }, new Ext.Button({
                                id:         'adminProjectEAUploadSrcBtn',
                                text:       getI18nResource('caqs.element.upload.src.btn'),
                                scope:      this,
                                disabled:   (this.idElt==null),
                                style:      'margin-left: 5px;',
                                handler:    function() {
                                    var wnd = new Ext.ux.CaqsUploadSrcWnd();
                                    wnd.setSourceDir(this.findById('adminEAsourceDir').getValue());
                                    wnd.show();
                                }
                            })
                    ]
                }, {
                    anchor:         '90%',
                    xtype:          'fieldset',
                    title:          getI18nResource("caqs.infosEA.versionning"),
                    defaultType:    'textfield',
                    id:             'adminEAversionningPanel',
                    name:           'adminEAversionningPanel',
                    collapsed:      true,
                    checkboxToggle: true,
                    autoHeight:     true,
                    items : [
                    {
                        fieldLabel: getI18nResource("caqs.infosEA.scmRepository"),
                        name:       'adminEAscmRepository',
                        id:         'adminEAscmRepository',
                        width:	720,
                        maxLength : 512
                    },{
                        fieldLabel: getI18nResource("caqs.infosEA.scmModule"),
                        maxLength : 512,
                        width:	720,
                        name:       'adminEAscmModule',
                        id:         'adminEAscmModule'
                    }
                    ]
                }
                ]
            })
            , {
                xtype     :     'fieldset',
                title:          getI18nResource("caqs.admin.common.info"),
                defaultType:    'textfield',
                collapsed:      true,
                id:             'adminEAinfoPanel',
                name:           'adminEAinfoPanel',
                checkboxToggle: true,
                autoHeight:     true,
                items : [
                {
                    fieldLabel: getI18nResource("caqs.admin.common.info1"),
                    name:       'adminEAinfo1',
                    id:         'adminEAinfo1',
                    width:	720,
                    maxLength : 512
                },{
                    fieldLabel: getI18nResource("caqs.admin.common.info2"),
                    maxLength : 512,
                    width:	720,
                    name:       'adminEAinfo2',
                    id:         'adminEAinfo2'
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
        Ext.ux.CaqsInfosEA.superclass.initComponent.call(this);
    },

    refresh : function(idElt, fatherId){
        this.idElt = idElt;
        this.fatherId = fatherId;
        this.findById('adminProjectEAUploadSrcBtn').setDisabled(idElt==null);
        this.startRefresh();
    },

    additionnalSave: function(obj) {
        obj['projectFilePath'] = this.findById('adminEAprojectFilePath').getValue();
        obj['libraries'] = this.findById('adminEAlibraries').getValue();
        obj['sourceDir'] = this.findById('adminEAsourceDir').getValue();
        obj['scmModule'] = this.findById('adminEAscmModule').getValue();
        obj['scmRepository'] = this.findById('adminEAscmRepository').getValue();
        obj['idDialecte'] = this.dialecteCB.getValue();
        obj['idModel'] = this.modelCB.getValue();
    },

    refreshDatas: function(json) {
        if(json!=null) {
            this.refreshCommonData(json, this);
            this.findById('adminEAprojectFilePath').setValue(json.projectFilePath);
            this.findById('adminEAlibraries').setValue(json.libraries);
            this.findById('adminEAsourceDir').setValue(json.sourceDir);
            if(!((json.scmModule!=null && json.scmModule.length>1) || (json.scmRepository!=null && json.scmRepository.length>1))) {
                this.findById('adminEAversionningPanel').collapse();
            } else {
                this.findById('adminEAversionningPanel').expand();
            }
            this.findById('adminEAscmModule').setValue(json.scmModule);
            this.findById('adminEAscmRepository').setValue(json.scmRepository);
            this.dialecteStore.load({
                scope:      this,
                callback:   function() {
                    this.dialecteCB.setValue(json.idDialecte);
                    this.onchangeCB();
                }
            });
            this.modelStore.load({
                scope:      this,
                callback:   function() {
                    this.modelCB.setValue(json.idModel);
                }
            });
        }
    },

    loadDatasFromJSON: function(json) {
        this.refreshDatas(json);
    }
});