Ext.ux.CaqsModelerI18NEditor = Ext.extend(Ext.grid.EditorGridPanel, {

    viewConfig: {
        forceFit:       true
    },
    enableColumnHide : 	false,
    enableColumnMove : 	false,
    enableHdMenu:	false,
    autoHeight:         true,
    clicksToEdit:	1,
    gridStore:          undefined,
    columnModel:        undefined,
    propertyColumnId:   'property',
    
    listeners:          {
        beforeedit: function(evt) {
            //pas d'edition de la premiere colonne
            if(evt.column == 0)
                return true;
            var textarea = new Ext.form.TextArea({
                value:      evt.value
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
                                    cls:		'x-btn-text-icon',
                                    icon:		requestContextPath+'/images/tick.gif',
                                    handler: function() {
                                        evt.record.set(evt.field, textarea.getValue());
                                        win.close();
                                    }
                                }
                                , {
                                    text:       getI18nResource('caqs.cancel'),
                                    cls:		'x-btn-text-icon',
                                    icon:		requestContextPath+'/images/cross.gif',
                                    handler: function() {
                                        win.close();
                                    }
                                }
                ]
            });
            win.show();

            // cancel this edit, because our pop up will handle it
            return false;
        }
    },

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsModelerI18NEditor.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        var config = {
            store:              new Ext.data.SimpleStore({
                                    fields:         [ {name: this.propertyColumnId} ]
                                }),
            columns:    [
                            {
                                id:     'property',
                                header: getI18nResource("caqs.modeleditor.grid.i18n.header.property")
                            }
            ],
            autoExpandColumn:   this.propertyColumnId
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelerI18NEditor.superclass.initComponent.apply(this, arguments);
    },

    getValue: function() {
        var retour = '';
        var modifiedRecords = this.gridStore.getModifiedRecords();
        if(modifiedRecords && modifiedRecords.length > 0) {
            var obj = new Array();
            /*
             *format : [
             *           {
             *             id : 'lib',
             *             datas: [
             *                  {
             *                      language: 'fr',
             *                      text:     'lib1'
             *                  }
             *                  , {
             *                      language: 'en',
             *                      text:     'libEn'
             *                  }
             *             ]
             *           }
             *           , {
             *             id : 'desc',
             *             datas: [
             *                  {
             *                      language: 'fr',
             *                      text:     'desc1'
             *                  }
             *                  , {
             *                      language: 'en',
             *                      text:     'descEn'
             *                  }
             *             ]
             *           }
             *         ]
             **/
            for(var i=0; i<modifiedRecords.length; i++) {
                var r = modifiedRecords[i];
                var tmpObj = {
                    id:     r.id
                }
                tmpObj.datas = new Array();
                for(var prop in r.modified) {
                    tmpObj.datas[tmpObj.datas.length] = {
                        language:   prop,
                        text:       r.data[prop]
                    }
                }
                obj[obj.length] = tmpObj;
            }
            retour = Ext.util.JSON.encode(obj);
        }
        return retour;
    }, 

    setValue: function(json) {
        if(json!=null && json!='') {
            this.setDatas(json.languagesDefinitions, json.languagesDatas);
        }
    },

    setDatas: function(languagesDefinitions, languagesDatas) {
        var storeArray = new Array();
        var newColumns = new Array();
        newColumns[0] = {
                    id:         this.propertyColumnId,
                    dataIndex:  this.propertyColumnId,
                    header: getI18nResource("caqs.modeleditor.grid.i18n.header.property")
                };
        storeArray[0] = {
                    name:     'id'
                };
        storeArray[1] = {
                    name:     this.propertyColumnId
                };
        for(var i=0; i < languagesDefinitions.length; i++) {
            newColumns[newColumns.length] = {
                    id:         languagesDefinitions[i].id,
                    header:     languagesDefinitions[i].lib,
                    dataIndex:  languagesDefinitions[i].id,
                    editor:     new Ext.form.TextField()
                }
            storeArray[storeArray.length] = {
                name:       languagesDefinitions[i].id
            }
        }
        this.gridStore = new Ext.data.SimpleStore({
            id:             0,
            fields:         storeArray
        });
        this.columnModel = new Ext.grid.ColumnModel(newColumns);
        this.reconfigure(this.gridStore, this.columnModel);
        this.gridStore.loadData(languagesDatas);
        this.getView().fitColumns();
    }

});

