<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script language="javascript">
    Ext.ux.CaqsLabellisationWindow = function(config) {
        // call parent constructor
        Ext.ux.CaqsLabellisationWindow.superclass.constructor.call(this, config);
    }; // end of Ext.ux.CaqsLabellisationWindow constructor

    Ext.extend(Ext.ux.CaqsLabellisationWindow, Ext.Window, {
        modal:          true,
        maximizable:    false,
        resizable:      false,
        minimizable:    false,
        width:          500,
        //autoHeight:     true,
        autoHeight:     true,//height:         300,
        shadow:         false,
        plain:          true,
        x:              20,
        y:              20,
        title:          getI18nResource("caqs.label.title"),
        elementLib:     undefined,
        user:           undefined,
        status:         undefined,
        labelLib:       undefined,
        labelDesc:      undefined,
        labelId:        undefined,
        caqsSynthesisFrame:    undefined,

        initComponent: function(){
            Ext.ux.CaqsLabellisationWindow.superclass.initComponent.call(this);
            this.on('beforeclose', function() {
                this.labelId = undefined;
                this.hide();
                return false;
            }, this);
            this.on('beforeRender', function(ct, position) {
                this.updateUI();
            }, this);
            this.on('beforeShow', function() {
                this.loadLabellisation();
            }, this);
        },

        updateUI: function() {
            this.elementLib = new Ext.ux.form.StaticTextField({
                parentClassName: 	'smallStaticField',
                fieldLabel:         getI18nResource("caqs.label.element")
            });
            this.user = new Ext.ux.form.StaticTextField({
                parentClassName: 	'smallStaticField',
                fieldLabel:         getI18nResource("caqs.label.auteur")
            });
            this.status = new Ext.ux.form.StaticTextField({
                parentClassName: 	'smallStaticField',
                fieldLabel:         getI18nResource("caqs.label.statut")
            });
            this.labelLib = new Ext.form.TextField({
                parentClassName: 	'smallStaticField',
                fieldLabel:         getI18nResource("caqs.label.libelle"),
                maxLength:          32,
                id:                 'lib',
                name:               'lib',
                allowBlank:         false
            });
            this.labelDesc = new Ext.form.TextField({
                parentClassName: 	'smallStaticField',
                fieldLabel:         getI18nResource("caqs.label.description"),
                allowBlank:     	false,
                name:           	'desc',
                id:             	'desc',
                maxLength:      	128,
                width:          	270,
                height:         	70
            });
            var formPanel = new Ext.form.FormPanel({
                autoHeight:     true,//height:         200,
                labelWidth:     100,
                bodyStyle:		'padding:5px 5px 5px 5px;',
                border:         true,
                monitorValid:   true,
                items: [
                    this.elementLib,
                    this.user,
                    this.status,
                    this.labelLib,
                    this.labelDesc
                ],
                buttons: [
                    {
                        text:       getI18nResource("caqs.valider"),
                        formBind:   true,
                        cls:		'x-btn-text-icon',
                        icon:		requestContextPath+'/images/tick.gif',
                        scope:      this,
                        handler:    function(){
                            this.saveLabellisation();
                        }
                    },
                    {
                        text:       getI18nResource("caqs.annuler"),
                        cls:		'x-btn-text-icon',
                        icon:		requestContextPath+'/images/cross.gif',
                        scope:      this,
                        handler:    function(){
                            this.hide();
                        }
                    }
                ]
            });
            this.add(formPanel);
            this.doLayout();
        },

        finishLabellisation: function(response) {
            if(response.responseText!='' && response.responseText!='[]') {
                var retour = Ext.util.JSON.decode(response.responseText);
                this.labelId = retour.labelId;
                Caqs.Labellisation.setLabellisationInfos(retour.newButtonLabel+' : '+this.labelDesc.getValue(),
                    retour.newButtonLabel, '/images/encours.gif', this.caqsSynthesisFrame);
            }
            this.hide();
        },
    
        saveLabellisation: function() {
            Ext.Ajax.request({
                url: 		requestContextPath+'/LabelUpdateAjax.do',
                scope:      this,
                params: {
                    labelId:    this.labelId,
                    labelLib:   this.labelLib.getValue(),
                    labelDesc:  this.labelDesc.getValue(),
                    labelUser:  this.user.getValue()
                },
                success:    this.finishLabellisation
            });
        },

        setLabelId: function(labid) {
            this.labelId = labid;
        },
        majForm: function(response) {
            if(response.responseText!='' && response.responseText!='[]') {
                var lab = Ext.util.JSON.decode(response.responseText);
                this.elementLib.setValue(lab.elementLib);
                this.status.setValue(lab.status);
                this.user.setValue(lab.user);
                this.labelDesc.setValue(lab.labelDesc);
                this.labelLib.setValue(lab.labelLib);
                this.labelId = lab.labelId;
            }
        },
    
        loadLabellisation:  function() {
            Ext.Ajax.request({
                success:    this.majForm,
                url:        requestContextPath+'/LabelSelectAjax.do',
                scope:      this,
                params: {
                    id_label: this.labelId
                }
            });
        }
    });

</script>
