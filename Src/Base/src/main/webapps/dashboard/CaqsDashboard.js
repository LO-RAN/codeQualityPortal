Ext.ux.CaqsDashboard = function(config) {
    // call parent constructor
    Ext.ux.CaqsDashboard.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsPortal constructor


Ext.extend(Ext.ux.CaqsDashboard, Ext.Panel, {
    id:                             'caqsDashboardPortal',
    caqsGridDashboard:              undefined,
    gridDashboardAdded:             false,
    gridDashboardPortlet:           undefined,
    caqsBaselinesGlobalTimeline:    undefined,
    baselinesGlobalTimelineAdded:   false,
    caqsConnectionsStatsTimeline:   undefined,
    connectionsStatsTimelineAdded:  false,
    border:                         false,
    hideMode:                       'offsets',
    displayConnectionTimeplot:      false,
    displayGlobalBaselineTimeplot:  false,
    //autoHeight:                     true,
    displayDomainsCB:               undefined,
    displayDomainsCBStore:          undefined,
    dashboardPortal:                undefined,
    layout:                         'border',
    dashboardDefaultDomain:         undefined,
    cls:                            'mask-below-menu',

    addBaselinesGlobalTimeline: function() {
        if(!this.baselinesGlobalTimelineAdded) {
            this.caqsBaselinesGlobalTimeline = new Ext.ux.CaqsDashboardTimeline({
                    id:                 'caqsBaselinesGlobalTimeline',
                    retrieveDatasUrl:   requestContextPath + '/DashboardElementTimeplotNumberOfAnalysis.do',
                    timeplotHeight:     150,
                    xaxis:              'caqsTimeAxe',
                    manageResize:       true,
                    tooltipTemplate:    '<b>'+getI18nResource("caqs.dashboard.timeplot.nbAnalysisForMonth.popup")
                        +' {datapointX:dateAsLong("'+javascriptDateFormatMonthYear+'")}</b> : {datapointY}',
                    propertiesHeaders: [
                                            getI18nResource("caqs.dashboard.timeplot.date.legend"),
                                            null,
                                            getI18nResource("caqs.dashboard.timeplot.nbAnalysisForMonth.legend")
                    ],

                    propertiesRenderers: [
                                            function(val) {
                                                return Ext.util.Format.dateAsLong(val, javascriptDateFormat);
                                            }
                    ],
                    loadDatas: function(datas){
                        if(datas.datas.length > 0) {
                            this.timeplotPanel.setSeries(datas.datas);
                            this.timeplotBirdView.setSeries(datas.datas);
                        }
                    },
                    timeplotSeriesOptions: [
                        {
                            color:  'rgb(0, 128, 180)',
                            label:  getI18nResource("caqs.dashboard.timeplot.nbAnalysisForMonth.legend")
                        }
                    ],
                    birdviewSeriesOptions: [
                        {
                            color:  'rgb(0, 128, 180)',
                            label: ''
                        }
                    ],
                    tools:      [
                        minimizeTool
                        , maximizeTool
                    ],
                    title:      getI18nResource("caqs.dashboard.gobalAnalysis.title"),
                    column:     'caqsDashboardPortalColumn0',
                    collapsible:    true,
                    frame:      true
                });
            Ext.getCmp('caqsDashboardPortalColumn0').add(this.caqsBaselinesGlobalTimeline);
            this.doLayout();
            this.baselinesGlobalTimelineAdded = true;
        }
    },

    addConnectionsTimeline: function() {
        if(!this.connectionsStatsTimelineAdded) {
            this.caqsConnectionsStatsTimeline = new Ext.ux.CaqsDashboardTimeline({
                    id:             'caqsConnectionsStatsTimeline',
                    timeplotHeight: 150,
                    xaxis:          'caqsTimeAxe',
                    yaxis:          'true',
                    retrieveDatasUrl: requestContextPath + '/DashboardElementTimeplotConnectionStats.do',
                    manageResize:   true,
                    tooltipTemplate:    '<tpl if="seriesIndex == 0">'+
                        '<b>'+getI18nResource("caqs.dashboard.timeplot.connection.perday")+' {datapointX:dateAsLong("'+javascriptDateFormat+'")}</b> : {datapointY}'+
                        '</tpl>'+
                        '<tpl if="seriesIndex == 1">'+
                        '<b>'+getI18nResource("caqs.dashboard.timeplot.connection.simultane")+' {datapointX:dateAsLong("'+javascriptDateFormat+'")}</b> : {datapointY}'+
                        '</tpl>',
                    propertiesHeaders: [
                                            getI18nResource("caqs.dashboard.timeplot.date.legend")
                    ],

                    propertiesRenderers: [
                                            function(val) {
                                                return Ext.util.Format.dateAsLong(val, javascriptDateFormat);
                                            }
                    ],
                    loadDatas: function(datas){
                        if(datas && datas.datas.length > 0) {
                            this.timeplotPanel.setSeries(datas.datas);
                            this.timeplotBirdView.setSeries(datas.datas);
                        }
                    },
                    timeplotSeriesOptions: [
                        {
                            color:  'rgb(0, 128, 180)',
                            label:  getI18nResource("caqs.dashboard.timeplot.connection.perday.title")
                        }
                        , {
                            color: 'rgb(128, 128, 180)',
                            label:  getI18nResource("caqs.dashboard.timeplot.connection.simultane.title")
                        }
                    ],
                    birdviewSeriesOptions: [
                        {
                            color:  'rgba(0, 128, 180, 1.0)',
                            label: ''
                        }
                        , {
                            color: 'rgba(128, 128, 180, 1.0)',
                            label: ''
                        }
                    ]
                    , column:     'caqsDashboardPortalColumn1'
                    , tools:      [
                        minimizeTool
                        , maximizeTool
                    ]
                    , title:      getI18nResource("caqs.dashboard.timeplot.connection.title")
                    , collapsible:    true
                    , frame:      true
                });
            Ext.getCmp('caqsDashboardPortalColumn1').add(this.caqsConnectionsStatsTimeline);
            this.doLayout();
            this.connectionsStatsTimelineAdded = true;
        }
    },

    addDashboardGridPortlet: function() {
        if(!this.gridDashboardAdded) {
            this.caqsGridDashboard = new Ext.ux.CaqsGridDashboard({
                    globalStatisticsTimeline:   this.caqsBaselinesGlobalTimeline,
                    parent:                     this
                });
            this.gridDashboardPortlet = new Ext.ux.Portlet({
                id:         'portletCaqsGridDashboard',
                title:      getI18nResource("caqs.dashboard.projectsGrid.title"),
                //plugins:    new Ext.ux.MaximizeTool(),
                items:      this.caqsGridDashboard,
                column:     'caqsDashboardPortalColumn2',
                tools:      [
                    minimizeTool
                    , maximizeTool
                ]
                /*,stateEvents: ["move","position","drop","hide","show","collapse","expand","columnmove","columnresize","sortchange"],
                stateful:true,
                getState: function(){
                    return {
                        collapsed:  this.collapsed,
                        hidden:     this.hidden,
                        column:     this.ownerCt.id
                    };
                }*/
                /*,listeners: {
                    scope:      this,
                    resize:     function(cmp, adjWidth, adjHeight, rawWidth, rawHeight) {
                        if(this.caqsGridDashboard) {
                            this.caqsGridDashboard.setWidth(adjWidth);
                            this.caqsGridDashboard.setHeight(adjHeight);
                        }
                    }
                }*/
            })
            Ext.getCmp(this.gridDashboardPortlet.column).add(this.gridDashboardPortlet);
            this.doLayout();
            this.gridDashboardAdded = true;
        }
    },
    
    afterLoadingDomainsList: function(store, records, options) {
        this.displayDomainsCB.setValue(this.dashboardDefaultDomain);
        this.caqsGridDashboard.refresh(this.dashboardDefaultDomain);
        this.caqsBaselinesGlobalTimeline.setParams({
            domainId: this.dashboardDefaultDomain
        });
    },

    onchangeDomain: function() {
        this.caqsBaselinesGlobalTimeline.showMask();
        this.caqsGridDashboard.refresh(this.displayDomainsCB.getValue());
        this.caqsBaselinesGlobalTimeline.setParams({
            domainId: this.displayDomainsCB.getValue()
        });
    },
    
    initComponent : function(){
        this.displayDomainsCBStore = new Ext.ux.CaqsJsonStore({
            url: requestContextPath + '/RetrieveDomainsListForUser.do',
            fields: ['id', 'lib']
        });
        this.displayDomainsCBStore.addListener('load', this.afterLoadingDomainsList, this);

        this.displayDomainsCB = new Ext.form.ComboBox({
            name:       	'dashboardDomainsListCB',
            id:         	'dashboardDomainsListCB',
            fieldLabel:         getI18nResource('caqs.dashboard.selectDomain'),
            displayField:	'lib',
            valueField: 	'id',
            hiddenName: 	'id',
            editable:		false,
            store:		this.displayDomainsCBStore,
            triggerAction:	'all',
            autocomplete:	false,
            mode:      		'local',
            width:              500
        });
       this.displayDomainsCB.addListener('select', this.onchangeDomain, this);

       var domainListPanel = new Ext.form.FormPanel({
          region:      'north',
          border:       true,
          frame:        true,
          style:        'margin-left: 5px; margin-bottom: 5px;',
          height:       40,
          labelWidth:   200,
          items:        this.displayDomainsCB
       });

        var columnItems = new Array();
        var colWidth = 0.49;
        if(!this.displayConnectionTimeplot) {
            colWidth = 0.98;
        }
        if(this.displayGlobalBaselineTimeplot) {
            columnItems[columnItems.length] = {
                    id:             'caqsDashboardPortalColumn0',
                    columnWidth:    colWidth,
                    style:          'padding-left: 5px; padding-top: 5px; padding-bottom: 5px; padding-right: 5px;'
                };
        }
        if(this.displayConnectionTimeplot) {
            columnItems[columnItems.length] = {
                    id:             'caqsDashboardPortalColumn1',
                    columnWidth:    colWidth,
                    style:          'padding-left: 5px; padding-top: 5px; padding-bottom: 5px; padding-right: 5px;'
                };
        }
        columnItems[columnItems.length] = {
                    id:             'caqsDashboardPortalColumn2',
                    columnWidth:    0.98,
                    style:          'padding-left: 5px; padding-bottom: 5px; padding-right: 5px;'
                };
        this.dashboardPortal = new Ext.ux.Portal({
            region:         'center',
            scrollOffset:   25,
            autoScroll:     true,
            border:         false,
            items:          columnItems
        });
        var config = {
            items:  [
                domainListPanel
                , this.dashboardPortal
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsDashboard.superclass.initComponent.call(this);
    },

    refresh: function() {
        if(this.displayConnectionTimeplot) {
            this.addConnectionsTimeline();
            if(this.caqsConnectionsStatsTimeline.maximized) {
                minimizeTool.handler(null, null, this.caqsConnectionsStatsTimeline);
            }
        }
        if(this.displayGlobalBaselineTimeplot) {
            this.addBaselinesGlobalTimeline();
            if(this.caqsBaselinesGlobalTimeline.maximized) {
                minimizeTool.handler(null, null, this.caqsBaselinesGlobalTimeline);
            }
        }
        this.addDashboardGridPortlet();
        if(this.gridDashboardPortlet.maximized) {
            minimizeTool.handler(null, null, this.gridDashboardPortlet);
        }
        this.caqsBaselinesGlobalTimeline.showMask();
        this.displayDomainsCBStore.load();
        if(this.displayConnectionTimeplot) {
            this.caqsConnectionsStatsTimeline.showMask();
            this.caqsConnectionsStatsTimeline.retrieveDatas();
        }
        Caqs.Portal.getCaqsPortal().setCurrentScreenId('dashboard');
    }
});
