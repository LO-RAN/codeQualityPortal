Ext.ux.CaqsJustificationDemandWindow = function(config) {
    // call parent constructor
    Ext.ux.CaqsJustificationDemandWindow.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor

Ext.extend(Ext.ux.CaqsJustificationDemandWindow, Ext.Window, {
    title:              getI18nResource('caqs.critere.askJustif.title'),
    modal: 		true,
    maximizable: 	true,
    resizable:		true,
    layout:		'fit',
    minimizable: 	false,
    width:		810,
    height:		330,
    shadow:		false,
    plain:		true,
    x:			20,
    y:			20,
    status:             undefined,
    idElt:              undefined,
    idBline:            undefined,
    idCrit:             undefined,
    idJust:             undefined,
    noteCalc:           undefined,
    update:             true,
    libElt:             undefined,
    libCrit:            undefined,
    parentGridElement:  undefined,

    setValues: function(json) {
        this.update = json.update;
        this.idJust = json.idJust;
        this.findById('justificationDemandLibElt').setValue(this.libElt);
        this.findById('justificationDemandLibCrit').setValue(this.libCrit);
        this.findById('justificationDemandScore').setValue(this.noteCalc);
        this.findById('justificationDemandAuteur').setValue(json.cuser);
        this.status.setVisible(json.update);
        this.findById('justificationDemandJustLib').setValue(json.libJust);
        this.findById('justificationDemandJustDesc').setValue(json.descJust);
    },

    validateFunction: function() {
        Ext.Ajax.request({
            url: requestContextPath+'/SetDemandJustification.do',
            scope: this,
            params: {
                'id_elt' : this.idElt,
                'id_bline' : this.idBline,
                'id_crit' : this.idCrit,
                'id_just': this.idJust,
                'just_notecalc': this.noteCalc,
                'cuser' : this.findById('justificationDemandAuteur').getValue(),
                'just_lib' : this.findById('justificationDemandJustLib').getValue(),
                'just_desc' : this.findById('justificationDemandJustDesc').getValue(),
                'update' : this.update
            },
            success: function() {
                this.parentGridElement.reload();
                this.close();
            }
        });
    },

    cancel: function() {
        this.close();
    },

    initComponent : function(){
        this.status = new Ext.ux.form.StaticTextField({
            parentClassName: 	'smallStaticField',
            fieldLabel:		getI18nResource("caqs.justificatif.statut"),
            value:              getI18nResource("caqs.justificatif.statutEnCours")
        });

        var buttons = new Array();
        if(canAccessFunction("Justification_Creation")) {
            buttons[buttons.length] = new Ext.Button({
                    text:	getI18nResource("caqs.valider"),
                    formBind:	true,
                    handler: 	this.validateFunction,
                    scope:      this
            });
        }
        buttons[buttons.length] = new Ext.Button({
                text:       getI18nResource("caqs.annuler"),
                handler:    this.cancel,
                scope:      this
        });

        var formPanel = new Ext.form.FormPanel({
            border:         false,
            monitorValid:   true,
            bodyStyle:      'padding-left: 5px; padding-top: 5px;',
            labelWidth:     100,
            items: [
                {
                    xtype:		'statictextfield',
                    name:               'justificationDemandLibElt',
                    id:                 'justificationDemandLibElt',
                    parentClassName: 	'smallStaticField',
                    fieldLabel:		getI18nResource("caqs.justificatif.element")
                },
                {
                    name:               'justificationDemandLibCrit',
                    id:                 'justificationDemandLibCrit',
                    xtype:		'statictextfield',
                    parentClassName: 	'smallStaticField',
                    fieldLabel:		getI18nResource("caqs.critere")
                },
                {
                    name:               'justificationDemandScore',
                    id:                 'justificationDemandScore',
                    xtype:              'statictextfield',
                    parentClassName:    'smallStaticField',
                    fieldLabel:         getI18nResource("caqs.justificatif.note")
                },
                {
                    name:               'justificationDemandAuteur',
                    id:                 'justificationDemandAuteur',
                    xtype:		'statictextfield',
                    parentClassName: 	'smallStaticField',
                    fieldLabel:		getI18nResource("caqs.justificatif.auteur")
                }
                , this.status
                , {
                    xtype:		'textfield',
                    fieldLabel:		getI18nResource("caqs.justificatif.libelle"),
                    allowBlank:		false,
                    name:		'justificationDemandJustLib',
                    id:			'justificationDemandJustLib',
                    maxLength:		32
                },
                {
                    xtype:		'textarea',
                    fieldLabel:		getI18nResource("caqs.justificatif.description"),
                    allowBlank:		false,
                    name:		'justificationDemandJustDesc',
                    id:			'justificationDemandJustDesc',
                    maxLength:		128,
                    width:		270,
                    height:		70
                }
            ],
            buttons: buttons
	});
        var config = {
            items:   formPanel
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsJustificationDemandWindow.superclass.initComponent.apply(this, arguments);
    },

    refresh: function(idElt, idBline, idCrit, idJust, noteCalc, libElt, libCrit) {
        this.idElt = idElt;
        this.idBline = idBline;
        this.idCrit = idCrit;
        this.noteCalc = noteCalc;
        this.libElt = libElt;
        this.libCrit = libCrit;
        this.idJust = idJust;
        Ext.Ajax.request({
            url: requestContextPath+'/RetrieveJustificationForDemand.do',
            scope: this,
            params: {
                'id_just': this.idJust,
                'notejust': this.noteCalc
            },
            success: function(response) {
                if(response!=undefined && response.responseText!=undefined) {
                    var json = Ext.util.JSON.decode(response.responseText);
                    if(json!=undefined) {
                        this.setValues(json);
                    }
                }
            }
        });
    }
});