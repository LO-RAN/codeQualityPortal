Ext.ux.CaqsEvolutionsGraph = function(config) {
    // call parent constructor
    Ext.ux.CaqsEvolutionsGraph.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsEvolutionsGraph constructor


Ext.extend(Ext.ux.CaqsEvolutionsGraph, Ext.Panel, {
    layout:                     'anchor',
    autoScroll:                 true,
    timeplotPanel:              undefined,
    timeplotBirdView:           undefined,
    timeplotHeight:             300,
    idElt:                      undefined,
    id:                         undefined,
    title:                      undefined,
    retrieveParams:             undefined,
    tooltipTemplate:            undefined,
    propertiesRenderers:        undefined,
    propertiesHeaders:          undefined,
    internalMask:               undefined,
    target:                     'GOAL',
    loadedDatas:                undefined,

    initComponent : function(){
        Ext.ux.CaqsEvolutionsGraph.superclass.initComponent.call(this);
        this.timeplotPanel = new Ext.ux.Flot({
            anchor:         '98%',
            id:             this.id+'timeplotpanel',
            cls:            'x-panel-body',
            border:         false,
            height:         this.timeplotHeight,
            tooltip:        '<b>{caqsCustom2} '+
                getI18nResource('caqs.evolution.goalsevolution.tooltip.date')+
                ' {caqsCustom1:dateAsLong("'+javascriptDateFormatWithHour+
                '")}</b> : {label} = {datapointY:roundToDecimals(100)}',
            selection:      {
                mode: null
            },
            shadowSize:     0,
            caqsTimeline:   this,
            yaxis: {
                min: 1.0,
                max: 4.5,
                color: 'rgba(0,0,0,1.0)',
                tickFormatter: function (v, axis) {
                    return v.toFixed(axis.tickDecimals);
                },
                ticks: [0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0]
            },
            grid: {
                backgroundColor: {colors: ["#fff", "#eee"]}
            },
            legend: {
                container: $("#goalsEvolutionsGraphLegendContainer")
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
                grid.parentFlot = this;
                this.bindGrid(grid);
                grid.setRenderers([
                    function(val, metadata, record, rowIndex, colIndex, store) {
                        return this.grid.parentFlot.loadedSeries.xaxisTicks[record.data.xaxis][1];
                    }
                ],
                    function(val, metadata, record, rowIndex, colIndex, store) {
                        var retour = '-';
                        if(val > 0) {
                            retour = Ext.util.Format.roundToDecimals(val, 100);
                        }
                        return retour;
                    }
                );
                grid.setHeaders([
                    getI18nResource("caqs.baseline")
                ]);
                wnd.show();
            }
        });

        this.timeplotBirdView = new Ext.ux.Flot({
          id:           this.id+'timeplotbirdview',
          cls:          'x-panel-body',
          anchor:       '98%',
          border:       false,
          height:       50,
          lines:        {show: true, lineWidth: 1, steps : false},
          shadowSize:   0,
          xaxis:        {labelHeight: 0},
          yaxis:        {ticks: [], min: 1.0, max: 4.5},
          selection:    {mode: "x"},
          tooltip:      false,
          contextMenu:  null,
          options: {
                series: {
                    points: {
                        show: false
                    }
                }
          },
          baseSeries:   {selectable: false},
          legend:       {
              show:     false
          },
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

        var config = {
            items:      [
            new Ext.form.FieldSet({
                anchor:     '98%',
                collapsible:    true,
                collapsed:  true,
                title:      getI18nResource('caqs.evolution.graphConfiguration'),
                autoHeight: true,
                layout:     'table',
                layoutConfig: {
                    columns: 3
                },
                items: [
                {
                    xtype:      'ux-radiogroup',
                    fieldLabel: getI18nResource('caqs.evolution.displayElements'),
                    name:       'caqsEvolutionGraphDisplayElementRadioGroup',
                    horizontal: true,
                    radios:[{
                        value:1,
                        boxLabel:   getI18nResource('caqs.objectifs'),
                        checked:    true,
                        listeners:{
                            'check':function(r,c){
                                if(c) {
                                    this.target = 'GOALS';
                                    this.retrieveDatas();
                                }
                            },
                            scope: this
                        }
                    }, {
                        value:2,
                        boxLabel:   getI18nResource('caqs.criteres'),
                        listeners:{
                            'check':function(r,c){
                                if(c) {
                                    this.target = 'CRIT';
                                    this.retrieveDatas();
                                }
                            },
                            scope: this
                        }
                    }]
                }
                , new Ext.Button({
                    text:   getI18nResource('caqs.evolution.selectAllSeriesButton'),
                    style:  'margin-left: 10px;',
                    handler: function() {
                        this.timeplotPanel.setHidden(this.loadedDatas, false);
                    },
                    scope:  this
                })
                , new Ext.Button({
                    text:   getI18nResource('caqs.evolution.unselectAllSeriesButton'),
                    style:  'margin-left: 10px;',
                    handler: function() {
                        this.timeplotPanel.setHidden(this.loadedDatas, true);
                    },
                    scope:  this
                })
                ]
            })
            , new Ext.Panel({
                border: false
                , layout: 'column'
                , items: [
                    {
                        columnWidth: .98
                        , layout: 'anchor'
                        , border: false
                        , items: [
                            this.timeplotPanel
                            , this.timeplotBirdView
                        ]
                    }, {
                        width:  200
                        , border: true
                        , autoScroll: true
                        , height: this.timeplotHeight + 50
                        , html: '<div id="goalsEvolutionsGraphLegendContainer" style="height:'+(this.timeplotHeight + 50)+'px; width:190px;"></div>'
                    }
                ]
            })
            ]
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsEvolutionsGraph.superclass.initComponent.apply(this, arguments);
    },

    onResize: function(adjWidth, adjHeight, rawWidth, rawHeight) {
        Ext.ux.CaqsEvolutionsGraph.superclass.onResize.call(this, adjWidth, adjHeight, rawWidth, rawHeight);
        this.timeplotPanel.setSize({
            width: adjWidth - 160,
            height: adjHeight - 30
        });
        this.timeplotBirdView.setSize({
            width: adjWidth - 160,
            height: 50
        });
    },

    setParams: function(p) {
        this.retrieveParams = p;
    },

    retrieveDatas: function() {
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        var params = this.retrieveParams;
        params.target = this.target;
        Ext.Ajax.request({
            url:	requestContextPath + '/RetrieveEvolutionGraphDatas.do',
            params:     this.retrieveParams,
            success:	this.onDataLoaded,
            scope:      this
        });
    },

    loadDatas: function(datas){
        if(datas.datas.length > 0) {
            /*this.timeplotPanel.legend = {
                container: Ext.get("goalsEvolutionsGraphLegendContainer").dom
            };*/
            this.timeplotPanel.setOption({
                  legend:       {
                      containerId: "goalsEvolutionsGraphLegendContainer",
                      container: $("#goalsEvolutionsGraphLegendContainer")
                  }
            });
            this.timeplotPanel.setSeries(datas);
            this.timeplotBirdView.setSeries(datas);
        }
    },

    onDataLoaded: function(response) {
        if(response.responseText!='' && response.responseText!='[]') {
            var json = Ext.util.JSON.decode(response.responseText);
            if(this.timeplotPanel) {
                this.loadDatas(json);
                this.loadedDatas = json.datas;
            }
        }
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
    },

    onHide: function(wnd) {
        this.timeplotPanel.zoomRatio(1.0);
    }

});