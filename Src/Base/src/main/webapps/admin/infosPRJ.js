Ext.ux.CaqsInfosPRJ = function(config) {
    // call parent constructor
    Ext.ux.CaqsInfosPRJ.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsInfosPRJ, Ext.ux.CaqsCommonInfosPanel, {
    purgeBaselineWnd:   undefined,
    from:               undefined,
    fatherId:           undefined,
    name:               undefined,
    telt:               'PRJ',
    analysisTree:       undefined,
    teltLib:            TYPE_PRJ_LIB,
    launchAnalysisBtn:  undefined,
    adminBlineBtn:      undefined,
    exportBtn:          undefined,

    purgeBaselineWndFunction: function() {
        if(!this.purgeBaselineWnd) {
            this.purgeBaselineWnd = new Ext.ux.CaqsPurgeBaselineWnd();
        }
        this.purgeBaselineWnd.setIdPro(this.idPro);
        this.purgeBaselineWnd.show();
    },

    exportProjectFunction: function() {
        Ext.Ajax.request({
            url:	requestContextPath + '/Export.do',
            params: {
                'idPro': this.idPro,
                'libPro':Ext.getCmp('adminPRJlib').getValue()
            }
   	});
   	window.setTimeout(function() {
            Caqs.Messages.checkMessages();
        }, 2000);
	Ext.Msg.alert('', getI18nResource("caqs.export.done"));
    },
    
    analysisLaunchWindowFunction: function() {
        var analysisWindow = new Ext.ux.CaqsLaunchAnalysisWnd({
            idPro:    this.idPro,
            parent:   this
        });
        analysisWindow.show();
    },

    initComponent : function(){
        Ext.ux.CaqsInfosPRJ.superclass.initComponent.apply(this, arguments);
        var mainFormItemsColumn1 = [
        {
            name:       'typeAction',
            id:         'typeAction',
            value:      '',
            xtype:      'hidden'
        },{
            name:       'adminPRJid',
            id:         'adminPRJid',
            xtype:      'hidden'
        }
        ];
        var mainFormItemsColumn2 = [
        {
            xtype:      'textfield',
            fieldLabel: getI18nResource("caqs.infosCOMMON.description"),
            name:       'adminPRJdesc',
            id:         'adminPRJdesc',
            width:      290,
            maxLength : 128,
            allowBlank: true
        }
        ];
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            name:       'adminPRJfrom',
            id:         'adminPRJfrom',
            value:      this.from,
            xtype:      'hidden'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] ={
            name:       'adminPRJfatherId',
            id:         'adminPRJfatherId',
            value:      this.fatherId,
            xtype:      'hidden'
        }
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            xtype:          'textfield',
            fieldLabel:     getI18nResource("caqs.infosCOMMON.nom")+' *',
            name:           'adminPRJlib',
            id:             'adminPRJlib',
            width:          220,
            validateOnBlur: true,
            validator:      verifSyntaxeName,
            maxLength :     64,
            allowBlank:     false
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            fieldLabel: getI18nResource("caqs.infosCOMMON.dateCreation"),
            name:       'adminPRJdinst',
            id:         'adminPRJdinst',
            xtype :     'statictextfield'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            name:       'adminPRJidPro',
            id:         'adminPRJidPro',
            xtype:      'hidden'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            xtype     : 'numberfield',
            fieldLabel: getI18nResource("caqs.infosCOMMON.poids")+' * ',
            width:      220,
            name:       'adminPRJpoids',
            id:         'adminPRJpoids',
            allowDecimals:false,
            minValue:   1,
            allowBlank : false
        }
        mainFormItemsColumn2[mainFormItemsColumn2.length] = {
            fieldLabel: getI18nResource("caqs.infosCOMMON.dateModif"),
            name:       'adminPRJdmaj',
            id:         'adminPRJdmaj',
            xtype :     'statictextfield'
        }

        mainFormItemsColumn2[mainFormItemsColumn2.length] = {
            //rien à droite du poids
            xtype:'panel',
            height:27,
            border:false,
            html:'&nbsp;'
        }

        this.adminBlineBtn = new Ext.Button({
            xtype:      'button',
            text:       getI18nResource('caqs.admin.adminBlTitle'),
            name:       'purgeBaseline',
            handler:    this.purgeBaselineWndFunction,
            scope:      this
        });

        var mainFormButtons = [
            this.updateButton
            , this.applyChildrenButton
            , this.annulerButton
            , this.adminBlineBtn
        ];
        if(caqsUserRights["IMPORT_EXPORT"]) {
           this.exportBtn = new Ext.Button({
                cls:        'x-btn-text-icon',
                icon:       requestContextPath+'/images/database_go.gif',
                text:       getI18nResource("caqs.export.export"),
                name:       'exportProject',
                handler:    this.exportProjectFunction,
                scope:      this
            });
            mainFormButtons[mainFormButtons.length] = this.exportBtn;
        }

       this.launchAnalysisBtn = new Ext.Button({
            text:       getI18nResource("caqs.admin.launchAnalysis.buttonTitle"),
            name:       'caqs.admin.update',
            handler:    this.analysisLaunchWindowFunction,
            style:      'margin-left: 5px;',
            scope:      this
        });

        mainFormButtons[mainFormButtons.length] = this.launchAnalysisBtn;
        
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
                xtype     :     'fieldset',
                title:          getI18nResource("caqs.admin.common.info"),
                defaultType:    'textfield',
                id:             'adminPRJinfoPanel',
                name:           'adminPRJinfoPanel',
                collapsed:      true,
                checkboxToggle: true,
                autoHeight:     true,
                items : [
                {
                    fieldLabel: getI18nResource("caqs.admin.common.info1"),
                    name:       'adminPRJinfo1',
                    id:         'adminPRJinfo1',
                    width:	720,
                    maxLength : 512
                },{
                    fieldLabel: getI18nResource("caqs.admin.common.info2"),
                    maxLength : 512,
                    width:	720,
                    name:       'adminPRJinfo2',
                    id:         'adminPRJinfo2'
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
        Ext.ux.CaqsInfosPRJ.superclass.initComponent.call(this);
    },

    refresh : function(idElt, fatherId){
        this.launchAnalysisBtn.setVisible((idElt!=null));
        this.adminBlineBtn.setVisible((idElt!=null));
        if(caqsUserRights["IMPORT_EXPORT"]) {
            this.exportBtn.setVisible((idElt!=null));
        }
        this.idElt = idElt;
        this.fatherId = fatherId;
        this.startRefresh();
    },

    refreshDatas: function(json) {
        if(json!=null) {
            this.refreshCommonData(json, this);
            this.analysisTree = json.projectTreeJSON;
        }
    },

    loadDatasFromJSON: function(json) {
        this.refreshDatas(json);
    }
});