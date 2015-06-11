Ext.ux.CaqsEAActionPlan = Ext.extend(Ext.Panel, {
    hideMode:           'offsets',
    autoHeight:         true,
    autoWidth:          true,
    layout:             'anchor',
    style:              'margin-left: 5px;',
    autoScroll:         true,
    //autoHeight:         true,
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
    impactedEltsGridPageSize: 10,
    pieChartUrl:        requestContextPath + '/ActionPlanPieChart.do?',
    kiviatUrl:          'ActionPlanKiviat.do?',
    nb:                 0,
    numberFormatConfig: '#.00',
    concurrentModification:false,
    priorityStore:      undefined,
    impactedEltsWnd:    undefined,
    impactedEltsGridStore: undefined,
    impactedEltsGridPanel: undefined,
    wndHeight:              0,
    apToolbarButton:        undefined,
    kiviatWidth:            0,
    pieChartWidth:          0,
    autoFillBtn:            undefined,
    gridBottomToolbar:      undefined,
    actionPlanUnitNb:       undefined,
    cost:                   undefined,
    priorityEditorCB:       undefined,
    tbFactorMenu:           undefined,
    actionPlanUnitsCBStore: undefined,
    actionPlanUnitsCB:      undefined,
    selectedAPULib:         undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsEAActionPlan.superclass.constructor.apply(this, arguments);
    },

    onComboChange: function(e) {
        Ext.Ajax.request({
            url: requestContextPath+'/ActionPlanManageSavesAction.do',
            params: {
                'id_elt':  	e.record.id,
                'priority':		e.value
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
        if(!this.readOnly) {
            if(this.commentWindow == undefined) {
                this.initCommentWindow();
            }
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
            this.apCommentWindow.setComment(this.actionPlanComment);
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
        var tooltip = Ext.util.Format.htmlEncode(record.data.lib);
        p.attr = 'ext:qtip="'+tooltip+'"';
        return record.data.lib;
    },

    simulatedPieChartRenderer: function(response, request) {
        if(response.responseText!='') {
            var json = Ext.util.JSON.decode(response.responseText);
            var vals = json.datas.split('!!!!');
            var img = document.getElementById('pieChartImgSimule');
            if(img!=undefined) {
                img.setAttribute("usemap", '#'+vals[0]);
                var src = './displaychart?width=';
                src += this.pieChartWidth+ '&filename='+vals[0];
                img.src = src;

            }
            var map = document.getElementById('pieChartImgSimuleMap');
            if(img!=undefined) {
                var newspan = document.createElement("span");
                newspan.setAttribute("id","pieChartImgSimuleMapSpan");
                newspan.innerHTML = vals[1];
                var oldspan = document.getElementById("pieChartImgSimuleMapSpan");
                if(oldspan!=undefined) {
                    map.removeChild(oldspan);
                }
                map.appendChild(newspan);
            }
        }
    },

    reloadKiviat: function() {
        this.nb++;
        var kiviatDiv = this.findById('eaactionplancolumn0');
        if(kiviatDiv != null) {
            var child = undefined;
            if(kiviatDiv.items != undefined) {
                child = kiviatDiv.getComponent('apImgKiviat');
                if(child != undefined) {
                    kiviatDiv.remove(child);
                }
            }
            var img = '<IMG id="kiviatImgSimule" src="';
            img += this.kiviatUrl+'&nb='+this.nb;
            img += "&width="+(this.kiviatWidth);
            img += "&height="+parseInt((this.kiviatWidth - 10)*0.5);

            img += '" />';
            child = new Ext.Panel({
                border: false,
                id: 'apImgKiviat',
                html:   img
            });
            kiviatDiv.add(child);
            kiviatDiv.doLayout();
        }
    },

    reloadPieChart: function() {
        this.nb++;
        var piecharturl = this.pieChartUrl + 'width='+this.pieChartWidth;
        piecharturl += "&height="+parseInt(this.pieChartWidth*0.5);
        piecharturl += '&nb='+this.nb;
        Ext.Ajax.request({
            url:        piecharturl,
            success:    this.simulatedPieChartRenderer,
            scope:      this
        });
    },

    reloadImages: function() {
        this.reloadKiviat();
        this.reloadPieChart();
    },

    renderRepartition: function(val, p, record) {
        var result = '&nbsp;';

        if(record.data.pct1 > 0) {
            addTooltip(record.data.id+'0', '',
                '<IMG src="'+requestContextPath+'/images/red.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.tooltip1);
            result += '<IMG id="'+record.data.id+'0" src="'+requestContextPath+'/images/red.gif" height="15" border="0" width="'+1.7*record.data.pct1+'" />';
        }
        if(record.data.pct2 > 0) {
            addTooltip(record.data.id+'1', '',
                '<IMG src="'+requestContextPath+'/images/orange.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.tooltip2);
            result += '<IMG id="'+record.data.id+'1" src="'+requestContextPath+'/images/orange.gif" height="15" border="0" width="'+1.7*record.data.pct2+'" />';
        }
        if(record.data.pct3 > 0) {
            addTooltip(record.data.id+'2', '',
                '<IMG src="'+requestContextPath+'/images/darkgreen.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.tooltip3);
            result += '<IMG id="'+record.data.id+'2" src="'+requestContextPath+'/images/darkgreen.gif" height="15" border="0" width="'+1.7*record.data.pct3+'" />';
        }
        if(record.data.pct4 > 0) {
            addTooltip(record.data.id+'3', '',
                '<IMG src="'+requestContextPath+'/images/green.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.tooltip4);
            result += '<IMG id="'+record.data.id+'3" src="'+requestContextPath+'/images/green.gif" height="15" border="0" width="'+1.7*record.data.pct4+'" />';
        }

        return result;
    },

    filterByGoal: function(checkbox, checked) {
        this.gridStore.load({
            params:{
                start:		0,
                limit:		this.gridPageSize,
                factor:		checkbox.id,
                checked:	checked
            }
        });
    },

    actionPlanPrint: function() {
        PopupCentrer(requestContextPath+"/ActionPlanPrint.do",600, 400,
            "menubar=no,statusbar=no,scrollbars=yes,resizable=yes");
    },

    /************************************************
     * Section sur la liste des elements impactes
     ************************************************/
    showImpactedGridWnd: function() {
        this.wndHeight = 470;
        if(this.impactedEltsWnd == undefined) {
            this.impactedEltsGridStore = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy({
                    url: requestContextPath+'/ActionPlanImpactedEltsList.do'
                }),

                // create reader that reads the Topic records
                reader: new Ext.data.JsonReader({
                    root: 'elements',
                    totalProperty: 'totalCount',
                    id: 'idElt',
                    fields: [
                    'idElt', 'descElt', 'criterions', 'idTelt', 'telt'
                    ]
                }),
                remoteSort: true
            });

            var impactedElementsColumnModel = new Ext.grid.ColumnModel([
            {
                header: 	getI18nResource("caqs.critere.nom"),
                dataIndex: 	'descElt',
                align:		'left',
                id:		'desc',
                sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.actionplan.impactedelementslist.criterionTitle"),
                dataIndex: 	'criterions',
                id:		'criterions',
                sortable: 	false
            }
            ,{
                header: 	"T",
                tooltip:	getI18nResource("caqs.critere.typeElt"),
                dataIndex: 	'idTelt',
                align:		'center',
                id:		'idTelt',
                renderer:	this.renderElementType,
                width:		30,
                sortable: 	true
            }
            ]);

            this.impactedEltsGridPanel = new Ext.grid.GridPanel({
                height:			400,
                header:			false,
                store: 			this.impactedEltsGridStore,
                cm: 			impactedElementsColumnModel,
                enableColumnHide : 	false,
                enableColumnMove : 	false,
                enableColumnResize:	true,
                enableHdMenu:		false,
                trackMouseOver:		true,
                frame:			false,
                sm: 			new Ext.grid.RowSelectionModel({
                    selectRow:Ext.emptyFn
                }),
                autoExpandColumn:	'criterions',
                loadMask: 		true,
                viewConfig: {
                    autoFill:		true
                },
                bbar: new Ext.PagingToolbar({
                    pageSize: 		this.impactedEltsGridPageSize,
                    store: 		this.impactedEltsGridStore,
                    displayInfo: 	true,
                    displayMsg: 	'{0} - {1} / {2}'
                })
            });
            var thisScope = this;
            this.impactedEltsWnd = new Ext.Window({
                modal: 		true,
                maximizable: 	true,
                resizable:	false,
                minimizable: 	false,
                title: 		getI18nResource("caqs.actionplan.impactedelementslist.title"),
                width:		550,
                height:		this.wndHeight,
                shadow:		false,
                plain:		true,
                x:		20,
                y:		20,
                layout:		'anchor',
                items: [
                this.impactedEltsGridPanel
                ],
                buttons: [
                {
                    text: getI18nResource("caqs.finir"),
                    handler: function(){
                        this.impactedEltsWnd.hide();
                    },
                    scope: thisScope
                }
                ]
            });
            this.impactedEltsWnd.on('beforeclose', function(wnd) {
                this.impactedEltsWnd.hide();
                return false;
            }, this);
            this.impactedEltsWnd.on('maximize', this.refreshGrid, this);
            this.impactedEltsWnd.on('restore', this.refreshGrid, this);
        }
        this.impactedEltsWnd.show(this);
        // trigger the data store load
        this.impactedEltsGridStore.load({
            params:{
                start:0,
                limit: this.impactedEltsGridPageSize
            },
            scope:  this
        });
    },


    refreshGrid: function(obj) {
        obj.setHeight(this.wndHeight);
        this.impactedEltsGridPanel.getView().refresh();
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
            this.cost = json.newCost;
            var costSpan = document.getElementById('eaTotalCost');
            costSpan.innerHTML = new Number(this.cost).numberFormat(this.numberFormatConfig);
            var costWorkUnitSpan = document.getElementById('eaTotalCostWorkUnit');
            costWorkUnitSpan.innerHTML = new Number(this.cost * this.actionPlanUnitNb).numberFormat(this.numberFormatConfig);
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
    
    renderPriority: function(val, cell, record, rowIndex, colIndex, store) {
        var retour = store.priorityValues.getById(val).get('lib');
        return retour;
    },

    renderReadOnlyCheckBox: function(val, cell, record, rowIndex, colIndex, store) {
        var retour = '&nbsp;';
        if(val==true) {
            retour = '<img width=16 height=16 src="'+requestContextPath+'/images/tick.gif" />';
        }
        return retour;
    },

    initComponent : function(){
        Ext.ux.CaqsEAActionPlan.superclass.initComponent.call(this);

        this.priorityStore = new Ext.ux.CaqsJsonStore({
            id:     'idPriority',
            fields: ['idPriority', 'lib'],
            data :  []
        });

        this.gridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/ActionPlanCriterionList.do'
            }),
            priorityValues: this.priorityStore,
            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 'criteres',
                totalProperty: 'totalCount',
                actionPlanComment: 'actionPlanComment',
                id: 'id',
                fields: [
                'id', 'lib', 'desc', 'compl', 'tendance', 'telt',
                'note', 'nbElts', 'indiceGravite', 'aggregation',
                'tooltip1', 'tooltip2', 'tooltip3', 'tooltip4',
                'pct1', 'pct2', 'pct3', 'pct4', 'selected',
                'repartition', 'cost', 'idTelt', 'priority', 'comment',
                'elementMaster', 'elementBeanId', 'iconImgClass',
                'factorsList'
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
            '</tpl>',
            '<tpl if="factorsList != \'\'">',
            '<br /><p><b>'+getI18nResource("caqs.actionplan.criterion.factorslist")+':</b> {factorsList}</p>',
            '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });

        var checkColumn = new Ext.grid.ActionPlanCheckColumn({
            header: 	"",
            dataIndex:	'selected',
            id:         'eaactionplancheckColumnId',
            fixed:	true,
            sortable:	false,
            width: 	30,
            thisScope:  this,
            totalCostId: 'eaTotalCost',
            totalWostWorkUnitId: 'eaTotalCostWorkUnit'
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

        this.priorityEditorCB = new Ext.form.ComboBox({
            name:       	'idPriorityCB',
            id:         	'idPriorityCB',
            store:      	this.priorityStore,
            displayField:	'lib',
            valueField: 	'idPriority',
            hiddenName: 	'idPriority',
            width:		220,
            editable:		false,
            allowBlank: 	false,
            forceSelection:	true,
            triggerAction:	'all',
            autocomplete:	false,
            mode:       	'local',
            lazyRender:		true,
            listClass: 		'x-combo-list-small'
        });
        this.priorityEditorCB.on('select', function(field, newValue, oldValue) {
            this.grid.stopEditing();
        }, this);

        var cm = new Ext.grid.ColumnModel([
            expander,
            {
                header: 	"S",
                tooltip:	getI18nResource("caqs.actionplan.severity"),
                fixed:		true,
                dataIndex: 	'indiceGravite',
                width: 		30,
                align:		'center',
                sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.critere.nom"),
                dataIndex: 	'lib',
                id:             'libelle',
                width: 		340,
                renderer:	this.renderName,
                sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.actionplan.note"),
                dataIndex:	'note',
                align:		'right',
                sortable: 	true,
                width: 		40
            },{
                header: 	"A",
                tooltip:	getI18nResource("caqs.actionplan.agregation"),
                width: 		40,
                sortable:	true,
                renderer:	this.renderAgregation,
                dataIndex:	'aggregation'
            },{
                header: 	"T",
                tooltip:	getI18nResource("caqs.actionplan.elementType"),
                align:		'center',
                width: 		60,
                sortable:	true,
                renderer:	this.renderElementType,
                dataIndex:	'idTelt'
            },{
                header: 	getI18nResource("caqs.actionplan.nbElt"),
                tooltip:	getI18nResource("caqs.actionplan.nbElt.tooltip"),
                width: 		40,
                sortable:	true,
                align:		'right',
                dataIndex:	'nbElts'
            },{
                header: 	getI18nResource("caqs.actionplan.repartition")+"&nbsp;<img id=\"repartitionHelp\" src=\""
                +requestContextPath+"/images/help.gif\" />",
                width: 		180,
                align:		'center',
                dataIndex:	'repartition',
                sortable: 	true,
                renderer: 	this.renderRepartition
            },{
                header: 	getI18nResource("caqs.actionplan.cost"),
                width: 		60,
                align:		'right',
                dataIndex:	'cost',
                sortable: 	true
            },{
                header: 	getI18nResource("caqs.actionplan.priority"),
                tooltip:	getI18nResource("caqs.actionplan.priority.tooltip"),
                width: 		220,
                align:		'center',
                dataIndex:	'priority',
                renderer:	this.renderPriority,
                id:             'eaactionplanpriorityCBColumn',
                editor:         this.priorityEditorCB,
                sortable: 	true
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
                id:             'eaactionplanreadonlycheckboxCBColumn',
                sortable: 	true
            }
            ]);

        this.tbFactorMenu = new Ext.menu.Menu({
            allowOtherMenus : false,
            items: []
        });
        var printButton = new Ext.Button({
            text:		getI18nResource("caqs.actionplan.print"),
            tooltip:	getI18nResource("caqs.actionplan.print.tooltip"),
            cls:		"x-btn-text-icon",
            icon:		requestContextPath+'/images/printer.gif',
            handler:	this.actionPlanPrint,
            scope:      this
        });

        var impactedElementsListButton = new Ext.Button({
            text:		getI18nResource("caqs.actionplan.impactedelementslist"),
            tooltip:	getI18nResource("caqs.actionplan.impactedelementslist.tooltip"),
            cls:		"x-btn-text-icon",
            icon:		requestContextPath+'/images/application_view_list.gif',
            handler:	this.showImpactedGridWnd,
            scope:      this
        });

        this.autoFillBtn = new Ext.Button({
            text:		getI18nResource("caqs.actionplan.autofillBtn"),
            tooltip:	getI18nResource("caqs.actionplan.autofillBtn.tooltip"),
            cls:		"x-btn-text-icon",
            icon:		requestContextPath+'/images/wand.gif',
            handler:	this.autoFillActionPlan,
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
                var sum = new Number(this.cost * nbapu).numberFormat(this.numberFormatConfig) + ' ' + firstRecord.data['lib_apu'];
                var elt = document.getElementById('eaTotalCostWorkUnit');
                elt.innerHTML = sum;
                this.selectedAPULib = firstRecord.data['lib_apu'];
            }
        }, this);
        this.actionPlanUnitsCB = new Ext.form.ComboBox({
            name:       	'gestionqualiteActionplanIdAPUCB',
            id:         	'gestionqualiteActionplanIdAPUCB',
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
            var sum = new Number(this.cost * nbapu).numberFormat(this.numberFormatConfig) + ' ' + record.data['lib_apu'];
            var elt = document.getElementById('eaTotalCostWorkUnit');
            elt.innerHTML = sum;
            this.selectedAPULib = record.data['lib_apu'];
         }, this);

        var totalCostPhrase1 = getI18nResource("caqs.actionplan.grid.totalCost")+
        ' <span id="eaTotalCost"></span> * ';
        var totalCostPhrase2 = ' = <span id="eaTotalCostWorkUnit"></span>';
        this.gridBottomToolbar = new Ext.PagingToolbar({
            pageSize: 	this.gridPageSize,
            store: 	this.gridStore,
            displayInfo:true,
            displayMsg: '{0} - {1} / {2}',
            items:      [
            '-'
            , new Ext.Toolbar.TextItem({
                text: totalCostPhrase1
            })
            , this.actionPlanUnitsCB
            , new Ext.Toolbar.TextItem({
                text: totalCostPhrase2
            })
            ]
        });

        this.grid = new Ext.grid.EditorGridPanel({
            anchor:             '95%',
            height:		200,
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
            viewConfig: {
                forceFit: true
            },
            tbar: new Ext.Toolbar({
                displayInfo: 	true,
                items: [
                {
                    text: 	getI18nResource("caqs.actionplan.filterByGoal"),
                    cls:	"x-btn-text-icon",
                    icon:	requestContextPath+'/images/menu-show.gif',
                    menu:      	this.tbFactorMenu
                },
                printButton,
                '-',
                impactedElementsListButton
                , this.autoFillBtn
                , this.apToolbarButton
                ]
            }),
            bbar: this.gridBottomToolbar,
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
                id:		'eaactionplancolumn0',
                autoScroll:     true,
                border:		false,
                listeners:      {
                    'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                        this.kiviatWidth = adjWidth;
                    },
                    scope:  this
                }
            },{//colonne 1
                id:		'eaactionplancolumn1',
                columnWidth:	.5,
                border:		false,
                items:  [
                {
                    border:     false,
                    autoHeight: true,
                    html:       '<div id="pieChartImgSimuleMap"></div><IMG id="pieChartImgSimule" />'
                }
                ],
                listeners:      {
                    'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                        this.pieChartWidth = adjWidth;
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
            this.reloadPieChart();
            this.reloadKiviat();
        }, this);
    },

    refresh: function() {
        Ext.Ajax.request({
            url: requestContextPath+'/RetrieveActionPlanGlobalProperties.do',
            scope:      this,
            success:    function(response) {
                if(response!=null && response.responseText!=null) {
                    var json = Ext.util.JSON.decode(response.responseText);
                    if(json) {
                        var elt = document.getElementById('eaTotalCost');
                        elt.innerHTML = new Number(json.totalCost).numberFormat(this.numberFormatConfig);
                        this.cost = json.totalCost;
                        
                        var colIndex = this.grid.getColumnModel().getIndexById('eaactionplancheckColumnId');
                        this.grid.getColumnModel().setHidden(colIndex, json.readOnly);
                        colIndex = this.grid.getColumnModel().getIndexById('eaactionplanreadonlycheckboxCBColumn');
                        this.grid.getColumnModel().setHidden(colIndex, !json.readOnly);
                        colIndex = this.grid.getColumnModel().getIndexById('eaactionplanpriorityCBColumn');
                        this.grid.getColumnModel().setEditable(colIndex, !json.readOnly);
                        this.readOnly = json.readOnly;

                        this.priorityStore.loadData(json.priorities, false);

                        for(i=0; i<json.factors.length; i++) {
                            var f = json.factors[i];
                            var it = new Ext.menu.CheckItem({
                                    id:		f.id,
                                    style:	true,
                                    text :	f.text,
                                    checked:	true
                            });
                            this.tbFactorMenu.add(it);
                            it.on('checkchange', this.filterByGoal, this);
                            it.on('check', this.filterByGoal, this);
                        }
                        this.autoFillBtn.setVisible(!json.readOnly);
                    }
                }
                this.gridStore.load({
                    params:{
                        start:  0,
                        limit:  this.gridPageSize,
                        readOnly:this.readOnly
                    }
                });
                this.actionPlanUnitsCBStore.load();
            }
        });
        Caqs.Portal.setCurrentScreen('action_plan');
    },

    onRender : function(cmpt){
        Ext.ux.CaqsEAActionPlan.superclass.onRender.call(this, cmpt);
        var tooltipRepartition = getI18nResource("caqs.actionplan.repartition.legende")+"<BR />";
        tooltipRepartition += '<IMG src="'+requestContextPath+'/images/red.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource("caqs.actionplan.repartitionhelp.notede", "1")+'<BR />';
        tooltipRepartition += '<IMG src="'+requestContextPath+'/images/orange.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource("caqs.actionplan.repartitionhelp.notede", "2")+'<BR />';
        tooltipRepartition += '<IMG src="'+requestContextPath+'/images/darkgreen.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource("caqs.actionplan.repartitionhelp.notede", "3")+'<BR />';
        tooltipRepartition += '<IMG src="'+requestContextPath+'/images/green.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource("caqs.actionplan.repartitionhelp.notede", "4");
        addTooltip('repartitionHelp',
            "<img src=\""+requestContextPath+"/images/help.gif\" />&nbsp;"+getI18nResource("caqs.actionplan.repartition.tooltip"),
            tooltipRepartition, 0, 450);

        if(this.concurrentModification) {
            this.showConcurrentModification();
        }
    }
});
