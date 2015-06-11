Ext.ux.CaqsVolumetryEvolutionsGraph = function(config) {
    // call parent constructor
    Ext.ux.CaqsVolumetryEvolutionsGraph.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsEvolutionsGraph constructor


Ext.extend(Ext.ux.CaqsVolumetryEvolutionsGraph, Ext.Panel, {
    layout:                     'fit',
    border:                     false,
    timeplotPanel:              undefined,
    idElt:                      undefined,
    id:                         undefined,
    title:                      undefined,
    xaxis:                      undefined,
    retrieveDatasUrl:           undefined,
    retrieveParams:             undefined,

    setXAxis: function(xaxis) {
        this.timeplotPanel.xaxis = xaxis;
        this.xaxis = xaxis;
    },

    initComponent : function(){
        Ext.ux.CaqsVolumetryEvolutionsGraph.superclass.initComponent.call(this);
        this.timeplotPanel = new Ext.ux.Flot({
            anchor:         '98% 95%',
            id:             this.id+'timeplotpanel',
            cls:            'x-panel-body',
            border:         false,
            tooltip:        '<b> {label} ({caqsCustom3}) </b> : {caqsCustom2}',
            selection:      {
                mode: null
            },
            caqsTimeline:   this,
            y2axis: { tickFormatter: function (v, axis) { return v.toFixed(axis.tickDecimals) +"%" }},
            height: 500,
            //y2axis:         'true',
            /*options: {
                series: {
                    selectable: true,
                    stack: true,
                    bars:{
                        show:   true,
                        align:  'center',
                        fill:   0.8,
                        barWidth: 0.4
                    }
                }
            },*/
            grid: {
                backgroundColor: { colors: ["#fff", "#eee"] }
            },
            legend: {
                container: $("#volumetryEvolutionsGraphLegendContainer")
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
                    , null
                    , function(val, metadata, record, rowIndex, colIndex, store) {
                        return this.grid.parentFlot.loadedSeries.datas[0].data[rowIndex][4];
                    }
                    , function(val, metadata, record, rowIndex, colIndex, store) {
                        var retour = '-';
                        if(val > 0) {
                            retour = Ext.util.Format.roundToDecimals(val, 100);
                        }
                        return retour;
                    }
                    , function(val, metadata, record, rowIndex, colIndex, store) {
                        var retour = '-';
                        if(val > 0) {
                            retour = Ext.util.Format.roundToDecimals(val, 100);
                        }
                        return retour;
                    }
                    , function(val, metadata, record, rowIndex, colIndex, store) {
                        return this.grid.parentFlot.loadedSeries.datas[3].data[rowIndex][4];
                    }
                ]
                );
                grid.setHeaders([
                    getI18nResource("caqs.baseline")
                ]);
                wnd.show();
            },
            listeners: {
                scope: this,
                'plotclick': function(flot, evt, pos, item) {
                    if(item!=null) {
                        var datapoints = item.series.data[item.dataIndex];
                        if(datapoints[4]) {
                            var wnd = new Ext.ux.CaqsElementsEvolutionsWindow({
                                title:      item.series.label
                            });
                            wnd.refresh(this.retrieveParams.idElt,
                                this.retrieveParams.currentIdBline,
                                this.retrieveParams.previousIdBline,
                                datapoints[3]);
                        }
                    }
                }
            }
        });

        var config = {
            items:      new Ext.Panel({
                border: false
                , layout: 'column'
                , items: [
                    {
                        columnWidth: .98
                        , layout: 'anchor'
                        , border: false
                        , items: this.timeplotPanel
                    }, {
                        width:  200
                        , border: false
                        , autoScroll: true
                        , html: '<div id="volumetryEvolutionsGraphLegendContainer" style="width:190px;"></div>'
                    }
                ]
            })
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsVolumetryEvolutionsGraph.superclass.initComponent.apply(this, arguments);
    },

    setParams: function(p) {
        this.retrieveParams = p;
    },

    retrieveDatas: function() {
        var params = this.retrieveParams;
        params.target = this.target;
        Ext.Ajax.request({
            url:	this.retrieveDatasUrl,
            params:     this.retrieveParams,
            success:	this.onDataLoaded,
            scope:      this,
            timeout:    60000
        });
    },

    loadDatas: function(datas){
        this.timeplotPanel.setOption({
              legend:       {
                  containerId: "volumetryEvolutionsGraphLegendContainer",
                  container: $("#volumetryEvolutionsGraphLegendContainer")
              }
        });
        if(datas.datas.length > 0) {
            this.timeplotPanel.setSeries(datas);
        }
    },

    onDataLoaded: function(response) {
        if(response.responseText!='' && response.responseText!='[]') {
            var json = Ext.util.JSON.decode(response.responseText);
            if(this.timeplotPanel) {
                this.loadDatas(json);
            }
        }
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
    },

    onHide: function(wnd) {
        this.timeplotPanel.zoomRatio(1.0);
    }
});