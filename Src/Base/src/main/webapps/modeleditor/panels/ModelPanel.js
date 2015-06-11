/*
 * gridColumns correspond à la définition des colonnes du tableau de résultat de
 * recherches
 * format :
 * [
 *      {
 *          fieldId:    'id',
 *          fieldName:  'Identifiant'
 *      }
 * ]
 */


Ext.ux.CaqsModelPanel = Ext.extend(Ext.ux.CaqsModelEditorElementPanel, {

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsModelPanel.superclass.constructor.apply(this, arguments);
    },

    duplicationEnd: function() {
        Caqs.Portal.hideGlobalLoadingMask();
        this.reloadGrid();
    },

    archiveEnd: function() {
        Caqs.Portal.hideGlobalLoadingMask();
        this.reloadGrid();
    },

    specificJSONReaderData: function(jsonReaderArray) {
        jsonReaderArray[jsonReaderArray.length] = 'formulaWithError';
    },

    importModel: function() {
        var upform = new Ext.ux.CaqsUploadFileForm({
            id:             'modelEditorUploadQualityModelForm',
            title:          '',
            target:         'Modele',
            uploadURL:      requestContextPath + '/ImportModel.do',
            uploadWaitMsg:  getI18nResource('caqs.modeleditor.modelEdition.import.wait'),
            window:         importModelWnd
        });
        var importModelWnd = new Ext.Window({
            layout:     'fit',
            width:      550,
            title:      getI18nResource('caqs.modeleditor.modelEdition.import.title'),
            items:      upform
        });
        upform.window = importModelWnd;
        importModelWnd.show();
    },

    getAdditionnalToolbarButtons: function() {
        var retour = new Array();
        if(canAccessFunction("IMPORT_EXPORT")) {
            retour[0] = new Ext.Button({
                text:       getI18nResource("caqs.modeleditor.modelEdition.import.tooltip"),
                cls:	'x-btn-text-icon',
                icon:	requestContextPath+'/images/database_in.gif',
                handler:    this.importModel,
                scope:      this
            });
        }
        return retour;
    },

    libRenderer: function(value, cell, record) {
        cell.attr += "ext:qtip=\""+value+"\"";
        var retour = '';
        if(record.json.formulaWithError) {
            retour += '<img id="'+record.data.id+'formulaError" src="'+requestContextPath+'/images/errorMsg.gif" />&nbsp;';
            addTooltip(record.data.id+'formulaError', '', getI18nResource("caqs.modeleditor.modelEdition.formulaError"));
        }
        retour += record.data.lib;
        return retour;
    },

    initComponent : function(){
        var actions = new Array();
        actions[actions.length] = {
                iconCls:   'icon-modeleditor-edit',
                tooltip:   getI18nResource('caqs.modeleditor.modelEdition.editer.tooltip')
            };
        actions[actions.length] = {
            iconCls:   'icon-duplicatemodel',
            tooltip:   getI18nResource('caqs.modeleditor.modelEdition.duplicate.tooltip')
        };
        actions[actions.length] = {
            iconCls:   'icon-printmodel',
            tooltip:   getI18nResource('caqs.modeleditor.modelEdition.versionimprimable.tooltip')
        };
        if(canAccessFunction("IMPORT_EXPORT")) {
            actions[actions.length] = {
                iconCls:   'icon-exportmodel',
                tooltip:   getI18nResource('caqs.modeleditor.modelEdition.export.tooltip')
            };
        }
        actions[actions.length] = {
            iconCls:   'icon-archivemodel',
            tooltip:   getI18nResource('caqs.modeleditor.modelEdition.archive.tooltip')
        };
        var rowActions = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:        actions
        });
        rowActions.on({
            scope:  this,
            action: function(grid, record, action, row, col) {
                if(action=='icon-modeleditor-edit') {
                    this.startEdition(record.data.id);
                } else if(action=='icon-duplicatemodel') {
                    Ext.MessageBox.prompt(
                        getI18nResource('caqs.modeleditor.modelEdition.duplicate.askTitle'),
                        getI18nResource('caqs.modeleditor.modelEdition.duplicate.ask'),
                        function(btn, id) {
                            if(btn == 'ok') {
                                Caqs.Portal.showGlobalLoadingMask();
                                Ext.Ajax.request({
                                    url:	requestContextPath+'/DuplicateModel.do',
                                    params: {
                                        usaSrc:      record.data.id,
                                        usaTarget:   id
                                    },
                                    scope:      this,
                                    success:	this.duplicationEnd
                                });
                            }
                        }, this, false);
                } else if(action=='icon-printmodel') {
                    window.open(requestContextPath + '/RetrieveModelPrintableVersion.do?modelId='+record.data.id);
                } else if(action=='icon-archivemodel') {
                    Ext.MessageBox.confirm(
                        '',
                        getI18nResource('caqs.modeleditor.modelEdition.archive.confirm'),
                        function(btn, id) {
                            if(btn == 'yes') {
                                Caqs.Portal.showGlobalLoadingMask();
                                Ext.Ajax.request({
                                    url:	requestContextPath+'/ArchiveModel.do',
                                    params: {
                                        usaSrc:      record.data.id
                                    },
                                    scope:      this,
                                    success:	this.archiveEnd
                                });
                            }
                        }, this, false);
                } else if(action=='icon-exportmodel') {
                    Ext.Ajax.request({
                        url:    requestContextPath+'/ExportModele.do',
                        params: {
                            'idUsa': record.data.id
                        }
                    });
                    window.setTimeout(function() {
                        Caqs.Portal.getCaqsPortal().messagesPanel.checkMessages('false');
                    }, 2000);
                }
            }
        }, this);

        var config = {
            rowActions:         rowActions,
            panelEditor:        new Ext.ux.CaqsModelEditorPanel({
                parentSearchScreen:     this
            }),
            dataStoreUrl:       '/ModelEditorRetrieveModels.do',
            gridColumns:        [
            {
                fieldId:    'id',
                width:      100,
                fieldName:  getI18nResource('caqs.modeleditor.grid.id')
            }
            , {
                fieldId:    'lib',
                width:      200,
                renderer:   this.libRenderer,
                fieldName:  getI18nResource('caqs.modeleditor.grid.lib')
            }
            , {
                fieldId:    'desc',
                fieldName:  getI18nResource('caqs.modeleditor.grid.desc')
            }
            ],
            allSearchesDefs:    [
            {
                paramName:  'id',
                fieldLabel: getI18nResource('caqs.modeleditor.grid.id')
            }
            , {
                paramName:  'lib',
                fieldLabel: getI18nResource('caqs.modeleditor.grid.lib')
            }
            ]
        };
        
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelPanel.superclass.initComponent.call(this);
        this.grid.getView().on('refresh', function() {
            putTooltips();
        }, this);
    }

});

