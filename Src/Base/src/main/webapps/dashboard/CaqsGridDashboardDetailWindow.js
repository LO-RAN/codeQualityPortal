
Ext.ux.CaqsGridDashboardDetailsWindow = function(config) {
    // call parent constructor
    Ext.ux.CaqsGridDashboardDetailsWindow.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsGridDashboardDetailsWindow, Ext.Window, {
    width:                      600,
    height:                     330,
    layout:                     'fit',
    modal:                      true,
    maximizable:                true,
    resizable:                  true,
    minimizable:                false,
    plain:                      true,
    shadow:                     false,
    timeline:                   undefined,

    initComponent : function(){
        this.timeline = new Ext.ux.CaqsDashboardTimeline({
            id:                 'caqsProjectDetailWindowTimeline',
            yaxis:              'caqsAnalysisScore',
            y2axis:             'true',
            xaxis:              'caqsTimeAxe',
            retrieveDatasUrl:   requestContextPath + '/DashboardElementTimeplotNumberOfAnalysis.do',
            manageResize:       true,
            tooltipTemplate:    '<tpl if="seriesIndex == 1">'+
                '<b>'+getI18nResource("caqs.dashboard.timeplot.nbAnalysisForMonth.popup")+' {datapointX:dateAsLong("'+javascriptDateFormatMonthYear+'")}</b> : {datapointY}'+
                '</tpl>'+
                '<tpl if="seriesIndex == 0">'+
                '<tpl if="caqsCustom1 == 1">'+
                '<b>'+getI18nResource("caqs.dashboard.timeplot.analysisEvent.popup")+' {datapointX:dateAsLong("'+javascriptDateFormat+'")}</b> : {datapointY:roundToDecimals(100)}'+
                '</tpl>'+
                '<tpl if="caqsCustom1 &gt; 1">'+
                '<b>'+getI18nResource("caqs.dashboard.timeplot.nbAnalysisForDay.numberOfAnalysis")+'&nbsp;{datapointX:dateAsLong("'+javascriptDateFormat+'")}:</b>&nbsp;{caqsCustom1}<BR />'+
                '<b>'+getI18nResource("caqs.dashboard.timeplot.nbAnalysisForDay.lastScore")+'</b>&nbsp;{datapointY:roundToDecimals(100)}<BR />'+
                '<b>'+getI18nResource("caqs.dashboard.timeplot.nbAnalysisForDay.worstScore")+'</b>&nbsp;{caqsCustom2:roundToDecimals(100)}<BR />'+
                '<b>'+getI18nResource("caqs.dashboard.timeplot.nbAnalysisForDay.bestScore")+'</b>&nbsp;{caqsCustom3:roundToDecimals(100)}'+
                '</tpl>'+
                '</tpl>',
            propertiesHeaders: [
                                    getI18nResource("caqs.dashboard.timeplot.date.legend")
            ],

            propertiesRenderers: [
                                    function(val) {
                                        return Ext.util.Format.dateAsLong(val, javascriptDateFormat);
                                    },
                                    null,
                                    function(val) {
                                        var retour = '-';
                                        if(val > 0) {
                                            retour = Ext.util.Format.roundToDecimals(val, 100);
                                        }
                                        return retour;
                                    }
                                    
            ],

            loadDatas: function(datas){
                this.timeplotPanel.setSeries(datas.datas);
                this.timeplotBirdView.setSeries([datas.datas[1]]);
            },
            timeplotSeriesOptions: [
                {
                    color:  'rgba(0, 0, 0, 1.0)',
                    label:  getI18nResource("caqs.dashboard.timeplot.analysis.legend"),
                    yaxis:  2,
                    bars: {
                        show :      true,
                        barWidth:   86400000,//largeur : un jour
                        fill:       0.8,
                        align:      'center',
                        fillColorFunctionUsingDatas:  function(x, y, ctx) {
                            var retour = '';
                            if(y < 2.0) {
                                retour = 'rgb(255, 0, 0)';
                            } else if (y < 3.0){
                                retour  = 'rgb(252, 171, 3)';
                            } else if (y < 4.0){
                                retour  = 'rgb(0, 128, 0)';
                            } else {
                                retour  = 'rgb(0, 255, 0)';
                            }
                            ctx.strokeStyle = retour;
                            return retour;
                        }
                    }
                }
                , {
                    color:  'rgb(0, 128, 180)',
                    label:  getI18nResource("caqs.dashboard.timeplot.nbAnalysisForMonth.legend")
                }
            ],
            birdviewSeriesOptions: [
                {
                    color:  'rgb(0, 128, 180)',
                    label: ''
                }
            ]

        });

        var config = {
            items:      [
                this.timeline
            ]
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsGridDashboardDetailsWindow.superclass.initComponent.apply(this, arguments);
        this.on('beforeclose', this.onBeforeClose, this);
//        this.on('show', this.refresh, this);
    },

    refresh: function(idElt) {
        if(this.timeline) {
            this.timeline.setParams({
                    idElt:  idElt
            });
            this.timeline.retrieveDatas();
            this.show();
        }
    },


    onBeforeClose: function(wnd) {
        if(this.timeline) {
            this.timeline.onHide();
        }
        wnd.hide();
        return false;
    }
});