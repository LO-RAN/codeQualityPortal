Ext.ux.CaqsTopDownSynthesisVolumetryPanel = function(config) {
    // call parent constructor
    Ext.ux.CaqsTopDownSynthesisVolumetryPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsTopDownSynthesisVolumetryPanel, Ext.Panel, {
    //autoHeight:         true,
    //autoWidth:          true,
    layout:             'column',
    border:             false,
    kiviatWidth:        300,
    fromLabellisation:  false,
    labelButton:        undefined,
    showJustifsDemandeWnd:undefined,
    volumetryPanel:     undefined,
    volumetryForm:      undefined,
    nb:                 0,
    style:              'padding-top: 5px; padding-left: 5px;',
    hideMode:           'offsets',
    volumetryElements:  undefined,

    initComponent : function(){
        Ext.ux.CaqsTopDownSynthesisVolumetryPanel.superclass.initComponent.call(this);
        
        this.volumetryPanel = new Ext.Panel({
            autoHeight: 	true,
            labelWidth: 	224,
            //autoWidth:      true,
            border:		false,
            layout: 		'form',
            style:		'margin-top:5px;'
        });
        this.volumetryForm = new Ext.form.FieldSet({
            title:          getI18nResource("caqs.synthese.volumetrie"),
            //autoWidth:      true,
            autoHeight:     true,
            items:          this.volumetryPanel
        });
        var config = {
            items:  [
            {//colonne 1
                columnWidth:	.49,
                id:		'TDVolcolumn0',
                border:		false,
                items: [
                //firstButtonsPanel,
                this.volumetryForm
                ]
            },{//colonne 1
                id:		'TDVolcolumn1',
                columnWidth:	.49,
                border:		false,
                listeners:      {
                    'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                        this.kiviatWidth = rawWidth;
                        this.reloadKiviat();
                    },
                    scope:  this
                }
            }
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsTopDownSynthesisVolumetryPanel.superclass.initComponent.apply(this, arguments);
    },

    reloadKiviat: function() {
        this.nb++;
        var kiviatDiv = this.findById('TDVolcolumn1');
        if(kiviatDiv != null) {
            var child = undefined;
            if(kiviatDiv.items != undefined) {
                child = kiviatDiv.getComponent('imgKiviat');
                if(child != undefined) {
                    kiviatDiv.remove(child);
                }
            }
            var img = '<IMG id="kiviatImg" src="';
            img += requestContextPath + '/Kiviat.do?nb='+this.nb;
            img += "&width="+(this.kiviatWidth - 10);
            img += "&height="+parseInt((this.kiviatWidth - 10)*0.7);
            if(this.idElt!=null) {
                img += "&id_elt="+this.idElt;
            }
            if(this.idBline!=null) {
                img += "&baselineId="+this.idBline;
            }
            if(this.idPro!=null) {
                img += "&id_pro="+this.idPro;
            }

            img += '" />';
            child = new Ext.Panel({
                border: false,
                id: 'imgKiviat',
                html:   img
            });
            kiviatDiv.add(child);
            kiviatDiv.doLayout();
        }
    },

    reload: function() {
        if(!this.fromLabellisation) {
            Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        }
        Ext.Ajax.request({
            url:	requestContextPath+'/GetSynthesisVolumetry.do',
            scope:      this,
            success:	this.fillVolumetry,
            params: {
                id_elt: this.idElt,
                baselineId: this.idBline,
                id_pro : this.idPro
            }
        });
        this.reloadKiviat();
        if(!this.fromLabellisation) {
            Caqs.Portal.setCurrentScreen('topdown_synthesis');
        }
    },

    showJustifs: function(wnd) {
        if(wnd!=undefined && !wnd.isVisible()) {
            wnd.setVisible(true);
        }
    },

    emptyVolumetryForm: function() {
        if(this.volumetryElements) {
            for(var i=0; i<this.volumetryElements.length; i++) {
                var elt = this.volumetryElements[i];
                if(elt!=null) {
                    this.volumetryPanel.remove(elt.id);
                }
            }
        }
    },

    fillVolumetry: function(response) {
        if(response.responseText!='' && response.responseText!='[]') {
            var vol = Ext.util.JSON.decode(response.responseText);
            this.emptyVolumetryForm();
            this.volumetryElements = new Array();
            
            if(vol.all_code) {
                var obj = new Ext.ux.form.StaticTextField({
                    fieldLabel: 	getI18nResource("caqs.synthese.nblignescode"),
                    parentClassName: 	'smallerStaticField',
                    smallLine:		true,
                    value:		vol.all_code_format
                });
                this.volumetryPanel.add(obj);
                this.volumetryElements[this.volumetryElements.length] = obj;
                if(vol.prev_all_code) {
                    var obj = new Ext.ux.form.StaticTextField({
                        fieldLabel:	'<i>&nbsp;&nbsp;'+getI18nResource("caqs.synthese.volEvol")+vol.prev_all_code_format+'</i>',
                        parentClassName:'smallerStaticField',
                        smallLine:	true,
                        labelWidth: 	300,
                        value:		'',
                        labelSeparator: ''
                    });
                    this.volumetryPanel.add(obj);
                    this.volumetryElements[this.volumetryElements.length] = obj;
                }
            }

            if(vol.pct_comments) {
                var obj = new Ext.ux.form.StaticTextField({
                    fieldLabel: 	vol.pct_txt,
                    parentClassName: 	'smallerStaticField',
                    smallLine:		true,
                    value:		vol.pct_comments_format
                });
                this.volumetryPanel.add(obj);
                this.volumetryElements[this.volumetryElements.length] = obj;
                if(vol.prev_pct_comments) {
                    var obj = new Ext.ux.form.StaticTextField({
                        fieldLabel:	'<i>&nbsp;&nbsp;'+getI18nResource("caqs.synthese.volEvol")+vol.prev_pct_comments_format+'</i>',
                        parentClassName:'smallerStaticField',
                        smallLine:	true,
                        labelWidth: 	300,
                        value:		'',
                        labelSeparator: ''
                    });
                    this.volumetryPanel.add(obj);
                    this.volumetryElements[this.volumetryElements.length] = obj;
                }
            }

            if(vol.volumetry != undefined) {
                for(var i=0; i<vol.volumetry.length; i++) {
                    var obj = new Ext.ux.form.StaticTextField({
                        fieldLabel:	vol.volumetry[i].label,
                        parentClassName:'smallerStaticField',
                        smallLine:	true,
                        value:		vol.volumetry[i].total
                    });
                    this.volumetryPanel.add(obj);
                    this.volumetryElements[this.volumetryElements.length] = obj;
                    var obj = new Ext.ux.form.StaticTextField({
                        fieldLabel:	vol.volumetry[i].creesup,
                        parentClassName:'smallerStaticField',
                        smallLine:	true,
                        labelWidth: 	300,
                        value:		'',
                        labelSeparator: ''
                    });
                    this.volumetryPanel.add(obj);
                    this.volumetryElements[this.volumetryElements.length] = obj;
                }
            }

            if(vol.complex_dest) {
                var obj = new Ext.ux.form.StaticTextField({
                    fieldLabel: 	getI18nResource("caqs.synthese.codecomplexe"),
                    parentClassName: 	'smallerStaticField',
                    smallLine:		true,
                    value:		vol.complex_dest_format
                });
                this.volumetryPanel.add(obj);
                this.volumetryElements[this.volumetryElements.length] = obj;
                if(vol.prev_complex_dest) {
                    var obj = new Ext.ux.form.StaticTextField({
                        fieldLabel:	'<i>&nbsp;&nbsp;'+getI18nResource("caqs.synthese.volEvol")+vol.prev_complex_dest_format+'</i>',
                        parentClassName:'smallerStaticField',
                        smallLine:	true,
                        labelWidth: 	300,
                        value:		'',
                        labelSeparator: ''
                    });
                    this.volumetryPanel.add(obj);
                    this.volumetryElements[this.volumetryElements.length] = obj;
                }
            }

            if(vol.ifpug) {
                var obj = new Ext.ux.form.StaticTextField({
                    fieldLabel: 	getI18nResource("caqs.synthese.ifpug"),
                    parentClassName: 	'smallerStaticField',
                    smallLine:		true,
                    value:		vol.ifpug_format
                });
                this.volumetryPanel.add(obj);
                this.volumetryElements[this.volumetryElements.length] = obj;
                if(vol.prev_ifpug) {
                    var obj = new Ext.ux.form.StaticTextField({
                        fieldLabel:	'<i>&nbsp;&nbsp;'+getI18nResource("caqs.synthese.volEvol")+vol.prev_ifpug_format+'</i>',
                        parentClassName:'smallerStaticField',
                        smallLine:	true,
                        labelWidth: 	300,
                        value:		'',
                        labelSeparator: ''
                    });
                    this.volumetryPanel.add(obj);
                    this.volumetryElements[this.volumetryElements.length] = obj;
                }
            }

            if(vol.isEA) {
                var nbDemandees = vol.justifDemand;
                var nbValidees  = vol.justifValid;
                var nbRefusees  = vol.justifRejet;
                var demandesValue = '';
                if(nbDemandees>0) {
                    demandesValue += '<a href="javascript:loadJustificationList(\'DEMAND\', \''
                    +getI18nResource("caqs.synthese.justifDemandList")+'\');">';
                }
                demandesValue += nbDemandees;
                if(nbDemandees>0) {
                    demandesValue += '<img src="'+requestContextPath+'/images/loupe.gif" />';
                    demandesValue += '</a>';
                } else {
                    demandesValue += '<img src="'+requestContextPath+'/images/empty16.gif" />';
                }

                var validesValue  = '';
                if(nbValidees>0) {
                    validesValue += '<a href="javascript:loadJustificationList(\'VALID\', \''+
                    getI18nResource("caqs.synthese.justifValidList")+'\');">';
                }
                validesValue += nbValidees;
                if(nbValidees>0) {
                    validesValue += '<img src="'+requestContextPath+'/images/loupe.gif" />';
                    validesValue += '</a>';
                } else {
                    validesValue += '<img src="'+requestContextPath+'/images/empty16.gif" />';
                }

                var refusValue    = '';
                if(nbRefusees>0) {
                    refusValue += '<a href="javascript:loadJustificationList(\'REJET\', \''
                    + getI18nResource("caqs.synthese.justifRejetList")+'\');">';
                }
                refusValue += nbRefusees;
                if(nbRefusees>0) {
                    refusValue += '<img src="'+requestContextPath+'/images/loupe.gif" />';
                    refusValue += '</a>';
                } else {
                    refusValue += '<img src="'+requestContextPath+'/images/empty16.gif" />';
                }
                var value = '<div style="text-align:right;"><span>'
                + getI18nResource("caqs.synthese.nbJustifDemand")
                + ' '+demandesValue+'</span><br />'
                + '<span>'+getI18nResource("caqs.synthese.nbJustifValid")
                + ' '+validesValue+'</span><br />'
                + '<span>' + getI18nResource("caqs.synthese.nbJustifRejet")
                + ' '+refusValue+'</span></div>';

                var just = new Ext.ux.form.StaticTextField({
                    fieldLabel:         getI18nResource("caqs.synthese.nbJustif"),
                    parentClassName:    'smallerStaticField',
                    smallLine:          true,
                    htmlEncode:         false,
                    //autoHeight:		true,
                    height:		60,
                    value:             	value
                });
                this.volumetryElements[this.volumetryElements.length] = just;
                this.volumetryPanel.add(just);
            }

            this.doLayout(true);
            if(!this.fromLabellisation) {
                Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
            }
        }
    }

});

