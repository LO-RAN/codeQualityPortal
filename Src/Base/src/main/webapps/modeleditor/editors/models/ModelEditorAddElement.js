Ext.ux.CaqsModelEditorAddElementWindow = function(config) {
    Ext.ux.CaqsModelEditorAddElementWindow.superclass.constructor.call(this, config);
};


Ext.extend(Ext.ux.CaqsModelEditorAddElementWindow, Ext.Window, {
    maximizable:        true,
    modelId:            undefined,
    bodyBorder:         false,
    addGoalBtn:         undefined,
    closeBtn:           undefined,
    parentPanel:        undefined,
    grid:               undefined,
    gridStore:          undefined,
    height:             600,
    width:              600,
    modal:              true,
    elementType:        undefined,

    setId: function(id) {
        this.modelId = id;
    },

    initComponent : function(){
        this.gridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/ModelEditorRetrieveAddableElements.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 		'elements',
                totalProperty: 	'totalCount',
                id: 		'id',
                fields: [
                    'id', 'lib', 'desc', 'compl'
                ]
            }),
            remoteSort: true
        });
        this.gridStore.setDefaultSort('id', 'asc');

        var myTemplate = new Ext.Template(
            '<B>'+getI18nResource('caqs.modeleditor.grid.desc')+'</B> : {desc}',
            '<tpl if="compl != \'\'">',
            '<BR/><B>'+getI18nResource('caqs.modeleditor.grid.compl')+'</B> : {compl}',
            '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });

        var action = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:[
                {
                    qtip:      getI18nResource('caqs.modeleditor.modelEdition.'+this.elementType+'Add.add'),
                    iconCls:   'icon-addgoal'
                }
            ]
        });

        action.on({
            scope:  this,
            action: function(grid, record, action, row, col) {
                this.addElement(record);
            }
        }, this);


        // create the Grid
        this.grid = new Ext.grid.GridPanel({
            //anchor:             '99%',
            region:             'center',
            store:              this.gridStore,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:       false,
            frame:		false,
            height:             180,
            loadMask:           true,
            cm: new Ext.grid.ColumnModel([
                expander
                , {
                    id:         'id',
                    header:     getI18nResource('caqs.modeleditor.grid.id'),
                    width:      180,
                    sortable:   true,
                    dataIndex:  'id',
                    filter: {xtype: 'textfield', filterName: 'filterid'}
                }
                , {
                    id:         'lib',
                    header:     getI18nResource('caqs.modeleditor.grid.lib'),
                    width:      200,
                    sortable:   true,
                    dataIndex:  'lib',
                    filter: {xtype: 'textfield', filterName: 'filterlib', filterWidth: 200}
                }
                , action
                ]),
            collapsible: 	false,
            iconCls: 		'icon-grid',
            autoExpandColumn:   'lib',
            plugins: 		[
                expander
                , action
                , new Ext.ux.grid.GridHeaderFilters()
            ]
            , bbar: new Ext.PagingToolbar({
                pageSize:       10,
                store: 		this.gridStore,
                displayInfo: 	true
            })
        });
        this.gridStore.on('beforeload', function(store, options) {
            store.baseParams.elementType = this.elementType;
            if(this.elementType=='goals') {
                store.baseParams.alreadyAddedButNotSaved = this.parentPanel.getAllTabsIds();
                store.baseParams.modelId = this.modelId;
            } else {
                store.baseParams.modelId = this.modelId;
                store.baseParams.goalId = this.parentPanel.getActiveGoalId();
            }
            
        }, this);
        
        var btn = new Ext.Button({
            text:	getI18nResource("caqs.close"),
            icon: 	requestContextPath + '/images/cross.gif',
            cls: 	'x-btn-text-icon',
            width:      80,
            handler:    this.close,
            scope:      this
        });
        this.on('beforeclose', function(wnd) {
            this.hide();
            return false;
        });
        this.on('show', function(wnd) {
            this.refreshGridDatas();
        });
        var msg = getI18nResource('caqs.modeleditor.modelEdition.'+this.elementType+'Add.info');
        // apply config
        var items = new Array();
        var layout = 'border';
        if(msg!='') {
            //layout = 'anchor';
            items[items.length] = new Ext.Panel({
                border:     false,
                height:     50,
                region:     'north',
                items: [
                    new Ext.Panel({
                        border:     true,
                        frame:      true,
                        style:      'margin-top: 5px; margin-left: 5px; margin-bottom: 5px; margin-right: 5px;',
                        items:      [
                            {
                                border:  false,
                                html:    '<img src="'+requestContextPath+'/images/information.gif" />&nbsp;&nbsp;' + msg

                            }
                        ]
                    })
                ]
            });
        }
        items[items.length] = this.grid;
        var config = {
            layout:         layout,
            title:          getI18nResource('caqs.modeleditor.modelEdition.'+this.elementType+'Add.wndTitle'),
            items:          items,
            buttons: [
                btn
            ]
        }
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelEditorAddElementWindow.superclass.initComponent.apply(this, arguments);
    },

    addElement: function(record) {
      //a surcharger
    },

    refreshGridDatas: function() {
        var params = null;
        if(this.elementType=='goals') {
            params = {
                elementType:                this.elementType,
                start:                      0,
                limit:                      10,
                alreadyAddedButNotSaved:    this.parentPanel.getAllTabsIds(),
                modelId:                    this.modelId
            };
        } else {
            params = {
                elementType:                this.elementType,
                start:                      0,
                limit:                      10,
                modelId:                    this.modelId,
                goalId:                     this.parentPanel.getActiveGoalId()
            };
        }
        this.gridStore.load({
            params:     params
        });
    }

});