Ext.ux.CaqsElementEditorPanel = Ext.extend(Ext.Panel, {
    layout:             'anchor',
    hideMode:           'offsets',
    autoScroll:         true,
    formPanel:          null,
    infoPanel:          null,
    showSave:           true,
    showCancel:         true,
    showDelete:         true,
    deleteBtn:          undefined,
    saveBtn:            undefined,
    parentSearchScreen: null,
    bodyStyle:          'padding:5px 5px 0',
    border:             false,
    defaults: {
        labelWidth:     150
    },
    formItems:          null,
    idTF:               null,
    elementId:          null,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsElementEditorPanel.superclass.constructor.apply(this, arguments);
    },

    putAdditionalParamsToSave: function(params) {
    //to be overloaded if necessary
    },

    saveFn: function() {
        this.launchSaveRequest('save');
    },

    cancelFn: function() {
        this.parentSearchScreen.finishEdition();
    },

    launchSaveRequest: function(action, endAction, specialScope) {
        var callScope = (specialScope!=undefined)?specialScope:this;
        var params = {};
        for(var i=0; i < this.fieldsDefinitions.length; i++) {
            var fieldName = this.fieldsDefinitions[i].name;
            var value = '';
            var cmp = this.findById(this.id+fieldName);
            if(cmp != null) {
                value = cmp.getValue();
                if(value instanceof Date) {
                    params[fieldName] = value.format(javascriptDateFormat);
                } else {
                    params[fieldName] = value;
                }
            }
        }
        this.putAdditionalParamsToSave(params);
        params['action'] = action;
        params['id'] = this.idTF.getValue();
        Ext.Ajax.request({
            url:	this.saveUrl,
            success:	function() {
                this.parentSearchScreen.reloadGrid();
                if(endAction) {
                    endAction.call(callScope);
                } else {
                    this.cancelFn();
                }
            },
            params:     params,
            scope:      callScope
        });
    },

    deleteFn: function() {
        Ext.MessageBox.confirm(getI18nResource('caqs.confirmDelete.title'),
            getI18nResource('caqs.confirmDelete'), function(btn) {
                if(btn == 'yes') {
                    this.launchSaveRequest('delete');
                }
            }, this);
    },

    specificDataManagement: function(json) {
    //to be overloaded if necessary
    },

    loadDatas: function(response) {
        var json = Ext.util.JSON.decode(response.responseText);
        if(json.id !== null) {
            this.elementId = json.id;
            for(var i=0; i < this.fieldsDefinitions.length; i++) {
                var fieldName = this.fieldsDefinitions[i].name;
                var val = json[fieldName];
                var cmp = this.findById(this.id+fieldName);
                if(cmp != null) {
                    if(cmp.getXType() == 'combo') {
                        var tmpcmp = cmp;
                        var tmpval = val;
                        tmpcmp.store.on('load', function() {
                            tmpcmp.setValue(tmpval);
                        }, this, {
                            single: true
                        });
                        tmpcmp.store.load(/*{
                            params: tmpcmp.additionnalParams
                        }*/);
                    } else {
                        cmp.setValue(val);
                       if(this.fieldsDefinitions[i].type == 'password') {
                         this.findById('check_'+fieldName).setValue(val);
                       }
                    }
                }
            }
            this.specificDataManagement(json);
        }
        Caqs.Portal.hideGlobalLoadingMask();
    },

    setInfoMessage: function(msg) {
        if(msg == null) {
            this.infoPanel.setVisible(false);
        } else {
            this.infoPanel.setVisible(true);
            var spanDoc = document.getElementById(this.id+'ModelEditorInfoPanel');
            if(spanDoc != null) {
                spanDoc.innerHTML = msg;
            }
        }
    },

    createFormItems: function() {
        if(this.fieldsDefinitions) {
            for(var i=0; i < this.fieldsDefinitions.length; i++) {
                var fieldDefinition = this.fieldsDefinitions[i];
                if(fieldDefinition.type != null) {
                    var elt = null;
                    if(fieldDefinition.type == 'id') {
                        this.idTF = new Ext.form.TextField({
                            fieldLabel:     fieldDefinition.fieldLabel,
                            id:             this.id + fieldDefinition.name,
                            msgTarget:      'side',
                            maxLength:      fieldDefinition.maxLength,
                            width:          220,
                            paramName:      fieldDefinition.name
                        });
                        elt = this.idTF;
                    } else if(fieldDefinition.type == 'i18n') {
                        elt = new Ext.ux.CaqsModelerI18NEditor({
                            title:          getI18nResource("caqs.modeleditor.grid.i18nFields"),
                            id:             this.id + fieldDefinition.name
                        });
                    } else if(fieldDefinition.type == 'date') {
                        elt = new Ext.form.DateField({
                            fieldLabel:     fieldDefinition.fieldLabel,
                            id:             this.id + fieldDefinition.name,
                            format:         javascriptDateFormat,
                            width:          220,
                            paramName:      fieldDefinition.name
                        });
                    } else if(fieldDefinition.type == 'staticfield') {
                        elt = new Ext.ux.form.StaticTextField({
                            fieldLabel:     fieldDefinition.fieldLabel,
                            id:             this.id + fieldDefinition.name,
                            paramName:      fieldDefinition.name
                        });
                    } else if(fieldDefinition.type == 'checkbox') {
                        elt = new Ext.form.Checkbox({
                            fieldLabel:     fieldDefinition.fieldLabel,
                            id:             this.id + fieldDefinition.name,
                            paramName:      fieldDefinition.name
                        });
                    } else if(fieldDefinition.type == 'combobox') {

                        var comboStore = new Ext.ux.CaqsJsonStore({
                            url: fieldDefinition.comboBoxStoreUrl,
                            additionnalParams: fieldDefinition.additionnalParams,
                            fields: ['id', 'lib']
                        });
                        comboStore.addListener('beforeload', function(store, opts) {
                            store.baseParams = store.additionnalParams;
                        }, this);
                        
                        elt = new Ext.form.ComboBox({
                            fieldLabel: 	fieldDefinition.fieldLabel,
                            paramName:          fieldDefinition.paramName,
                            name:       	this.id + fieldDefinition.paramName,
                            id:         	this.id + fieldDefinition.name,
                            store:      	comboStore,
                            displayField:	'lib',
                            valueField: 	'id',
                            hiddenName: 	'idComboBox' + this.id + fieldDefinition.paramName,
                            width:		220,
                            editable:		false,
                            forceSelection:	true,
                            triggerAction:	'all',
                            autocomplete:	false,
                            additionnalParams:  fieldDefinition.additionnalParams
                        });
                    } else if(fieldDefinition.type == 'number'){
                        elt = new Ext.form.NumberField({
                            fieldLabel:     fieldDefinition.fieldLabel,
                            id:             this.id + fieldDefinition.name,
                            msgTarget:      'side',
                            width:          220,
                            maxLength:      fieldDefinition.maxLength,
                            allowDecimals:  (fieldDefinition.allowDecimals) ? fieldDefinition.allowDecimals:false,
                            paramName:      fieldDefinition.name
                        });
                    } else if(fieldDefinition.type == 'blank') {
                        elt = {
                            xtype:  'panel',
                            height: 27,
                            border: false,
                            html:   '&nbsp;'
                        }
                    }
                    else if(fieldDefinition.type == 'password') {

                        var elt2 = new Ext.form.TextField({
                        fieldLabel:     fieldDefinition.fieldLabel,
                        id:             'check_' + fieldDefinition.name,
                        msgTarget:      'side',
                        width:          220,
                        maxLength:      fieldDefinition.maxLength,
                        paramName:      fieldDefinition.name,
                        columnNumber:   fieldDefinition.columnNumber,
                        inputType:      'password'
                        });

                        elt2.editorFieldType = fieldDefinition.type;
                        elt2.columnNumber = fieldDefinition.columnNumber;
                        this.formItems[this.formItems.length] = elt2;

                        // Add the additional 'advanced' VTypes
                        Ext.apply(Ext.form.VTypes, {
                            passwordCheck: function(val, field) {
                                if (field.initialPassField) {
                                    var pwd = Ext.getCmp(field.initialPassField);
                                    return (val == pwd.getValue());
                                }
                                return true;
                            },
                            passwordText: getI18nResource("caqs.modeleditor.grid.passwordsDontMatch")
                        });


                        elt = new Ext.form.TextField({
                        fieldLabel:     fieldDefinition.fieldLabel+'(2)',
                        id:             this.id + fieldDefinition.name,
                        msgTarget:      'side',
                        width:          220,
                        maxLength:      fieldDefinition.maxLength,
                        paramName:      fieldDefinition.name,
                        columnNumber:   fieldDefinition.columnNumber,
                        inputType:      'password',
                        vtype: 'passwordCheck',
                        initialPassField: elt2.getId()
                    });



                    }
                    elt.editorFieldType = fieldDefinition.type;
                    elt.columnNumber = fieldDefinition.columnNumber;
                    this.formItems[this.formItems.length] = elt;
                } else {
                    this.formItems[this.formItems.length] = new Ext.form.TextField({
                        fieldLabel:     fieldDefinition.fieldLabel,
                        id:             this.id + fieldDefinition.name,
                        msgTarget:      'side',
                        width:          220,
                        maxLength:      fieldDefinition.maxLength,
                        paramName:      fieldDefinition.name,
                        columnNumber:   fieldDefinition.columnNumber
                    });
                }
            }
        }
        this.createAdditionalFormItems();
    },

    createAdditionalFormItems: function() {
    //to be overloaded if necessary
    },

    setId: function(id) {
        Caqs.Portal.showGlobalLoadingMask();
        this.idTF.setValue(id);
        this.idTF.setDisabled( id != '' && id != null);
        this.deleteBtn.setVisible( id != '' && id != null);
        Ext.Ajax.request({
            url:	this.retrieveDatasUrl,
            success:	this.loadDatas,
            scope:      this,
            params:     {
                id:     this.idTF.getValue()
            }
        });
    },

    initComponent : function(){
        this.formItems = new Array();

        this.createFormItems();

        var buttons = new Array();
        if(this.showSave) {
            this.saveBtn = new Ext.Button({
                text:       getI18nResource('caqs.update'),
                cls:	'x-btn-text-icon',
                icon:	requestContextPath+'/images/database_save.gif',
                handler:    this.saveFn,
                formBind:   true,
                scope:      this
            });
            buttons[buttons.length] = this.saveBtn;
        }
        if(this.showDelete) {
            this.deleteBtn = new Ext.Button({
                text:       getI18nResource('caqs.delete'),
                cls:	'x-btn-text-icon',
                icon:	requestContextPath+'/images/database_delete.gif',
                handler:    this.deleteFn,
                scope:      this
            });
            buttons[buttons.length] = this.deleteBtn;
        }
        if(this.showCancel) {
            buttons[buttons.length] = new Ext.Button({
                text:       getI18nResource('caqs.cancel'),
                cls:	'x-btn-text-icon',
                icon:	requestContextPath+'/images/cross.gif',
                handler:    this.cancelFn,
                scope:      this
            });
        }

        this.infoPanel = new Ext.Panel({
            border:      true,
            frame:       true,
            hidden:      true,
            anchor:      '95%',
            style:       'margin-bottom: 10px; margin-right: 5px;',
            items:       [
            {
                border:  false,
                html:    '<img src="'+requestContextPath+'/images/information.gif" />&nbsp;&nbsp;' +
                '<span id="'+this.id+'ModelEditorInfoPanel"></span>'
            }
            ]
        });

        var itemsPanel = null;
        var columns0Array = new Array();
        var columns1Array = new Array();
        var outsideColumnsArray = new Array();
        var needsColumns = false;

        for(i=0; i<this.formItems.length; i++) {
            var it = this.formItems[i];
            if(it.columnNumber != null) {
                needsColumns = true;
                if(it.columnNumber==1) {
                    columns0Array[columns0Array.length] = it;
                } else {
                    columns1Array[columns1Array.length] = it;
                }
            } else {
                outsideColumnsArray[outsideColumnsArray.length] = it;
            }
        }

        if(needsColumns) {
            var columnsItems = new Ext.Panel({
                layout:     'column',
                border:     false,
                anchor:     '100%',
                items:[
                {//colonne 1
                    columnWidth:    .5,
                    layout:         'form',
                    border:         false,
                    items:          columns0Array
                },
                {
                    columnWidth:    .5,
                    layout:         'form',
                    border:         false,
                    items:          columns1Array
                }
                ]
            });
            itemsPanel = [columnsItems];
            for(i=0; i<outsideColumnsArray.length; i++) {
                itemsPanel[itemsPanel.length] = outsideColumnsArray[i];
            }
        } else {
            itemsPanel = this.formItems;
        }

        this.formPanel = new Ext.form.FormPanel({
            hideMode:       'offsets'
            , anchor:       '95%'
            , border:       false
            , monitorValid: true
            , items:        itemsPanel
            , buttons:      buttons
        });

 
        var config = {
            items:  [
            this.infoPanel
            , this.formPanel
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsElementEditorPanel.superclass.initComponent.apply(this, arguments);
    }

});


