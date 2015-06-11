Ext.ux.CaqsGlobalProjectSynthesisPanel = function(config) {
    Ext.ux.CaqsGlobalProjectSynthesisPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsGlobalProjectSynthesisPanel constructor


Ext.extend(Ext.ux.CaqsGlobalProjectSynthesisPanel, Ext.Panel, {
    layout:             'column',
    //autoHeight:         true,
    //height:             450,
    autoScroll:         true,
    border:             false,
    kiviatWidth:        0,
    kiviatUrl:          '/Kiviat.do?',
    projectId:           undefined,
    treemap:            undefined,
    autoWidth:          true,
    nb:                 0,
//    anchor:             '100% 60%',
//    hideMode:           'offsets',
    
    reloadKiviat: function() {
        this.nb++;
        var kiviatDiv = this.findById('ProjectSynthesisGlobalColumn1');
        if(kiviatDiv != null) {
            var child = undefined;
            if(kiviatDiv.items != undefined) {
                child = kiviatDiv.getComponent('projectSynthesisGlobalImgKiviat');
                if(child != undefined) {
                    kiviatDiv.remove(child);
                }
            }
            var img = '<IMG id="golbalProjectSynthesisKiviatImg" src="';
            img += requestContextPath + this.kiviatUrl+'nb='+this.nb;
            img += "&width="+(this.kiviatWidth - 10);
            img += "&height="+parseInt((this.kiviatWidth - 10)*0.5);

            img += '" />';
            child = new Ext.Panel({
                border: false,
                id: 'projectSynthesisGlobalImgKiviat',
                html:   img
            });
            kiviatDiv.add(child);
            kiviatDiv.doLayout();
        }
    },

    initComponent : function(){
        Ext.ux.CaqsGlobalProjectSynthesisPanel.superclass.initComponent.call(this);
        this.treemap = new Ext.ux.CaqsProjectTreeMapPanel({
            columnWidth:    0.49
        });
        var config = {
            items:  [
                this.treemap
                ,{//colonne 2
                    id:             'ProjectSynthesisGlobalColumn1',
                    columnWidth:    .49,
                    border:         false,
                    listeners:      {
                                    'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                                        this.kiviatWidth = adjWidth;
                                        this.reloadKiviat();
                                    },
                                    scope:  this
                    }
                }
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsGlobalProjectSynthesisPanel.superclass.initComponent.call(this);
    },

    refresh: function() {
        //Caqs.Portal.showGlobalLoadingMask();
        this.reloadKiviat();
        this.treemap.refresh();
    },

    removeMask: function() {
        //Caqs.Portal.hideGlobalLoadingMask();
    }
});