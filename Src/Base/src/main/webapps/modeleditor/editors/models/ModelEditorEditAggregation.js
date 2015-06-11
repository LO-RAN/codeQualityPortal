Ext.ux.CaqsModelEditorEditAggregation = function(config) {
    Ext.ux.CaqsModelEditorEditAggregation.superclass.constructor.call(this, config);
};

Ext.extend(Ext.ux.CaqsModelEditorEditAggregation, Ext.Panel, {
    frame:                  true,
    border:                 true,
    anchor:                 '98%',
    layout:                 'anchor',
    autoHeight:             true,
    parentPanel:            undefined,
    agregationTypeCB:       undefined,
    elementTypeCB:          undefined,
    parametersPanel:        undefined,
    header:                 false,
    closeBtn:               undefined,
    //title:                  '&nbsp;',
    defaultValues:          undefined,
    bodyStyle:              'margin-top: 5px;',
    style:                  'margin-top: 5px;',
    defaultValueTF:         undefined,
    exclusTF:               undefined,
    seuilTF:                undefined,
    seuil1TF:               undefined,
    seuil2TF:               undefined,
    seuil3TF:               undefined,

    removeAgregation: function() {
        this.parentPanel.removeAgregation(this);
    },

    getJSONAgregation: function() {
      var retour = {
            id:             this.agregationTypeCB.getValue(),
            elementType:    this.elementTypeCB.getValue(),
            VALUE_EXCLUS:   this.exclusTF.getValue(),
            SEUIL1:         this.seuil1TF.getValue(),
            SEUIL2:         this.seuil2TF.getValue(),
            SEUIL3:         this.seuil3TF.getValue(),
            DEFAULT:        this.defaultValueTF.getValue(),
            SEUIL:          this.seuilTF.getValue()
      };
      return retour;
    },

    setParameters: function(aggregationId) {
        switch(aggregationId) {
            case 'MULTI_SEUIL' :
                this.parametersPanel.setVisible(true);
                this.exclusTF.show();
                this.seuil1TF.show();
                this.seuil2TF.show();
                this.seuil3TF.show();
                this.defaultValueTF.hide();
                this.seuilTF.hide();
                break;
            case 'EXCLUS' :
                this.parametersPanel.setVisible(true);
                this.exclusTF.show();
                this.seuil1TF.hide();
                this.seuil2TF.hide();
                this.seuil3TF.hide();
                this.defaultValueTF.show();
                this.seuilTF.hide();
                break;
            case 'EXCLUS_AVG_SEUIL' :
                this.parametersPanel.setVisible(true);
                this.exclusTF.show();
                this.seuil1TF.hide();
                this.seuil2TF.hide();
                this.seuil3TF.hide();
                this.defaultValueTF.hide();
                this.seuilTF.show();
                break;
            case 'EXCLUS_AVG' :
                this.parametersPanel.setVisible(true);
                this.exclusTF.show();
                this.seuil1TF.hide();
                this.seuil2TF.hide();
                this.seuil3TF.hide();
                this.defaultValueTF.hide();
                this.seuilTF.hide();
                break;
            case 'AVG' :
            case 'AVG_WEIGHT' :
            case 'AVG_ALL' :
                this.parametersPanel.setVisible(false);
                this.exclusTF.hide();
                this.seuil1TF.hide();
                this.seuil2TF.hide();
                this.seuil3TF.hide();
                this.defaultValueTF.hide();
                this.seuilTF.hide();
        }
    },

    initComponent : function(){
        this.exclusTF = new Ext.form.NumberField({
            fieldLabel:     getI18nResource('caqs.modeleditor.modelEdition.editAgregation.VALUE_EXCLUS'),
            width:          220,
            validateOnBlur: true,
            maxValue:       4,
            minValue:       1,
            value:          (this.defaultValues && this.defaultValues.VALUE_EXCLUS)?this.defaultValues.VALUE_EXCLUS:'',
            allowDecimals:  false
        });
        this.seuil1TF = new Ext.form.NumberField({
            fieldLabel:     getI18nResource('caqs.modeleditor.modelEdition.editAgregation.SEUIL1'),
            width:          220,
            validateOnBlur: true,
            maxValue:       100,
            minValue:       0,
            value:          (this.defaultValues && this.defaultValues.SEUIL1)?this.defaultValues.SEUIL1:'',
            allowDecimals:  false
        });
        this.seuil2TF = new Ext.form.NumberField({
            fieldLabel:     getI18nResource('caqs.modeleditor.modelEdition.editAgregation.SEUIL2'),
            width:          220,
            validateOnBlur: true,
            maxValue:       100,
            minValue:       0,
            value:          (this.defaultValues && this.defaultValues.SEUIL2)?this.defaultValues.SEUIL2:'',
            allowDecimals:  false
        });
        this.seuil3TF = new Ext.form.NumberField({
            fieldLabel:     getI18nResource('caqs.modeleditor.modelEdition.editAgregation.SEUIL3'),
            width:          220,
            validateOnBlur: true,
            maxValue:       100,
            minValue:       0,
            value:          (this.defaultValues && this.defaultValues.SEUIL3)?this.defaultValues.SEUIL3:'',
            allowDecimals:  false
        });
        this.seuilTF = new Ext.form.NumberField({
            fieldLabel:     getI18nResource('caqs.modeleditor.modelEdition.editAgregation.SEUIL'),
            width:          220,
            validateOnBlur: true,
            maxValue:       100,
            minValue:       1,
            value:          (this.defaultValues && this.defaultValues.SEUIL)?this.defaultValues.SEUIL:'',
            allowDecimals:  false
        });
        this.defaultValueTF = new Ext.form.NumberField({
            fieldLabel:     getI18nResource('caqs.modeleditor.modelEdition.editAgregation.DEFAULT'),
            width:          220,
            validateOnBlur: true,
            maxValue:       4,
            minValue:       1,
            value:          (this.defaultValues && this.defaultValues.DEFAULT)?this.defaultValues.DEFAULT:'',
            allowDecimals:  false
        });
        

        var agregationTypeStore = new Ext.ux.CaqsJsonStore({
            url:        requestContextPath + '/AgregationList.do',
            fields:     ['id', 'lib']
        });
        var defaultAg = (this.defaultValues!=undefined)?this.defaultValues.id:null;
        this.agregationTypeCB = new Ext.form.ComboBox({
            store:      	agregationTypeStore,
            displayField:	'lib',
            valueField: 	'id',
            hiddenName: 	'id',
            width:		300,
            editable:		false,
            allowBlank: 	false,
            forceSelection:	true,
            triggerAction:	'all',
            autocomplete:	false
        });
        agregationTypeStore.on('load', function(store, options) {
            if(defaultAg==null) {
                if(store.data.items!=null && store.data.items.length > 0) {
                    defaultAg = store.data.items[0].data.id;
                    this.agregationTypeCB.setValue(store.data.items[0].data.id);
                }
            }
            this.agregationTypeCB.setValue(defaultAg);
            this.setParameters(defaultAg);
        }, this);
        this.agregationTypeCB.on('select', function(combo, record, index) {
            this.setParameters(record.data.id);
        }, this);


        var typeEltsStore = new Ext.ux.CaqsJsonStore({
            url:        requestContextPath + '/ElementTypeList.do',
            fields:     ['id_telt', 'lib_telt']
        });
        typeEltsStore.on('beforeload', function(store, options) {
            store.baseParams.addElementTypeAll = true;
        }, this);

        var defaultET = (this.defaultValues!=undefined)?this.defaultValues.elementType:null;
        this.elementTypeCB = new Ext.form.ComboBox({
            store:      	typeEltsStore,
            displayField:	'lib_telt',
            valueField: 	'id_telt',
            hiddenName: 	'id_telt',
            width:		220,
            editable:		false,
            allowBlank: 	false,
            forceSelection:	true,
            triggerAction:	'all',
            autocomplete:	false,
            anchor:             '90%',
            fieldLabel:         getI18nResource('caqs.modeleditor.modelEdition.editAgregation.elementType')
        });
        this.elementTypeCB.on('select', function(combo, record, index) {
            if(!this.parentPanel.checkElementTypeForAgregation(record.data.id)) {
                this.elementTypeCB.setValue('ALL');
            }
        }, this);
        typeEltsStore.on('load', function(store, options) {
            if(defaultET!=null) {
                this.elementTypeCB.setValue(defaultET);
            } else {
                if(store.data.items!=null && store.data.items.length > 0) {
                    this.elementTypeCB.setValue(store.data.items[0].data.id_telt);
                }
            }
        }, this);

        this.parametersPanel = new Ext.Panel({
            style:      'margin-left: 5px; margin-top: 5px;',
            anchor:     '90%',
            layout:     'column',
            //hidden:     true,
            autoHeight: true,
            items: [
                {
                    columnWidth:    '0.5',
                    layout:         'form',
                    labelWidth:     150,
                    items:      [
                        this.exclusTF,
                        this.seuil2TF
                    ]
                }
                , {
                    columnWidth:    '0.5',
                    layout:         'form',
                    labelWidth:     150,
                    items:      [
                        this.defaultValueTF,
                        this.seuilTF,
                        this.seuil1TF,
                        this.seuil3TF
                    ]
                }
            ]
        });

        this.closeBtn = new Ext.Button({
            text:	getI18nResource("caqs.modeleditor.modelEdition.editAgregation.remove"),
            icon: 	requestContextPath + '/images/delete.gif',
            cls: 	'x-btn-text-icon',
            width:      80,
            handler:    this.removeAgregation,
            scope:      this
        });

        var config = {
            items: [
                new Ext.form.FormPanel({
                    border:     false,
                    labelWidth: 250,
                    items: [
                        this.elementTypeCB
                    ]
                }),
                this.parametersPanel
            ],
            tbar: [
                this.agregationTypeCB,
                {xtype: 'tbfill'},
                this.closeBtn
            ]
        }
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelEditorEditAggregation.superclass.initComponent.apply(this, arguments);
        typeEltsStore.load();
        agregationTypeStore.load();
    }
});