Ext.namespace("Ext.ux.grid");

/**
 * @class Ext.ux.grid.GridHeaderFilters
 * @extends Ext.util.Observable
 *
 * Plugin that enables filters in columns headers.
 *
 * To add a grid header filter, put the "filter" attribute in column configuration of the grid column model.
 * This attribute is the configuration of the Ext.form.Field to use as filter in the header:<br>
 *
 * The filter configuration object can include some attributes to manage filter configuration:
 * "filterName": to specify the name of the filter and the corresponding HTTP parameter used to send filter value to server.
 * 					If not specified column "dataIndex" attribute will be used.
 * "value": to specify default value for filter. If no value is provided for filter, this value will be used as default filter value
 * "filterEncoder": a function used to convert filter value returned by filter field "getValue" method to a string. Useful if the filter field getValue() method
 * 						returns an object that is not a string
 * "filterDecoder": a function used to convert a string to a valid filter field value. Useful if the filter field setValue(obj) method
 * 						needs an object that is not a string
 *
 * The GridHeaderFilter constructor accept a configuration object with these properties:
 * "stateful": Switch GridHeaderFilter plugin to attempt to save and restore filters values with the configured Ext.state.Provider. Default true.
 * "height": Height of filters header area. Default 30.
 * "padding": Padding of header filters cells. Default 4.
 * "highlightOnFilter": Enabled grid header highlight if at least one filter is set. Default true.
 * "highlightColor": Color to use when highlight header (see "highlightOnFilter"). Default "orange".
 *
 * This plugin enables "filterupdate" event for the grid:
 * filterupdate(filtername, filtervalue, field)
 *
 * This plugin enables some new grid methods:
 * getHeaderFilter(name)
 * getHeaderFilterField(name)
 * setHeaderFilter(name, value)
 * setHeaderFilters(object, [bReset], [bReload])
 * resetHeaderFilters([bReload])
 *
 * The "name" is the dataIndex of the corresponding column or to the filterName (if specified in filter cfg)
 *
 * @constructor Create a new GridHeaderFilters plugin
 * @param cfg Plugin configuration.
 * @author Damiano Zucconi - http://www.isipc.it
 * @version 1.0.5 - 13/07/2009
 */
Ext.ux.grid.GridHeaderFilters = function(cfg){
    if(cfg) Ext.apply(this, cfg)
        }

