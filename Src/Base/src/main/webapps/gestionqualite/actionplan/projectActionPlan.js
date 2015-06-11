Ext.ux.CaqsProjectActionPlan = Ext.extend(Ext.Panel, {
    layout:             'anchor',
    style:              'margin-left: 5px;',
    autoScroll:         true,
    border:             false,
    editedRecordId:     undefined,
    commentWindow:      undefined,
    gridStore:          undefined,
    grid:               undefined,
    editedCommentRecord:undefined,
    readOnly:           false,
    apCommentWindow:    undefined,
    actionPlanComment:  undefined,
    gridPageSize:       6,
    kiviatUrl:          'ActionPlanKiviat.do?',
    nb:                 0,
    numberFormatConfig: '#.00',
    concurrentModification:false,
    priorityStore:      undefined,
    eaAPsGridStore:     undefined,
    eaAPsGridPanel:     undefined,
    wndHeight:          0,
    apToolbarButton:    undefined,
    kiviatWidth:        0,
    actionPlanUnitsCBStore: undefined,
    actionPlanUnitsCB:  undefined,
    totalCost:               0,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsProjectActionPlan.superclass.constructor.apply(this, arguments);
    },

    onComboChange: function(e) {
        Ext.Ajax.request({
            url: requestContextPath+'/ActionPlanManageSavesAction.do',
            params: {
                'id_elt':  	e.record.id,
                'priority':	e.value
            },
            scope:      this
        });
    },

    initCommentWindow: function() {
        this.commentWindow = new Ext.ux.CaqsCommentEditWindow({
            parentElement:  this,
            title:          getI18nResource("caqs.actionplan.commentTitle"),
            updateValue: function(val) {
                var newValue = val;
                var record = this.parentElement.gridStore.getById(this.parentElement.editedRecordId);
                record.data.comment = newValue;
                record.data.iconImgClass = (newValue=='')?'icon-comment-edit':'icon-has-comment';
                this.parentElement.grid.getView().refresh();
                Ext.Ajax.request({
                    url:    requestContextPath+'/ActionPlanManageSavesAction.do',
                    scope:  this.parentElement,
                    params: {
                        'id_elt':           record.id,
                        'criterionComment': newValue
                    }
                });
            }
        });
    },

    editComment: function(recordId) {
        if(this.commentWindow == undefined) {
            this.initCommentWindow();
        }
        if(!this.readOnly) {
            var record = this.gridStore.getById(recordId);
            this.editedRecordId = recordId;

            var value = record.data.comment;

            this.editedCommentRecord = record.data;
            this.commentWindow.setComment(value);
            this.commentWindow.show(this);
        }
    },

    initActionPlanCommentWindow: function() {
        this.apCommentWindow = new Ext.ux.CaqsCommentEditWindow({
            title:          getI18nResource("caqs.actionplan.apCommentTitle"),
            parentElement:  this,
            updateValue: function(val) {
                var newValue = val;
                this.parentElement.actionPlanComment = newValue;
                var tooltip = this.parentElement.getAPCommentTooltip();
                this.parentElement.apToolbarButton.setTooltip(tooltip);
                var iconPath = (newValue=='') ? requestContextPath+'/images/comment.gif':requestContextPath+'/images/hasComment.gif';
                this.parentElement.apToolbarButton.setIcon(iconPath);
                Ext.Ajax.request({
                    url: requestContextPath+'/ActionPlanManageSavesAction.do',
                    scope: this.parentElement,
                    params: {
                        'apComment': 	newValue
                    }
                });
            }
        });
    },

    editAPComment: function() {
        if(!this.readOnly) {
            if(this.apCommentWindow==undefined) {
                this.initActionPlanCommentWindow();
            }
            var value = this.actionPlanComment;
            this.apCommentWindow.setComment(value);
            this.apCommentWindow.show(this);
        }
    },

    renderElementType: function(value, p, record){
        p.attr = 'ext:qtip="'+record.data.telt+'"';
        return record.data.idTelt;
    },

    renderAgregation: function(value, p, record){
        p.attr = 'ext:qtip="'+getI18nResource('caqs.modeleditor.modelEdition.impression.agg.'+record.data.aggregation)+'"';
        return record.data.aggregation;
    },

    renderName: function(value, p, record){
        p.attr = 'ext:qtip="'+record.data.lib+'"';
        return record.data.lib;
    },

    reloadKiviat: function() {
        this.nb++;
        var kiviatDiv = this.findById('projectActionPlanColumn0');
        if(kiviatDiv != null) {
            var child = undefined;
            if(kiviatDiv.items != undefined) {
                child = kiviatDiv.getComponent('projectActionPlanImgKiviat');
                if(child != undefined) {
                    kiviatDiv.remove(child);
                }
            }
            var img = '<IMG id="projectActionPlankiviatImgSimule" src="';
            img += this.kiviatUrl+'&nb='+this.nb;
            img += "&width="+(this.kiviatWidth);
            img += "&height="+parseInt((this.kiviatWidth - 10)*0.5);

            img += '" />';
            child = new Ext.Panel({
                border: false,
                id: 'projectActionPlanImgKiviat',
                html:   img
            });
            kiviatDiv.add(child);
            kiviatDiv.doLayout();
        }
    },

    reloadImages: function() {
        this.reloadKiviat();
    },

    actionPlanPrint: function() {
        PopupCentrer(requestContextPath+"/ActionPlanPrint.do",600, 400,
            "menubar=no,statusbar=no,scrollbars=yes,resizable=yes");
    },

    renderPriority: function(val, cell, record, rowIndex, colIndex, store) {
        var retour = store.priorityValues.getById(val).get('lib');
        return retour;
    },

    renderTotalCost: function(val, cell, record, rowIndex, colIndex, store) {
        var retour = record.data.totalCost;
        return retour;
    },

    /************************************************
    * Section sur la liste des plans d'actions définis pour les eas
    ************************************************/
    showEAsAPsGridWnd: function() {
        this.wndHeight = 470;
        this.eaAPsGridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/ActionPlanProjectCostList.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 'eas',
                totalProperty: 'totalCount',
                id: 'id',
                fields: [
                'id', 'lib', 'idBline', 'libUsage', 'nbActionPlanUnit', 'actionPlanUnit',
                'libDialecte', 'nbCriterionsIncluded', 'totalCost', 'criterions'
                ]
            }),
            remoteSort: false
        });
        this.eaAPsGridStore.on('load', function(store, records, options) {
            this.totalCost = store.reader.jsonData.totalCost;
            this.actionPlanUnitsCBStore.load();
        }, this);
            
        var myTemplate = new Ext.XTemplate(
            '<tpl for=".">',
            '<TABLE>',
            '<TR>',
            '<TD style="align: left; vertical-align: middle;"><img id="projectActionPlanEA{id}{idBline}" src="'+requestContextPath+'/ActionPlanKiviat.do?id_elt={id}&baselineId={idBline}&width=320&height=200&showLegend=false&showTitle=false" /></TD>',
            '<TD style="padding-right:30px; vertical-align: middle;"><TABLE>',
            '<TR><TD><span style="font-weight:bold;">'+getI18nResource("caqs.criteres")+'</span></TD></TR>',
            '<tpl for="criterions">',
            '<TR><TD>{criterionLib} : {criterionCost}</TD></TR>',
            '</tpl>',
            '</TABLE></TD>',
            '</TR>',
            '</TABLE>',
            '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });

        var eaAPsColumnModel = new Ext.grid.ColumnModel([
            expander
            , {
                header: 	getI18nResource("caqs.projectActionPlan.eaList.eaName"),
                dataIndex: 	'lib',
                align:		'left',
                id:		'lib',
                sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.projectActionPlan.eaList.nbCrits"),
                dataIndex: 	'nbCriterionsIncluded',
                id:		'nbCriterionsIncluded',
                sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.projectActionPlan.eaList.model"),
                dataIndex: 	'libUsage',
                align:		'left',
                id:		'libUsage',
                sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.projectActionPlan.eaList.dialect"),
                dataIndex: 	'libDialecte',
                align:		'center',
                id:		'libDialecte',
                width:		100,
                sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.projectActionPlan.eaList.cost"),
                dataIndex: 	'totalCost',
                align:		'right',
                id:		'totalCost',
                width:		100,
                renderer:	this.renderTotalCost,
                sortable: 	true
            }
            ]);

        this.actionPlanUnitsCBStore = new Ext.ux.CaqsJsonStore({
            id:     'id_telt',
            url:    requestContextPath + '/RetrieveActionPlanUnitsList.do',
            fields: ['id_apu', 'lib_apu', 'nb_apu', 'display_field']
        });
        this.actionPlanUnitsCBStore.on('load', function(store, records, options) {
            if(records.length>0) {
                var firstRecord = records[0];
                this.actionPlanUnitsCB.setValue(firstRecord.data.id_apu);
                var nbapu = firstRecord.data['nb_apu'];
                var sum = new Number(this.totalCost * nbapu).numberFormat(this.numberFormatConfig) + ' ' + firstRecord.data['lib_apu'];
                var elt = document.getElementById('prjTotalCostWorkUnit');
                elt.innerHTML = sum;
                elt = document.getElementById('prjTotalCost');
                elt.innerHTML = new Number(this.totalCost).numberFormat(this.numberFormatConfig);
            }
        }, this);
        this.actionPlanUnitsCB = new Ext.form.ComboBox({
            name:       	'gestionqualitePrjActionplanIdAPUCB',
            id:         	'gestionqualitePrjActionplanIdAPUCB',
            store:      	this.actionPlanUnitsCBStore,
            displayField:	'display_field',
            valueField: 	'id_apu',
            hiddenName: 	'id_apu',
            width:		220,
            editable:		false,
            allowBlank: 	false,
            forceSelection:	true,
            triggerAction:	'all',
            autocomplete:	false,
            lazyRender:		true,
            listClass: 		'x-combo-list-small',
            tpl: new Ext.XTemplate('<tpl for="."><div class="x-combo-list-item" >{nb_apu} {lib_apu}</div></tpl>')
        });
        this.actionPlanUnitsCB.on('select', function(combo, record, index) {
            var nbapu = record.data['nb_apu'];
            var sum = new Number(this.totalCost * nbapu).numberFormat(this.numberFormatConfig) + ' ' + record.data['lib_apu'];
            var elt = document.getElementById('prjTotalCostWorkUnit');
            elt.innerHTML = sum;
         }, this);

        var totalCostPhrase1 = getI18nResource("caqs.actionplan.grid.totalCost")+
        ' <span id="prjTotalCost"></span> * ';
        var totalCostPhrase2 = ' = <span id="prjTotalCostWorkUnit"></span>';
        var gridBottomToolbar = new Ext.Toolbar({
            displayInfo: 	true,
            items:      [
            new Ext.Toolbar.TextItem({
                text: totalCostPhrase1
            })
            , this.actionPlanUnitsCB
            , new Ext.Toolbar.TextItem({
                text: totalCostPhrase2
            })
            ]
        });

        this.eaAPsGridPanel = new Ext.grid.GridPanel({
            height:		400,
            header:		false,
            store: 		this.eaAPsGridStore,
            cm: 		eaAPsColumnModel,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableColumnResize:	true,
            enableHdMenu:	false,
            trackMouseOver:	true,
            frame:		false,
            sm: 			new Ext.grid.RowSelectionModel({
                selectRow:Ext.emptyFn
            }),
            autoExpandColumn:   'lib',
            loadMask: 		true,
            viewConfig: {
                forceFit:       true,
                autoFill:	true
            },
            plugins:                [
            expander
            ],
            bbar:               gridBottomToolbar
        });
        var thisScope = this;
        this.eaAPsWnd = new Ext.Window({
            modal:          true,
            maximizable:    true,
            resizable:      false,
            minimizable:    false,
            title:          getI18nResource("caqs.projectActionPlan.eaList.title"),
            width:          550,
            height:         this.wndHeight,
            shadow:         false,
            plain:          true,
            x:              20,
            y:              20,
            layout:         'fit',
            items: [
            this.eaAPsGridPanel
            ],
            buttons: [
            {
                text: getI18nResource("caqs.finir"),
                handler: function(){
                    this.eaAPsWnd.close();
                },
                scope: thisScope
            }
            ]
        });
        this.eaAPsWnd.show(this);
        // trigger the data store load
        this.eaAPsGridStore.load({
            scope:  this
        });
    },

    refreshGrid: function(obj) {
        obj.setHeight(this.wndHeight);
        this.eaAPsGridPanel.getView().refresh();
    },

    showConcurrentModification: function() {
        Ext.Msg.show({
            title:	getI18nResource("caqs.actionplan.concurrentmodification.title"),
            msg: 	getI18nResource("caqs.actionplan.concurrentmodification.msg"),
            buttons: 	Ext.Msg.OK,
            icon: 	Ext.MessageBox.INFO
        });
    },

    getAPCommentTooltip: function() {
        return '<B>'+ getI18nResource("caqs.actionplan.apCommentTitle") + '</B><BR />'+((this.actionPlanComment==undefined)?'':this.actionPlanComment);
    },
    
    autoFillActionPlanResponse: function(response) {
        if(response.responseText!='') {
            var json = Ext.util.JSON.decode(response.responseText);
            this.gridStore.load({
                params: {
                    start: 0,
                    limit: this.gridPageSize
                }
            });
            this.reloadImages();
        }
    },

    autoFillActionPlan: function() {
        Ext.Ajax.request({
            url: requestContextPath+'/ActionPlanAutoFill.do',
            scope: this,
            success: this.autoFillActionPlanResponse
        });
    },

    renderReadOnlyCheckBox: function(val, cell, record, rowIndex, colIndex, store) {
        var retour = '&nbsp;';
        if(val==true) {
            retour = '<img width=16 height=16 src="'+requestContextPath+'/images/tick.gif" />';
        }
        return retour;
    },

    initComponent : function(){
        Ext.ux.CaqsProjectActionPlan.superclass.initComponent.call(this);

        this.priorityStore = new Ext.ux.CaqsJsonStore({
            id:     'idPriority',
            fields: ['idPriority', 'lib'],
            data :  []
        });

        this.gridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/ActionPlanGoalsList.do'
            }),
            priorityValues: this.priorityStore,
            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 'goals',
                totalProperty: 'totalCount',
                actionPlanComment: 'actionPlanComment',
                id: 'id',
                fields: [
                'id', 'lib', 'desc', 'compl', 'tendance', 
                'note', 'indiceGravite', 
                'selected', 'priority', 'comment', 'iconImgClass'
                ]
            }),
            remoteSort: true
        });
        this.gridStore.setDefaultSort('indiceGravite', 'asc');
        this.gridStore.on('load', function(store, records, options) {
            this.actionPlanComment = store.reader.jsonData.actionPlanComment;
            var iconPath = (this.actionPlanComment=='' || this.actionPlanComment==undefined) ? requestContextPath+'/images/comment.gif':requestContextPath+'/images/hasComment.gif';
            this.apToolbarButton.setIcon(iconPath);
            this.apToolbarButton.setTooltip(this.getAPCommentTooltip());
        }, this);

        var myTemplate = new Ext.XTemplate(
            '<p><b>'+getI18nResource("caqs.facteursynthese.desc")+':</b> {desc}</p>',
            '<tpl if="compl != \'\'">',
            '<br /><p><b>'+getI18nResource("caqs.facteursynthese.compl")+':</b> {compl}</p>',
            '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });

        var checkColumn = new Ext.grid.ActionPlanCheckColumn({
            header: 	"",
            dataIndex:	'selected',
            id:         'projectAPCheckColumnId',
            fixed:	true,
            sortable:	false,
            width: 	30,
            thisScope:  this
        });

        var editCommentAction = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:[
            {
                qtipIndex: 'comment',
                iconIndex: 'iconImgClass'
            }
            ]
        });

        editCommentAction.on({
            scope:  this,
            action: function(grid, record, action, row, col) {
                this.editComment(record.data.id);
            }
        }, this);

        var combo = new Ext.form.ComboBox({
            name:       	'projectAPIdPriorityCB',
            id:         	'projectAPIdPriorityCB',
            store:      	this.priorityStore,
            displayField:	'lib',
            valueField: 	'idPriority',
            hiddenName: 	'idPriority',
            width:			220,
            editable:		false,
            allowBlank: 	false,
            forceSelection:	true,
            triggerAction:	'all',
            autocomplete:	false,
            mode:       	'local',
            lazyRender:		true,
            listClass: 		'x-combo-list-small'
        });
        combo.on('select', function(field, newValue, oldValue) {
            this.grid.stopEditing();
        }, this);

        var cm = new Ext.grid.ColumnModel([
            expander,
            {
                header:     "S",
                tooltip:    getI18nResource("caqs.actionplan.severity"),
                dataIndex:  'indiceGravite',
                width:      30,
                align:      'center',
                sortable:   true
            }
            ,{
                header:     getI18nResource("caqs.critere.nom"),
                dataIndex:  'lib',
                id:         'libelle',
                width:      340,
                renderer:   this.renderName,
                sortable:   true
            }
            ,{
                header:     getI18nResource("caqs.actionplan.note"),
                dataIndex:  'note',
                align:      'right',
                sortable:   true,
                width:      40
            },{
                header:     getI18nResource("caqs.actionplan.priority"),
                tooltip:    getI18nResource("caqs.actionplan.priority.tooltip"),
                width:      80,
                align:      'center',
                dataIndex:  'priority',
                id:         'projectactionplanpriorityCBColumn',
                renderer:   this.renderPriority,
                editor:     (this.readOnly)?undefined:combo,
                sortable:   true
            }
            , editCommentAction
            , checkColumn
            ,{
                header: 	'',
                tooltip:	'',
                width: 		220,
                align:		'center',
                dataIndex:	'selected',
                renderer:	this.renderReadOnlyCheckBox,
                id:             'projectactionplanreadonlycheckboxCBColumn',
                sortable: 	true
            }
            ]);

        var printButton = new Ext.Button({
            text:   	getI18nResource("caqs.actionplan.print"),
            tooltip:	getI18nResource("caqs.actionplan.print.tooltip"),
            cls:	"x-btn-text-icon",
            icon:	requestContextPath+'/images/printer.gif',
            handler:	this.actionPlanPrint,
            scope:      this
        });

        var easAPsListButton = new Ext.Button({
            text:	getI18nResource("caqs.projectActionPlan.eaList.title"),
            tooltip:	getI18nResource("caqs.projectActionPlan.eaList.title.tooltip"),
            cls:	"x-btn-text-icon",
            icon:	requestContextPath+'/images/application_view_list.gif',
            handler:	this.showEAsAPsGridWnd,
            scope:      this
        });

        this.apToolbarButton = new Ext.Button({
            text:	'',
            tooltip:	this.getAPCommentTooltip(),
            cls:	"x-btn-icon",
            icon:	requestContextPath+'/images/comment.gif',
            handler:	this.editAPComment,
            scope:      this
        });

        this.grid = new Ext.grid.EditorGridPanel({
            anchor:             '95%',
            //autoWidth:          true,
            height:		255,
            header:		false,
            store: 		this.gridStore,
            cm: 		cm,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableColumnResize:	true,
            enableHdMenu:	false,
            clicksToEdit:	1,
            trackMouseOver:	true,
            frame:		true,
            sm: 		new Ext.grid.RowSelectionModel({
                selectRow:Ext.emptyFn
            }),
            style:              'margin-top: 5px;',
            loadMask: 		true,
            autoExpandColumn:   'libelle',
            tbar: new Ext.Toolbar({
                displayInfo: 	true,
                items: [
                printButton,
                '-',
                easAPsListButton
                , this.apToolbarButton
                ]
            }),
            viewConfig: {
                forceFit: true
            },
            bbar: new Ext.PagingToolbar({
                pageSize: 	this.gridPageSize,
                store: 		this.gridStore,
                displayInfo: 	true,
                displayMsg: 	'{0} - {1} / {2}'
            }),
            plugins: [
            expander
            ,checkColumn
            ,editCommentAction
            ]
        });

        this.grid.on('afteredit', this.onComboChange, this);

        var charts = new Ext.Panel({
            anchor:         '95%',
            layout:         'column',
            border:         false,
            autoScroll:     true,
            layoutConfig:   {
                scrollOffset:25
            },
            items:      [
            {//colonne 1
                columnWidth:	.5,
                id:		'projectActionPlanColumn0',
                autoScroll:     true,
                border:		false,
                listeners:      {
                    'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                        this.kiviatWidth = adjWidth;
                        this.reloadKiviat();
                    },
                    scope:  this
                }
            }
            ]
        });

        this.add(charts);
        this.add(this.grid);

        this.grid.getView().on('refresh', function() {
            putTooltips();
        }, this);
    },

    refreshPriorities: function(priorities) {
        this.priorityStore.loadData(priorities, false);
    },

    refreshState: function(json) {
        var colIndex = this.grid.getColumnModel().getIndexById('projectAPCheckColumnId');
        this.grid.getColumnModel().setHidden(colIndex, json.actionPlanreadOnly);
        colIndex = this.grid.getColumnModel().getIndexById('projectactionplanreadonlycheckboxCBColumn');
        this.grid.getColumnModel().setHidden(colIndex, !json.actionPlanreadOnly);
        colIndex = this.grid.getColumnModel().getIndexById('projectactionplanpriorityCBColumn');
        this.grid.getColumnModel().setEditable(colIndex, !json.actionPlanreadOnly);
        this.readOnly = json.actionPlanreadOnly;

        this.refreshPriorities(json.priorities);
    },

    refresh: function(json) {
        // trigger the data store load
        this.gridStore.load({
            params:{
                start:0,
                limit: this.gridPageSize
            }
        });
        this.reloadImages();

        if(this.concurrentModification) {
            this.showConcurrentModification();
        }
    }
});
