Ext.ux.CaqsUploadDialogBox = function(config) {
    // call parent constructor
    Ext.ux.CaqsUploadDialogBox.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsUploadDialogBox, Ext.form.FormPanel, {
    csvSeparatorComboBox:           undefined,
    csvSeparatorRecord:             undefined,
    csvSeparatorStore:              undefined,
    fileTypeComboBox:               undefined,
    fileTypeRecord:                 undefined,
    fileTypeStore:                  undefined,
    masterCheckBox:                 undefined,
    border:                         false,
    monitorValid:                   true,
    fileUpload:                     true,
    updateButton:                   undefined,
    cancelButton:                   undefined,
    fileUploadField:                undefined,
    style:                          'margin-top: 5px; margin-left: 5px',
    frame:                          true,
    labelWidth:                     200,
    width:                          550,

    submit: function() {
        this.getForm().getEl().dom.submit();
    },

    updateFunction: function() {
        var formEl = this.getForm().getEl();
        formEl.dom.enctype = 'MULTIPART/FORM-DATA';
        formEl.dom.action= requestContextPath + '/UploadData.do';
        formEl.dom.submit();
    },

    cancelFunction: function() {
        self.close();
    },

    fileTypeSelectionChanged: function(combo, record, index) {
        this.csvSeparatorComboBox.setDisabled(!record.data.needsSeparator);
        if(record.data.needsSeparator) {
            this.csvSeparatorComboBox.setValue(this.csvSeparatorComboBox.store.getAt(0).data.idSeparator);
        }
    },

    initComponent : function(){
        this.updateButton = new Ext.Button({
            text:       getI18nResource("caqs.upload"),
            name:       'caqs.upload.update',
            formBind:   true,
            handler:    this.updateFunction,
            scope:      this
	});

        this.cancelButton = new Ext.Button({
            text:       getI18nResource("caqs.annuler"),
            name:       'caqs.upload.cancel',
            handler:    this.cancelFunction,
            scope:      this
	});

        this.fileUploadField = new Ext.form.Field({
            xtype: 	'field',
            inputType:	'file',
            style:      'width: 400px; margin-bottom:5px;',
            fieldLabel: getI18nResource("caqs.upload.file"),
            name:	'file',
            allowBlank: false
        });

        this.fileTypeRecord = Ext.data.Record.create([
            {name:  'idTypeFichier'},
            {name:  'libTypeFichier'},
            {name:  'needsSeparator'}
        ]);
        this.fileTypeStore = new Ext.ux.CaqsJsonStore({
                fields:     ['idTypeFichier', 'libTypeFichier', 'needsSeparator']
        });

        this.fileTypeComboBox = new Ext.form.ComboBox({
                store:              this.fileTypeStore,
                name:               'fileTypeCB',
                id:                 'fileTypeCB',
                displayField:       'libTypeFichier',
                valueField:         'idTypeFichier',
                hiddenName:         'fileType',
                emptyText:          getI18nResource("caqs.upload.selectFileType"),
                editable:           false,
                triggerAction:      'all',
                style:              'margin-bottom:5px;',
                fieldLabel:         getI18nResource("caqs.upload.fileType"),
                autocomplete:       false,
                width:              300,
                mode:               'local',
                allowBlank:         false
        });
        this.fileTypeComboBox.on('select', this.fileTypeSelectionChanged, this);

        this.csvSeparatorRecord = Ext.data.Record.create([
            {name:  'idSeparator'},
            {name:  'libSeparator'}
        ]);
        this.csvSeparatorStore = new Ext.data.SimpleStore({
                fields:     ['idSeparator', 'libSeparator']
        });

        this.csvSeparatorComboBox = new Ext.form.ComboBox({
                store:              this.csvSeparatorStore,
                name:               'csvSeparatorCB',
                id:                 'csvSeparatorCB',
                displayField:       'libSeparator',
                valueField:         'idSeparator',
                hiddenName:         'csvSeparator',
                editable:           false,
                disabled:           true,
                style:              'margin-bottom:5px;',
                triggerAction:      'all',
                fieldLabel:         getI18nResource("caqs.upload.separator"),
                autocomplete:       false,
                allowBlank:         true,
                width:              300,
                mode:               'local'
        });

        this.masterCheckBox = new Ext.form.Checkbox({
            fieldLabel:         getI18nResource("caqs.upload.master"),
            id:                 'master',
            llowBlank:          true,
            name:               'master'
        });

        var config = {
            items:      [
                this.fileUploadField,
                this.fileTypeComboBox,
                this.csvSeparatorComboBox,
                this.masterCheckBox
            ],
            buttons: [
                this.updateButton,
                this.cancelButton
            ]
        };

        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsUploadDialogBox.superclass.initComponent.apply(this, arguments);
        this.on('clientvalidation', function(form, validated) {
            if(validated) {
                this.updateButton.setDisabled(this.fileUploadField.getValue() == '');
            }
        }, this);
    },

    onRender : function(cmpt){
        Ext.ux.CaqsUploadDialogBox.superclass.onRender.call(this, cmpt);
        Ext.Ajax.request({
            url:		requestContextPath+'/UploadRetrieveFileTypes.do',
            scope:      this,
            success:	this.fillDropDowns
        });
    },

    fillDropDowns: function(response) {
        if(response.responseText!='' && response.responseText!='[]') {
            var globalResults = Ext.util.JSON.decode(response.responseText);
            var results = globalResults.dataArray[0];
            for(var i=0; i<results.length; i++) {
                var record = new this.fileTypeRecord({
                    idTypeFichier: results[i].idTypeFichier,
                    libTypeFichier:results[i].libTypeFichier,
                    needsSeparator:results[i].needsSeparator
                });
                this.fileTypeComboBox.store.add(record);
            }
            
            results = globalResults.dataArray[1];
            for(var i=0; i<results.length; i++) {
                var record = new this.csvSeparatorRecord({
                    idSeparator: results[i].idSeparator,
                    libSeparator:results[i].libSeparator
                });
                this.csvSeparatorComboBox.store.add(record);
            }
        }
    }
});