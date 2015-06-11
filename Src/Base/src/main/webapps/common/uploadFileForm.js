Ext.ux.CaqsUploadFileForm = Ext.extend(Ext.form.FormPanel,  {
    fileUpload:     true,
    width:          500,
    frame:          true,
    autoHeight:     true,
    bodyStyle:      'padding: 10px 10px 0 10px;',
    labelWidth:     100,
    defaults: {
        anchor:     '95%',
        allowBlank: false,
        msgTarget:  'side'
    },
    uploadField:    undefined,
    monitorValid:   true,
    target:         undefined,
    uploadURL:      undefined,
    uploadWaitMsg:  undefined,
    window:         undefined,

    handleUploadResponse: function(fp, o) {
        if(o.response.responseText != null) {
            var json = Ext.util.JSON.decode(o.response.responseText);
            if(json) {
                Ext.Ajax.request({
                    url:	requestContextPath + '/ImportZipFile.do',
                    params: {
                        'target': 	this.target,
                        fileName:	json.filePathToUpload
                    }
                });
                if(this.window) {
                    this.window.close();
                }
                Caqs.Messages.checkMessages();
            }
        }
    },

    raz: function() {
        this.uploadField.setValue('');
    },

    initComponent: function(){
        Ext.ux.CaqsUploadFileForm.superclass.initComponent.apply(this, arguments);
        this.uploadField = new Ext.ux.form.FileUploadField({
            id:         this.id+'file',
            fieldLabel: getI18nResource("caqs.importupload.label"),
            name:       'file',
            buttonText: getI18nResource('caqs.importupload.browse')
        });
        var config = {
            items:  this.uploadField,
            buttons: [{
                text: getI18nResource('caqs.telecharger'),
                formBind:   true,
                handler: function(){
                    if(this.getForm().isValid()){
                        this.getForm().submit({
                            url:        this.uploadURL,
                            waitMsg:    this.uploadWaitMsg,
                            success:    this.handleUploadResponse,
                            scope:      this
                        });
                    }
                },
                scope:  this
            }]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsUploadFileForm.superclass.initComponent.call(this);
    }
});