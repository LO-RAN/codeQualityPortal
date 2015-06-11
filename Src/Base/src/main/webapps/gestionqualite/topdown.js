Ext.ux.CaqsTopDown = function(config) {
    // call parent constructor
    Ext.ux.CaqsTopDown.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsDomainSynthesisRepartitionChart constructor


Ext.extend(Ext.ux.CaqsTopDown, Ext.Panel, {
    topDownSynthesis:           undefined,
    facteursynthese:            undefined,
    border:                     false,
    autoScroll:                 true,
    layout:                     'card',
    hideMode:                   'offsets',

    initComponent : function(){
        this.topDownSynthesis = new Ext.ux.CaqsTopDownSynthesisVolumetryPanel({
            fromLabellisation:  false
        });
        this.facteursynthese = new Ext.ux.CaqsTopDownFacteurSynthese({
            id:             'topDownFactorSynthese'
        });

        var config = {
            items:  [
            this.topDownSynthesis,
            this.facteursynthese
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsTopDown.superclass.initComponent.apply(this, arguments);
    },

    setTopDownSynthesis: function() {
        this.setActiveItem(0);
        this.topDownSynthesis.reload();
    },

    setGoal: function(goalId) {
        this.setActiveItem(1);
        this.facteursynthese.reload(goalId);
        Caqs.Portal.setCurrentScreen('topdown_synthesis_goal');
    },

    setActiveItem: function(index) {
        var l = this.getLayout();
        l.setActiveItem(index);
    }
});