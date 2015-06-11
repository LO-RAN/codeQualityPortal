Ext.ux.CaqsQAMetriqueChangeWindow = function(config) {
    // call parent constructor
    Ext.ux.CaqsQAMetriqueChangeWindow.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsQAMetriqueChangeWindow, Ext.Window, {
    title:          getI18nResource('caqs.qametrique.saisie'),
    width:          600,
    height:         430,
    layout:         'fit',
    modal:          true,
    maximizable:    true,
    resizable:      true,
    minimizable:    false,
    plain:          true,
    shadow:         false,
    idElt:          undefined,
    descElt:        undefined,
    idMet:          undefined,
    validateBtn:    undefined,
    cancelBtn:      undefined,
    descEltSTF:     undefined,
    idMetSTF:       undefined,
    valueNF:        undefined,
    idEltHF:        undefined,
    parentGrid:     undefined,


    initComponent : function(){
        this.validateBtn = new Ext.Button({
		text:		getI18nResource("caqs.update"),
		name:		'update',
		formBind:	true,
		handler: 	this.validateFn,
                scope:          this
	});

	this.cancelBtn = new Ext.Button({
		text:		getI18nResource("caqs.annuler"),
		handler: 	this.cancelFn,
                scope:          this
	});

        this.descEltSTF = new Ext.ux.form.StaticTextField({
            fieldLabel:		getI18nResource("caqs.qametrique.element")
        });
        this.idMetSTF = new Ext.ux.form.StaticTextField({
            fieldLabel:		getI18nResource("caqs.qametrique.metrique")
        });
        this.valueNF = new Ext.form.NumberField({
            fieldLabel:		getI18nResource("caqs.qametrique.valeur"),
            id:			'valbrute',
            name:		'valbrute',
            allowDecimals:      false,
            allowBlank:		false
        });
        this.idEltHF = new Ext.form.Hidden({
            name:       'idElt',
            id:         'idElt'
        });
        var config = {
            items:      [
                new Ext.form.FormPanel({
                    bodyStyle:      'padding-top:5px; padding-left:5px;',
                    monitorValid:   true,
                    items: [
                        this.descEltSTF
                        , this.idMetSTF
                        , this.valueNF
                        , this.idEltHF
                    ],
                    buttons: [
			this.validateBtn,
			this.cancelBtn
                    ]
                })
            ]
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsQAMetriqueChangeWindow.superclass.initComponent.apply(this, arguments);
    },

    cancelFn: function() {
        this.close();
    },

    validateFn: function() {
        Ext.Ajax.request({
            url:	requestContextPath + '/QametriqueUpdate.do',
            success:	this.reloadParentGrid,
            params:     {
                id_elt:     this.idElt,
                id_met:     this.idMet,
                valbrute:   this.valueNF.getValue()
            },
            scope:      this
        });
    },

    reloadParentGrid: function() {
        if(this.parentGrid) {
            this.parentGrid.reload();
        }
        this.close();
    },

    loadDatas: function(e, d, m) {
        this.idElt = e;
        this.idEltHF.setValue(this.idElt);
        this.idMet = m;
        this.idMetSTF.setValue(this.idMet);
        this.descElt = d;
        this.descEltSTF.setValue(this.descElt);
        Ext.Ajax.request({
            url:	requestContextPath + '/QametriqueSelect.do',
            success:	this.updateWindow,
            params:     {
                id_elt:     this.idElt,
                id_met:     this.idMet
            },
            scope:      this
        });
    },

    updateWindow: function(response) {
        if(response!=null && response.responseText!=null) {
            var json = Ext.util.JSON.decode(response.responseText);
            if(json) {
                this.valueNF.setValue(json.valbrute);
            }
        }
    }
});

