Ext.ux.CaqsGestionQualiteCritereGrid = Ext.extend(Ext.grid.GridPanel, {
    idCrit:             undefined,
    libCrit:            undefined,
    filter:             undefined,
    teltFilter:         undefined,
    dataStore:          undefined,
    pageSize:           10,
    gridColumns:        undefined,
    critereListSize:    0,
    all:                false,
    full:               undefined,
    idBline:            undefined,
    idEa:               undefined,
    metrics:            undefined,
    bottomBar:          undefined,
    printBtn:           undefined,
    displayAllElementsBtn:undefined,
    displayAllElementsTitle: undefined,
    displayProblemsTitle: undefined,
    displayFormulasBtn: undefined,
    loadingMask:        true,
    autoHeight:         true,
    caqsParentElement:  undefined,
    nbColumns:          0,
    gridWidth:          0,

    getGridStore: function(json) {
        this.metrics = new Array();
        var fields = ['eltBType', 'eltBDesc', 'eltBStereotype', 'eltBId', 'eltBLib', 'eltBHasSource', 'hasJust', 'critBJust',
        'justStatus', 'note', 'critBOldNote',
        'justId', 'justDesc', 'critBNeedJust', 'critTendance', 'critTendanceLib'];
        if(json && json.metriques) {
            for(var i=0; i<json.metriques.length; i++) {
                this.metrics[this.metrics.length] = json.metriques[i].id;
                fields[fields.length] = 'met'+i;
            }
        }
        var newGridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath + '/CritereListAjax.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 'criteres',
                totalProperty: 'totalCount',
                id: 'eltBId',
                fields: fields
            }),
            remoteSort: true
        });
        newGridStore.setDefaultSort('eltBLib', 'asc');
        /*newGridStore.on('beforeload', function() {
            this.dataStore.baseParams.idCrit = this.idCrit;
            this.dataStore.baseParams.idBline = this.idBline;
            this.dataStore.baseParams.all = this.all;
            this.dataStore.baseParams.idEa = this.idEa;
            this.dataStore.baseParams.metrics = this.metrics;
            this.dataStore.baseParams.full = this.full;
        }, this);*/
        return newGridStore;
    },

    getColumnHeaders: function(json) {
        var columnArray = [{
            id:          'filter',
            header:      getI18nResource('caqs.critere.filter'),
            width:       40,
            sortable:    false,
            renderer:    this.renderFilter
        }, {
            header:      getI18nResource('caqs.critere.nom'),
            dataIndex:   'eltBLib',
            width:       450,
            sortable:    true,
            renderer:    this.renderName
        }];
        this.gridWidth = 40 + 475;
        if(json && json.metriques) {
            for(var i=0; i<json.metriques.length; i++) {
                columnArray[columnArray.length] = {
                    header: 	'&nbsp;',
                    width: 	55,
                    dataIndex:	'met'+i,
                    sortable: 	true,
                    align:	'right',
                    renderer: 	this.renderMet
                }
                this.gridWidth += 55;
            }
        }
        columnArray[columnArray.length] = {
            header: 	"&nbsp;",
            renderer: 	this.renderNote,
            dataIndex:	'note',
            align:	'right',
            sortable: 	true,
            width: 	55
        };
        columnArray[columnArray.length] = {
            header: 	getI18nResource('caqs.critere.tendance'),
            width: 	40,
            sortable:	false,
            align:	'center',
            tooltip:	getI18nResource('caqs.critere.tendance.popup'),
            renderer: 	this.renderTendance
        };
        this.gridWidth += 55 + 40;
        this.nbColumns = columnArray.length;
        return new Ext.grid.ColumnModel(columnArray);
    },

    reconfigureGrid: function(json) {
        this.libCrit = json.critLib;
        this.dataStore = this.getGridStore(json);
        var newcolumnmodel = this.getColumnHeaders(json);
        this.reconfigure(this.dataStore, newcolumnmodel);
        this.bottomBar.bind(this.dataStore);
        this.setWidth(this.gridWidth);
        this.getView().fitColumns();
    },

    versionImprimableFct: function() {
        if(!this.full || this.full=='false') {
            var url = 'CritereSelect.do?id_crit='+this.idCrit+'&full=true&sort=NAMEDESC&sortpart=NAMEDESC&all='+this.all+'&vi=true';
            PopupCentrer(url);
        }
    },

    displayAllElementsFn: function() {
        this.all = !this.all;
        if(this.all) {
            this.displayAllElementsBtn.setText(this.displayProblemsTitle);
        } else {
            this.displayAllElementsBtn.setText(this.displayAllElementsTitle);
        }
        this.reload();
    },

    renderFilter: function(val, p, record, rowIndex, colIndex, store) {
        var result = '';
        if (record.data.eltBType!="EA" && record.data.eltBType!="MET") {
            p.attr = 'style="cursor:pointer;" ext:qtip="<img src=\''+requestContextPath+'/images/help.gif\'/>&nbsp;&nbsp;'+getI18nResource("caqs.critere.filterImgTooltip")+'"';
            addTooltip('filterIMG'+record.data.eltBId,
                '', '<img src="'+requestContextPath+'/images/help.gif">&nbsp;&nbsp;'+getI18nResource("caqs.critere.filterImgTooltip"));
            result += '<IMG id="filterIMG'+record.data.eltBId+'" src="'+requestContextPath+'/images/filter.gif" border="0">';
        } else {
            result += '<img src="'+requestContextPath+'/images/empty16.gif" />&nbsp;';
        }
        
        return result;
    },

    renderName: function(val, p, record, rowIndex, colIndex, store) {
        //nouvelle image pour le lien
        var result = '';
        p.attr = 'ext:qmaxForcedWidth=900 ext:qtip="<img src=\''+requestContextPath+'/images/info.gif\' style=\'vertical-align:middle\'>&nbsp;&nbsp;'
        + record.data.eltBDesc+'" ';
        addTooltip('name'+record.data.eltBId, '',
            '<img src="'+requestContextPath+'/images/info.gif" style="vertical-align:middle">&nbsp;&nbsp;' + record.data.eltBDesc,
            0, 'auto', 900);
        result += "<span id=\"name"+record.data.eltBId+"\" >";

        if (record.data.eltBStereotype != null && record.data.eltBStereotype != '') {
            addTooltip('stereotype'+record.data.eltBId, '',
                '<img src="'+requestContextPath+'/images/help.gif" style="vertical-align:middle">&nbsp;&nbsp;' + record.data.eltBStereotype);
            result += "<img id='stereotype"+record.data.eltBId+"' src='"+requestContextPath+"/images/stereotype.gif' />&nbsp;"
        }
        if ((record.data.eltBType != null) && (record.data.eltBType == "MET")) {
            if (this.grid.idCrit == 'ANTI_COPIER_COLLER') {
                result += "<A href='javascript:PopupCentrer(\"./LinkSelect.do?id_elt=" + record.data.eltBId
                + "&desc_elt=" + record.data.eltBDesc + "&id_bline="+this.grid.idBline
                + "&id_crit="+this.grid.idCrit+"&state=20\",800,600,\"menubar=no,statusbar=no,scrollbars=yes,resizable=yes\")'>";
                result += record.data.eltBLib;
                result += "</A>";
            } else {
                result += record.data.eltBLib;
            }
        } else {
            if ((record.data.eltBType != null) && (record.data.eltBType == "EA")) {
                if(this.grid.idCrit.startsWith("ANTI_ARCHI")) {
                    var state = REAL_LINK_ANTI; // équivaut à id_crit.equals("ANTI_ARCHI_CS")
                    if (this.grid.idCrit == "ANTI_ARCHI_NP") {
                        state = REAL_LINK_NOTEXPECTED;
                    }
                    result += "<A href='javascript:PopupCentrer(\"./LinkSelect.do?id_elt=" + record.data.eltBId
                    + "&id_bline="+this.grid.idBline+"&id_crit="+this.grid.idCrit+"&state="+state+"\",800,600,\"menubar=no,statusbar=no,scrollbars=yes,resizable=yes\")'>";
                    result += record.data.eltBLib;
                    result += "</A>";
                } else {
                    result += record.data.eltBLib;
                }
            } else {
                result += record.data.eltBLib;
            }
        }
        if (this.grid.idCrit == "ANTI_COPIER_COLLER") {
            addTooltip('antiCopierColler'+record.data.eltBId, '',
                "<img src='"+requestContextPath+"/images/help.gif' style='vertical-align:middle'>&nbsp;&nbsp;"+getI18nResource('caqs.critere.srcfileaccess'));
            result += "&nbsp;<a href=\"CopyPasteRetrieve.do?id_elt=" + record.data.eltBId
            + "&desc_elt=" + record.data.eltBDesc + "&id_bline="+this.grid.idBline+"&id_crit="+this.grid.idCrit+"\" target=\"_blank\"><IMG id='antiCopierColler"+record.data.eltBId
            +"' src='"+requestContextPath+"/images/page_white_copy.gif' border='0'></a>";
        } else {
            if (record.data.eltBHasSource) {
                addTooltip('hasSource'+record.data.eltBId, '',
                    "<img src='"+requestContextPath+"/images/help.gif' style='vertical-align:middle'>&nbsp;&nbsp;"+getI18nResource('caqs.critere.srcfileaccess'));
                result += "&nbsp;<a href=\"RetrieveSourceFile.do?id_elt=" + record.data.eltBId
                + "&id_bline="+this.grid.idBline+"&id_crit="+this.grid.idCrit+"\" target=\"_blank\"><IMG id='hasSource"+record.data.eltBId+"' src='"+requestContextPath+"/images/loupe.gif' border='0'></a>";
            }
        }
        addTooltip('impactGraph'+record.data.eltBId, '',
            "<img src='"+requestContextPath+"/images/help.gif' style='vertical-align:middle'>&nbsp;&nbsp;"+getI18nResource('caqs.critere.callgraphaccess'));
        result += "&nbsp;<a style=\"cursor:pointer;\" onClick=\"closeExistingWindow();setTimeout('parent.parent.openedGraphWindow=window.open(\\'impactgraph.jsp?id_elt=" + record.data.eltBId + "&id_bline="+this.grid.idBline+"&id_crit="+this.grid.idCrit+"&desc_elt=" + record.data.eltBDesc + "\\', \\'impactgraph\\')', 750);\"><IMG id='impactGraph"+record.data.eltBId+"' src='"+requestContextPath+"/images/graph.gif' border='0' /></a>";
        if (this.grid.idCrit == "CYCLICDEPENDENCY") {
            addTooltip('adpFileAccess'+record.data.eltBId, '',
                "<img src='"+requestContextPath+"/images/help.gif' style='vertical-align:middle'>&nbsp;&nbsp;"+getI18nResource('caqs.critere.adpfileaccess'));
            result += "&nbsp;<a href=\"RetrieveADPFile.do?id_elt=" + record.data.eltBId
            + "&id_bline="+this.grid.idBline+"&id_crit="+this.grid.idCrit+"\" target=\"_blank\"><IMG id='adpFileAccess"+record.data.eltBId+"' src='"+requestContextPath+"/images/cycle.gif' border='0'></a>";
        }
        result += "</span>";
        return result;
    },

    renderNote: function(val, p, record, rowIndex, colIndex, store) {
        var result = "";
        if (record.data.hasJust) {
            if (record.data.justStatus == "DEMAND") {
                result += '<span class="demandJustif" style="cursor:pointer;">';
                result += record.data.note;
                result += "</span>&nbsp;";
                addTooltip('demandJustif'+record.data.eltBId,
                    "<img src='"+requestContextPath+"/images/info.gif' style='vertical-align:middle'>&nbsp;&nbsp;"+getI18nResource('caqs.facteursynthese.demandecours'),
                    record.data.justDesc);
                result += "<IMG id='demandJustif"+record.data.eltBId+"' src='"+
                requestContextPath+"/images/encours.gif' />";
            } else {
                if (record.data.justStatus == "REJET") {
                    addTooltip('rejetJustif'+record.data.eltBId,
                        "<img src='"+requestContextPath+"/images/info.gif' style='vertical-align:middle'>&nbsp;&nbsp;"+getI18nResource('caqs.facteursynthese.demanderejetee'),
                        record.data.justDesc);
                    result += "<span class='rejetJustif'>" + record.data.note + "</span>&nbsp;";
                    result += "<IMG id='rejetJustif"+record.data.eltBId+"' src='"+requestContextPath+"/images/delete.gif' />";
                } else {
                    addTooltip('validJustif'+record.data.eltBId,
                        "<img src='"+requestContextPath+"/images/info.gif' style='vertical-align:middle'>&nbsp;&nbsp;"+getI18nResource('caqs.facteursynthese.accepte'),
                        record.data.justDesc);
                    result += "<span class='validJustif'>" + record.data.note + "</span>&nbsp;";
                    result += "<IMG id='validJustif"+record.data.eltBId+"' src='"+requestContextPath+"/images/tick.gif' />";
                }
            }
        } else {
            if (record.data.critBNeedJust) {
                addTooltip('toJustif'+record.data.eltBId, '',
                    "<img src='"+requestContextPath+"/images/help.gif' style='vertical-align:middle'>&nbsp;&nbsp;"+getI18nResource('caqs.facteursynthese.justif')
                    );
                result += '<span id="toJustif"'+record.data.eltBId+'" class="toJustif" '+
                ' style="cursor:pointer;" >';
                result += record.data.note;
                result += "</span>";
            } else {
                result += record.data.note;
            }
            result += "&nbsp;";
        }
        return result;
    },

    renderTendance: function(val, p, record) {
        p.attr = 'ext:qtip="'+record.data.critTendanceLib+'"';
        addTooltip('trend'+record.data.eltBId,
            '',
            record.data.critTendanceLib);
        var result = "&nbsp;<IMG id='trend"+record.data.eltBId+"' src='images/note_" + record.data.critTendance + ".gif' />";
        return result;
    },

    displayFormulas: function() {
        Ext.Ajax.request({
            url:	requestContextPath + '/CritereDisplayFormulas.do',
            success:function(response) {
                if(response != null) {
                    var json = Ext.util.JSON.decode(response.responseText);
                    if(json && json.dataArray) {
                        var array = json.dataArray;
                        var msg = '';
                        var first = true;
                        for(var i=0; i<array.length; i++) {
                            var formule = array[i];
                            if(!first) {
                                msg += '<BR />';
                            }
                            if(formule.formula != 'true') {
                                msg += ' - '+ getI18nResource('caqs.modeleditor.modelEdition.formula.mark', formule.score, formule.formula);
                            } else {
                                msg += ' - '+ getI18nResource('caqs.modeleditor.modelEdition.formula.markAlwaysTrue', formule.score);
                            }
                            first = false;
                        }
                        if(msg != '') {
                            Ext.Msg.alert('', msg);
                        }
                    }
                }
            },
            params:     {
                id_crit:     this.idCrit
            },
            scope:      this
        });
    },

    constructor: function(config) {
        Ext.apply(this, config);
        Ext.ux.CaqsGestionQualiteCritereGrid.superclass.constructor.apply(this, arguments);
    },

    columnRenderer: function(val, cell, record) {
        cell.attr += "ext:qtip=\""+val+"\"";
        return val;
    },

    renderMet: function(val, p, record, rowIndex, colIndex, store) {
        var retour = null;
        if (record.data['met'+(colIndex-2)] != null && record.data['met'+(colIndex-2)] != '') {
            retour = record.data['met'+(colIndex-2)];
        }
        else {
            p.attr = 'style="cursor: pointer;" ext:qtip="<img src=\''+requestContextPath+'/images/help.gif\' style=\'vertical-align:middle\' />&nbsp;&nbsp;'+getI18nResource("caqs.critere.saisieMetrique")+'"';
            addTooltip('enterMet'+record.data.eltBId, '',
                '<img src="'+requestContextPath+'/images/help.gif" style="vertical-align:middle" />&nbsp;&nbsp;'+getI18nResource("caqs.critere.saisieMetrique"));
            retour = '&nbsp;<span id="enterMet'+record.data.eltBId+'">-</span>&nbsp;';
        }
        return retour;
    },

    initComponent : function(){
        Ext.ux.CaqsGestionQualiteCritereGrid.superclass.initComponent.apply(this, arguments);
        this.dataStore = this.getGridStore(null);

        this.bottomBar = new Ext.PagingToolbar({
            pageSize: 		this.pageSize,
            store: 		this.dataStore,
            displayInfo: 	true,
            displayMsg: 	'{0} - {1} / {2}'
        });

        var config = {
            header:		false,
            cm:                 this.getColumnHeaders(null),
            store:              this.dataStore,
            enableColumnHide : 	false,
            enableColumnMove : 	false,
            enableColumnResize:	false,
            enableHdMenu:	false,
            trackMouseOver:	true,
            sm: 		new Ext.grid.RowSelectionModel({
                selectRow:Ext.emptyFn
            }),
            loadMask: 		true,
            bbar: 		this.bottomBar

        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));

        this.printBtn = new Ext.Button({
            tooltip:    getI18nResource("caqs.critere.versionimprimable"),
            icon:       requestContextPath+'/images/printer.gif',
            cls:        'x-btn-icon',
            handler:    this.versionImprimableFct,
            scope:      this
        });

        this.displayAllElementsTitle = getI18nResource('caqs.critere.displayAll');
        this.displayProblemsTitle = getI18nResource('caqs.critere.displayallproblems');

        this.displayAllElementsBtn = new Ext.Button({
            text:   this.displayAllElementsTitle,
            handler:this.displayAllElementsFn,
            scope:  this
        });

        this.displayFormulasBtn = new Ext.Button({
            tooltip:getI18nResource("caqs.critere.displayFormulas"),
            icon:	requestContextPath+'/images/calculator_add.gif',
            cls:    'x-btn-icon',
            handler:this.displayFormulas,
            scope:  this
        });
        this.addListener('render', this.onRenderFn, this);
        this.addListener('cellclick', this.cellClick, this);
        Ext.ux.CaqsGestionQualiteCritereGrid.superclass.initComponent.apply(this, arguments);
        this.getView().on('refresh', function() {
            putTooltips();
        }, this);
    },

    cellClick: function(grid, rowIndex, columnIndex, evt) {
        var record = grid.getStore().getAt(rowIndex);
        if(columnIndex==0) {//filtre
            if(record.data.eltBType!="EA" && record.data.eltBType!="MET") {
                this.caqsParentElement.setElementFilter(record.data.eltBDesc +'%');
            }
        } else if(record.data['met'+(columnIndex-2)] != null) {//metrique
            if(record.data['met'+(columnIndex-2)] == '') {
                var qametUpdateWnd = new Ext.ux.CaqsQAMetriqueChangeWindow({
                    parentGrid:     this
                });
                qametUpdateWnd.loadDatas(record.data.eltBId, record.data.eltBDesc, this.metrics[columnIndex-2]);
                qametUpdateWnd.show();
            }
        } else if(columnIndex == (this.nbColumns-2)) {
            if(canAccessFunction("Justification")
              && ((record.data.hasJust && record.data.justStatus == "DEMAND")
                    || (record.data.critBNeedJust)) ) {
                var justificationDemandWnd = new Ext.ux.CaqsJustificationDemandWindow({
                    parentGridElement: this
                });
                justificationDemandWnd.refresh(record.data.eltBId,
                    this.idBline, this.idCrit, record.data.justId,
                    record.data.note, record.data.eltBDesc, this.libCrit);
                justificationDemandWnd.show();
            }
        }
    },

    loadDatas: function(idCrit, filter, teltFilter, idBline, idEa, all) {
        this.idCrit = idCrit;
        this.idBline = idBline;
        this.idEa = idEa;
        this.teltFilter = teltFilter;
        this.filter = filter;
        this.all = all;
        this.dataStore.on('beforeload', function() {
            this.dataStore.baseParams.idCrit = this.idCrit;
            this.dataStore.baseParams.filter = this.filter;
            this.dataStore.baseParams.typeElt = this.teltFilter;
            this.dataStore.baseParams.all = this.all;
        }, this);
        this.dataStore.load({
            params:{
                start:      0,
                limit:      10,
                fromCache:  false
            }
        });
    },

    reload: function() {
        this.dataStore.load({
            params:{
                start:      0,
                limit:      10,
                fromCache:  false
            }
        });
    },

    onRenderFn: function() {
        this.bottomBar.add('-');
        this.bottomBar.add(this.printBtn);
        this.bottomBar.add('-');
        this.bottomBar.add(this.displayAllElementsBtn);
        this.bottomBar.add('-');
        this.bottomBar.add(this.displayFormulasBtn);
        this.getView().on('refresh', function() {
            putTooltips();
        });
    }
});

