Ext.grid.UserRightsCheckColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.UserRightsCheckColumn.prototype ={
    init : function(grid){
        this.grid = grid;
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
        }, this);
    },

    onMouseDown : function(e, t){
        if(t.className && t.className.indexOf('x-grid3-cc-'+this.id) != -1){
            e.stopEvent();
            var rowIndex = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(rowIndex);
            record.set(this.dataIndex, !record.data[this.dataIndex]);

            var rightId = record.data.id;
            var roleId =  this.id;
            var newValue = record.data[this.dataIndex];

            this.grid.showMask();

            Ext.Ajax.request({
                url: requestContextPath+'/UserRightsSave.do',
                params: {
                    'rightId':      rightId,
                    'roleId':       roleId,
                    'newValue':     newValue
                },
                scope:      this.grid,
                success:    this.grid.removeMask
            });
        }
    },

    renderer : function(v, p, record){
        p.css += ' x-grid3-check-col-td';
        return '<div class="x-grid3-check-col'+(v?'-on':'')+' x-grid3-cc-'+this.id+'">&#160;</div>';
    }
};


Ext.ux.CaqsUserRightsGridPanel = function(config) {
    // call parent constructor
    Ext.ux.CaqsUserRightsGridPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsUserRightsGridPanel, Ext.grid.GridPanel, {
    //autoWidth:                  true,
    autoHeight:             true,
    enableColumnHide :      false,
    enableColumnMove :      false,
    enableColumnResize:     false,
    header:                 false,
    rightsStore:            undefined,
    reader:                 undefined,
    gridPageSize:           17,
    anchor:                 '98%',
    style:                  'margin-top: 5px; margin-left: 5px;',

    initComponent : function(){
        var thisColumns = new Array();
        thisColumns[0] = {
            id:         'rightName',
            header:     getI18nResource("caqs.userRights.right"),
            width:      100,
            sortable:   true,
            renderer:   this.renderRightName,
            dataIndex:  'rightName'
        };
        for(var i=0; i<this.checkColumns.length; i++) {
            thisColumns[thisColumns.length] = this.checkColumns[i];
        }
        this.rightsStore = new Ext.data.Store({
            proxy:              new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveUserRights.do'
            }),
            reader:             this.reader,
            remoteSort:         true
        });
        var config = {
            store:              this.rightsStore,
            autoExpandColumn:   'rightName',
            columns:            thisColumns,
            viewConfig:{
                forceFit:       true
            },
            loadMask:           true,
            plugins:            this.checkColumns,
            sm:                 new Ext.grid.RowSelectionModel({selectRow:Ext.emptyFn}),
            clicksToEdit:       1,
            bbar:               new Ext.PagingToolbar({
                pageSize: 	this.gridPageSize,
                store: 		this.rightsStore,
                displayInfo: 	true,
                displayMsg: 	'{0} - {1} / {2}'
            })
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));

        Ext.ux.CaqsUserRightsGridPanel.superclass.initComponent.apply(this, arguments);
        this.rightsStore.setDefaultSort('rightName', 'asc');
    },

    onRender : function(ct, position){
        Ext.ux.CaqsUserRightsGridPanel.superclass.onRender.apply(this, arguments);
        this.rightsStore.load({
            params:{
                start:  0,
                limit:  this.gridPageSize
            }
        });
    },

    renderRightName: function(v, p, record) {
        p.attr = 'ext:qtip="'+record.data.rightName+'"';
        return record.data.rightName;
    },

    showMask: function() {
        Caqs.Portal.getCaqsPortal().getAdministrationActivity().showMask();
    },

    removeMask: function() {
        Caqs.Portal.getCaqsPortal().getAdministrationActivity().hideMask();
    },

    refreshGrid: function() {
        this.removeMask();
        this.getBottomToolbar().onClick('refresh');
        Caqs.Portal.getCaqsPortal().setCurrentScreenId('userRights');
    }
});