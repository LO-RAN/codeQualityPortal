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


Ext.ux.CaqsToolPanel = Ext.extend(Ext.ux.CaqsModelEditorElementPanel, {

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsToolPanel.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        var config = {
            autoLoadGrid:       true,
            panelEditor:        new Ext.ux.CaqsToolEditorPanel({
                id:                     'modelEditorToolEditor',
                parentSearchScreen:     this
            }),
            gridColumns:        [
                {
                   fieldId:    'id',
                   width:      100,
                   fieldName:  getI18nResource('caqs.modeleditor.grid.id')
                }
                , {
                   fieldId:    'lib',
                   width:      200,
                   fieldName:  getI18nResource('caqs.modeleditor.grid.lib')
                }
                , {
                   fieldId:    'desc',
                   fieldName:  getI18nResource('caqs.modeleditor.grid.desc')
                }
            ],
            dataStoreUrl:       '/ModelEditorRetrieveTools.do',
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
        Ext.ux.CaqsToolPanel.superclass.initComponent.call(this);
    }

});

