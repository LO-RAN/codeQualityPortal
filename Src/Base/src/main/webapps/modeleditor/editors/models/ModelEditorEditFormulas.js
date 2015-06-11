Ext.ux.CaqsModelEditorEditFormulasWindow = function(config) {
    Ext.ux.CaqsModelEditorEditFormulasWindow.superclass.constructor.call(this, config);
};


Ext.extend(Ext.ux.CaqsModelEditorEditFormulasWindow, Ext.Window, {
    layout:             'anchor',
    maximizable:        true,
    modelId:            undefined,
    critId:             undefined,
    bodyBorder:         false,
    border:             true,
    validateBtn:        undefined,
    parentPanel:        undefined,
    grid:               undefined,
    gridStore:          undefined,
    rulesGrid:          undefined,
    rulesGridStore:     undefined,
    height:             700,
    width:              900,
    modal:              true,
    elementType:        undefined,
    addMetricWnd:       undefined,
    alwaysTrueCB:       undefined,
    alwaysTrueScore:    undefined,
    firstLoad:          true,
    numberOfFormulas:   0,
    costCorrectionFormula:undefined,
    costCorrectionFormulaWithoutErrors:undefined,
    autoScroll:         true,
    bodyStyle:          'margin-left: 5px;',


    setId: function(idUsa, idCrit, lib) {
        this.modelId = idUsa;
        this.critId = idCrit;
        this.firstLoad = true;
        this.setTitle(lib);
    },

    addMetric: function() {
        if(this.addMetricWnd==null) {
            this.addMetricWnd = new Ext.ux.CaqsModelEditorAddMetricToCriterionWindow({
                parentPanel:        this
            });
        }
        this.addMetricWnd.setId(this.modelId, this.critId);
        this.addMetricWnd.show(this);
    },

    removeMetric: function(idMet) {
        Ext.MessageBox.confirm(
            getI18nResource('caqs.confirmDelete'),
            getI18nResource('caqs.modeleditor.modelEdition.metricDelete.confirm'),
            function(btn) {
                if(btn == 'yes') {
                    Ext.Ajax.request({
                        url:	requestContextPath + '/ManageRegle.do',
                        success:	this.refreshGridDatas,
                        params:     {
                            critId:     this.critId,
                            modelId:    this.modelId,
                            idMet:      idMet,
                            action:     'delete'
                        },
                        scope:      this
                    });
                }
            }, this);
    },

    saveFormulas: function() {
        Ext.Ajax.request({
            url:        requestContextPath + '/SaveFormulas.do',
            success:    this.close,
            scope:      this,
            params: {
                modelId:                this.modelId,
                critId:                 this.critId,
                alwaysTrueFormula:      this.alwaysTrueCB.getValue(),
                alwaysTrueFormulaScore: this.alwaysTrueScore.getValue()
            }
        });
    },

    addFormula: function() {
        this.showFormulaEditingWnd(null);
    },

    deleteFormula: function(row) {
      Ext.MessageBox.confirm(
            getI18nResource('caqs.confirmDelete'),
            getI18nResource('caqs.modeleditor.modelEdition.formulaEdition.confirmDeleteFormula'),
            function(btn) {
                if(btn == 'yes') {
                   Ext.Ajax.request({
                        url:        requestContextPath + '/UpdateFormula.do',
                        success:    this.refreshFormulaGrid,
                        scope:      this,
                        params: {
                            action:         'delete',
                            modelId:        this.modelId,
                            critId:         this.critId,
                            formulaIndex:   row
                        }
                    });
                }
            }, this);
    },

    initComponent : function(){
        this.rulesGridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveMetricsRegardingCriterion.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 		'elements',
                totalProperty: 	'totalCount',
                fields: [
                    'id', 'lib', 'desc', 'compl', 'tool'
                ]
            }),
            remoteSort: true
        });

        var rulesAction = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:[
                {
                    qtip:      getI18nResource('caqs.supprimer'),
                    iconCls:   'icon-delete'
                }
            ]
        });

        rulesAction.on({
            scope:  this,
            action: function(grid, record, action, row, col) {
                if(action=='icon-delete') {
                    this.removeMetric(record.data.id);
                }
            }
        }, this);

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

        // create the Grid
        this.rulesGrid = new Ext.grid.GridPanel({
            store:              this.rulesGridStore,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:       false,
            frame:		false,
            loadMask:           true,
            collapsible: 	false,
            iconCls: 		'icon-grid',
            autoExpandColumn:   'lib',
            cm: new Ext.grid.ColumnModel([
                expander
                , {
                    id:         'id',
                    header:     getI18nResource('caqs.modeleditor.grid.id'),
                    width:      50,
                    sortable:   false,
                    dataIndex:  'id'
                }
                , {
                    id:         'lib',
                    header:     getI18nResource('caqs.modeleditor.grid.lib'),
                    sortable:   false,
                    dataIndex:  'lib'
                }
                , {
                    id:         'tool',
                    header:     getI18nResource('caqs.modeleditor.grid.tool'),
                    sortable:   false,
                    dataIndex:  'tool'
                }
                , rulesAction
                ]),
            viewConfig: {
                forceFit: true
            },
            plugins: 		[
                expander
                , rulesAction
            ],
            tbar:   new Ext.Toolbar({
                items: [
                    new Ext.Toolbar.Button({
                        text:		getI18nResource("caqs.modeleditor.modelEdition.model.addMetric"),
                        icon: 		requestContextPath + '/images/add.gif',
                        cls: 		'x-btn-text-icon',
                        width:          80,
                        handler:	function() {
                            this.addMetric();
                        },
                        scope:          this
                    })
                ]
                , autoHeight:     true
            })
        });

        this.gridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveFormulaForCriterionAndModel.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root:               'elements',
                totalProperty:      'totalCount',
                alwaysTrueformula:  'alwaysTrueFormula',
                alwaysTrueScore:    'alwaysTrueScore',
                fields: [
                    'score', 'formulaWithErrors', 'action', 'formulaWithoutErrors',
                    'hasError', 'costFormulaWithoutErrors', 'costFormulaWithErrors',
                    'costFormulaHasErrors'
                ]
            }),
            remoteSort: true
        });
        this.gridStore.addListener('load', function(store, records, options) {
            if(this.firstLoad) {
                this.alwaysTrueCB.setValue(store.reader.jsonData.alwaysTrueFormula);
                this.alwaysTrueScore.setValue(store.reader.jsonData.alwaysTrueScore);
            }
            var costformula = store.reader.jsonData.costFormulaWithErrors;
            if(costformula==null) {
                costformula = '';
            }
            this.costCorrectionFormula.setValue(costformula);
            this.costCorrectionFormulaWithoutErrors = store.reader.jsonData.costFormulaWithoutErrors;
            var disableSave = store.reader.jsonData.costFormulaHasErrors;
            for(var i=0; i<records.length; i++) {
                var rec = records[i];
                if(rec.data.hasError) {
                    disableSave = true;
                }
            }
            if(records.length==0 && !this.alwaysTrueCB.getValue()) {
                disableSave = true;
            }
            this.validateBtn.setDisabled(disableSave);
            this.numberOfFormulas = records.length;
            this.firstLoad = false;
        }, this);

        var action = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:[
                {
                    qtip:      getI18nResource('caqs.modeleditor.modelEdition.formula.up'),
                    iconCls:   'icon-formulaup'
                }
                , {
                    qtip:      getI18nResource('caqs.modeleditor.modelEdition.formula.down'),
                    iconCls:   'icon-formuladown'
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
                if(action=='icon-formulaup') {
                    Ext.Ajax.request({
                        url:        requestContextPath + '/MoveFormula.do',
                        success:    this.refreshFormulaGrid,
                        scope:      this,
                        params: {
                            modelId:        this.modelId,
                            critId:         this.critId,
                            sens:           'up',
                            formulaIndex:   row
                        }
                    });
                } else if(action=='icon-formuladown') {
                    Ext.Ajax.request({
                        url:        requestContextPath + '/MoveFormula.do',
                        success:    this.refreshFormulaGrid,
                        scope:      this,
                        params: {
                            modelId:        this.modelId,
                            critId:         this.critId,
                            sens:           'down',
                            formulaIndex:   row
                        }
                    });
                } else if(action=='icon-delete') {
                    this.deleteFormula(row);
                }
            }
        }, this);

        var scoreStore = new Ext.data.SimpleStore({
            id:		0,
            fields: ['id', 'score'],
            data : [
                ['1', '1']
                , ['2', '2']
                , ['3', '3']
                , ['4', '4']
            ]
        });

        var scoreCombo = new Ext.form.ComboBox({
            store:      	scoreStore,
            displayField:	'score',
            valueField: 	'id',
            hiddenName: 	'id',
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
        scoreCombo.on('select', function(field, newValue, oldValue) {
            this.grid.stopEditing();
        }, this);


        // create the Grid
        this.grid = new Ext.grid.EditorGridPanel({
            anchor:             '99% 75%',
            store:              this.gridStore,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableHdMenu:       false,
            frame:		false,
            loadMask:           true,
            clicksToEdit:	1,
            collapsible: 	false,
            iconCls: 		'icon-grid',
            autoExpandColumn:   'formulaWithErrors',
            viewConfig: {
                forceFit: true
            },
            listeners:          {
                beforeedit: function(evt) {
                    //pas d'edition de la premiere colonne
                    if(evt.column == 1) {
                        this.showFormulaEditingWnd(evt);
                        return false;
                    }
                    return true;
                },
                afteredit: function(evt) {
                    if(evt.column == 0) {
                        Ext.Ajax.request({
                            url:        requestContextPath + '/UpdateFormula.do',
                            success:    this.refreshFormulaGrid,
                            scope:      this,
                            params: {
                                action:         'updatescore',
                                modelId:        this.modelId,
                                critId:         this.critId,
                                formulaIndex:   evt.row,
                                score:          evt.value
                            }
                        });
                    }
                },
                scope:      this
            },
            cm: new Ext.grid.ColumnModel([
                {
                    id:         'score',
                    header:     getI18nResource('caqs.modeleditor.modelEdition.formula.grid.mark'),
                    width:      50,
                    sortable:   false,
                    dataIndex:  'score',
                    editor:     scoreCombo
                }
                , {
                    id:         'formula',
                    header:     getI18nResource('caqs.modeleditor.modelEdition.formula.grid.formula'),
                    sortable:   false,
                    editor:     new Ext.form.TextField(),
                    dataIndex:  'formulaWithErrors'
                }
                , action
            ]),
            plugins: 		[
                action
            ]
            , tbar:   new Ext.Toolbar({
                items: [
                    new Ext.Toolbar.Button({
                        text:		getI18nResource("caqs.modeleditor.modelEdition.model.addFormula"),
                        icon: 		requestContextPath + '/images/add.gif',
                        cls: 		'x-btn-text-icon',
                        width:          80,
                        handler:	function() {
                            this.addFormula();
                        },
                        scope:          this
                    })
                ]
                , autoHeight:     true
            })
        });

        var closeBtn = new Ext.Button({
            text:	getI18nResource("caqs.close"),
            icon: 	requestContextPath + '/images/cross.gif',
            cls: 	'x-btn-text-icon',
            width:      80,
            handler:    this.close,
            scope:      this
        });
        this.validateBtn = new Ext.Button({
            text:	getI18nResource("caqs.update"),
            cls:	'x-btn-text-icon',
            icon:	requestContextPath+'/images/database_save.gif',
            width:      80,
            handler:    this.saveFormulas,
            scope:      this
        });
        this.on('beforeclose', function(wnd) {
            this.hide();
            this.parentPanel.reloadGrid();
            return false;
        }, this);
        this.on('show', function(wnd) {
            this.setPosition(0,0);
            this.refreshGridDatas();
        });

        this.alwaysTrueCB = new Ext.form.Checkbox({
            fieldLabel:         getI18nResource('caqs.modeleditor.modelEdition.formulaEdition.alwaysTrueFormula'),
            name:       	'alwaysTrueScoreCheckbox',
            id:         	'alwaysTrueScoreCheckbox'
        });
        this.alwaysTrueCB.on('check', function(cb, checked) {
            this.alwaysTrueScore.setDisabled(!checked);
            if(this.numberOfFormulas==0) {
                this.validateBtn.setDisabled(!checked);
            }
        }, this);

        this.alwaysTrueScore = new Ext.form.ComboBox({
            fieldLabel: getI18nResource('caqs.modeleditor.modelEdition.formulaEdition.alwaysTrueFormulaScore'),
            name:       	'alwaysTrueScoreCombo',
            id:         	'alwaysTrueScoreCombo',
            store:      	scoreStore,
            disabled:           true,
            displayField:	'score',
            valueField: 	'id',
            hiddenName: 	'id',
            editable:		false,
            allowBlank: 	false,
            value:              '1',
            forceSelection:	true,
            triggerAction:	'all',
            autocomplete:	false,
            mode:       	'local'
        });

        this.costCorrectionFormula = new Ext.ux.form.StaticTextField({
            labelSeparator:     '',
            htmlEncode:         false,
            border:             false
        });
        var editCostFomula = new Ext.ux.ComponentImage({
            id:     'costCorrectionEditImg',
            style:  'cursor:pointer; width:16px; height:16px;',
            border: false,
            src:    requestContextPath + '/images/calculator_add.gif'
        });
        addTooltip('costCorrectionEditImg', '',
            getI18nResource('caqs.modeleditor.modelEdition.formulaEdition.editCostFormula'));
        editCostFomula.on('click', function(c) {
            this.showCostFormulaEditingWnd();
        }, this);

        var config = {
            title:          getI18nResource('caqs.modeleditor.modelEdition.formula.title'),
            items:          [
                new Ext.form.FieldSet ({
                    title:      getI18nResource('caqs.modeleditor.modelEdition.formula.rules.fieldSet.title'),
                    anchor:     '95% 35%',
                    layout:     'fit',
                    items:      [
                        this.rulesGrid
                    ]
                }),
                new Ext.form.FieldSet ({
                    title:      getI18nResource('caqs.modeleditor.modelEdition.formula.fieldSet.title'),
                    anchor:     '95% 40%',
                    layout:     'anchor',
                    items:      [
                        this.grid
                        , new Ext.Panel({
                            anchor:     '99% 20%',
                            frame:      true,
                            border:     true,
                            layout:     'column',
                            style:      'margin-top: 5px; margin-bottom: 5px;',
                            items: [
                                {
                                    layout:     'form',
                                    columnWidth:.4,
                                    labelWidth: 180,
                                    items:      this.alwaysTrueCB
                                }
                                , {
                                    layout:     'form',
                                    columnWidth:.6,
                                    labelWidth: 180,
                                    items:      this.alwaysTrueScore
                                }
                            ]
                        })
                    ]
                }),
                new Ext.form.FieldSet ({
                    title:      getI18nResource('caqs.modeleditor.modelEdition.formulaEdition.costFormula'),
                    anchor:     '95% 15%',
                    layout:     'column',
                    items:      [
                            {
                                layout:         'form',
                                columnWidth:    .8,
                                border:         false,
                                items:          this.costCorrectionFormula
                            }
                            , {
                                columnWidth:    .2,
                                border:         false,
                                items:          editCostFomula
                            }
                    ]
                })
            ],
            buttons: [
                this.validateBtn
                , closeBtn
            ]
        }
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelEditorEditFormulasWindow.superclass.initComponent.apply(this, arguments);
        this.on('render', function() {
            putTooltips();
        }, this);
    },

    showFormulaEditingWnd: function(evt) {
        var taVal = '';
        var formulaIndex = -1;
        if (evt!=null) {
            taVal = evt.record.data.formulaWithoutErrors;
            formulaIndex = evt.row;
        }
        var textarea = new Ext.form.TextArea({
            value:      taVal
        });
        textarea.on('render', function() {
            textarea.focus(false, 500);
        }, this);

        var win = new Ext.Window({
            width:          350,
            height:         300,
            layout:         'fit',
            bodyBorder:     false,
            closable:       true,
            resizable:      false,
            modal:          true,
            maximizable:    true,
            items:          textarea,
            bbar: [
                {
                    text:       getI18nResource('caqs.update'),
                    cls:	'x-btn-text-icon',
                    icon:	requestContextPath+'/images/tick.gif',
                    scope:      this,
                    handler: function() {
                        //evt.record.set(evt.field, textarea.getValue());
                        Ext.Ajax.request({
                            url:	requestContextPath + '/UpdateFormula.do',
                            success:	this.refreshFormulaGrid,
                            scope:      this,
                            params: {
                                action:         'update',
                                modelId:        this.modelId,
                                critId:         this.critId,
                                formula:        textarea.getValue(),
                                formulaIndex:   formulaIndex
                            }
                        });

                        win.close();
                    }
                }
                , {
                    text:       getI18nResource('caqs.cancel'),
                    cls:        'x-btn-text-icon',
                    icon:	requestContextPath+'/images/cross.gif',
                    handler: function() {
                        win.close();
                    }
                }
            ]
        });
        win.show();
    },

    showCostFormulaEditingWnd: function() {
        var taVal = '';
        taVal = this.costCorrectionFormulaWithoutErrors;
        var textarea = new Ext.form.TextArea({
            value:      taVal
        });
        textarea.on('render', function() {
            textarea.focus(false, 500);
        }, this);

        var win = new Ext.Window({
            width:          350,
            height:         300,
            layout:         'fit',
            bodyBorder:     false,
            closable:       true,
            resizable:      false,
            modal:          true,
            maximizable:    true,
            items:          textarea,
            bbar: [
                {
                    text:       getI18nResource('caqs.update'),
                    cls:	'x-btn-text-icon',
                    icon:	requestContextPath+'/images/tick.gif',
                    scope:      this,
                    handler: function() {
                        //evt.record.set(evt.field, textarea.getValue());
                        Ext.Ajax.request({
                            url:	requestContextPath + '/UpdateFormula.do',
                            success:	this.refreshFormulaGrid,
                            scope:      this,
                            params: {
                                action:         'updateCostCorrection',
                                modelId:        this.modelId,
                                critId:         this.critId,
                                formula:        textarea.getValue()
                            }
                        });

                        win.close();
                    }
                }
                , {
                    text:       getI18nResource('caqs.cancel'),
                    cls:        'x-btn-text-icon',
                    icon:	requestContextPath+'/images/cross.gif',
                    handler: function() {
                        win.close();
                    }
                }
            ]
        });
        win.show();
    },

    refreshFormulaGrid: function() {
        this.gridStore.load({
            params:     {
                modelId:    this.modelId,
                critId:     this.critId,
                firstLoad:  this.firstLoad
            }
        });
    },

    refreshGridDatas: function() {
        this.rulesGridStore.load({
            params:     {
                modelId:    this.modelId,
                critId:     this.critId,
                associated: true
            }
        });
        this.gridStore.load({
            params:     {
                modelId:    this.modelId,
                critId:     this.critId,
                firstLoad:  this.firstLoad
            }
        });
    }
});