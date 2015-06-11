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


Ext.ux.CaqsCriterePanel = Ext.extend(Ext.ux.CaqsModelEditorElementPanel, {

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsCriterePanel.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        var config = {
            panelEditor:        new Ext.ux.CaqsCriterionEditorPanel({
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
            dataStoreUrl:       '/ModelEditorRetrieveCriterions.do',
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
                    paramName:  'goal',
                    fieldLabel: getI18nResource('caqs.modeleditor.grid.goal')
                }
                , {
                    paramName:  'model',
                    fieldLabel: getI18nResource('caqs.modeleditor.grid.model')
                }
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsCriterePanel.superclass.initComponent.call(this);
    }

});

