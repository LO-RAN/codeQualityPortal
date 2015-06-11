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


Ext.ux.CaqsLanguePanel = Ext.extend(Ext.ux.CaqsModelEditorElementPanel, {
    

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsLanguePanel.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        var config = {
            autoLoadGrid:       true,
            panelEditor:        new Ext.ux.CaqsLangueEditorPanel({
                id:                     'modelEditorLangueEditor',
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
            ]
            , dataStoreUrl:       '/ModelEditorRetrieveLangues.do'
            , allSearchesDefs:    [
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
        Ext.ux.CaqsLanguePanel.superclass.initComponent.apply(this, arguments);
    }

});

