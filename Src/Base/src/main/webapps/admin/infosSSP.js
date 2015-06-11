Ext.ux.CaqsInfosSSP = function(config) {
    // call parent constructor
    Ext.ux.CaqsInfosSSP.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsInfosSSP, Ext.ux.CaqsCommonInfosPanel, {
    from:               undefined,
    fatherId:           undefined,
    name:               undefined,
    telt:               'SSP',
    teltLib:            TYPE_SSP_LIB,

    initComponent : function(){
        Ext.ux.CaqsInfosSSP.superclass.initComponent.apply(this, arguments);
        var mainFormItemsColumn1 = [
        {
            name:       'typeAction',
            id:         'typeAction',
            value:      '',
            xtype:      'hidden'
        },{
            name:       'adminSSPid',
            id:         'adminSSPid',
            xtype:      'hidden'
        }
        ];
        var mainFormItemsColumn2 = [
        {
            xtype:      'textfield',
            fieldLabel: getI18nResource("caqs.infosCOMMON.description"),
            name:       'adminSSPdesc',
            id:         'adminSSPdesc',
            width:      290,
            maxLength : 128,
            allowBlank: true
        }
        ];
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            name:       'adminSSPfrom',
            id:         'adminSSPfrom',
            value:      this.from,
            xtype:      'hidden'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] ={
            name:       'adminSSPfatherId',
            id:         'adminSSPfatherId',
            value:      this.fatherId,
            xtype:      'hidden'
        }
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            xtype:          'textfield',
            fieldLabel:     getI18nResource("caqs.infosCOMMON.nom")+' *',
            name:           'adminSSPlib',
            id:             'adminSSPlib',
            width:          220,
            validateOnBlur: true,
            validator:      verifSyntaxeName,
            maxLength :     64,
            allowBlank:     false
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            fieldLabel: getI18nResource("caqs.infosCOMMON.dateCreation"),
            name:       'adminSSPdinst',
            id:         'adminSSPdinst',
            xtype :     'statictextfield'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            name:       'adminSSPidPro',
            id:         'adminSSPidPro',
            value:      this.idPro,
            xtype:      'hidden'
        };
        mainFormItemsColumn1[mainFormItemsColumn1.length] = {
            xtype     : 'numberfield',
            fieldLabel: getI18nResource("caqs.infosCOMMON.poids")+' * ',
            width:      220,
            name:       'adminSSPpoids',
            id:         'adminSSPpoids',
            allowDecimals:false,
            minValue:   1,
            allowBlank : false
        }
        mainFormItemsColumn2[mainFormItemsColumn2.length] = {
            fieldLabel: getI18nResource("caqs.infosCOMMON.dateModif"),
            name:       'adminSSPdmaj',
            id:         'adminSSPdmaj',
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
                xtype     :     'fieldset',
                title:          getI18nResource("caqs.admin.common.info"),
                defaultType:    'textfield',
                collapsed:      true,
                checkboxToggle: true,
                autoHeight:     true,
                id:             'adminSSPinfoPanel',
                name:           'adminSSPinfoPanel',
                items : [
                {
                    fieldLabel: getI18nResource("caqs.admin.common.info1"),
                    name:       'adminSSPinfo1',
                    id:         'adminSSPinfo1',
                    width:	720,
                    maxLength : 512
                },{
                    fieldLabel: getI18nResource("caqs.admin.common.info2"),
                    maxLength : 512,
                    width:	720,
                    name:       'adminSSPinfo2',
                    id:         'adminSSPinfo2'
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
        Ext.ux.CaqsInfosSSP.superclass.initComponent.call(this);
    },

    refresh : function(idElt, fatherId){
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