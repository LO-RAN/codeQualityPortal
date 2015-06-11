Ext.ux.CaqsAjaxSearchByNamePanel = function(config) {
    // call parent constructor
    Ext.ux.CaqsAjaxSearchByNamePanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsProjectsTreePanel constructor

Ext.extend(Ext.ux.CaqsAjaxSearchByNamePanel, Ext.form.FieldSet, {
    hideLabels:                 true,
    autoHeight:                 true,
    searchByNamePanelWidth:     485,
    searchFilterButton:         undefined,
    upFilterButton:             undefined,
    resetFilterButton:          undefined,
    filterComboBoxStoreData:    undefined,
    filterComboBox:             undefined,
    lastTypeElt:                undefined,
    filterInput:                undefined,
    hasPointInLastFilter:       false,
    filterValue:                undefined,
    parentPanelElement:         undefined,

    refresh: function() {
        Ext.Ajax.request({
            url: requestContextPath+'/SearchByNamePanelDatas.do',
            scope: this,
            success: function(response) {
                if(response!=undefined && response.responseText!=undefined) {
                    var json = Ext.util.JSON.decode(response.responseText);
                    if(json!=undefined) {
                        this.filterInput.setValue(json.filter);
                        this.filterComboBoxStore.loadData(json.elementTypes, false);
                        this.filterComboBox.setValue(json.typeElt);
                        this.search();
                    }
                }
            }
        });
    },

    getFilter: function() {
        return this.filterInput.getValue();
    },

    getTeltFilter: function() {
        return this.filterComboBox.getValue();
    },

    search: function() {
        var filterWidth = 230;
        var filterValue = this.filterInput.getValue();
        if (filterValue.indexOf(".") > 0) {
            filterWidth = 190;
            this.upFilterButton.setVisible(true);
        } else {
            this.upFilterButton.setVisible(false);
        }
        this.filterInput.setWidth(filterWidth);
        if(this.parentPanelElement!=undefined) {
            this.parentPanelElement.filterDatas(this.filterInput.getValue(), this.filterComboBox.getValue());
        }
    },

    setFilterValue: function(newFilter) {
        this.filterInput.setValue(newFilter);
        this.search();
    },

    searchFilterHandler: function() {
        this.search();
    },

    resetFilterHandler: function() {
        this.filterComboBox.setValue('ALL');
        this.setFilterValue("%");
    },

    upFilterHandler: function() {
        var upFilter = this.filterInput.getValue();
        if (upFilter.indexOf(".") > 0) {
            upFilter = upFilter.substring(0, upFilter.lastIndexOf("."))+'%';
        } else {
            upFilter = '%';
        }
        this.setFilterValue(upFilter);
    },

    initComponent : function(){
        //construction de l'arbre des projets
        this.searchFilterButton = new Ext.Button({
            cellStyle:  'padding-left: 5px;',
            handler:	this.searchFilterHandler,
            cls:	'x-btn-bigicon',
            scope:      this,
            icon:	requestContextPath + '/images/search.gif',
            tooltip:	getI18nResource("caqs.search.title")
        });
        this.upFilterButton = new Ext.Button({
            handler:	this.upFilterHandler,
            cls:	'x-btn-bigicon',
            scope:      this,
            icon:	requestContextPath + '/images/upfolder.gif',
            tooltip:	getI18nResource("caqs.search.parent")
        });
        this.resetFilterButton = new Ext.Button({
            cellStyle:  'padding-left: 5px;',
            handler:	this.resetFilterHandler,
            scope:      this,
            cls:	'x-btn-bigicon',
            icon:	requestContextPath + '/images/clear.gif',
            tooltip:	getI18nResource("caqs.critere.labelresetfilter")
        });

        this.filterComboBoxStore = new Ext.ux.CaqsJsonStore({
            fields:     ['id', 'lib'],
            data :      []
        });

        this.filterComboBox = new Ext.form.ComboBox({
            cellStyle:  'padding-left: 10px;',
            name:       this.id+'typeEltCB',
            id:         this.id+'typeEltCB',
            store:      this.filterComboBoxStore,
            disableKeyFilter: 	true,
            editable:	false,
            autocomplete:   false,
            triggerAction:  'all',
            //style:	'height: 19px',
            height:     19,
            width:	120,
            displayField:'lib',
            valueField: 'id',
            mode:       'local'
        });

        this.filterInput = new Ext.form.TextField({
            name:       this.id+'filter',
            id:         this.id+'filter',
            height:	19,
            width:      230,
            value:      this.lastFilter
        });

        var config = {
            width:	this.searchByNamePanelWidth,
            layout:     'table',
            layoutConfig: {
                columns : 5
            },
            title:      getI18nResource("caqs.searchbynamepanel.recherchenom") +
                '&nbsp;<img ext:qtitle="<img src=\''+requestContextPath+
                '/images/help.gif\' style=\'vertical-align:middle\'>&nbsp;&nbsp;'+getI18nResource("caqs.help")+'" '
                +' ext:qtip="'+getI18nResource("caqs.searchbynamepanel.remplacesuitecar")+'<BR/>'+
                getI18nResource("caqs.searchbynamepanel.remplacecar")+
                '<BR /><span style=\'font-weight: bold;\'>'+getI18nResource("caqs.searchbynamepanel.sensitivesearch")
                +'</span>"' +' src="' + requestContextPath + '/images/help.gif" />',
            items: [
                this.filterInput,
                this.upFilterButton,
                this.filterComboBox,
                this.searchFilterButton,
                this.resetFilterButton
            ]
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsAjaxSearchByNamePanel.superclass.initComponent.apply(this, arguments);
    }
});
