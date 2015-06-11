//utilisé pour la gestion qualité et l'architecture'
Ext.ux.CommonLayoutPanel = function(config) {
    // call parent constructor
    Ext.ux.CommonLayoutPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CommonLayoutPanel constructor


Ext.extend(Ext.ux.CommonLayoutPanel, Ext.Panel, {
    westPanel:                  undefined,
    treesPanel:                 undefined,
    iframePanel:                undefined,
    hideMode:                   'offsets',
    internalMask:               undefined,
    cardPanel:                  undefined,
    cardPanelItems:             undefined,
    cls:                        'mask-below-menu',
    maskedPanel:                undefined,
    selectElementPanel:         undefined,

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

    manageActionForElementType: function(telt) {
        return false;
    },

    setCardPanelItems: function() {

    },

    setSelectElement: function() {
        var l = this.cardPanel.getLayout();
        l.setActiveItem(1);
    },

    initComponent : function(){
        this.iframePanel = new Ext.Panel({
            id:             this.id + 'CenterPanel',
            layout:         'fit'
        });
        this.treesPanel = new Ext.ux.CaqsProjectsTreePanel({
            id:                     this.id + 'TreeProjectsPanel',
            selectedElementId:      undefined,
            commonLayoutContainer:  this
        });
        this.cardPanelItems = new Array();
        this.cardPanelItems[this.cardPanelItems.length] = this.iframePanel;
        this.selectElementPanel = new Ext.Panel({
            autoHeigt:              true,
            border:                 true,
            items: {
                fieldLabel: 		'',
                htmlEncode:		false,
                value:      		'<img src="'+requestContextPath+'/images/info.gif" /><span style="font-weight:bold;" class="normalText">&nbsp;'
                    +getI18nResource("caqs.unavailable.body")+'</span>',
                labelSeparator:		'',
                xtype :			'statictextfield'
            }
        });
        this.cardPanelItems[this.cardPanelItems.length] = this.selectElementPanel;
        this.setCardPanelItems();
        this.cardPanel = new Ext.Panel({
            border: false,
            region: 'center',
            layout: 'card'
            , items: this.cardPanelItems
        });

        this.maskedPanel = new Ext.Panel({
            layout: 'border'
            , border: false
            , items: [
                this.treesPanel,
                this.cardPanel
            ]
        })

        var config = {
            layout:         'fit',
            border:         false,
            items:          this.maskedPanel
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CommonLayoutPanel.superclass.initComponent.apply(this, arguments);
    },

    onRender : function(ct, position){
        Ext.ux.CommonLayoutPanel.superclass.onRender.call(this, ct, position);
    },

    refresh: function() {
        this.treesPanel.refresh();
    }

});
