Ext.ux.CaqsModelEditorSearchPanel = Ext.extend(Ext.Panel, {
    layout:             'column',
    title:              getI18nResource('caqs.modeleditor.searchfilters'),
    border:             false,
    fields:             undefined,
    filterButton:       undefined,
    allSearchesDefs:    undefined,
    bindedGrid:         undefined,
    height:             150,
    bodyStyle:          'padding:5px 5px 0;',
    parentSearchScreen: undefined,

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsModelEditorSearchPanel.superclass.constructor.apply(this, arguments);
    },

    initComponent : function(){
        Ext.ux.CaqsModelEditorSearchPanel.superclass.initComponent.call(this);
        this.fields = new Array();
        var column1 = new Array();
        var column2 = new Array();
        if(this.allSearchesDefs != null) {
            for(var i=0; i<this.allSearchesDefs.length; i++) {
                var searchField = this.allSearchesDefs[i];
                var col = ((i%2)==0) ? column1 : column2;
                var field = null;
                if(searchField.type == null || searchField.type == 'textfield') {
                    field = new Ext.form.TextField({
                        labelWidth:     150,
                        width:          220,
                        paramName:      searchField.paramName,
                        fieldLabel:     searchField.fieldLabel,
                        id:             this.id + searchField.paramName
                    });
                    field.on('specialkey', function(field, ev) {
                        if(ev.getKey() == ev.ENTER){
                            this.filter();
                        }
                    }, this);
                } else if(searchField.type != null) {
                    if(searchField.type == 'combobox') {
                        var comboStore = new Ext.ux.CaqsJsonStore({
                            url: searchField.comboBoxStoreUrl,
                            fields: [ 'id', 'lib' ]
                        });
                        field = new Ext.form.ComboBox({
                                fieldLabel: 	searchField.fieldLabel,
                                paramName:      searchField.paramName,
                                name:       	'comboBox' + this.id + searchField.paramName,
                                id:         	'comboBox' + this.id + searchField.paramName,
                                store:      	comboStore,
                                displayField:	'lib',
                                valueField: 	'id',
                                hiddenName: 	'idComboBox' + this.id + searchField.paramName,
                                width:		220,
                                editable:	false,
                                forceSelection:	true,
                                triggerAction:	'all',
                                autocomplete:	false
                        });
                    }
                }
                this.fields[this.fields.length] = field;
                col[col.length] = field;
            }
        }

        this.filterButton = new Ext.Button({
            text:       getI18nResource('caqs.modeleditor.search.filterBtn'),
            cls:	'x-btn-text-icon',
            icon:	requestContextPath+'/images/filter.gif',
            handler:    this.filter,
            scope:      this
        });
        //
        //calcul de la hauteur : nombre de lignes de champs de recherche
        //plus bouton de filtre
        this.height = Math.round(this.allSearchesDefs.length / 2) * 25 + 85;

        var config = {
            items:      [
                    {
                        columnWidth:    .49,
                        layout:         'form',
                        border:         false,
                        items:          column1
                    }
                    , {
                        columnWidth:    .49,
                        layout:         'form',
                        border:         false,
                        items:          column2
                    }
            ]
            , buttons: [
                        this.filterButton
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelEditorSearchPanel.superclass.initComponent.call(this);
    },

    addFilterParams: function(store) {
        for(var i=0; i<this.fields.length; i++) {
            var f = this.fields[i];
            store.baseParams[f.paramName] = f.getValue();
        }
    },
    
    filter: function() {
        if(this.bindedGrid != null) {
            var params = {
                start:      0,
                limit:      this.parentSearchScreen.pageSize
            }
            for(var i=0; i<this.fields.length; i++) {
                var f = this.fields[i];
                params[f.paramName] = f.getValue();
            }
            this.parentSearchScreen.loadDatas(params);
        }
    }
});

