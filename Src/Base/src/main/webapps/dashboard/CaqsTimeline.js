
Ext.ux.CaqsDashboardTimeline = function(config) {
    // call parent constructor
    Ext.ux.CaqsDashboardTimeline.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsDashboardTimeline, Ext.Panel, {
    baselineAnalysisStore:      undefined,
    timeplotPanel:              undefined,
    timeplotBirdView:           undefined,
    timeplotHeight:             300,
    timeplotBirdViewHeight:     50,
    idElt:                      undefined,
    id:                         undefined,
    title:                      undefined,
    xaxis:                      undefined,
    x2axis:                     undefined,
    yaxis:                      'true',
    y2axis:                     undefined,
    retrieveDatasUrl:           undefined,
    retrieveParams:             undefined,
    layout:                     null,
    tooltipTemplate:            undefined,
    propertiesRenderers:        undefined,
    propertiesHeaders:          undefined,
    internalMask:               undefined,
    lastLoadedJSON:             undefined,
    timeplotSeriesOptions:      undefined,
    birdviewSeriesOptions:      undefined,
    legend:                     undefined,

    showMask: function() {
        if(this.internalMask==null) {
            this.internalMask = new Ext.LoadMask(this.getEl());
        }
        this.internalMask.show();
    },

    hideMask: function() {
        if(this.internalMask!=null) {
            this.internalMask.hide();
        }
    },

    createTimeplotBirdview: function() {
        this.timeplotBirdView = new Ext.ux.Flot({
          id:           this.id+'timeplotbirdview',
          cls:          'x-panel-body',
          border:       false,
          height:       this.timeplotBirdViewHeight,
          lines:        {show: true, lineWidth: 1, steps : false},
          shadowSize:   0,
          xaxis:        this.xaxis,
          yaxis:        {ticks: [], min: 0},
          selection:    {mode: "x"},
          tooltip:      false,
          contextMenu:  null,
          options: {
                points: {
                    show: false
                }
          },
          baseSeries:   {selectable: false},
          listeners:    {
                plotselected: function(flot, event, ranges, item) {
                    this.timeplotPanel.zoom(ranges);
                },

                plotunselected: function(event, pos, item) {
                    this.timeplotPanel.resetPlotZoom();
                },

                scope: this
          },
          seriesOptions: this.birdviewSeriesOptions
        });
    },

    createTimeplotPanel: function() {
        this.timeplotPanel = new Ext.ux.Flot({
            id:             this.id+'timeplotpanel',
            cls:            'x-panel-body',
            border:         false,
            height:         this.timeplotHeight,
            xaxis:          this.xaxis,
            x2axis:         this.x2axis,
            y2axis:         this.y2axis,
            yaxis:          this.yaxis,
            tooltip:        this.tooltipTemplate,
            selection:      {mode: null},
            shadowSize:     0,
            caqsTimeline:   this,
            legend:         this.legend,
            grid: {
                backgroundColor: { colors: ["#fff", "#eee"] }
            },
            showProperty: function() {
                var wnd = new Ext.Window({
                    modal:          true,
                    maximizable:    true,
                    resizable:      true,
                    layout:         'fit',
                    width:          400,
                    height:         300,
                    items: [{
                        xtype:      'caqsflotpropertygrid',
                        flot:       this,
                        layout:     'fit'
                    }]
                });
                var grid = wnd.findByType('caqsflotpropertygrid')[0];
                this.bindGrid(grid);
                if(this.caqsTimeline.propertiesRenderers) {
                    grid.setRenderers(this.caqsTimeline.propertiesRenderers);
                }
                if(this.caqsTimeline.propertiesHeaders) {
                    grid.setHeaders(this.caqsTimeline.propertiesHeaders);
                }
                wnd.show();
            },
            seriesOptions: this.timeplotSeriesOptions
        });
    },

    initComponent : function(){
        Ext.ux.CaqsDashboardTimeline.superclass.initComponent.call(this);
        this.createTimeplotPanel();
        this.createTimeplotBirdview();
        var config = {
            items:      [
                this.timeplotPanel,
                this.timeplotBirdView
            ]
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsDashboardTimeline.superclass.initComponent.apply(this, arguments);
    },

    resetAxes: function() {
        if(this.xaxis) {
            this.timeplotPanel.xaxis = this.xaxis;
            this.timeplotBirdView.xaxis = this.xaxis;
        }
        if(this.y2axis) {
            this.timeplotPanel.y2axis = this.y2axis;
        }
        if(this.x2axis) {
            this.timeplotPanel.x2axis = this.x2axis;
        }
        if(this.yaxis) {
            this.timeplotPanel.yaxis = this.yaxis;
        }
    },

    setParams: function(p) {
        this.retrieveParams = p;
    },

    retrieveDatas: function() {
        Ext.Ajax.request({
            url:	this.retrieveDatasUrl,
            params:     this.retrieveParams,
            success:	this.onDataLoaded,
            scope:      this
        });
    },

    loadDatas: function(datas){
        //fonction devant etre surchargee par l'instance
    },

    onDataLoaded: function(response) {
        if(response.responseText!='' && response.responseText!='[]') {
            var json = Ext.util.JSON.decode(response.responseText);
            if(this.timeplotPanel) {
                this.resetAxes();
                this.loadDatas(json);
                this.lastLoadedJSON = json;
            }
        }
        this.hideMask();
    },

    reloadDatas: function() {
        if(this.lastLoadedJSON) {
            this.loadDatas(this.lastLoadedJSON);
        }
    },

    onHide: function(wnd) {
        this.timeplotPanel.zoomRatio(1.0);
    },

    onResize: function(adjWidth, adjHeight, rawWidth, rawHeight) {
        Ext.ux.CaqsDashboardTimeline.superclass.onResize.call(this, adjWidth, adjHeight, rawWidth, rawHeight);
        this.timeplotPanel.setSize({width: adjWidth, height: adjHeight - this.timeplotBirdViewHeight - 30 });
        this.timeplotBirdView.setSize({width: adjWidth, height: this.timeplotBirdViewHeight});
    }
});