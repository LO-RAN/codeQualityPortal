Ext.ux.CaqsActionsPlanEvolutionsGraph = function(config) {
    // call parent constructor
    Ext.ux.CaqsActionsPlanEvolutionsGraph.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsActionsPlanEvolutionsGraph constructor


Ext.extend(Ext.ux.CaqsActionsPlanEvolutionsGraph, Ext.Panel, {
    layout:                     'anchor',
    autoScroll:                 true,
    border:                     false,
    timeplotPanel:              undefined,
    timeplotHeight:             300,
    idElt:                      undefined,
    id:                         undefined,
    title:                      undefined,
    retrieveDatasUrl:           undefined,
    retrieveParams:             undefined,
    propertiesRenderers:        undefined,
    propertiesHeaders:          undefined,
    internalMask:               undefined,

    initComponent : function(){
        Ext.ux.CaqsActionsPlanEvolutionsGraph.superclass.initComponent.call(this);
        this.timeplotPanel = new Ext.ux.Flot({
            anchor:         '98%',
            id:             this.id+'timeplotpanel',
            cls:            'x-panel-body',
            height:         this.timeplotHeight,
            border:         false,
            tooltip:        '<b> {caqsCustom1} </b> : {caqsCustom5}',
            selection:      {
                mode: null
            },
            legend: {
                container: $("#actionsPlanEvolutionsGraphLegendContainer")
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
                        barWidth:1.0
                    }
                }
            },
            grid: {
                backgroundColor: { colors: ["#fff", "#eee"] }
            },
            showProperty: function() {
                var convertedStore = new Array();
                //6 series et x criteres
                //pour chaque critere
                var nbCriterions = this.loadedSeries.xaxisTicks.length;
                var datas = this.loadedSeries.datas;
                for(i=0; i<nbCriterions; i++) {
                    convertedStore[i] = new Array();
                    var s = convertedStore[i];
                    var x = this.loadedSeries.xaxisTicks[i];
                    s[0] = x[1];
                    for(j=0; j<datas.length; j++) {
                        //pour chaque serie
                        var critDatas = datas[j].data[i];
                        switch(critDatas[5]) {
                            case 14 ://ano
                                s[1] = critDatas[1];
                                break;
                            case 9 ://corrected
                                s[2] = critDatas[1];
                                break;
                            case 10 :
                                s[3] = critDatas[1];
                                break;
                            case 11 :
                                s[4] = critDatas[1];
                                break;
                            case 12 :
                                s[5] = critDatas[1];
                                break;
                            case 13 :
                                s[6] = critDatas[1];
                                break;
                        }
                    }
                }
                
                //on converti le store pour qu'il s'affiche comme il faut
                var reader = new Ext.data.ArrayReader({}, [
                   {name: 'criterion'},
                   {name: 'ano'},
                   {name: 'corrected'},
                   {name: 'degraded'},
                   {name: 'partiallycorrected'},
                   {name: 'stables'},
                   {name: 'suppressed'}
                ]);
                
                var grid = new Ext.grid.GridPanel({
                    store: 		new Ext.data.GroupingStore({
                        reader:         reader,
                        data:           convertedStore,
                        sortInfo:       {field: 'criterion', direction: "ASC"},
                        groupField:     'criterion'
                    }),
                    viewConfig: {
                        forceFit:true
                    },
                    enableColumnHide : 	false,
                    enableColumnMove : 	false,
                    collapsible: 	false,
                    header:             false,
                    iconCls:            'icon-grid',

                    columns: [
                        {id:'criterion',header: getI18nResource("caqs.critere"), width: 100, sortable: true, dataIndex: 'criterion'},
                        {id:'ano',header: getI18nResource("caqs.evolutions.actionplans.grid.ano"), width: 100, sortable: true, dataIndex: 'ano', align:'right'},
                        {id:'corrected',header: getI18nResource("caqs.evolutions.actionplans.grid.corrected"), width: 100, sortable: true, dataIndex: 'corrected', align:'right'},
                        {id:'degraded',header: getI18nResource("caqs.evolutions.actionplans.grid.degraded"), width: 100, sortable: true, dataIndex: 'degraded', align:'right'},
                        {id:'partiallycorrected',header: getI18nResource("caqs.evolutions.actionplans.grid.partiallycorrected"), width: 100, sortable: true, dataIndex: 'partiallycorrected', align:'right'},
                        {id:'suppressed',header: getI18nResource("caqs.evolutions.actionplans.grid.suppressed"), width: 100, sortable: true, dataIndex: 'suppressed', align:'right'},
                        {id:'stables',header: getI18nResource("caqs.evolutions.actionplans.grid.stables"), width: 100, sortable: true, dataIndex: 'stables', align:'right'}
                    ]
                });
                var wnd = new Ext.Window({
                    modal:          true,
                    maximizable:    true,
                    resizable:      true,
                    layout:         'fit',
                    width:          400,
                    height:         300,
                    items:          grid
                });
                wnd.show();
            },
            listeners: {
                scope: this,
                'plotclick': function(flot, evt, pos, item) {
                    if(item!=null) {
                        var datapoints = item.series.data[item.dataIndex];
                        if(datapoints[6]) {
                            var wnd = new Ext.ux.CaqsActionsPlanEvolutionsWnd({});
                            wnd.refresh(datapoints[5], this.retrieveParams.idElt,
                                this.retrieveParams.currentIdBline,
                                this.retrieveParams.previousIdBline,
                                datapoints[4]);
                        }
                    }
                }
                , 'plothover': function(flot, evt, pos, item) {
                    if(item!=null) {
                        var datapoints = item.series.data[item.dataIndex];
                        if(datapoints[6]) {
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
                        , border: false
                        , layout: 'anchor'
                        , items:  this.timeplotPanel
                    }, {
                        width:  200
                        , border: true
                        , autoScroll: true
                        , height: this.timeplotHeight
                        , html: '<div id="actionsPlanEvolutionsGraphLegendContainer" style="height:'+(this.timeplotHeight)+'px; width:190px;"></div>'
                    }
                ]
            })
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsActionsPlanEvolutionsGraph.superclass.initComponent.apply(this, arguments);
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
        if(datas.datas.length > 0) {
            this.timeplotPanel.setOption({
                  legend:       {
                      containerId: "actionsPlanEvolutionsGraphLegendContainer",
                      container: $("#actionsPlanEvolutionsGraphLegendContainer")
                  }
            });
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

    onResize: function(adjWidth, adjHeight, rawWidth, rawHeight) {
        Ext.ux.CaqsActionsPlanEvolutionsGraph.superclass.onResize.call(this, adjWidth, adjHeight, rawWidth, rawHeight);
        this.timeplotPanel.setSize({width: adjWidth, height: adjHeight - 60 });
    }
});