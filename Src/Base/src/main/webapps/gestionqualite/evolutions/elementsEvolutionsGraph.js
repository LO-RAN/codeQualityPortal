Ext.ux.CaqsElementsEvolutionsGraph = function(config) {
    // call parent constructor
    Ext.ux.CaqsElementsEvolutionsGraph.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsEvolutionsGraph constructor


Ext.extend(Ext.ux.CaqsElementsEvolutionsGraph, Ext.Panel, {
    layout:                     'fit',
    border:                     false,
    timeplotPanel:              undefined,
    idElt:                      undefined,
    id:                         undefined,
    title:                      undefined,
    xaxis:                      undefined,
    x2axis:                     undefined,
    yaxis:                      'true',
    y2axis:                     undefined,
    retrieveDatasUrl:           undefined,
    retrieveParams:             undefined,
    linear:                     false,

    setLinear: function(lin) {
        this.linear = lin;
        this.timeplotPanel.setYAxisTransformFunctions(this.linear);
    },

    initComponent : function(){
        Ext.ux.CaqsElementsEvolutionsGraph.superclass.initComponent.call(this);
        var transformFn = (this.linear) ? function (v) { return v; } : function (v) { return (v>0)?Math.log(v):0; };
        var inverseTransformFn = (this.linear) ? function (v) { return v; } : function (v) { return (v>0)?Math.exp(v):0; };
        this.timeplotPanel = new Ext.ux.Flot({
            anchor:         '98% 95%',
            id:             this.id+'timeplotpanel',
            cls:            'x-panel-body',
            border:         false,
            height:         500,
            tooltip:        '<b> {label} </b> : {caqsCustom3}',
            selection:      {
                mode: null
            },
            caqsTimeline:   this,
            options: {
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
                , yaxis: {
                    transform:transformFn
                    , inverseTransform: inverseTransformFn
                    , min: 0
                }
            },
            grid: {
                backgroundColor: { colors: ["#fff", "#eee"] }
            },
            legend: {
                container: $("#elementsEvolutionsGraphLegendContainer")
            },
            setYAxisTransformFunctions: function(linear) {
                this.options.yaxis.transform = (linear) ? function (v) { return v; } : function (v) { return (v>0)?Math.log(v):0; };
                this.options.yaxis.inverseTransform = (linear) ? function (v) { return v; } : function (v) { return (v>0)?Math.exp(v):0; };
                this.plot();
            },
            showProperty: function() {
                var s = this.loadedSeries.datas;
                var items = new Array();
                for(i=0; i<s.length; i++) {
                    var item = s[i];
                    items[items.length] = {
                        xtype :     'statictextfield',
                        fieldLabel: item.label,
                        value:      item.data[0][5]
                    }
                }
                var wnd = new Ext.Window({
                    modal:          true,
                    maximizable:    true,
                    resizable:      true,
                    width:          400,
                    height:         300,
                    layout:         'fit',
                    items: [{
                        layout:     'form',
                        autoHeight: true,
                        autoWidth:  true,
                        labelWidth: 220,
                        border:     false,
                        items:      items
                    }]
                });
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
                , 'plothover': function(flot, evt, pos, item) {
                    if(item!=null) {
                        var datapoints = item.series.data[item.dataIndex];
                        if(datapoints[4]) {
                            this.timeplotPanel.flot.getOverlay().style.cursor = 'pointer';
                        }
                        else {
                            this.timeplotPanel.flot.getOverlay().style.cursor = 'default';
                        }
                    } else {
                        this.timeplotPanel.flot.getOverlay().style.cursor = 'default';
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
                        , items:  this.timeplotPanel
                    }, {
                        width:  200
                        , border: false
                        , autoScroll: true
                        , html: '<div id="elementsEvolutionsGraphLegendContainer" style="width:190px;"></div>'
                    }
                ]
            })
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsElementsEvolutionsGraph.superclass.initComponent.apply(this, arguments);
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
                  containerId: "elementsEvolutionsGraphLegendContainer",
                  container: $("#elementsEvolutionsGraphLegendContainer")
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