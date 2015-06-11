Ext.ux.AdministrationPanel = function(config) {
    // call parent constructor
    Ext.ux.AdministrationPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.AdministrationPanel constructor
//used to have a masked panel specific to administration

Ext.extend(Ext.ux.AdministrationPanel, Ext.Panel, {
    hideMode:                   'offsets',
    internalMask:               undefined,
    cls:                        'mask-below-menu',
    maskedPanel:                undefined,
    administration:             undefined,

    showMask: function() {
        if(this.internalMask==null) {
            this.internalMask = new Ext.LoadMask(this.maskedPanel.getEl());
        }
        Caqs.Portal.setShownMask(this.internalMask);
        this.internalMask.show();
    },

    hideMask: function() {
        if(this.internalMask!=null) {
            this.internalMask.hide();
        }
        Caqs.Portal.setShownMask(undefined);
    },

    initComponent : function(){
        this.administrationPanel = new Ext.ux.CaqsAdministrationPanel();
        
        this.maskedPanel = new Ext.Panel({
            layout:     'fit'
            , border:   false
            , items:    this.administrationPanel
        })

        var config = {
            layout:         'fit',
            border:         false,
            items:          this.maskedPanel
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CommonLayoutPanel.superclass.initComponent.apply(this, arguments);
    },

    lazyLoad: function() {
        this.administrationPanel.lazyLoad();
    },
    
    refresh: function() {
    }

});
