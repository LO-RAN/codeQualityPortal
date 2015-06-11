Ext.ux.CaqsActionsPlanEvolutionsWnd = function(config) {
    // call parent constructor
    Ext.ux.CaqsActionsPlanEvolutionsWnd.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor

Ext.extend(Ext.ux.CaqsActionsPlanEvolutionsWnd, Ext.Window, {
    modal: 		true,
    maximizable: 	false,
    resizable:		false,
    layout:		'fit',
    minimizable: 	false,
    width:		810,
    height:		340,
    shadow:		false,
    plain:		true,
    x:			20,
    y:			20,
    store:              undefined,
    grid:               undefined,
    gridPageSize:       12,
    title:              getI18nResource('caqs.evolution.actionplan.concernedElements'),
    
    initComponent : function(){
        this.store = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveActionsPlanElementsGridList.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 		'elements',
                totalProperty: 	'totalCount',
                id: 		'id',
                fields: [
                    'id', 'descElt', 'idTelt', 'lib_telt'
                ]
            }),
            remoteSort: true
        });
        this.store.setDefaultSort('descElt', 'asc');

        this.grid = new Ext.grid.GridPanel({
            store: 		this.store,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:	false,
            loadMask: 		true,
            frame:		false,
            cm: new Ext.grid.ColumnModel([
                {
                    header: 	getI18nResource("caqs.element.desc"),
                    width: 	400,
                    sortable: 	true,
                    dataIndex: 	'descElt'
                },
                {
                    header: 	getI18nResource("caqs.element.telt"),
                    width: 	80,
                    sortable: 	true,
                    dataIndex: 	'idTelt',
                    renderer:   this.teltRenderer,
                    qtip:       'lib_telt'
                }
            ]),
            collapsible: false,
            bbar: new Ext.PagingToolbar({
                pageSize: 	this.gridPageSize,
                store: 		this.store,
                displayInfo: 	true,
                displayMsg: 	'{0} - {1} / {2}'
            }),
            autoExpandColumn:   'descElt',
            viewConfig: {
                    forceFit:   true
            },
            iconCls: 'icon-grid'
        });
        var config = {
            items:      this.grid
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsActionsPlanEvolutionsWnd.superclass.initComponent.apply(this, arguments);
        this.on('beforeclose', this.onBeforeClose, this);
    },

    teltRenderer: function(val, cell, record, rowIndex, colIndex, store) {
        cell.attr = "ext:qtip='"+record.data.lib_telt+"'";
        return record.data.idTelt;
    },

    refresh: function(category, idElt, idBline, idPrevBline, idCrit) {
        this.store.on('beforeload', function(store, options) {
            store.baseParams.category = category;
            store.baseParams.idElt = idElt;
            store.baseParams.previousIdBline = idPrevBline;
            store.baseParams.idBline = idBline;
            store.baseParams.idCrit = idCrit;
        }, this);
        this.show();
        this.store.load({
            params:{
                start:  0,
                limit:  this.gridPageSize
            }
        });
    },

    onBeforeClose: function(wnd) {
        wnd.hide();
        return false;
    }
});