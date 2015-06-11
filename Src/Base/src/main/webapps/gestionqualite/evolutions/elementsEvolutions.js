Ext.ux.CaqsElementsEvolutionsPanel = Ext.extend(Ext.Panel, {
    id:                             'caqsElementsEvolutionsPanel',
    caqsElementsEvolutionsGraph:    undefined,
    linearCheckbox:                 undefined,
    border:                         false,
    hideMode:                       'offsets',
    previousBaselineId:             null,
    currentBaselineId:              null,
    previousBaselineLib:            null,
    currentBaselineLib:             null,
    idElt:                          null,
    layout:                         'anchor',
    autoScroll:                     true,


    initCaqsElementsEvolutionsGraph: function() {
        this.caqsElementsEvolutionsGraph = new Ext.ux.CaqsElementsEvolutionsGraph({
            id:                 'caqsElementsEvolutionsGraphTimeline',
            anchor:             '98% 100%',
            retrieveDatasUrl:   requestContextPath + '/RetrieveElementsEvolutionGraphDatas.do',
            manageResize:       true
        });
    },

    initComponent : function(){
        this.initCaqsElementsEvolutionsGraph();
        this.linearCheckbox = new Ext.form.Checkbox({
            boxLabel:   getI18nResource("caqs.evolutions.elementEvolution.linearAxis")
            , listeners: {
                scope: this
                , 'check': function(checkbox, checked) {
                    this.caqsElementsEvolutionsGraph.setLinear(checked);
                }
            }
        });
        
        var config = {
            border:         false,
            items:          [
                new Ext.Panel({
                    style:  'margin-bottom: 5px; margin-left: 5px; margin-top: 5px;',
                    layout: 'fit',
                    anchor: '98%',
                    height: 25,
                    border: false,
                    items:  this.linearCheckbox
                })
                , this.caqsElementsEvolutionsGraph
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsGoalsCriterionsEvolutionsPanel.superclass.initComponent.call(this);
    },

    refresh: function(idElt, previousBaselineId, previousBaselineLib, currentBaselineId, currentBaselineLib) {
        this.idElt = idElt;
        this.previousBaselineId = previousBaselineId;
        this.previousBaselineLib = previousBaselineLib;
        this.currentBaselineId = currentBaselineId;
        this.currentBaselineLib = currentBaselineLib;
        this.caqsElementsEvolutionsGraph.setParams({
            idElt: this.idElt,
            currentIdBline: this.currentBaselineId,
            previousIdBline: this.previousBaselineId
        });
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        this.caqsElementsEvolutionsGraph.retrieveDatas();
    }
});
