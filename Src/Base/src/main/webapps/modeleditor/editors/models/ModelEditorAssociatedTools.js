Ext.grid.ModelEditorAssociatedToolsCheckColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.ModelEditorAssociatedToolsCheckColumn.prototype ={
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

            var toolId = record.data.id;
            var newValue = record.data[this.dataIndex];

            this.grid.showMask();

            Ext.Ajax.request({
                url: requestContextPath+'/ToolModelAssociationSave.do',
                params: {
                        'modelId':      this.grid.modelId,
                        'toolId':       toolId,
                        'associate':    newValue
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


Ext.ux.CaqsModelEditorAssociatedToolsGridPanel = function(config) {
    Ext.ux.CaqsModelEditorAssociatedToolsGridPanel.superclass.constructor.call(this, config);
};


Ext.extend(Ext.ux.CaqsModelEditorAssociatedToolsGridPanel, Ext.grid.GridPanel, {
    enableColumnHide :    false,
    enableColumnMove :    false,
    enableColumnResize:   false,
    header:               false,
    mask:                 undefined,
    store:                undefined,
    reader:               undefined,
    gridPageSize:         17,
    modelId:              undefined,

    setId: function(id) {
        this.modelId = id;
        this.store.load({
            params:{
                start:  0,
                limit:  this.gridPageSize,
                modelId:this.modelId
            }
        });
    } ,

    initComponent : function(){
        var myTemplate = new Ext.XTemplate(
               '<p><b>'+getI18nResource('caqs.modeleditor.grid.desc')+':</b> {desc}</p>',
                '<tpl if="compl != \'\'">',
                '<p><b>'+getI18nResource('caqs.modeleditor.grid.compl')+':</b> {compl}</p>',
                '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });

        var thisColumns = new Array();
        thisColumns[thisColumns.length] = expander;
        thisColumns[thisColumns.length] = {
            id:         'id',
            header:     getI18nResource('caqs.modeleditor.grid.id'),
            width:      100,
            sortable:   true,
            dataIndex:  'id'
        };
        thisColumns[thisColumns.length] = {
            header:     getI18nResource('caqs.modeleditor.grid.lib'),
            width:      100,
            sortable:   true,
            dataIndex:  'lib'
        };
        thisColumns[thisColumns.length] = {
            header:     getI18nResource('caqs.modeleditor.modelEdition.tools.nbAssociatedMetrics'),
            width:      100,
            sortable:   true,
            dataIndex:  'nbAssociatedMetrics'
        };
        var checkboxCol = new Ext.grid.ModelEditorAssociatedToolsCheckColumn({
            header:     '',
            width:      50,
            dataIndex:  'associated'
        });
        thisColumns[thisColumns.length] = checkboxCol;
        this.store = new Ext.data.Store({
                proxy:              new Ext.data.HttpProxy({
                                        url: requestContextPath+'/ModelEditorRetrieveAssociatedTools.do'
                                    }),
                reader:             new Ext.data.JsonReader({
                                        root:           'elements',
                                        totalProperty:  'totalCount',
                                        id: 'id',
                                        fields: [
                                            'id', 'lib', 'desc', 'compl',
                                            'nbAssociatedMetrics', 'associated'
                                        ]
                                    }),
                remoteSort:         true
            });
        var config = {
            store:              this.store,
            autoExpandColumn:   'lib',
            columns:            thisColumns,
            viewConfig:{
                forceFit:       true
            },
            loadMask:           true,
            plugins:            [
                checkboxCol,
                expander
            ],
            sm:                 new Ext.grid.RowSelectionModel({selectRow:Ext.emptyFn}),
            clicksToEdit:       1
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        this.bbar = new Ext.PagingToolbar({
            pageSize: 		this.gridPageSize,
            store: 		this.store,
            displayInfo: 	true,
            displayMsg: 	'{0} - {1} / {2}'
        });

        Ext.ux.CaqsModelEditorAssociatedToolsGridPanel.superclass.initComponent.apply(this, arguments);
        this.store.setDefaultSort('lib', 'asc');
    },

    showMask: function() {
        Caqs.Portal.showGlobalLoadingMask();
    },

    removeMask: function() {
        Caqs.Portal.hideGlobalLoadingMask();
    }
});

