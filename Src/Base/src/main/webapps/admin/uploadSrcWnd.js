Ext.ux.CaqsUploadSrcWnd = function(config) {
    // call parent constructor
    Ext.ux.CaqsUploadSrcWnd.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsUploadSrcWnd, Ext.Window, {
    modal:          true,
    maximizable:    false,
    resizable:      false,
    minimizable:    false,
    plain:          true,
    shadow:         false,
    title:          getI18nResource("caqs.admin.infosEA.upload"),
    width:          550,
    autoHeight:     true,
    layout:         'fit',
    uploadForm:     undefined,
    sourceDir:      undefined,

    uploadSrc: function(sourcedir) {

    },

    setSourceDir: function(sd) {
        this.sourceDir = sd;
    },

    initComponent : function(){
        var wnd = this;
        this.uploadForm = new Ext.ux.CaqsUploadFileForm({
            title:          '',
            id:             'projectsAdminUploadEASrcForm',
            uploadURL:      requestContextPath + '/ImportEASrc.do',
            uploadWaitMsg:  getI18nResource('caqs.admin.importSrc.wait'),
            parentWnd:      this,
            handleUploadResponse: function(fp, o) {
                if(o.response.responseText != null) {
                    var json = Ext.util.JSON.decode(o.response.responseText);
                    if(json) {
                        Caqs.Portal.getCaqsPortal().getAdministrationActivity().showMask();
                        Ext.Ajax.request({
                            url:	requestContextPath + '/UploadEASrcFile.do',
                            params: {
                                srcFilePath:    json.filePathToUpload,
                                sourceDir:      this.parentWnd.sourceDir
                            }
                            , scope:       this
                            , success: function(response) {
                                Caqs.Portal.getCaqsPortal().getAdministrationActivity().hideMask();
                                wnd.close();
                                if(response!=null && response.responseText != null) {
                                    var json2 = Ext.util.JSON.decode(response.responseText);
                                    if(!json2.uploadDone) {
                                        Ext.MessageBox.alert(getI18nResource("caqs.admin.infosEA.uploadKO"), json2.uploadError);
                                    } else {
                                        Ext.MessageBox.alert('', getI18nResource("caqs.admin.infosEA.uploadOK"));
                                    }
                                }
                            }
                        });
                        Caqs.Messages.checkMessages();
                    }
                }
            }
        });
        var config = {
            items: [
                    this.uploadForm
            ]
            , buttons:    [
                new Ext.Button({
                    scope:      this,
                    cls: 	'x-btn-text-icon',
                    icon:	requestContextPath + '/images/help.gif',
                    handler:    function() {
                        Ext.MessageBox.alert('', getI18nResource('caqs.infosEA.uploadSrcHelp'));
                    },
                    text:       getI18nResource('caqs.help')
                })
                , new Ext.Button({
                    scope:      this,
                    handler:    function() {
                        this.close();
                    },
                    text:       getI18nResource('caqs.close')
                })
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsUploadSrcWnd.superclass.initComponent.call(this);
    }
});

