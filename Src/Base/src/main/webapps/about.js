Ext.ux.AboutWindow = function() {
    // call parent constructor
    Ext.ux.AboutWindow.superclass.constructor.call(this,  {
		});
}; // end of Ext.ux.AboutWindow constructor


Ext.extend(Ext.ux.AboutWindow, Ext.Window, {
    
    initComponent : function(){
        var imgPanel = {
            xtype: 'box',
            autoEl: {
                tag:    'img',
                src:    requestContextPath+'/images/CAQS_logo.gif',
                border: false
            }
        };

        var form = new Ext.form.FormPanel({
            labelWidth:     100,
            border:         false,
            bodyStyle:      'margin-left: 5px;',
            items: [
                imgPanel,
                {
                    fieldLabel:         getI18nResource("caqs.about.organisation"),
                    value:              '%CAQS_ORGANISATION%',
                    parentClassName: 	'smallStaticField',
                    smallLine:          true,
                    xtype :             'statictextfield'
                },
                {
                    fieldLabel:         '',
                    value:              '<A href="%CAQS_ORGANISATION_URL%" target="compuwareWebSite">%CAQS_ORGANISATION_URL%</A>',
                    htmlEncode:         false,
                    parentClassName: 	'smallStaticField',
                    smallLine:          true,
                    labelSeparator:     '',
                    xtype :             'statictextfield'
                },
                {
                    fieldLabel:         getI18nResource("caqs.about.version"),
                    value:              getI18nResource("caqs.about.versionNumber"),
                    parentClassName: 	'smallStaticField',
                    smallLine:          true,
                    xtype :             'statictextfield'
                },
                {
                    fieldLabel:         getI18nResource("caqs.about.date"),
                    value:              '%CAQS_DATE%',
                    parentClassName: 	'smallStaticField',
                    smallLine:          true,
                    xtype :             'statictextfield'
                }
            ]
        });

        this.on('beforeclose', function(wnd) {
            wnd.hide();
            return false;
        }, this);
        
        var config = {
            id:             'caqsAboutPage',
            layout:         'fit',
            modal: 			true,
            maximizable: 	true,
            resizable:		true,
            minimizable: 	false,
            width:			350,
            height:			250,
            shadow:			false,
            plain:			true,
            title:          getI18nResource("caqsPortal.about"),
            items:          form
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.AboutWindow.superclass.initComponent.call(this);
    },

    onRender: function(ct, target) {
        Ext.ux.AboutWindow.superclass.onRender.call(this, ct, target);
    }
});

