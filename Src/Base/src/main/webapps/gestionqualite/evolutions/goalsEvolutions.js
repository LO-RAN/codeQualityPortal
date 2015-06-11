Ext.ux.CaqsGoalsCriterionsEvolutionsPanel = Ext.extend(Ext.ux.Portal, {
    anchor:                         '98% 98%',
    id:                             'caqsGoalsCriterionsEvolutionsPanel',
    caqsEvolutionsGraph:            undefined,
    caqsPreviousKiviat:             undefined,
    caqsCurrentKiviat:              undefined,
    border:                         false,
    hideMode:                       'offsets',
    kiviatUrl:                      'Kiviat.do?',
    nb:                             0,
    previousKiviatWidth:            0,
    previousKiviatHeight:           0,
    currentKiviatWidth:             0,
    currentKiviatHeight:            0,
    previousBaselineId:             null,
    currentBaselineId:              null,
    previousBaselineLib:            null,
    currentBaselineLib:             null,
    idElt:                          null,
    previousKiviatPortlet:          null,
    currentKiviatPortlet:           null,
    caqsEvolutionsGraphPortlet:     null,
    evolutionsGrid:                 null,
    evolutionsGridPortlet:          null,
    evolutionsGridStore:            null,

    reloadPreviousKiviat: function() {
        if(this.previousBaselineId) {
            this.nb++;
            var url = this.kiviatUrl+'baselineId='+this.previousBaselineId+'&displayTitle=false'+
                '&nb='+this.nb+"&width="+this.previousKiviatWidth
                + "&height="+this.previousKiviatHeight;
            var imgElt = document.getElementById('caqsEvolutionsPreviousKiviatImg');
            if(imgElt != null) {
                imgElt.src = url;
            } else {
                var kiviatDiv = this.findById('portletCaqsEvolutionsPreviousKiviat');
                if(kiviatDiv != null) {
                    var img = '<IMG id="caqsEvolutionsPreviousKiviatImg" src="';
                    img += url+ '" />';
                    var child = new Ext.Panel({
                        border: false,
                        id:     'caqsEvolutionsPreviousKiviat',
                        html:   img
                    });
                    kiviatDiv.add(child);
                    kiviatDiv.doLayout();
                }
            }
        }
    },

    reloadCurrentKiviat: function() {
        if(this.currentBaselineId) {
            this.nb++;
            var url = this.kiviatUrl+'baselineId='+this.currentBaselineId+'&displayTitle=false'+
                '&nb='+this.nb+"&width="+this.currentKiviatWidth
                + "&height="+this.currentKiviatHeight;
            var imgElt = document.getElementById('caqsEvolutionsCurrentKiviatImg');
            if(imgElt != null) {
                imgElt.src = url;
            } else {
                var kiviatDiv = this.findById('portletCaqsEvolutionsCurrentKiviat');
                if(kiviatDiv != null) {
                    var img = '<IMG id="caqsEvolutionsCurrentKiviatImg" src="';
                    img += url+ '" />';
                    var child = new Ext.Panel({
                        border: false,
                        id:     'caqsEvolutionsCurrentKiviat',
                        html:   img
                    });
                    kiviatDiv.add(child);
                    kiviatDiv.doLayout();
                }
            }
        }
    },

    initCaqsEvolutionsGraph: function() {
        this.caqsEvolutionsGraph = new Ext.ux.CaqsEvolutionsGraph({
            style:              'margin-top: 5px; margin-left: 5px; margin-bottom: 10px;'
        });
        this.caqsEvolutionsGraphPortlet = new Ext.ux.Portlet({
            id:         'portletCaqsEvolutionsGraphPortlet',
            title:      getI18nResource("caqs.evolution.goalsEvolutions.gridEvolutions"),
            collapsible: false,
            tools:      [
                        minimizeTool
                        , maximizeTool
                    ],
            items:      this.caqsEvolutionsGraph,
            column:     'caqsEvolutionsPortalColumn2'
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
                    if(this.caqsEvolutionsGraph) {
                        this.caqsEvolutionsGraph.setWidth(adjWidth);
                        this.caqsEvolutionsGraph.setHeight(adjHeight);
                    }
                }
            }
        });
    },

    initPreviousKiviatPortlet: function() {
        this.previousKiviatPortlet = new Ext.ux.Portlet({
            id:         'portletCaqsEvolutionsPreviousKiviat',
            title:      getI18nResource("caqs.dashboard.projectsGrid.title"),
            collapsible: false,
            tools:      [
                        minimizeTool
                        , maximizeTool
                    ],
            //items:      this.caqsGridDashboard,
            column:     'caqsEvolutionsPortalColumn0'
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
                    this.previousKiviatWidth = adjWidth;
                    this.previousKiviatHeight = adjHeight;
                    this.reloadPreviousKiviat();
                }
            }
        });
    },


    initCurrentKiviatPortlet: function() {
        this.currentKiviatPortlet = new Ext.ux.Portlet({
            id:         'portletCaqsEvolutionsCurrentKiviat',
            title:      getI18nResource("caqs.dashboard.projectsGrid.title"),
            tools:      [
                        minimizeTool
                        , maximizeTool
                    ],
            collapsible: false,
            //items:      this.caqsGridDashboard,
            column:     'caqsEvolutionsPortalColumn1'
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

    renderTrend: function(value, p, record){
        p.attr = 'ext:qtip="'+record.data.trendPopup+'"';
        addTooltip('trend'+record.data.trend, '', record.data.trendPopup);
        var retour = '<IMG id="trend'+record.data.trend+
        '" src="'+requestContextPath+'/images/note_'+record.data.trend+'.gif" />';
        return retour;
    },

    renderScore: function(value, p, record){
        var retour = '-';
        if(record.data.doubleScore > 0) {
            retour = record.data.score;
        }
        return retour;
    },

    renderPreviousScore: function(value, p, record){
        var retour = '-';
        if(record.data.doublePreviousScore > 0) {
            retour = record.data.previousScore;
        }
        return retour;
    },

    initEvolutionsGridPortlet: function() {
        this.evolutionsGridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/EvolutionFactorEvolutionsList.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 		'goals',
                totalProperty: 	'totalCount',
                id: 		'id',
                fields: [
                'id', 'lib', 'desc', 'compl', 'score', 'previousScore', 'trend',
                'trendPopup', 'doublePreviousScore', 'doubleScore'
                ]
            })
        });
        this.evolutionsGridStore.setDefaultSort('score', 'asc');
        this.evolutionsGridStore.on('beforeload', function() {
            this.evolutionsGridStore.baseParams.id_elt = this.idElt;
            this.evolutionsGridStore.baseParams.id_bline = this.currentBaselineId;
            this.evolutionsGridStore.baseParams.id_previous_bline = this.previousBaselineId;
        }, this);

        var myTemplate = new Ext.XTemplate(
            '<p><b>'+getI18nResource("caqs.facteursynthese.desc")+':</b> {desc}</p>',
            '<tpl if="compl != \'\'">',
            '<BR/><p><b>'+getI18nResource("caqs.facteursynthese.compl")+':</b> {compl}</p>',
            '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });
        this.evolutionsGrid = new Ext.grid.GridPanel({
            store: 		this.evolutionsGridStore,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:	false,
            loadMask: 		true,
            frame:		false,
            autoHeight:         true,
            cm: new Ext.grid.ColumnModel([
                expander,
                {
                    id:         'factor',
                    header:     getI18nResource("caqs.evolution.synthese.factorevolution"),
                    width:      100,
                    sortable:   true,
                    dataIndex:  'lib'
                },
                {
                    header:     getI18nResource("caqs.evolution.synthese.factorevolution.previousScore"),
                    width:      50,
                    sortable:   true,
                    dataIndex:  'previousScore',
                    renderer:   this.renderPreviousScore,
                    align:      'right'
                },
                {
                    header:     getI18nResource("caqs.evolution.synthese.factorevolution.note"),
                    width:      50,
                    sortable:   true,
                    renderer:   this.renderScore,
                    dataIndex:  'score',
                    align:      'right'
                },
                {
                    header:     getI18nResource("caqs.evolution.synthese.factorevolution.tendance"),
                    width:      50,
                    sortable:   true,
                    dataIndex:  'trend',
                    renderer:   this.renderTrend,
                    align:      'center'
                }
                ]),
            collapsible: false,
            plugins: expander,
            iconCls: 'icon-grid',
            viewConfig: {
                forceFit:           true,
                autoExpandColumn:   0
            },
            listeners:      {
                'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                    //this.evolutionsGrid.setHeight(adjHeight);
                    this.evolutionsGrid.getView().fitColumns();
                },
                scope:  this
            }
        });
        
        this.evolutionsGrid.getView().on('refresh', function() {
            putTooltips();
        });
        this.evolutionsGridPortlet = new Ext.ux.Portlet({
            id:         'portletCaqsEvolutionsGrid',
            title:      getI18nResource("caqs.evolution.goalsEvolutions.gridEvolutions"),
            tools:      [
                        minimizeTool
                        , maximizeTool
                    ],
            collapsible: false,
            items:      this.evolutionsGrid,
            column:     'caqsEvolutionsPortalColumn3',
            listeners: {
                scope:      this,
                resize:     function(cmp, adjWidth, adjHeight, rawWidth, rawHeight) {
                    this.evolutionsGrid.setSize(adjWidth, adjHeight);
                }
            }
        /*,stateEvents: ["move","position","drop","hide","show","collapse","expand","columnmove","columnresize","sortchange"],
                stateful:true,
                getState: function(){
                    return {
                        collapsed:  this.collapsed,
                        hidden:     this.hidden,
                        column:     this.ownerCt.id
                    };
                }*/
        });
    },

    initComponent : function(){
        this.initCurrentKiviatPortlet();
        this.initPreviousKiviatPortlet();
        this.initCaqsEvolutionsGraph();
        this.initEvolutionsGridPortlet();
        var columnItems = new Array();
        var colWidth = 0.32;
        columnItems[columnItems.length] = {
            id:             'caqsEvolutionsPortalColumn0',
            columnWidth:    colWidth,
            style:          'padding-left: 5px; padding-top: 5px; padding-bottom: 5px; padding-right: 5px;',
            items:          this.previousKiviatPortlet
        };
        columnItems[columnItems.length] = {
            id:             'caqsEvolutionsPortalColumn1',
            columnWidth:    colWidth,
            style:          'padding-left: 5px; padding-top: 5px; padding-bottom: 5px; padding-right: 5px;',
            items:          this.currentKiviatPortlet
        };
        columnItems[columnItems.length] = {
            id:             'caqsEvolutionsPortalColumn3',
            columnWidth:    colWidth,
            style:          'padding-left: 5px; padding-top: 5px; padding-bottom: 5px; padding-right: 5px;',
            items:          this.evolutionsGridPortlet
        };
        columnItems[columnItems.length] = {
            id:             'caqsEvolutionsPortalColumn2',
            columnWidth:    0.96,
            style:          'padding-left: 5px; padding-bottom: 5px; padding-right: 5px;',
            items:          this.caqsEvolutionsGraphPortlet
        };
        var config = {
            scrollOffset:   25,
            autoScroll:     true,
            border:         false,
            items:          columnItems
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
        this.previousKiviatPortlet.setTitle(getI18nResource("caqs.evolution.synthese.kiviat", this.previousBaselineLib));
        this.currentKiviatPortlet.setTitle(getI18nResource("caqs.evolution.synthese.kiviat", this.currentBaselineLib));
        //this.caqsBaselinesGlobalTimeline.showMask();
        /*this.displayDomainsCBStore.load();
        if(this.displayConnectionTimeplot) {
            this.caqsConnectionsStatsTimeline.showMask();
            this.caqsConnectionsStatsTimeline.retrieveDatas();
        }*/
        if(this.previousKiviatPortlet.maximized) {
            minimizeTool.handler(null, null, this.previousKiviatPortlet);
        } else if(this.currentKiviatPortlet.maximized) {
            minimizeTool.handler(null, null, this.currentKiviatPortlet);
        } else if(this.caqsEvolutionsGraphPortlet.maximized) {
            minimizeTool.handler(null, null, this.caqsEvolutionsGraphPortlet);
        } else if(this.evolutionsGridPortlet.maximized) {
            minimizeTool.handler(null, null, this.evolutionsGridPortlet);
        }
        this.reloadPreviousKiviat();
        this.reloadCurrentKiviat();
        this.caqsEvolutionsGraph.setParams({
            idElt: this.idElt,
            idBline: this.currentBaselineId
        });
        this.caqsEvolutionsGraph.retrieveDatas();
        this.evolutionsGridStore.load();
    }
});
