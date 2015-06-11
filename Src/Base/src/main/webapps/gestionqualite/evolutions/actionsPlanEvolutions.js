Ext.ux.CaqsActionsPlanEvolutionsPanel = Ext.extend(Ext.ux.Portal, {
    anchor:                         '98% 98%',
    id:                             'caqsActionsPlanEvolutionsPanel',
    caqsActionsPlanEvolutionsGraph: undefined,
    caqsPreviousKiviat:             undefined,
    caqsCurrentKiviat:              undefined,
    border:                         false,
    hideMode:                       'offsets',
    nb:                             0,
    previousActionsPlanKiviatWidth: 0,
    previousActionsKiviatHeight:    0,
    currentKiviatWidth:             0,
    currentKiviatHeight:            0,
    previousBaselineId:             null,
    currentBaselineId:              null,
    previousBaselineLib:            null,
    currentBaselineLib:             null,
    idElt:                          null,
    previousActionsPlanKiviatPortlet:null,
    currentKiviatPortlet:           null,
    caqsActionsPlanEvolutionsGraphPortlet:     null,

    reloadPreviousActionsPlanKiviat: function() {
        if(this.previousBaselineId) {
            this.nb++;
            var url = 'ActionPlanKiviat.do?baselineId='+this.previousBaselineId+'&showTitle=false'+
                '&nb='+this.nb+"&width="+this.previousActionsPlanKiviatWidth
                + "&height="+this.previousActionsKiviatHeight;
            var imgElt = document.getElementById('caqsActionPlanEvolutionsPreviousActionsPlanKiviatImg');
            if(imgElt != null) {
                imgElt.src = url;
            } else {
                var kiviatDiv = this.findById('portletCaqsActionsPlanEvolutionsPreviousKiviat');
                if(kiviatDiv != null) {
                    var img = '<IMG id="caqsActionPlanEvolutionsPreviousActionsPlanKiviatImg" src="';
                    img += url+ '" />';
                    var child = new Ext.Panel({
                        border: false,
                        id:     'caqsActionPlanEvolutionsPreviousActionsPlanKiviat',
                        html:   img
                    });
                    kiviatDiv.add(child);
                    kiviatDiv.doLayout();
                }
            }
        }
    },

    initPreviousActionsPlanKiviatPortlet: function() {
        this.previousActionsPlanKiviatPortlet = new Ext.ux.Portlet({
            id:         'portletCaqsActionsPlanEvolutionsPreviousKiviat',
            title:      getI18nResource("caqs.dashboard.projectsGrid.title"),
            collapsible: false,
            tools:      [
                        minimizeTool
                        , maximizeTool
                    ],
            //items:      this.caqsGridDashboard,
            column:     'caqsActionsPlanEvolutionsPortalColumn0'
            /*,stateEvents: ["move","position","drop","hide","show","collapse","expand","columnmove","columnresize","sortchange"],
            stateful:true,
            getState: function(){
                return {
                    collapsed:  this.collapsed,
                    hidden:     this.hidden,
                    column:     this.ownerCt.id
                };
            }*/
            ,
            listeners: {
                scope:      this,
                resize:     function(cmp, adjWidth, adjHeight, rawWidth, rawHeight) {
                    this.previousActionsPlanKiviatWidth = adjWidth;
                    this.previousActionsKiviatHeight = adjHeight;
                    this.reloadPreviousActionsPlanKiviat();
                }
            }
        });
    },

    reloadCurrentKiviat: function() {
        if(this.previousBaselineId) {
            this.nb++;
            var url = 'ActionPlanKiviat.do?baselineId='+this.currentBaselineId+'&showTitle=false'+
                '&nb='+this.nb+"&width="+this.currentKiviatWidth
                + "&height="+this.currentKiviatHeight;
            var imgElt = document.getElementById('caqsActionsPlanEvolutionsCurrentKiviatImg');
            if(imgElt != null) {
                imgElt.src = url;
            } else {
                var kiviatDiv = this.findById('portletCaqsActionsPlanEvolutionsCurrentKiviat');
                if(kiviatDiv != null) {
                    var img = '<IMG id="caqsActionsPlanEvolutionsCurrentKiviatImg" src="';
                    img += url+ '" />';
                    var child = new Ext.Panel({
                        border: false,
                        id:     'caqsActionsPlanEvolutionsCurrentKiviat',
                        html:   img
                    });
                    kiviatDiv.add(child);
                    kiviatDiv.doLayout();
                }
            }
        }
    },

    initCurrentKiviatPortlet: function() {
        this.currentKiviatPortlet = new Ext.ux.Portlet({
            id:         'portletCaqsActionsPlanEvolutionsCurrentKiviat',
            title:      getI18nResource("caqs.dashboard.projectsGrid.title"),
            tools:      [
                        minimizeTool
                        , maximizeTool
                    ],
            collapsible: false,
            //items:      this.caqsGridDashboard,
            column:     'caqsActionsPlanEvolutionsPortalColumn1'
            /*,stateEvents: ["move","position","drop","hide","show","collapse","expand","columnmove","columnresize","sortchange"],
                stateful:true,
                getState: function(){
                    return {
                        collapsed:  this.collapsed,
                        hidden:     this.hidden,
                        column:     this.ownerCt.id
                    };
                }*/
            ,
            listeners: {
                scope:      this,
                resize:     function(cmp, adjWidth, adjHeight, rawWidth, rawHeight) {
                    this.currentKiviatWidth = adjWidth;
                    this.currentKiviatHeight = adjHeight;
                    this.reloadCurrentKiviat();
                }
            }
        });
    },

    initActionsPlanEvolutionsGraph: function() {
        this.caqsActionsPlanEvolutionsGraph = new Ext.ux.CaqsActionsPlanEvolutionsGraph({
            id:                 'caqsActionsPlanEvolutionsGraphTimeline',
            style:              'margin-top: 5px; margin-left: 5px; margin-bottom: 10px;',
            retrieveDatasUrl:   requestContextPath + '/RetrieveActionsPlanEvolutionGraphDatas.do',
            propertiesHeaders: [
            getI18nResource("caqs.dashboard.timeplot.date.legend"),
            null,
            getI18nResource("caqs.dashboard.timeplot.nbAnalysisForMonth.legend")
            ]
        });
        this.caqsActionsPlanEvolutionsGraphPortlet = new Ext.ux.Portlet({
            id:         'portletCaqsActionPlanEvolutionsGraphPortlet',
            title:      getI18nResource("caqs.evolution.synthese.actionsplangraph"),
            collapsible: false,
            tools:      [
                        minimizeTool
                        , maximizeTool
                    ],
            items:      this.caqsActionsPlanEvolutionsGraph,
            column:     'caqsActionsPlanEvolutionsPortalColumn2'
            /*,stateEvents: ["move","position","drop","hide","show","collapse","expand","columnmove","columnresize","sortchange"],
            stateful:true,
            getState: function(){
                return {
                    collapsed:  this.collapsed,
                    hidden:     this.hidden,
                    column:     this.ownerCt.id
                };
            }*/
            , listeners: {
                scope:      this,
                resize:     function(cmp, adjWidth, adjHeight, rawWidth, rawHeight) {
                    if(this.caqsActionsPlanEvolutionsGraph) {
                        this.caqsActionsPlanEvolutionsGraph.setWidth(adjWidth);
                        this.caqsActionsPlanEvolutionsGraph.setHeight(adjHeight);
                    }
                }
            }
        });
    },
    
    initComponent : function(){
        this.initCurrentKiviatPortlet();
        this.initPreviousActionsPlanKiviatPortlet();
        this.initActionsPlanEvolutionsGraph();
        var columnItems = new Array();
        var colWidth = 0.48;
        columnItems[columnItems.length] = {
            id:             'caqsActionsPlanEvolutionsPortalColumn0',
            columnWidth:    colWidth,
            style:          'padding-left: 5px; padding-top: 5px; padding-bottom: 5px; padding-right: 5px;',
            items:          this.previousActionsPlanKiviatPortlet
        };
        columnItems[columnItems.length] = {
            id:             'caqsActionsPlanEvolutionsPortalColumn1',
            columnWidth:    colWidth,
            style:          'padding-left: 5px; padding-top: 5px; padding-bottom: 5px; padding-right: 5px;',
            items:          this.currentKiviatPortlet
        };
        columnItems[columnItems.length] = {
            id:             'caqsActionsPlanEvolutionsPortalColumn2',
            columnWidth:    0.96,
            style:          'padding-left: 5px; padding-bottom: 5px; padding-right: 5px;',
            items:          this.caqsActionsPlanEvolutionsGraphPortlet
        };
        var config = {
            scrollOffset:   25,
            autoScroll:     true,
            border:         false,
            items:          columnItems
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsActionsPlanEvolutionsPanel.superclass.initComponent.call(this);
    },

    refresh: function(idElt, previousBaselineId, previousBaselineLib, currentBaselineId, currentBaselineLib) {
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        this.idElt = idElt;
        this.previousBaselineId = previousBaselineId;
        this.previousBaselineLib = previousBaselineLib;
        this.currentBaselineId = currentBaselineId;
        this.currentBaselineLib = currentBaselineLib;
        if(this.previousActionsPlanKiviatPortlet.maximized) {
            minimizeTool.handler(null, null, this.previousActionsPlanKiviatPortlet);
        } else if(this.currentKiviatPortlet.maximized) {
            minimizeTool.handler(null, null, this.currentKiviatPortlet);
        } else if(this.caqsActionsPlanEvolutionsGraphPortlet.maximized) {
            minimizeTool.handler(null, null, this.caqsActionsPlanEvolutionsGraphPortlet);
        }
        this.previousActionsPlanKiviatPortlet.setTitle(getI18nResource("caqs.evolution.synthese.kiviat", this.previousBaselineLib));
        this.currentKiviatPortlet.setTitle(getI18nResource("caqs.evolution.synthese.kiviat", this.currentBaselineLib));
        this.reloadPreviousActionsPlanKiviat();
        this.reloadCurrentKiviat();
        this.caqsActionsPlanEvolutionsGraph.setParams({
            idElt: this.idElt,
            currentIdBline: this.currentBaselineId,
            previousBlineLib: this.previousBaselineLib,
            currentBlineLib: this.currentBaselineLib,
            previousIdBline: this.previousBaselineId
        });
        this.caqsActionsPlanEvolutionsGraph.retrieveDatas();
    }
});
