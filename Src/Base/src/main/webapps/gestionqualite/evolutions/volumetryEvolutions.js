Ext.ux.CaqsVolumetryEvolutionsPanel = Ext.extend(Ext.Panel, {
    id:                             'caqsVolumetryEvolutionsPanel',
    caqsVolumetryEvolutionsGraph:    undefined,
    border:                         false,
    hideMode:                       'offsets',
    currentBaselineId:              null,
    currentBaselineLib:             null,
    idElt:                          null,
    layout:                         'fit',


    initCaqsVolumetryEvolutionsGraph: function() {
        this.caqsVolumetryEvolutionsGraph = new Ext.ux.CaqsVolumetryEvolutionsGraph({
            id:                 'caqsVolumetryEvolutionsGraphTimeline',
            retrieveDatasUrl:   requestContextPath + '/RetrieveVolumetryEvolutionGraphDatas.do',
            manageResize:       true
        });
    },

    initComponent : function(){
        this.initCaqsVolumetryEvolutionsGraph();
        var config = {
            border:         false,
            items:          this.caqsVolumetryEvolutionsGraph
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsVolumetryEvolutionsPanel.superclass.initComponent.call(this);
    },

    refresh: function(idElt, previousBaselineId, previousBaselineLib, currentBaselineId, currentBaselineLib) {
        this.idElt = idElt;
        this.currentBaselineId = currentBaselineId;
        this.currentBaselineLib = currentBaselineLib;
        this.caqsVolumetryEvolutionsGraph.setParams({
            idElt: this.idElt,
            idBline: this.currentBaselineId
        });
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        this.caqsVolumetryEvolutionsGraph.retrieveDatas();
    }
});
