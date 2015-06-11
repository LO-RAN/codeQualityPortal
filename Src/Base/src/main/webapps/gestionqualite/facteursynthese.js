Ext.ux.CaqsTopDownFacteurSynthese = function(config) {
    // call parent constructor
    Ext.ux.CaqsTopDownFacteurSynthese.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsDomainSynthesisRepartitionChart constructor


Ext.extend(Ext.ux.CaqsTopDownFacteurSynthese, Ext.Panel, {
    factorBeanId:               undefined,
    //autoHeight:                 true,
    //autoWidth:                  true,
    border:                     false,
    autoScroll:                 true,
    grid:                       undefined,
    layout:                     'card',
    criterionDetail:            undefined,
    hideMode:                   'offsets',
    activeItem:                 0,

    initComponent : function(){
        this.grid = new Ext.ux.CaqsFacteurSyntheseGridPanel({
            facteursynthese: this
        });

        this.criterionDetail = new Ext.ux.CaqsGestionQualiteCritere({});

        /*Ext.EventManager.onWindowResize(function(){
            this.grid.setWidth(Ext.getBody().getWidth()-20);
        }, this);*/

        var config = {
            items:  [
            this.grid,
            this.criterionDetail
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsTopDownFacteurSynthese.superclass.initComponent.apply(this, arguments);
    },

    reload: function(goalId) {
        this.factorBeanId = goalId;
        this.setActiveItem(0);
        this.grid.reloadGrid(goalId);
        Caqs.Portal.setCurrentScreen('topdown_synthesis_goal');
    },

    setActiveItem: function(index) {
        var l = this.getLayout();
        l.setActiveItem(index);
    }
});