/*
 * gridColumns correspond à la définition des colonnes du tableau de résultat de
 * recherches
 * format :
 * [
 *      {
 *          fieldId:    'id',
 *          fieldName:  'Identifiant'
 *      }
 *      ...
 * ]
 */
/*
 *un element dans le tableau allSearchesDefs correspond à un champ de recherche
[
    {
        paramName:  'id',
        fieldLabel: 'Identifiant'
    }
    , {
        paramName:  'lib',
        fieldLabel: 'Libellé'
    }
]
 */

Ext.ux.CaqsModelEditorElementPanel = Ext.extend(Ext.Panel, {
    layout:             'card',
    border:             false,
    hideMode:           'offsets',
    tablename:          undefined,
    searchPanel:        undefined,
    grid:               undefined,
    panelEditor:        undefined,
    allSearchesDefs:    undefined,
    gridColumns:        undefined,
    dataStoreUrl:       undefined,
    rowActions:         undefined,
    autoLoad:           false,

    getAdditionnalToolbarButtons: function() {
        return null;
    },


    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsModelEditorElementPanel.superclass.constructor.apply(this, arguments);
    },

    setBeforeLoadSearchFilters: function(store) {
        this.searchPanel.addFilterParams(store);
    },

    createElements: function() {
        this.grid = new Ext.ux.CaqsModelEditorGridPanel({
            listeners:          this.gridListeners,
            gridColumns:        this.gridColumns,
            dataStoreUrl:       this.dataStoreUrl,
            rowActions:         this.rowActions,
            parentSearchScreen: this,
            additionnalToolbarBtns: this.getAdditionnalToolbarButtons()
        });
        this.searchPanel = new Ext.ux.CaqsModelEditorSearchPanel({
            region:             'north',
            allSearchesDefs:    this.allSearchesDefs,
            bindedGrid:         this.grid,
            parentSearchScreen: this
        });
    },

    loadDatas: function(params) {
        if(params == null) {
            params = {
                start:      0,
                limit:      this.searchPanel.pageSize
            }
        }
        this.grid.loadDatas(params);
    },

    startEdition: function(id) {
        this.getLayout().setActiveItem(1);
        this.panelEditor.setId(id);
    },

    finishEdition: function() {
        this.getLayout().setActiveItem(0);
    },

    reloadGrid: function() {
        this.grid.refresh();
    },

    loadGrid: function() {
        this.loadDatas();
    },

    initComponent : function(){
        Ext.ux.CaqsModelEditorElementPanel.superclass.initComponent.apply(this, arguments);
        this.createElements();

        var searchScreen = new Ext.Panel({
            hideMode:       'offsets',
            layout:         'border',
            items:          [
                            this.searchPanel,
                            this.grid
            ]
        });

        var config = {
            activeItem:     0,
            items:  [
                            searchScreen,
                            this.panelEditor
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelEditorElementPanel.superclass.initComponent.apply(this, arguments);
        if(this.autoLoadGrid) {
            this.on('show', this.loadGrid, this);
        }
    },

    refresh: function() {
        this.getLayout().setActiveItem(0);
    }
});

