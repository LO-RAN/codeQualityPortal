Ext.ux.CaqsGestionQualiteCritere = Ext.extend(Ext.Panel, {
    hideMode:           'offsets',
    border:             false,
    autoHeight:         true,
    autoWidth:          true,
    critereGrid:        undefined,
    idCrit:             undefined,
    all:                false,
    full:               undefined,
    idBline:            undefined,
    idEa:               undefined,
    metrics:            0,
    layout:             'table',
    searchPanel:        undefined,
    style:              'margin-left: 5px;',
    columnsHeaders:     undefined,
    metricsHeaders:     undefined,
    autoScroll:         true,
    layoutConfig: {
        columns: 2
    },

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsGestionQualiteCritere.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        Ext.ux.CaqsGestionQualiteCritere.superclass.initComponent.apply(this, arguments);
        this.searchPanel = new Ext.ux.CaqsAjaxSearchByNamePanel({
            id:                     'gestionQualiteTopDownCriterionSearchPanel',
            parentPanelElement:     this
        });
        this.critereGrid = new Ext.ux.CaqsGestionQualiteCritereGrid({
            colspan: 2,
            caqsParentElement: this
        });
        this.columnsHeaders = new Ext.Panel({
            id:         'gestionQualiteTopDownCriterionPanelColumnsHeader',
            border:     false,
            layout:     'column',
            style:      'margin-left: 5px',
            autoHeight: true
        });
        var config = {
                items: [
                new Ext.Panel({
                    cellStyle:  'width: 488px;',
                    border:     false,
                    width:      488,
                    layout:     'anchor',
                    items: [
                    {
                        anchor:  '100%',
                        border: false,
                        style:  'margin-bottom: 5px;',
                        html:   '<span id="gestionQualiteTopDownCriterionLib">&nbsp;</span>'
                    }
                    , this.searchPanel
                    ]
                })
                , this.columnsHeaders
                , this.critereGrid
                ]
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsGestionQualiteCritere.superclass.initComponent.apply(this, arguments);
    },

    setElementFilter: function(elementDesc) {
        this.searchPanel.setFilterValue(elementDesc);
    },

    filterDatas: function(filter, teltFilter) {
        this.critereGrid.loadDatas(this.idCrit, filter, teltFilter, this.idBline, this.idEa, false);
    },

    refreshPageLayout: function(json) {
        var elt = document.getElementById('gestionQualiteTopDownCriterionLib');
        elt.innerHTML = '<H2>'+json.critLib+'</H2>';
        if(this.metricsHeaders!=null) {
            for(var i=0; i<this.metricsHeaders.length; i++) {
                this.columnsHeaders.remove(this.metricsHeaders[i]);
            }
        }
        this.metricsHeaders = new Array();
        for(var i=0; i<json.metriques.length; i++) {
            var met = json.metriques[i];
            this.metricsHeaders[i] = new Ext.Panel({
                border: false,
                width: 55,
                style:  'padding:0px 0px 0px 0px; border-left:1px solid #EEEEEE; border-right:1px solid #D0D0D0; line-height:15px; vertical-align:middle;',
                html:   '<IMG id="verticalHeader'+
                    met.id+'" src="verticalheader?text='+
                    escape(met.lib)+'&color=335588" />'
            });
            addTooltip('verticalHeader'+met.id,
                    '<img src="'+requestContextPath
                        +'/images/info.gif" style="vertical-align:middle">&nbsp;&nbsp;'+met.lib,
                    met.desc, 0, 500, 500);
            this.columnsHeaders.add(this.metricsHeaders[i]);
        }
        this.metricsHeaders[i] = new Ext.Panel({
            border: false,
            width: 55,
            style:  'padding:0px 0px 0px 0px;',
            html:   '<IMG id="verticalHeaderCrit'+
                json.critId+'" src="verticalheader?text='+
                escape(json.escapedCritLib)+'" />'
        });
        addTooltip('verticalHeaderCrit'+json.critId,
                '<img src="'+requestContextPath
                    +'/images/info.gif" style="vertical-align:middle">&nbsp;&nbsp;'+json.escapedCritLib,
                json.escapedCritDesc, 0, 500, 500);
        this.columnsHeaders.add(this.metricsHeaders[i]);
        this.columnsHeaders.setWidth(55*this.metricsHeaders.length+40);
        this.doLayout();
        this.critereGrid.reconfigureGrid(json);
    },

    load: function(idCrit) {
        this.idCrit = idCrit;
        Ext.Ajax.request({
            url: requestContextPath+'/LoadCritereDatas.do',
            scope: this,
            params: {
                id_crit: idCrit
            },
            success: function(response) {
                if(response!=undefined && response.responseText!=undefined) {
                    var json = Ext.util.JSON.decode(response.responseText);
                    if(json!=undefined) {
                        this.idEa = json.idEa;
                        this.idBline = json.idBline;
                        this.refreshPageLayout(json);
                        this.searchPanel.refresh();
                    }
                }
            }
        });
        Caqs.Portal.setCurrentScreen('topdown_synthesis_criterion');
    }
});

