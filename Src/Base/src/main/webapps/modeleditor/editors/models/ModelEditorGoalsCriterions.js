Ext.ux.CaqsModelEditorGoalsCriterionsPanel = function(config) {
    Ext.ux.CaqsModelEditorGoalsCriterionsPanel.superclass.constructor.call(this, config);
};


Ext.extend(Ext.ux.CaqsModelEditorGoalsCriterionsPanel, Ext.Panel, {
    modelId:                undefined,
    goalsPanel:             undefined,
    displayedGoals:         undefined,
    layout:                 'anchor',
    addGoalBtn:             undefined,
    addGoalWnd:             undefined,
    editFormulasWnd:        undefined,
    editAggregationsWnd:    undefined,
    addCriterionWnd:        undefined,
    criterionsGrid:         undefined,
    criterionsGridStore:    undefined,
    elementTypeCBStore:     undefined,

    setId: function(id) {
        this.modelId = id;
        this.refresh();
    },

    renderTypeElement: function(val, cell, record, rowIndex, colIndex, store) {
        var retour = store.elementTypesStore.getById(val);
        if(retour) {
            retour = retour.data.lib_telt;
        }
        return retour;
    },

    getActiveGoalId: function() {
        return this.goalsPanel.getActiveTab().id;
    },

    renderCriterionName: function(v, p, record) {
        var retour = record.data.lib;
        if(!record.data.formulasPresent) {
            p.attr = 'style="color: red;" ext:qtip="'+getI18nResource('caqs.modeleditor.modelEdition.goalsCriterions.manageFormulas.noFormula')+'"';
        }
        if(!record.data.agregationsPresent) {
            p.attr = 'style="color: red;" ext:qtip="'+getI18nResource('caqs.modeleditor.modelEdition.goalsCriterions.manageAgregations.noAgregation')+'"';
        }

        return retour;
    },

    deleteCriterion: function(idCrit) {
        Ext.MessageBox.confirm(
            getI18nResource('caqs.confirmDelete'),
            getI18nResource('caqs.modeleditor.modelEdition.criterionsDelete.confirm'),
            function(btn) {
                if(btn == 'yes') {
                    Ext.Ajax.request({
                        url:	requestContextPath + '/DeleteCriterionGoalModelAssociation.do',
                        success:	this.reloadGrid,
                        params:     {
                            idCrit:     idCrit,
                            modelId:    this.modelId,
                            goalId:     this.goalsPanel.getActiveTab().id
                        },
                        scope:      this
                    });
                }
            }, this);
    },

    editFormula: function(idCrit, lib) {
        if(this.editFormulasWnd==null) {
            this.editFormulasWnd = new Ext.ux.CaqsModelEditorEditFormulasWindow({
                parentPanel:        this
            });
        }
        this.editFormulasWnd.setId(this.modelId, idCrit, lib);
        this.editFormulasWnd.show(this);
    },

    initComponent : function(){
        this.elementTypeCBStore = new Ext.ux.CaqsJsonStore({
            id:     'id_telt',
            url:    requestContextPath + '/ElementTypeList.do',
            fields: ['id_telt', 'lib_telt']
        });
        var teltEditor = new Ext.form.ComboBox({
            name:       	'idTELTCB',
            id:         	'idTELTCB',
            store:      	this.elementTypeCBStore,
            displayField:	'lib_telt',
            valueField: 	'id_telt',
            hiddenName: 	'id_telt',
            width:		220,
            editable:		false,
            allowBlank: 	false,
            forceSelection:	true,
            triggerAction:	'all',
            autocomplete:	false,
            lazyRender:		true,
            listClass: 		'x-combo-list-small'
        });
        teltEditor.on('select', function(field, newValue, oldValue) {
            this.criterionsGrid.stopEditing();
        }, this);
        var myTemplate = new Ext.XTemplate(
               '<p><b>'+getI18nResource('caqs.modeleditor.grid.desc')+':</b> {desc}</p>',
                '<tpl if="compl != \'\'">',
                '<p><b>'+getI18nResource('caqs.modeleditor.grid.compl')+':</b> {compl}</p>',
                '</tpl>',
                '<tpl if="formulasPresent">',
                '<p><b>'+getI18nResource('caqs.modeleditor.modelEdition.formulaExpand.title')+':</b></p>',
                '<tpl for="formulaArray">',
                '<tpl if="formula != \'true\'">',
                '<p>'+getI18nResource('caqs.modeleditor.modelEdition.formula.mark', '{score}', '{formula}')+'</p>',
                '</tpl>',
                '<tpl if="formula == \'true\'">',
                '<p>'+getI18nResource('caqs.modeleditor.modelEdition.formula.markAlwaysTrue', '{score}')+'</p>',
                '</tpl>',
                '</tpl>',
                '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            lazyRender:true,
            enableCaching:false,
            tpl : myTemplate
        });

        var action = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:[
                {
                    qtip:      getI18nResource('caqs.modeleditor.modelEdition.goalsCriterions.manageAgregation'),
                    iconCls:   'icon-criterionagregation'
                }
                , {
                    qtip:      getI18nResource('caqs.modeleditor.modelEdition.goalsCriterions.manageFormulas'),
                    iconCls:   'icon-criterionformula'
                }
                , {
                    qtip:      getI18nResource('caqs.supprimer'),
                    iconCls:   'icon-delete'
                }
            ]
        });

        action.on({
            scope:  this,
            action: function(grid, record, action, row, col) {
                if(action=='icon-delete') {
                    this.deleteCriterion(record.data.id);
                } else if(action=='icon-criterionformula') {
                    this.editFormula(record.data.id, record.data.lib);
                } else if(action=='icon-criterionagregation') {
                    this.manageAgregation(record.data.id);
                }
            }
        }, this);


        var cm = new Ext.grid.ColumnModel([
            expander
            , {
               header: 		getI18nResource("caqs.modeleditor.grid.id"),
               dataIndex: 	'id',
               id:              'id',
               sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.modeleditor.grid.lib"),
                dataIndex:	'lib',
                renderer:       this.renderCriterionName,
                sortable: 	true
            }
            ,{
                header:         getI18nResource("caqs.modeleditor.grid.telt"),
                sortable:	true,
                editor:         teltEditor,
                width:          200,
                renderer:       this.renderTypeElement,
                dataIndex:	'telt'
            }
            ,{
                header:         getI18nResource("caqs.modeleditor.grid.weight"),
                sortable:	true,
                width:          100,
                align:          'right',
                editor:         new Ext.form.NumberField({
                    allowNegative:      false,
                    allowDecimals:      false,
                    allowBlank:         false,
                    minValue:           1
                }),
                dataIndex:	'weight'
            }
            , action
        ]);
        this.criterionsGridStore = new Ext.data.Store({
            elementTypesStore:      this.elementTypeCBStore,
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveGoalsAssociatedCriterions.do'
            }),
            reader: new Ext.data.JsonReader({
                root:           'criterions',
                totalProperty:  'totalCount',
                id:             'id',
                fields: [ 
                    'id', 'lib', 'desc', 'compl', 'weight', 'telt',
                    'formulasPresent', 'formulaArray', 'agregationsPresent'
                ]
            }),
            remoteSort: true
        });
        this.criterionsGridStore.setDefaultSort('lib', 'asc');

        this.criterionsGrid = new Ext.grid.EditorGridPanel({
            anchor:                 '100% 85%',
            //autoHeight:             true,
            //height:                 350,
            header:                 false,
            store:                  this.criterionsGridStore,
            cm:                     cm,
            enableColumnHide :      false,
            enableColumnMove :      false,
            enableColumnResize:     true,
            enableHdMenu:           false,
            clicksToEdit:           1,
            trackMouseOver:         true,
            frame:                  false,
            sm:                     new Ext.grid.RowSelectionModel({selectRow:Ext.emptyFn}),
            style:                  'margin-top: 5px;',
            loadMask:               true,
            autoExpandColumn:       'lib',
            viewConfig: {
                forceFit: true
            },
            plugins: [
                expander
                , action
            ],
            tbar:   new Ext.Toolbar({
                items: [
                    new Ext.Toolbar.Button({
                        text:		getI18nResource("caqs.modeleditor.modelEdition.model.addCriterion"),
                        icon: 		requestContextPath + '/images/add.gif',
                        cls: 		'x-btn-text-icon',
                        width:          80,
                        handler:	function() {
                            this.addCriterionsToGoal();
                        },
                        scope:          this
                    })
                ]
                , autoHeight:     true
            })
        });

        this.criterionsGrid.on('afteredit', this.afterCellEdit, this);

        this.displayedGoals = new Array();
        this.displayedGoals[this.displayedGoals.length] = {
            id:     'temporaryTab',
            title:  'chargement en cours',
            layout: 'fit'
        };
        this.goalsPanel = new Ext.TabPanel({
            anchor:             '100%',
            enableTabScroll:	true,
            plain:		true,
            activeTab:		0,
            border:             false,
            hideMode:           'offsets',
            titlecollapse:      true,
            hideCollapseTool:   true,
            layoutOnTabChange:	true,
            items:              this.displayedGoals,
            listeners: {
                tabchange:      this.reloadGrid,
                scope:          this
            }
        });
        this.goalsPanel.on('beforeremove', this.deleteGoal, this);
        
        var config = {
            items: [
                this.goalsPanel
                , this.criterionsGrid
            ],
            tbar:   new Ext.Toolbar({
                items: [
                    new Ext.Toolbar.Button({
                        text:		getI18nResource("caqs.modeleditor.modelEdition.model.addGoal"),
                        icon: 		requestContextPath + '/images/add.gif',
                        cls: 		'x-btn-text-icon',
                        width:          80,
                        handler:	function() {
                            this.addGoals();
                        },
                        scope:          this
                    })
                ]
                , autoHeight:     true
            })
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        
        Ext.ux.CaqsModelEditorGoalsCriterionsPanel.superclass.initComponent.apply(this, arguments);
        this.elementTypeCBStore.load();
    },

    manageAgregation: function(idCrit) {
        if(this.editAggregationsWnd==null) {
            this.editAggregationsWnd = new Ext.ux.CaqsModelEditorEditAggregationsWindow({
                parentPanel:        this
            });
        }
        this.editAggregationsWnd.setId(this.modelId, idCrit);
        this.editAggregationsWnd.show(this, this.editAggregationsWnd.onShowWnd, this.editAggregationsWnd);
    },

    afterCellEdit: function(e) {
        var params = null;
        if(e.field=='weight') {
            params = {
                action:     'saveWeight',
                modelId:    this.modelId,
                weight:     e.value,
                idCrit:     e.record.data.id,
                goalId:     this.goalsPanel.getActiveTab().id
            }
        } else if(e.field=='telt') {
            params = {
                action:     'saveTelt',
                modelId:    this.modelId,
                idTelt:     e.value,
                idCrit:     e.record.data.id
            }
        }
        if(params!=null) {
            Ext.Ajax.request({
                url:	requestContextPath + '/SaveGoalCriterionModification.do',
                success:	this.reloadGrid,
                params:     params,
                scope:      this
            });
        }
    },

    reloadGrid: function() {
        if(this.modelId != null) {
            this.criterionsGridStore.load({
               params: {
                   modelId:     this.modelId,
                   goalId:      this.goalsPanel.getActiveTab().id
               }
            });
        }
    },

    addCriterionsToGoal: function() {
        if(this.addCriterionWnd==null) {
            this.addCriterionWnd = new Ext.ux.CaqsModelEditorAddElementWindow({
                elementType:        'criterions',
                parentPanel:        this,
                addElement: function(record) {
                    Ext.Ajax.request({
                        url:	requestContextPath + '/SaveCriterionGoalModelAssociation.do',
                        success:	this.afterAdding,
                        params:     {
                            idCrit:     record.data.id,
                            goalId:     this.parentPanel.getActiveGoalId(),
                            modelId:    this.modelId
                        },
                        scope:      this
                    });
                },

                afterAdding: function() {
                    this.refreshGridDatas();
                    this.parentPanel.reloadGrid();
                }
            });
        }
        this.addCriterionWnd.setId(this.modelId);
        this.addCriterionWnd.show(this);
    },

    addGoals: function() {
        if(this.addGoalWnd==null) {
            this.addGoalWnd = new Ext.ux.CaqsModelEditorAddElementWindow({
                elementType:        'goals',
                parentPanel:        this,
                addElement: function(record) {
                    this.parentPanel.addGoal({
                        id:     record.data.id,
                        lib:     record.data.lib,
                        desc:     record.data.desc,
                        compl:     record.data.compl
                    });
                    this.refreshGridDatas();
                }
            });
        }
        this.addGoalWnd.setId(this.modelId);
        this.addGoalWnd.show(this);
    },

    addGoal: function(goal) {
        var tabtip = '<B>'+getI18nResource('caqs.modeleditor.grid.desc')+'</B> : '+goal.desc+'<BR />';
        if(goal.compl!='') {
            tabtip += '<B>'+getI18nResource('caqs.modeleditor.grid.compl')+'</B> : '+goal.compl+'<BR />';
        }
        var tmp = new Ext.Panel({
            id:         goal.id,
            title:      goal.lib,
            closable:   true,
            tabTip:     tabtip,
            layout:     'fit'
        });
        this.goalsPanel.add(tmp);
        this.displayedGoals[this.displayedGoals.length] = tmp;
    },

    removeAllTabs: function() {
        for(var i=0; i<this.displayedGoals.length; i++) {
            var goal = this.displayedGoals[i];
            if(goal!=null) {
                var elt = this.goalsPanel.getComponent(goal.id);
                this.goalsPanel.remove(elt);
            }
        }
        this.displayedGoals.length = 0;
    },

    displayGoalsTabs: function(response) {
        this.goalsPanel.un('beforeremove', this.deleteGoal, this);
        this.removeAllTabs();
        this.goalsPanel.on('beforeremove', this.deleteGoal, this);
        if(response!=null && response.responseText!=null) {
            var json = Ext.util.JSON.decode(response.responseText);
            var array = json.datas;
            for(var i=0; i<array.length; i++) {
                var goal = array[i];
                var tabtip = '<B>'+getI18nResource('caqs.modeleditor.grid.desc')+'</B> : '+goal.desc+'<BR />';
                if(goal.compl!='') {
                    tabtip += '<B>'+getI18nResource('caqs.modeleditor.grid.compl')+'</B> : '+goal.compl+'<BR />';
                }
                var tmp = new Ext.Panel({
                    id:         goal.id,
                    title:      goal.lib,
                    tabTip:     tabtip,
                    closable:   true,
                    layout:     'fit'
                });
                this.goalsPanel.add(tmp);
                this.displayedGoals[this.displayedGoals.length] = tmp;
            }
            this.goalsPanel.setActiveTab(0);
        }
    },

    refresh: function() {
        Ext.Ajax.request({
            url:	requestContextPath + '/ModelEditorRetrieveAssociatedGoals.do',
            success:	this.displayGoalsTabs,
            params:     {
                modelId:    this.modelId
            },
            scope:      this
        });
    },

    getAllTabsIds: function() {
        var retour = '';
        var first = true;
        for(var i=0; i<this.displayedGoals.length; i++) {
            if(this.displayedGoals[i]!=null) {
                var goal = this.displayedGoals[i];
                if(!first) {
                    retour += ';';
                }
                retour += goal.id;
            }
            first = false;
        }
        return retour;
    },

    deleteGoal: function(tabpanel, tab) {
        Ext.MessageBox.confirm(
            getI18nResource('caqs.modeleditor.modelEdition.deleteGoal.confirmTitle'),
            getI18nResource('caqs.modeleditor.modelEdition.deleteGoal.confirmText'),
            function(btn) {
                if(btn == 'yes') {
                    Ext.Ajax.request({
                        url:	requestContextPath + '/RemoveGoalFromModel.do',
                        success:	function() {
                            this.goalsPanel.un('beforeremove', this.deleteGoal, this);
                            this.goalsPanel.remove(tab);
                            this.goalsPanel.on('beforeremove', this.deleteGoal, this);
                            for(var i=0; i<this.displayedGoals.length; i++) {
                                if(this.displayedGoals[i]!=null && this.displayedGoals[i].id==tab.id) {
                                    this.displayedGoals[i]=null;
                                    break;
                                }
                            }
                        },
                        params:     {
                            goalId:     tab.id,
                            modelId:    this.modelId
                        },
                        scope:      this
                    });
                }
            }, this);
         return false;
    }
});

