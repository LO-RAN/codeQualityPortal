Ext.ux.CaqsTrashGridPanel = function(config) {
    Ext.ux.CaqsTrashGridPanel.superclass.constructor.call(this, config);
};

Ext.extend(Ext.ux.CaqsTrashGridPanel, Ext.grid.GridPanel, {
    autoWidth:                  true,
    autoHeight:                 true,
    enableColumnHide :          false,
    enableColumnMove :          false,
    enableColumnResize:         false,
    header:                     false,
    deleteButton:               undefined,
    emptyTrashButton:           undefined,
    mask:                       undefined,
    trashPageSize:              15,
    gridStore:                  undefined,


    initComponent : function(){
        Ext.ux.CaqsTrashGridPanel.superclass.initComponent.apply(this, arguments);

        this.gridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrievePeremptedElements.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root:           'elements',
                totalProperty:  'totalCount',
                id:             'id',
                fields: [
                    'id', 'lib', 'dperemption', 'fatherLib', 'telt',
                    'id_telt', 'desc', 'iconCls', 'msgIcon', 'msgTxt',
                    'deletable'
                ]
            }),
            remoteSort: true
        });

        var config = {
            store:              this.gridStore,
            autoExpandColumn:   'lib',
            columns:   [
                {
                    id: 		'msg',
                    header: 	'',
                    sortable:	false,
                    width:       20,
                    align:      'center',
                    resizable:  false,
                    renderer: 	this.renderMsg
                },{
                    id: 		'lib',
                    header: 	getI18nResource("caqs.admin.trashgrid.label"),
                    sortable:	true,
                    dataIndex: 	'lib',
                    renderer:   this.renderLib
                },{
                    header: 	getI18nResource("caqs.admin.trashgrid.fatherPath"),
                    dataIndex: 	'fatherLib',
                    sortable: 	false
                }
                ,{
                    header: 	getI18nResource("caqs.admin.trashgrid.desc"),
                    dataIndex:	'desc',
                    sortable: 	true
                }
                ,{
                    header: 	getI18nResource("caqs.admin.trashgrid.elementType"),
                    dataIndex:	'telt',
                    sortable: 	true,
                    width:      50,
                    tooltip:    getI18nResource("caqs.admin.trashgrid.elementType.tooltip"),
                    renderer: 	this.renderTelt
                }
                ,{
                    header: 	getI18nResource("caqs.admin.trashgrid.dperemption"),
                    dataIndex:	'dperemption',
                    width:      100,
                    sortable: 	true
                }
            ],
            viewConfig:{
                forceFit:true
            },
            loadMask:  true,
            bbar : new Ext.PagingToolbar({
                pageSize: 		this.trashPageSize,
                store: 			this.gridStore,
                displayInfo: 	true,
                displayMsg: 	'{0} - {1} / {2}'
            })
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsTrashGridPanel.superclass.initComponent.apply(this, arguments);

        this.gridStore.setDefaultSort('lib', 'asc');
        this.getView().on('refresh', function() {
            putTooltips();
        }, this);
        this.getSelectionModel().on('selectionchange', this.validateButtons, this);
    },

    onRender : function(ct, position){
        Ext.ux.CaqsTrashGridPanel.superclass.onRender.apply(this, arguments);
        this.getBottomToolbar().add('-');
        if(canAccessFunction("DELETE_FROM_TRASH")) {
            this.deleteButton = new Ext.Button({
                text:       getI18nResource("caqs.admin.trashgrid.deleteButton"),
                scope:      this,
                cls:		'x-btn-text-icon',
                icon:		requestContextPath+'/images/delete.gif',
                handler:    this.askDeleteSelectedElements
            });
            this.getBottomToolbar().add(this.deleteButton);
            this.emptyTrashButton = new Ext.Button({
                text:       getI18nResource("caqs.admin.trashgrid.emptyTrash"),
                scope:      this,
                cls:		'x-btn-text-icon',
                icon:		requestContextPath+'/images/bin_empty.gif',
                handler:    this.askEmptyTrash
            });
            this.getBottomToolbar().add(this.emptyTrashButton);
            this.getBottomToolbar().add('-');
        }
        this.getBottomToolbar().add({
            text:       getI18nResource("caqs.admin.trashgrid.restoreButton"),
            scope:      this,
            cls:		'x-btn-text-icon',
            icon:       requestContextPath + '/images/arrow_redo.gif',
            handler:    this.askRestoreSelectedElements
        });
        this.gridStore.load({
            params:{
                start:  0,
                limit:  this.trashPageSize
            }
        });
    },

    validateButtons: function(selModel) {
        var records = selModel.getSelections();
        if(records!=null && records.length>0) {
            var canDelete = true;
            for(var i=0; i<records.length && canDelete; i++) {
                canDelete = records[i].data.deletable;
            }
            if(this.deleteButton) {
                this.deleteButton.setDisabled(!canDelete);
            }
        }
    },

    renderLib: function(val, p, record) {
        var retour = '<img src="'+requestContextPath;
        retour += '/images/customExt/'+record.data.iconCls+'.gif" />';
        retour += '&nbsp;';
        retour += record.data.lib;
        retour += '</div>';
        return retour;
    },

    renderTelt: function(val, p, record) {
        p.attr = 'ext:qtip="'+record.data.telt+'"';
        return record.data.id_telt;
    },

    renderMsg: function(val, p, record) {
        var retour = '&nbsp;';
        if(record.data.msgTxt != '') {
            p.attr = 'ext:qtip="'+record.data.msgTxt+'"';
            retour = '<img src="'+requestContextPath+'/images/'+record.data.msgIcon+
                '.gif" id="msgIcon'+record.data.id+'" />';
            addTooltip('msgIcon'+record.data.id, '', record.data.msgTxt);
        }
        return retour;
    },

    getAllSelectedIds: function(records) {
        var allIds = '';
        var first = true;
        for(var i=0; i<records.length; i++) {
            if(!first) {
                allIds += ',';
            }
            first = false;
            allIds += records[i].data.id;
        }
        return allIds;
    },

    refreshGrid: function() {
        if(this.mask) {
            this.mask.hide();
            this.mask.destroy();
            this.mask = null;
        }
        this.getBottomToolbar().onClick('refresh');
        Caqs.Portal.getCaqsPortal().setCurrentScreenId('recycleBin');
    },

    deleteSelectedElements: function(answer) {
        if(answer=='yes') {
            var records = this.getSelectionModel().getSelections();
            var allIds = this.getAllSelectedIds(records);
            this.mask = new Ext.LoadMask(Ext.getBody(), {msg: getI18nResource("caqs.admin.trashgrid.nowDeleting")});
            this.mask.show();
            Ext.Ajax.request({
                url:		requestContextPath+'/ManagePeremptedElements.do',
                params: {
                            allIds:     allIds,
                            action:     'delete'
                },
                scope:      this,
                success:	this.refreshGrid
            });
        }
    },

    askDeleteSelectedElements: function() {
        var records = this.getSelectionModel().getSelections();
        if(records==null || records.length==0) {
            Ext.Msg.show({
                title:   '',
                msg:     getI18nResource("caqs.admin.trashgrid.deleteSelectOne"),
                buttons: Ext.Msg.OK,
                animEl:  Ext.getBody(),
                icon:    Ext.MessageBox.ERROR
            });
        } else {
            Ext.Msg.confirm('', getI18nResource("caqs.admin.trashgrid.confirmDelete"),
                this.deleteSelectedElements, this);
        }
    },

    emptyTrash: function(answer) {
        if(answer=='yes') {
            this.mask = new Ext.LoadMask(Ext.getBody(), {msg: getI18nResource("caqs.admin.trashgrid.nowDeleting")});
            this.mask.show();
            Ext.Ajax.request({
                url:		requestContextPath+'/ManagePeremptedElements.do',
                params: {
                            emptyTrash:     true,
                            action:         'delete'
                },
                scope:      this,
                success:	this.refreshGrid
            });
        }
    },

    askEmptyTrash: function() {
        Ext.Msg.confirm('', getI18nResource("caqs.admin.trashgrid.confirmEmpty"),
            this.emptyTrash, this);
    },

    restoreSelectedElements: function(answer) {
        if(answer=='yes') {
            var records = this.getSelectionModel().getSelections();
            var allIds = this.getAllSelectedIds(records);
            this.mask = new Ext.LoadMask(Ext.getBody(), {msg: getI18nResource("caqs.admin.trashgrid.nowRestoring")});
            this.mask.show();
            Ext.Ajax.request({
                url:		requestContextPath+'/ManagePeremptedElements.do',
                params: {
                            allIds:     allIds,
                            action:     'restore'
                },
                scope:      this,
                success:	this.refreshGrid
            });
        }
    },

    askRestoreSelectedElements: function() {
        var records = this.getSelectionModel().getSelections();
        if(records==null || records.length==0) {
            Ext.Msg.show({
                title:   '',
                msg:     getI18nResource("caqs.admin.trashgrid.restoreSelectOne"),
                buttons: Ext.Msg.OK,
                animEl:  Ext.getBody(),
                icon:    Ext.MessageBox.ERROR
            });
        } else {
            Ext.Msg.confirm('', getI18nResource("caqs.admin.trashgrid.confirmRestore"),
                this.restoreSelectedElements, this);
        }
    }
});
