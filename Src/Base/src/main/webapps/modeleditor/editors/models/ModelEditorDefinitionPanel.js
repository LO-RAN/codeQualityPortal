Ext.ux.CaqsModelEditorDefinitionPanel = Ext.extend(Ext.ux.CaqsElementEditorPanel, {
    showCancel:         false,
    title:              getI18nResource('caqs.modeleditor.modelEdition.title'),
    retrieveDatasUrl:   requestContextPath + '/ModelEditorRetrieveModelInfos.do',
    saveUrl:            requestContextPath + '/ModelEditorSaveModelInfos.do',
    fieldsDefinitions: [
    {
        name: 'id'
        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.id')
        , maxLength:    40
        , columnNumber: 1
        , type: 'id'
    }
    , {
        columnNumber: 2
        , type: 'blank'
    }
    , {
        name: 'mediumRun'
        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.mediumRun')
        , maxLength:    40
        , allowDecimals : true
        , columnNumber: 1
        , type: 'number'
    }
    , {
        name: 'longRun'
        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.longRun')
        , maxLength:    40
        , allowDecimals : true
        , columnNumber: 2
        , type: 'number'
    }
    , {
        name:   'dinstallation'
        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.dinst')
        , columnNumber: 1
        , type: 'staticfield'
    }
    , {
        name:   'dmaj'
        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.dmaj')
        , columnNumber: 2
        , type: 'staticfield'
    }
    , {
        name:   'dapplication'
        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.dapplication')
        , columnNumber: 1
        , type: 'date'
    }
    , {
        name:   'dperemption'
        , fieldLabel:   getI18nResource('caqs.modeleditor.grid.dperemption')
        , columnNumber: 2
        , type: 'date'
    }
    , {
        name:   'i18n'
        , type: 'i18n'
    }
    ],
    actionAsked:    undefined,
    ifpugStaticTextField: undefined,
    ifpugFormulaWithoutErrors: undefined,
    ifpugTeltCB:    undefined,
    ifpugTeltID:    undefined,
    additionalAssociationsGrid: undefined,
    additionalAssociationsGridStore: undefined,
    easFS:          undefined,


    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsModelEditorDefinitionPanel.superclass.constructor.apply(this, arguments);
    },

    cancelFn: function() {
    },

    goBackToList: function() {
        if('delete'===this.actionAsked) {
            this.actionAsked = null;
            this.parentSearchScreen.finishEdition();
        }
    },

    deleteFn: function() {
        Ext.MessageBox.confirm(getI18nResource('caqs.modeleditor.modelEdition.deleteModel.confirmTitle'),
            getI18nResource('caqs.modeleditor.modelEdition.deleteModel.confirmText'), function(btn) {
                if(btn == 'yes') {
                    this.actionAsked = 'delete';
                    this.launchSaveRequest('delete', this.goBackToList, this);
                }
            }, this);
    },

    specificDataManagement: function(json) {
        this.ifpugFormulaWithoutErrors = (json.ifpugWithoutErrors)?json.ifpugWithoutErrors:'';
        this.saveBtn.setDisabled(json.ifpugHasErrors);
        this.ifpugStaticTextField.setValue((json.ifpug)?json.ifpug:'');
        this.ifpugTeltCB.setValue(json.idfugLibTelt);
        this.ifpugTeltID = json.idfugIdTelt;
        var disable = (json.nbEAsUsingIt && json.nbEAsUsingIt>0);
        this.deleteBtn.setDisabled(disable);
        var msg = (disable) ? getI18nResource('caqs.modeleditor.modelEdition.noDeletionPossible') : null;
        this.setInfoMessage(msg);
        this.easFS.collapse();
    },

    putAdditionalParamsToSave: function(params) {
        params['ifpug'] = this.ifpugStaticTextField.getValue();
        params['ifpugIdTelt'] = this.ifpugTeltID;
    },

    createAdditionalFormItems: function() {
        var comboStore = new Ext.ux.CaqsJsonStore({
            url:    requestContextPath + '/ElementTypeList.do',
            fields: [ 'id_telt', 'lib_telt' ]
        });
        comboStore.addListener('load', function() {
            this.ifpugTeltCB.setValue(this.ifpugTeltID);
        }, this);
        this.ifpugTeltCB = new Ext.form.ComboBox({
            fieldLabel:     getI18nResource('caqs.modeleditor.ifpug.telt'),
            paramName:      'ifpugTelt',
            name:           'comboBox' + this.id + 'ifpugTelt',
            id:             'comboBox' + this.id + 'ifpugTelt',
            store:          comboStore,
            displayField:   'lib_telt',
            valueField:     'id_telt',
            hiddenName:     'idComboBox' + this.id + 'ifpugTelt',
            width:          210,//150+220
            editable:       false,
            forceSelection: true,
            triggerAction:  'all',
            autocomplete:   false
        });
        this.ifpugTeltCB.on('change', function(cb, newvalue, oldvalue) {
            this.ifpugTeltID = newvalue;
        }, this);
        this.ifpugStaticTextField = new Ext.ux.form.StaticTextField({
            id:             this.id+'ifpug',
            fieldLabel:     getI18nResource('caqs.modeleditor.ifpug.formula'),
            name:           this.id+'ifpug',
            paramName:      'ifpug',
            htmlEncode:     false
        });
        //this.ifpugStaticTextField.on('click', this.showIFPUGFormulaEditingWnd, this);
        var editIFPUGFomula = new Ext.ux.ComponentImage({
            id:     this.id+'ifpugEditImg',
            style:  'cursor:pointer; width:16px; height:16px;',
            border: false,
            src:    requestContextPath + '/images/calculator_add.gif'
        });
        /*addTooltip('costCorrectionEditImg', '',
            getI18nResource('caqs.modeleditor.modelEdition.formulaEdition.editCostFormula'));*/
        editIFPUGFomula.on('click', function(c) {
            this.showIFPUGFormulaEditingWnd();
        }, this);

        var panel = new Ext.Panel({
            style:      'margin-top: 5px;',
            title:      getI18nResource('caqs.modeleditor.ifpug'),
            frame:      true,
            layout:     'anchor',
            defaults:   {
                anchor: '98%',
                border: true
            },
            anchor:     '100%',
            items: [
                new Ext.Panel({
                    layout:     'table',
                    layoutConfig: {
                        columns:2
                    },
                    items:      [
                    {
                        layout:         'form',
                        //columnWidth:    .8,
                        border:         false,
                        items:          this.ifpugStaticTextField
                    }
                    , {
                        //columnWidth:    .2,
                        border:         false,
                        items:          editIFPUGFomula
                    }
                    ]

                })
                , new Ext.Panel({
                    layout:     'form',
                    items:      this.ifpugTeltCB
                })
            ]
        })

        this.formItems[this.formItems.length] = panel;
        //tableau d'affichage des eas utilisant ce modele
        var cm = new Ext.grid.ColumnModel([
            {
               header: 		getI18nResource("caqs.EA"),
               dataIndex: 	'eaLib',
               id:              'eaLib',
               sortable: 	true
            }
            ,{
                header: 	getI18nResource("caqs.projet"),
                dataIndex:	'projectLib',
                id:             'projectLib',
                //groupRenderer:   this.criterionGroupRenderer,
                sortable: 	true
            }
        ]);
        this.additionalAssociationsGridStore = new Ext.data.GroupingStore({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/RetrieveEAsForModel.do'
            }),
            reader: new Ext.data.JsonReader({
                root:           'elements',
                totalProperty:  'totalCount',
                fields: [ 'eaId', 'eaLib', 'projectId', 'projectLib' ]
            }),
            remoteSort: false,
            groupField: 'projectLib',
            sortInfo:           {
                field:          'eaLib',
                direction:      'ASC'
            }
        });

        this.additionalAssociationsGrid = new Ext.grid.GridPanel({
            height:		260,
            header:		false,
            store: 		this.additionalAssociationsGridStore,
            cm: 		cm,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableColumnResize:	true,
            enableHdMenu:	true,
            trackMouseOver:	true,
            frame:		true,
            sm: 		new Ext.grid.RowSelectionModel({selectRow:Ext.emptyFn}),
            style:              'margin-top: 5px;',
            loadMask: 		true,
            autoExpandColumn:   'eaLib',
            view:               new Ext.grid.GroupingView({
                                    forceFit:		true,
				    groupTextTpl:
                                        '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "'+getI18nResource('caqs.EAs')+'" : "'+getI18nResource('caqs.EA')+'"]})'
				})
        });
        this.easFS = new Ext.form.FieldSet({
            title:      getI18nResource('caqs.modeleditor.modelEdition.associatedElements.title'),
            style:      'margin-top: 10px;',
            collapsed:  true,
            collapsible:true,
            autoHeight: true,
            items:      this.additionalAssociationsGrid
        })
        this.easFS.on('expand', function() {
            this.additionalAssociationsGridStore.baseParams = {
                idUsa:     this.elementId
            };
            this.additionalAssociationsGridStore.load();
        }, this, {

        });
        this.formItems[this.formItems.length] = this.easFS;
    },

    refreshIFPUG: function(response) {
        if(response!=null && response.responseText!=null) {
            var json = Ext.util.JSON.decode(response.responseText);
            if(json.ifpugHasError) {
                this.formPanel.stopMonitoring();
            } else {
                this.formPanel.startMonitoring();
            }
            this.saveBtn.setDisabled(json.ifpugHasError);
            this.ifpugStaticTextField.setValue(json.ifpug);
            this.ifpugFormulaWithoutErrors = json.ifpugWithoutErrors;
        }
    },

    showIFPUGFormulaEditingWnd: function() {
        var okBtn = new Ext.Button({
            text:       getI18nResource('caqs.update'),
            cls:	'x-btn-text-icon',
            icon:	requestContextPath+'/images/tick.gif',
            scope:      this,
            handler: function() {
                //evt.record.set(evt.field, textarea.getValue());
                Ext.Ajax.request({
                    url:	requestContextPath + '/ModelEditorCheckIFPUGFormula.do',
                    success:	this.refreshIFPUG,
                    scope:      this,
                    params: {
                        id_usa:         this.idTF.getValue(),
                        formula:        textarea.getValue()
                    }
                });

                win.close();
            }
        });
        var taVal = '';
        taVal = this.ifpugFormulaWithoutErrors;
        var textarea = new Ext.form.TextArea({
            value:      taVal,
            allowBlank: false
        });
        textarea.on('render', function() {
            textarea.focus(false, 500);
        }, this);
        textarea.on('change', function(field, newValue, oldValue) {
            okBtn.setDisabled(newValue.length==0);
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
            buttons: [
            okBtn
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
        win.show(this);
    },

    initComponent : function(){
        //Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelEditorDefinitionPanel.superclass.initComponent.apply(this, arguments);
    }

});