Ext.extend(Ext.ux.grid.GridHeaderFilters, Ext.util.Observable,
{
    /**
	 * @cfg {Number} height
	 * Height of filter area in grid header. Default: 32px
	 */
    height: 30,

    /**
	 * @cfg {Number} padding
	 * Padding for filter header cells. Default: 4px
	 */
    padding: 4,

    /**
	 * @cfg {Boolean} highlightOnFilter
	 * Enable grid header highlight if active filters
	 */
    highlightOnFilter: true,

    /**
	 * @cfg {String} highlightColor
	 * Color for highlighted grid header
	 */
    highlightColor: 'orange',

    /**
	 * @cfg {Boolean} stateful
	 * Enable or disable filters save and restore through enabled Ext.state.Provider
	 */
    stateful: true,

    init:function(grid)
    {
        this.grid = grid;
        this.gridView = null;
        this.panels = [];
        //I TD corrispondenti ai vari headers
        this.headerCells = null;
        this.grid.on("render", this.onRender, this);
        this.grid.on("columnmove", this.onRender, this);
        this.grid.on("columnresize", this.onColResize, this);
        if(this.stateful)
        {
            this.grid.on("beforestatesave", this.saveFilters, this);
            this.grid.on("beforestaterestore", this.loadFilters, this);
        }

        this.grid.getColumnModel().on("hiddenchange", this.onColHidden, this);

        this.grid.addEvents({
            "filterupdate": true
        });

        this.grid.stateEvents[this.grid.stateEvents.length] = "filterupdate";

        this.grid.headerFilters = this;

        this.grid.getHeaderFilter = function(sName){
            if(!this.headerFilters)
                return null;
            if(this.headerFilters.filterFields[sName])
                return this.headerFilters.getFieldValue(this.headerFilters.filterFields[sName]);
            else
                return null;
        }

        this.grid.setHeaderFilter = function(sName, sValue){
            if(!this.headerFilters)
                return;
            var fd = {};
            fd[sName] = sValue;
            this.setHeaderFilters(fd);
        }

        this.grid.setHeaderFilters = function(obj, bReset, bReload)
        {
            if(!this.headerFilters)
                return;
            if(bReset)
                this.resetHeaderFilters(false);
            if(arguments.length < 3)
                var bReload = true;
            var bOne = false;
            for(var fn in obj)
            {
                if(this.headerFilters.filterFields[fn])
                {
                    var el = this.headerFilters.filterFields[fn];
                    this.headerFilters.setFieldValue(el,obj[fn]);
                    this.headerFilters.applyFilter(el, false);
                    bOne = true;
                }
            }
            if(bOne && bReload)
                this.headerFilters.storeReload();
        }

        this.grid.getHeaderFilterField = function(sFn)
        {
            if(!this.headerFilters)
                return;
            if(this.headerFilters.filterFields[fn])
                return this.headerFilters.filterFields[fn];
            else
                return null;
        }

        this.grid.resetHeaderFilters = function(bReload)
        {
            if(!this.headerFilters)
                return;
            if(arguments.length == 0)
                var bReload = true;
            for(var fn in this.headerFilters.filterFields)
            {
                var el = this.headerFilters.filterFields[fn];
                this.headerFilters.setFieldValue(el, "");
                this.headerFilters.applyFilter(el, false);
            }
            if(bReload)
                this.headerFilters.storeReload();
        }
    },
    onRender: function()
    {
        if(!this.filters)
            this.filters = {};

        this.filterFields = {};

        //Elimino pannelli esistenti
        for(var pId in this.panels)
        {
            if((this.panels[pId] != null) && (Ext.type(this.panels[pId].destroy) == "function"))
                this.panels[pId].destroy();
        }
        this.panels = [];

        this.cm = this.grid.getColumnModel();
        this.gridView = this.grid.view;
        this.headTr = Ext.DomQuery.selectNode("tr",this.gridView.mainHd.dom);
        this.headerCells = Ext.query("td",this.headTr);

        var cols = this.cm.getColumnsBy(function(){
            return true
            });
        for ( var i = 0; i < cols.length; i++)
        {
            var co = cols[i];
            this.panels[co.dataIndex] = this.createFilterPanel(co, this.grid);
        }
        //Check if some filter is already active
        if(this.isFiltered())
        {
            //Apply filters
            this.storeReload();
            //Highlight header
            this.highlightFilters(true);
        }
    },

    onRefresh: function(){
        this.onRender();
    },

    onColResize: function(index, iWidth){
        var colId = this.cm.getDataIndex(index);
        var panel = this.panels[colId];
        if(panel && (panel != null))
        {
            var filterW = (iWidth < 2) ? 0 : (iWidth - 2);
            panel.setWidth(filterW);
            panel.doLayout();
        }
    },

    onColHidden: function(cm, index, bHidden){
        if(bHidden)
            return;
        var colId = cm.getDataIndex(index);
        var panel = this.panels[colId];
        if(panel && (panel != null))
        {
            var iWidth = cm.getColumnWidth(index);
            var filterW = (iWidth < 2) ? 0 : (iWidth - 2);
            panel.setWidth(filterW);
            panel.doLayout();
        }
    },

    saveFilters: function(grid, status)
    {
        var vals = {};
        for(var name in this.filters)
        {
            vals[name] = this.filters[name];
        }
        status["gridHeaderFilters"] = vals;
        return true;
    },

    loadFilters: function(grid, status)
    {
        var vals = status.gridHeaderFilters;
        if(vals)
        {
            this.filters = {};
            var bOne = false;
            for(var name in vals)
            {
                this.filters[name] = vals[name];
                this.grid.store.baseParams[name] = vals[name];
                bOne = true;
            }
        /*if(bOne)
				this.grid.store.reload();*/
        }

    },

    isFiltered: function()
    {
        for(var k in this.filters)
        {
            if(!Ext.isEmpty(this.filters[k]))
                return true;
        }
        return false;
    },

    highlightFilters: function(enable)
    {
        if(!this.highlightOnFilter)
            return;
        var color = enable ? this.highlightColor : "transparent";
        for(var fn in this.filterFields)
        {
            this.filterFields[fn].ownerCt.getEl().dom.style.backgroundColor = color;
        }
    },

    getFieldValue: function(eField)
    {
        if(Ext.type(eField.filterEncoder) == "function")
            return eField.filterEncoder.call(eField, eField.getValue());
        else
            return eField.getValue();
    },

    setFieldValue: function(eField, value)
    {
        if(Ext.type(eField.filterDecoder) == "function")
            value = eField.filterDecoder.call(eField, value);
        eField.setValue(value);
    },

    applyFilter: function(el, bLoad)
    {
        if(arguments.length < 2)
            bLoad = true;
        if(!el)
            return;

        if(!el.isValid())
            return;

        var sValue = this.getFieldValue(el);

        if(Ext.isEmpty(sValue))
        {
            delete this.grid.store.baseParams[el.filterName];
            delete this.filters[el.filterName];
        }
        else
        {
            this.grid.store.baseParams[el.filterName] = sValue;
            this.filters[el.filterName] = sValue;

            //Controllo che la colonna del filtro applicato sia visibile
            var ci = this.grid.getColumnModel().findColumnIndex(el.dataIndex);
            if((ci >= 0) && (this.grid.getColumnModel().isHidden(ci)))
                this.grid.getColumnModel().setHidden(ci, false);
        }

        //Evidenza filtri se almeno uno attivo
        this.highlightFilters(this.isFiltered());

        this.grid.fireEvent("filterupdate",el.filterName,sValue,el);

        if(bLoad)
            this.storeReload();
    },

    storeReload: function()
    {
        /*if(!Ext.isEmpty(this.grid.store.baseParams["limit"]))
		{
			this.grid.store.baseParams.start = 0;
		}*/
        if(!this.grid.store.lastOptions)
            return;
        var slp = {
            start: 0
        };
        if(this.grid.store.lastOptions.params && this.grid.store.lastOptions.params.limit)
            slp.limit = this.grid.store.lastOptions.params.limit;
        this.grid.store.load({
            params: slp
        });
    },

    createFilterPanel: function(colCfg, grid)
    {
        // = this.cm.findColumnIndex(colCfg.dataIndex);
        //Thanks to dzj
        var iColIndex = this.cm.getIndexById(colCfg.id);
        //var headerTd = Ext.get(this.gridView.getHeaderCell(iColIndex));
        var headerTd = Ext.get(this.headerCells[iColIndex]);
        //Patch for field text selection on Mozilla
        if(Ext.isGecko)
            headerTd.dom.style.MozUserSelect = "text";
        /*var filterPanelDiv = headerTd.createChild({id: Ext.id(), tag: "div"});
		filterPanelDiv.setHeight(this.height);*/
        var filterPanel = null;

        if(colCfg.filter)
        {
            var iColWidth = this.cm.getColumnWidth(iColIndex);
            var iPanelWidth = iColWidth - 2;
            if(colCfg.filter.filterWidth!=null && colCfg.filter.filterWidth>0) {
                iPanelWidth = colCfg.filter.filterWidth;
            }
            //Pannello filtri
            var panelConfig = {
                /*id: "filter-panel-"+colCfg.id,*/
                renderTo: headerTd,
                width: iPanelWidth,
                height: this.height,
                border: false,
                bodyStyle: "padding: "+this.padding+"px; background-color: transparent",
                bodyBorder: false,
                layout: "fit",
                items: [],
                stateful: false
            };

            var filterConfig = {};
            Ext.apply(filterConfig, colCfg.filter);
            filterConfig.dataIndex = colCfg.dataIndex;
            filterConfig.enableKeyEvents = true;
            var filterName = filterConfig.filterName ? filterConfig.filterName : colCfg.dataIndex;

            filterConfig.listeners =
            {
                "change": function(field){
                    this.applyFilter(field)
                    },
                "keypress": function(el,ev){
                    ev.stopPropagation();
                    if(ev.getKey() == Ext.EventObject.ENTER)
                    {
                        //ev.stopEvent();
                        el.el.dom.blur();
                    }
                },
                "select": function(field){
                    this.applyFilter(field)
                    },
                scope: this
            }
            filterConfig.filterName = filterName;
            filterConfig.stateful = false;

            panelConfig.items[panelConfig.items.length] = filterConfig;
            filterPanel = new Ext.Panel(panelConfig);
            var filterField = filterPanel.items.first();
            this.filterFields[filterName] = filterField;
            if(filterConfig.value)
            {
                filterField.setValue(filterConfig.value);
                this.applyFilter(filterField, false);
            }
            else if(!Ext.isEmpty(this.filters[filterName]))
            {
                this.setFieldValue(filterField,this.filters[filterName]);
                this.applyFilter(filterField, false);
            }
        }

        return filterPanel;
    }
});
