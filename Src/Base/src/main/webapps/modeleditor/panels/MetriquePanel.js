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


Ext.ux.CaqsMetriquePanel = Ext.extend(Ext.ux.CaqsModelEditorElementPanel, {

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsMetriquePanel.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        var config = {
            panelEditor:        new Ext.ux.CaqsMetricEditorPanel({
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
                , {
                   fieldId:    'tool',
                   fieldName:  getI18nResource('caqs.modeleditor.grid.tool')
                }
            ],
            dataStoreUrl:       '/ModelEditorRetrieveMetrics.do',
            allSearchesDefs:    [
                {
                    paramName:  'id',
                    fieldLabel: getI18nResource('caqs.modeleditor.grid.id')
                }
                , {
                    paramName:  'lib',
                    fieldLabel: getI18nResource('caqs.modeleditor.grid.lib')
                }
                , {
                    type:       'combobox',
                    comboBoxStoreUrl: requestContextPath + '/ModelEditorToolsCB.do',
                    paramName:  'tool',
                    fieldLabel: getI18nResource('caqs.modeleditor.grid.tool')
                }
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsMetriquePanel.superclass.initComponent.call(this);
    }

});

