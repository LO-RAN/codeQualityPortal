Ext.ux.CaqsPurgeBaselineWnd = function(config) {
    // call parent constructor
    Ext.ux.CaqsPurgeBaselineWnd.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsPurgeBaselineWnd, Ext.Window, {
    //autoWidth:                  true,
    width:                      500,
    autoHeight:                 true,
    idPro:                      undefined,
    layout:                     'fit',
    adminBlBaselinesCB:         undefined,
    adminBlBaselinesCBStore:    undefined,
    modal:                      true,
    maximizable:                false,
    resizable:                  false,
    minimizable:                false,
    plain:                      true,
    shadow:                     false,
    title:                      getI18nResource("caqs.admin.adminBlTitle"),

    setIdPro: function(i) {
        this.idPro = i;
    },

    adminBlAfterLoadingBaselines: function(store, records, options) {
        if(records.length > 0) {
            this.adminBlBaselinesCB.setValue(records[0].data.baselineId);
        } else {
            this.adminBlBaselinesCB.setValue('');
        }
        Caqs.Portal.getCaqsPortal().getAdministrationActivity().hideMask();
    },

    reloadBaselines: function() {
        Caqs.Portal.getCaqsPortal().getAdministrationActivity().hideMask();
        this.adminBlBaselinesCBStore.load({
            params: {
                idPro: this.idPro
            }
        });
    },

    deleteBaseline: function(result) {
        if(result == 'yes') {
            var idBaseline = this.adminBlBaselinesCB.getValue();
            Caqs.Portal.getCaqsPortal().getAdministrationActivity().showMask();
            Ext.Ajax.request({
                url:		requestContextPath+'/BaselineAdmin.do',
                params: {
                            projectId:  this.idPro,
                            baselineId: idBaseline,
                            action:     DELETE_BASELINE_ACTION
                },
                timeout:    1800000,
                scope:      this,
                success:    this.reloadBaselines
            });
        }
    },

    askDeleteBaseline: function() {
        Ext.MessageBox.confirm('', getI18nResource("caqs.purgeBL.confirmdelete"), this.deleteBaseline, this);
    },

    renameBaseline: function(result, text) {
        if(result == 'ok') {
            var idBaseline = this.adminBlBaselinesCB.getValue();
            Caqs.Portal.getCaqsPortal().getAdministrationActivity().showMask();
            Ext.Ajax.request({
                url:		requestContextPath+'/BaselineAdmin.do',
                params: {
                            projectId:      this.idPro,
                            baselineId:     idBaseline,
                            baselineName:   text,
                            action:         RENAME_BASELINE_ACTION
                },
                scope:      this,
                success:	this.reloadBaselines
            });
        }
    },

    askRenameBaseline: function() {
        Ext.MessageBox.prompt('', getI18nResource("caqs.adminbl.askNewName"), this.renameBaseline, this);
    },

    initAdminBlComboBoxes: function() {
        this.adminBlBaselinesCBStore = new Ext.ux.CaqsJsonStore({
            url: requestContextPath + '/AdminBlRetrieveBaselinesForProject.do',
            fields: ['baselineId', 'baselineLib']
        });

        this.adminBlBaselinesCB = new Ext.form.ComboBox({
            name:       	'idAdminBlBaselinesCB',
            id:         	'idAdminBlBaselinesCB',
            displayField:	'baselineLib',
            valueField: 	'baselineId',
            hiddenName: 	'baselineId',
            editable:		false,
            store:		this.adminBlBaselinesCBStore,
            triggerAction:	'all',
            autocomplete:	false,
            fieldLabel:         baselinesCbLib,
            anchor:		'90%',
            allowBlank:         false,
            mode:      		'local'
        });
        this.adminBlBaselinesCBStore.addListener('load', this.adminBlAfterLoadingBaselines, this);
        this.adminBlBaselinesCBStore.on('beforeload', function() {
            Caqs.Portal.getCaqsPortal().getAdministrationActivity().showMask();
        }, this);
    },

    initComponent : function(){
        this.initAdminBlComboBoxes();
        Ext.ux.CaqsPurgeBaselineWnd.superclass.initComponent.apply(this, arguments);

        var adminBlPanel = new Ext.form.FormPanel({
            border:             false,
            autoHeight:         true,
            autoWidth:          true,
            monitorValid:       true,
            bodyStyle:          'padding-top: 5px; padding-left: 5px;',
            labelWidth:         100,
            items: [
                                this.adminBlBaselinesCB
            ],
            buttons: [
                new Ext.Button({
                    scope:      this,
                    text:       getI18nResource("caqs.purgeBL.effacer"),
                    formBind:   true,
                    handler:    this.askDeleteBaseline
                }),
                new Ext.Button({
                    scope:      this,
                    text:       getI18nResource("caqs.adminbl.rename"),
                    formBind:   true,
                    handler:    this.askRenameBaseline
                })
            ]
        });

        this.add(adminBlPanel);
        this.on('beforeclose', this.onBeforeClose, this);
        this.on('show', this.onShow, this);
    },

    onRender : function(cmpt){
        Ext.ux.CaqsPurgeBaselineWnd.superclass.onRender.call(this, cmpt);
        this.reloadBaselines();
    },

    onShow: function(cmpt) {
        Ext.ux.CaqsPurgeBaselineWnd.superclass.onShow.call(this, cmpt);
        this.reloadBaselines();
    },

    onBeforeClose: function(wnd) {
        wnd.hide();
        return false;
    }
});

