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


Ext.ux.CaqsUserPanel = Ext.extend(Ext.ux.CaqsModelEditorElementPanel, {
    

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsUserPanel.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        var config = {
            autoLoadGrid:       true,
            panelEditor:        new Ext.ux.CaqsUserEditorPanel({
                id:                     'userEditor',
                parentSearchScreen:     this
            }),
            gridColumns:        [
                {
                   fieldId:    'id',
                   width:      100,
                   fieldName:  getI18nResource('caqs.admin.user.id')
                }
                , {
                   fieldId:    'lastname',
                   width:      200,
                   fieldName:  getI18nResource('caqs.admin.user.lastname')
                }
                , {
                   fieldId:    'firstname',
                   fieldName:  getI18nResource('caqs.admin.user.firstname')
                }
                , {
                   fieldId:    'email',
                   fieldName:  getI18nResource('caqs.admin.user.email')
                }
            ]
            , dataStoreUrl:       '/RetrieveUsers.do'
            , allSearchesDefs:    [
                {
                    paramName:  'id',
                    fieldLabel: getI18nResource('caqs.admin.user.id')
                }
                , {
                    paramName:  'lastname',
                    fieldLabel: getI18nResource('caqs.admin.user.lastname')
                }
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsUserPanel.superclass.initComponent.apply(this, arguments);
    }
});

