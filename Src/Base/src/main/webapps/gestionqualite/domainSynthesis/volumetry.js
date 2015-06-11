Ext.ux.CaqsDomainSynthesisVolumetryPanel = function(config) {
    Ext.ux.CaqsDomainSynthesisVolumetryPanel.superclass.constructor.call(this, config);
};


Ext.extend(Ext.ux.CaqsDomainSynthesisVolumetryPanel, Ext.Panel, {
    volumetryGrid:      undefined,
    volumetryGridStore: undefined,
    grid:               undefined,
    gridStore:          undefined,
    domainId:           undefined,
    repartitionHelp:    undefined,
    border:             false,
    factorDetailWnd:    undefined,
    factorDetailStore:  undefined,
    nb:                 0,
    autoScroll:         true,
    height:             420,//correspond a la somme des hauteurs des 2 tableaux + un espace
    //layout:             'anchor',
    //anchor:             '100% 100%',

    buildFactorDetailWindow: function() {
        this.factorDetailStore = new Ext.data.GroupingStore({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/DomainSynthesisRepartitionFactorDetail.do'
            }),

            sortInfo:       {
                field: 'lib',
                direction: "ASC"
            },
            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 			'projects',
                totalProperty: 	'totalCount',
                id: 			'id',
                fields: [
                'id', 'lib', 'desc', 'groupLib', 'mark'
                ]
            }),
            groupField: 'groupLib',
            remoteSort: false
        });

        var factorDetailGrid = new Ext.grid.GridPanel({
            autoWidth:          true,
            store:              this.factorDetailStore,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:		false,
            frame:				false,
            view: 				new Ext.grid.GroupingView({
                forceFit:		true,
                groupTextTpl: 	'{text} ({[values.rs.length]} {[values.rs.length > 1 ? "'
                + getI18nResource('caqs.projets') +'" : "' + getI18nResource('caqs.projet') + '"]})'
            }),
            autoExpandColumn:   'lib',
            cm: new Ext.grid.ColumnModel([
            {
                id:         'lib',
                header:     getI18nResource('caqs.domainsynthese.repartition.factorsdetails.label'),
                //width:      130,
                sortable:   true,
                dataIndex:  'lib'
            }
            ,{
                header:     getI18nResource('caqs.domainsynthese.repartition.factorsdetails.desc'),
                //width:      150,
                sortable:   true,
                dataIndex:  'desc'
            }
            ,{
                header:     getI18nResource('caqs.domainsynthese.repartition.factorsdetails.mark'),
                //width:      30,
                sortable:   true,
                align:      'right',
                dataIndex:  'mark'
            }
            ,{
                header:     getI18nResource('caqs.projets'),
                hidden:     true,
                dataIndex: 'groupLib'
            }
            ]),
            //style: 			'margin-bottom:20px;',
            collapsible: 	false,
            iconCls: 		'icon-grid',
            autoHeight:		true
        });

        this.factorDetailWnd = new Ext.Window({
            layout:			'fit',
            modal: 			true,
            maximizable: 	true,
            minimizable: 	false,
            width:			600,
            autoHeight:     true,
            shadow:			false,
            plain:			true,
            items:          factorDetailGrid
        });
        this.factorDetailWnd.on('beforeclose', function(wnd) {
            this.factorDetailWnd.hide();
            return false;
        }, this);
    },

    launchWindow:   function(factorId, title) {
        if(this.factorDetailWnd==null) {
            this.buildFactorDetailWindow();
        }
        this.factorDetailStore.load({
            params:{
                factorId:    			factorId
            }
        });
        this.factorDetailWnd.setTitle(title);
        this.factorDetailWnd.show(this);
    },

    setTooltips: function() {
        var msg = '<IMG src="images/red.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource('caqs.domainsynthese.repartition.help1')+'<BR />';
        msg += '<IMG src="images/orange.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource('caqs.domainsynthese.repartition.help2')+'<BR />';
        msg += '<IMG src="images/darkgreen.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource('caqs.domainsynthese.repartition.help3')+'<BR />';
        msg += '<IMG src="images/green.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource('caqs.domainsynthese.repartition.help4');
        addTooltip('repartitionHelp',
            "<img src=\""+requestContextPath+"/images/help.gif\" />&nbsp;"+getI18nResource('caqs.domainsynthese.repartition.helpTitle'),
            msg, 0, 400);
    },

    dispatchingRenderer: function(val, p, record) {
        var result = '&nbsp;';

        if(record.data.pct1 > 0) {
            addTooltip(record.data.id+'0', '',
                '<IMG src="images/red.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.tooltip1);
            result += '<IMG id="'+record.data.id+'0" src="'+requestContextPath+'/images/red.gif" height="15" border="0" width="'+1.7*record.data.pct1+'" />';
        }
        if(record.data.pct2 > 0) {
            addTooltip(record.data.id+'1', '',
                '<IMG src="images/orange.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.tooltip2);
            result += '<IMG id="'+record.data.id+'1" src="'+requestContextPath+'/images/orange.gif" height="15" border="0" width="'+1.7*record.data.pct2+'" />';
        }
        if(record.data.pct3 > 0) {
            addTooltip(record.data.id+'2', '',
                '<IMG src="images/darkgreen.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.tooltip3);
            result += '<IMG id="'+record.data.id+'2" src="'+requestContextPath+'/images/darkgreen.gif" height="15" border="0" width="'+1.7*record.data.pct3+'" />';
        }
        if(record.data.pct4 > 0) {
            addTooltip(record.data.id+'3', '',
                '<IMG src="images/green.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.tooltip4);
            result += '<IMG id="'+record.data.id+'3" src="'+requestContextPath+'/images/green.gif" height="15" border="0" width="'+1.7*record.data.pct4+'" />';
        }

        return result;
    },

    initComponent : function(){
        Ext.ux.CaqsDomainSynthesisVolumetryPanel.superclass.initComponent.call(this);

        this.repartitionHelp = getI18nResource('caqs.domainsynthese.repartition')
        +'&nbsp;<img id="repartitionHelp" src="'+requestContextPath+'/images/help.gif" />';

        var myTemplate = new Ext.XTemplate(
            "<p><b>"+getI18nResource('caqs.domainsynthese.desc')+":</b> {desc}</p>",
            '<tpl if="compl != \'\'">',
            "<BR /><p><b>"+getI18nResource('caqs.domainsynthese.compl')+":</b> {compl}</p>",
            '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });

        this.gridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/DomainSynthesisRepartitionGrid.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 'factors',
                totalProperty: 'totalCount',
                id: 'id',
                fields: [
                'id', 'lib', 'desc', 'compl',
                'tooltip1', 'tooltip2', 'tooltip3', 'tooltip4',
                'pct1', 'pct2', 'pct3', 'pct4', 'percentageOk',
                'formattedPercentageOk'
                ]
            }),
            remoteSort: true
        });
        //this.gridStore.setDefaultSort('lib', 'asc');

        var percentageColumn = new Ext.ux.grid.ProgressColumn({
            header :        '%',
            tooltip:        getI18nResource('caqs.domainsynthese.repartition.percentageTooltip'),
            dataIndex :     'percentageOk',
            align:          'center',
            textDataIndex:  'formattedPercentageOk',
            width :         85,
            colored :       true,
            textPst :       '%' // string added to the end of the cell value (defaults to '%')
        });

        var action = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:[
            {
                qtip:      getI18nResource('caqs.domainsynthese.repartition.factorsdetails.buttonTooltip')
                ,
                iconCls:   'icon-application-list'
            }
            ]
        });

        action.on({
            scope:  this,
            action: function(grid, record, action, row, col) {
                this.launchWindow(record.data.id, record.data.lib);
            }
        }, this);

        this.grid = new Ext.grid.GridPanel({
            store:              this.gridStore,
            style:		'padding-top: 5px;',
            header:		true,
            title:              getI18nResource('caqs.domainSynthesis.projectDispatching'),
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:	false,
            height:		230,
            frame:		false,
            loadMask:           true,
            cm: new Ext.grid.ColumnModel([
                expander,
                {
                    header:     getI18nResource('caqs.domainsynthese.objectif'),
                    width:      300,
                    sortable:   false,
                    dataIndex:  'lib'
                },
                {
                    header:     this.repartitionHelp,
                    width:      180,
                    align:      'center',
                    sortable:   false,
                    renderer:   this.dispatchingRenderer
                },
                percentageColumn,
                action
                ]),
            viewConfig: {
                forceFit:true
            },
            plugins : [
            expander,
            percentageColumn,
            action
            ],
            collapsible: false,
            iconCls: 'icon-grid'
        });

        this.volumetryGridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/DomainSynthesisVolumetry.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root:           'volumetries',
                totalProperty:  'totalCount',
                id:             'idTelt',
                fields: [
                'idTelt', 'libTelt', 'nbElts', 'nbCrees',
                'nbSupp'
                ]
            }),
            remoteSort: true
        });

        this.volumetryGrid = new Ext.grid.GridPanel({
            store:              this.volumetryGridStore,
            header:             true,
            enableColumnHide :  false,
            enableColumnMove :  false,
            enableHdMenu:       false,
            title:              getI18nResource("caqs.domainSynthesis.volumetry"),
            height:             180,
            anchor:             '100% 40%',
            frame:              false,
            loadMask:           true,
            cm: new Ext.grid.ColumnModel([
            {
                header: 	getI18nResource("caqs.domainSynthesis.volumetry.telt"),
                width: 		300,
                sortable: 	true,
                dataIndex: 	'libTelt'
            },
            {
                header:         getI18nResource("caqs.domainSynthesis.volumetry.nb"),
                width:          50,
                align:          'right',
                sortable:       true,
                dataIndex:      'nbElts'
            },
            {
                header:         getI18nResource("caqs.domainSynthesis.volumetry.crees"),
                width:          50,
                align:          'right',
                sortable:       true,
                dataIndex:      'nbCrees'
            },
            {
                header:         getI18nResource("caqs.domainSynthesis.volumetry.supp"),
                width:          50,
                align:          'right',
                sortable:       true,
                dataIndex:      'nbSupp'
            }
            ]),
            viewConfig: {
                forceFit:true
            },
            collapsible: false,
            iconCls: 'icon-grid'
        });

        var config = {
            items:  [
            this.volumetryGrid
            , this.grid
            ]
        };

        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsDomainSynthesisVolumetryPanel.superclass.initComponent.apply(this, arguments);
        /*
        this.grid.on('beforerender', function() {
            this.grid.setSize({
                width: Ext.getBody().getSize().width-40,
                height: 230
            });
        }, this);
        this.volumetryGrid.on('beforerender', function() {
            this.volumetryGrid.setSize({
                width: Ext.getBody().getSize().width-40,
                height: 180
            });
        }, this);*/
        this.grid.getView().on('refresh', function() {
            putTooltips();
        });
    },

    refresh : function(domainId){
        this.domainId = domainId;
        this.setTooltips();
        this.gridStore.load({
            params:{
                domainId: this.domainId
            }
        });
        this.volumetryGridStore.load({
            params:{
                domainId: this.domainId
            }
        });
    }
});

