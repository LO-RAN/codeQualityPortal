Ext.ux.CaqsGridDashboard = function(config) {
    // call parent constructor
    Ext.ux.CaqsGridDashboard.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsPortal constructor


Ext.extend(Ext.ux.CaqsGridDashboard, Ext.grid.GridPanel, {
    store:              undefined,
    reader:             undefined,
    detailsWnd:         undefined,
    today:              undefined,
    //autoHeight:         true,
    height:             250,
    globalStatisticsTimeline:   undefined,
    parent:             undefined,
    
    renderMeteo: function(v, p, record) {
        var retour = ' - ';
        if(record.data.meteoImg != '') {
            p.attr = 'ext:qtip="'+record.data.meteoTooltip+'"';
            addTooltip('meteoImg'+record.data.meteoImg, '', record.data.meteoTooltip);
            retour = '<img id="meteoImg'+record.data.meteoImg +
            '" src="'+requestContextPath+'/images/weather-'+record.data.meteoImg+'-mini.gif" />';
        }
        return retour;
    },

    renderGoalsAverage: function(v, p, record) {
        var retour = record.data.formattedGoalsAvg;
        if(record.data.goalsAvg < 3.0) {
            retour = '<span style="color: red; font-weight: bold;">'+retour+"</span>";
        }
        return retour;
    },

    groupRendererGoalsAverage: function(v, p, record) {
        var retour = '';
        if(record.data.goalsAvg < 2.0) {
            retour = getI18nResource("caqs.dashboard.goalsAvg.reject");
        } else if(record.data.goalsAvg < 3.0) {
            retour = getI18nResource("caqs.dashboard.goalsAvg.reserve");
        } else if(record.data.goalsAvg < 4.0) {
            retour = getI18nResource("caqs.dashboard.goalsAvg.accepte");
        } else {
            retour = getI18nResource("caqs.dashboard.goalsAvg.quatre");
        }
        return retour;
    },

    groupRendererLOC: function(v, p, record) {
        var retour = '';
        if(record.data.nbLOCWithoutFormat < 1000) {
            retour = getI18nResource("caqs.dashboard.nbloc.1000");
        } else if(record.data.nbLOCWithoutFormat < 5000) {
            retour = getI18nResource("caqs.dashboard.nbloc.5000");
        } else if(record.data.nbLOCWithoutFormat < 10000) {
            retour = getI18nResource("caqs.dashboard.nbloc.10000");
        } else if(record.data.nbLOCWithoutFormat < 50000) {
            retour = getI18nResource("caqs.dashboard.nbloc.50000");
        } else if(record.data.nbLOCWithoutFormat < 100000) {
            retour = getI18nResource("caqs.dashboard.nbloc.100000");
        } else if(record.data.nbLOCWithoutFormat < 500000) {
            retour = getI18nResource("caqs.dashboard.nbloc.500000");
        } else {
            retour = getI18nResource("caqs.dashboard.nbloc.500001");
        }
        return retour;
    },

    groupRendererFile: function(v, p, record) {
        var retour = '';
        if(record.data.nbFileElementsWithoutFormat < 50) {
            retour = getI18nResource("caqs.dashboard.nbfile.50");
        } else if(record.data.nbFileElementsWithoutFormat < 100) {
            retour = getI18nResource("caqs.dashboard.nbfile.100");
        } else if(record.data.nbFileElementsWithoutFormat < 250) {
            retour = getI18nResource("caqs.dashboard.nbfile.250");
        } else if(record.data.nbFileElementsWithoutFormat < 500) {
            retour = getI18nResource("caqs.dashboard.nbfile.500");
        } else if(record.data.nbFileElementsWithoutFormat < 1000) {
            retour = getI18nResource("caqs.dashboard.nbfile.1000");
        } else {
            retour = getI18nResource("caqs.dashboard.nbfile.more");
        }
        return retour;
    },

    renderBaselineDMaj: function(v, p, record) {
        var retour = record.data.dmaj;
        var dmajDate = new Date.parseDate(record.data.dmaj, record.data.dmajJSParser);
        var diff = dmajDate.getElapsed(new Date());
        var nbDays = diff / 86400000;
        if(nbDays > record.data.nbLastDaysWarning) {
            p.attr = 'style="color: red;" ext:qtip="'+record.data.nbLastDaysWarningPopup+'"';
        }
        return retour;
    },

    groupRendererDMaj: function(v, p, record) {
        var retour = '';
        var dmajDate = new Date.parseDate(record.data.dmaj, record.data.dmajJSParser);
        var today = new Date();

        if(isDateEqual(today, dmajDate)) {
            //aujourd'hui
            retour = getI18nResource("caqs.dashboard.dmaj.today");
        } else {
            var yesterday = today.add(Date.DAY, -1);
            if(isDateEqual(yesterday, dmajDate)) {
                //hier
                retour = getI18nResource("caqs.dashboard.dmaj.yesterday");
            } else {
                if(isDateInSameWeek(today, dmajDate)) {
                    retour = getI18nResource("caqs.dashboard.dmaj.week");
                } else {
                    var lastWeek = today.add(Date.DAY, -7);
                    if(isDateInSameWeek(lastWeek, dmajDate)) {
                        //hier
                        retour = getI18nResource("caqs.dashboard.dmaj.lastweek");
                    } else {
                        var d1 = (new Date()).add(Date.DAY, -14);
                        var trancheSup = d1.add(Date.DAY, (7-d1.getDay()));
                        var d2 = (new Date()).add(Date.DAY, -28);
                        var trancheInf = d2.add(Date.DAY, -1*d2.getDay());
                        if(dmajDate.between(trancheInf, trancheSup)) {
                            retour = getI18nResource("caqs.dashboard.dmaj.twoweeks");
                        } else {
                            retour = getI18nResource("caqs.dashboard.dmaj.older");
                        }
                    }
                }
            }
        }
        return retour;
    },

    renderName: function(v, p, record) {
        var retour = '<span style="text-decoration: underline; cursor: pointer;" ' +
        ' onClick="goToSelectedFavorite(\'' + record.data.eltId + '\', \''+record.data.prjId+'\')" >' +
        record.data.lib + '</span>';
        return retour;
    },

    renderTendanceLabel: function(v, p, record) {
        p.attr = 'ext:qtip="'+record.data.tendancePopup+'"';
        addTooltip('trend'+record.data.tendanceLabel, '', record.data.tendancePopup);
        var retour = '<IMG id="trend'+record.data.tendanceLabel+
        '" src="'+requestContextPath+'/images/note_'+record.data.tendanceLabel+'.gif" />';
        return retour;
    },

    initComponent : function(){
        this.detailsWnd = new Ext.ux.CaqsGridDashboardDetailsWindow();
        var myTemplate = new Ext.XTemplate(
            '<tpl for=".">',
            '<TABLE>',
            '<TR>',
            '<TD style="align: left; vertical-align: middle;"><img src="'+requestContextPath+'/DashboardElementKiviat.do?idElt={eltId}" /></TD>',
            '<TD style="padding-right:30px; vertical-align: middle;"><TABLE>',
            '<tpl for="goals">',
            '<TR><TD>{goalLib}</TD><TD>{goalScore}</TD><TD><img ext:qtip="{goalTrendPopup}" src="'
            +requestContextPath+'/images/note_{goalTrend}.gif" /></TD></TR>',
            '</tpl>',
            '</TABLE></TD>',
            '<TD style="align: left; vertical-align: middle;"><img src="'+requestContextPath+'/DashboardElementEvolution.do?idElt={eltId}" /></TD>',
            '</TR>',
            '</TABLE>',
            '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });

        this.reader = new Ext.data.JsonReader({
            root:           'elements',
            totalProperty:  'totalCount',
            id:             'eltId',
            fields: [
            'goalsAvg',
            'eltId', 'meteoImg', 'meteoTooltip', 'eltDesc',
            'lib', 'prjId', 'prjLib', 'telt', 'libTelt',
            'blineLib', 'sortableDmaj',
            {
                name:       'dmaj',
                sortType:   this.sortDMaj
            }, 'tendanceLabel',
            'tendancePopup',
            {
                name:       'nbLOC',
                sortType:   Ext.data.SortTypes.asInt
            },
            {
                name:       'nbFileElements',
                sortType:   Ext.data.SortTypes.asInt
            },
            'nbLOCWithoutFormat', 'dmajJSParser', 'nbFileElementsWithoutFormat',
            'nbLastDaysWarning', 'nbLastDaysWarningPopup', 'goals',
            'formattedGoalsAvg'
            ]
        });
        this.store = new Ext.data.GroupingStore({
            proxy:              new Ext.data.HttpProxy({
                url: requestContextPath+'/DashboardRetrieveProjects.do'
            }),
            reader:             this.reader,
            remoteSort:         false,
            remoteGroup:        false,
            sortInfo:           {
                field:          'lib',
                direction:      'ASC'
            }
        });
        /*this.store.on('beforeload', function() {
            this.parent.showMask();
        }, this);*/

        this.store.on('load', function() {
            //this.parent.hideMask();
            if(this.globalStatisticsTimeline) {
                this.globalStatisticsTimeline.retrieveDatas();
            }
        }, this);
        var action = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:[
            {
                qtip:      getI18nResource("caqs.dashboard.timeplot.tooltip"),
                iconCls:   'icon-timeplot'
            }
            ]
        });

        action.on({
            scope:  this,
            action: function(grid, record, action, row, col) {
                this.rowAction(record);
            }
        }, this);
        var config = {
            autoScroll:         true,
            store:              this.store,
            enableColumnHide : 	false,
            autoExpandColumn:   'lib',
            trackMouseOver:     false,
            columns:            [
            expander,
            {
                id:		'meteo',
                header: 	getI18nResource("caqs.dashboard.evolution"),
                tooltip:        getI18nResource("caqs.dashboard.evolution"),
                width: 		30,
                sortable: 	true,
                align:          'center',
                renderer:       this.renderMeteo,
                dataIndex: 	'meteoImg'
            }
            ,{
                id:		'lib',
                header: 	getI18nResource("caqs.dashboard.prjName"),
                width: 		100,
                sortable: 	true,
                renderer:       this.renderName,
                menuDisabled:   true,
                dataIndex: 	'lib'
            }
            ,{
                id:		'dmaj',
                header: 	getI18nResource("caqs.dashboard.dmaj"),
                width: 		100,
                sortable: 	true,
                groupRenderer:  this.groupRendererDMaj,
                renderer:       this.renderBaselineDMaj,
                align:          'right',
                dataIndex: 	'sortableDmaj'
            }
            ,{
                id:		'goalsAvg',
                header: 	getI18nResource("caqs.dashboard.avg"),
                width: 		100,
                sortable: 	true,
                renderer:       this.renderGoalsAverage,
                groupRenderer:  this.groupRendererGoalsAverage,
                align:          'right',
                dataIndex: 	'goalsAvg'
            }
            ,{
                id:		'tendanceLabel',
                header: 	getI18nResource("caqs.dashboard.trend"),
                width: 		100,
                sortable: 	true,
                align:          'center',
                renderer:       this.renderTendanceLabel,
                dataIndex: 	'tendanceLabel'
            }
            ,{
                id:		'nbLOC',
                header: 	getI18nResource("caqs.dashboard.nbloc"),
                tooltip:        getI18nResource("caqs.dashboard.nbloc"),
                width: 		100,
                sortable: 	true,
                groupRenderer:  this.groupRendererLOC,
                align:          'right',
                dataIndex: 	'nbLOC'
            }
            ,{
                id:		'nbFileElements',
                header: 	getI18nResource("caqs.dashboard.nbFile"),
                tooltip:        getI18nResource("caqs.dashboard.nbFile"),
                width: 		100,
                sortable: 	true,
                align:          'right',
                groupRenderer:  this.groupRendererFile,
                dataIndex: 	'nbFileElements'
            }
            , action
            ],
            view: new Ext.grid.GroupingView({
                forceFit:       true,
                groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "'+
                getI18nResource("caqs.projets")+'" : "'+
                getI18nResource("caqs.projet")+'"]})'
            }),
            plugins: [
            expander,
            action
            ],
            viewConfig:{
                autoExpandColumn:   'lib',
                forceFit:       true
            },
            loadMask:           true,
            sm:                 new Ext.grid.RowSelectionModel({
                selectRow:Ext.emptyFn
            })
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsGridDashboard.superclass.initComponent.call(this);
    },

    rowAction: function(record) {
        this.detailsWnd.setTitle(record.data.lib);
        this.detailsWnd.refresh(record.data.eltId);
    },

    onRender: function(cmpt){
        Ext.ux.CaqsGridDashboard.superclass.onRender.call(this, cmpt);
        this.getView().on('refresh', function() {
            putTooltips();
        });
    },

    refresh: function(domainId) {
        this.today = new Date();
        this.store.load({
            params:{
                domainId:   domainId,
                fromCache:  false
            }
        });
    }
});
