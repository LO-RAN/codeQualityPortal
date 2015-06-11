Ext.ux.CaqsModelEditorGridPanel = Ext.extend(Ext.grid.GridPanel, {
    dataStoreUrl:       undefined,
    dataStore:          undefined,
    pageSize:           10,
    gridColumns:        undefined,
    rowActions:         undefined,
    parentSearchScreen: undefined,
    newBtn:             undefined,
    additionnalToolbarBtns:undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsModelEditorGridPanel.superclass.constructor.apply(this, arguments);
    },

    columnRenderer: function(val, cell, record) {
        cell.attr += "ext:qtip=\""+val+"\"";
        return val;
    },

    initComponent : function(){
        Ext.ux.CaqsModelEditorGridPanel.superclass.initComponent.apply(this, arguments);
        
        var columns = new Array();
        var jsonReaderArray = new Array();
        for(var i=0; i<this.gridColumns.length; i++) {
            columns[columns.length] = {
                id:         this.gridColumns[i].fieldId,
                header:     this.gridColumns[i].fieldName,
                sortable:   true,
                width:      this.gridColumns[i].width,
                renderer:   (this.gridColumns[i].renderer)?this.gridColumns[i].renderer:this.columnRenderer,
                dataIndex:  this.gridColumns[i].fieldId
            }
            jsonReaderArray[jsonReaderArray.length] = this.gridColumns[i].fieldId;
        }
        //ajout d'un bidon pour l'action d'edition
        jsonReaderArray[jsonReaderArray.length] = 'editAction';
        if(this.specificJSONReaderData) {
            this.specificJSONReaderData(jsonReaderArray);
        }

        if(this.rowActions == null) {
            this.rowActions = new Ext.ux.grid.RowActions({
                 header:        '',
                 align:         'center',
                 actions:[
                    {
                         iconCls:   'icon-modeleditor-edit',
                         tooltip:   getI18nResource('caqs.modeleditor.modelEdition.editer.tooltip')
                    }
                ]
            });
            this.rowActions.on({
                scope:  this,
                action: function(grid, record, action, row, col) {
                    this.parentSearchScreen.startEdition(record.data.id);
                }
            }, this);
        }
        columns[columns.length] = this.rowActions;
        
        this.dataStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url:    requestContextPath + this.dataStoreUrl
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root:           'datas',
                totalProperty:  'totalCount',
                id:             'id',
                fields:         jsonReaderArray
            }),
            remoteSort: true
        });
        this.dataStore.setDefaultSort('id', 'asc');
        this.dataStore.on('beforeload', function(store) {
            Caqs.Portal.showGlobalLoadingMask();
            this.parentSearchScreen.setBeforeLoadSearchFilters(store);
        }, this);
        this.dataStore.on('load', function() {
            Caqs.Portal.hideGlobalLoadingMask();
        }, this);


        var config = {
            columns:            columns,
            region:             'center',
            store:              this.dataStore,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:	false,
            autoExpandColumn:   'lib',
            viewConfig: {
                forceFit:       true
            },
            plugins: [
                this.rowActions
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        this.bbar = new Ext.PagingToolbar({
            pageSize: 		this.pageSize,
            store: 			this.dataStore,
            displayInfo: 	true,
            displayMsg: 	'{0} - {1} / {2}'
        });

        this.newBtn = new Ext.Button({
            text:       getI18nResource("caqs.new"),
            cls:		'x-btn-text-icon',
            icon:		requestContextPath+'/images/database_add.gif',
            handler:    this.startCreation,
            scope:      this
        });

        this.on('render', function() {
            this.getBottomToolbar().add('-');
            this.getBottomToolbar().add(this.newBtn);
            if(this.additionnalToolbarBtns!=null && Ext.isArray(this.additionnalToolbarBtns)) {
                for(var i=0; i<this.additionnalToolbarBtns.length; i++) {
                    this.getBottomToolbar().add('-');
                    this.getBottomToolbar().add(this.additionnalToolbarBtns[i]);
                }
            }
        }, this);
        this.on('rowdblclick', function(grid, rowIndex, evt) {
            var record = this.dataStore.getAt(rowIndex);
            this.parentSearchScreen.startEdition(record.data.id);
        }, this);
        Ext.ux.CaqsModelEditorGridPanel.superclass.initComponent.apply(this, arguments);
    },

    startCreation: function() {
        this.parentSearchScreen.startEdition();
    },

    loadDatas: function(params) {
        this.dataStore.load({
            params:     params
        });
    },

    refresh: function() {
        this.dataStore.reload();
    }
});

