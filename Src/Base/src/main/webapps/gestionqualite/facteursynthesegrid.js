Ext.ux.CaqsFacteurSyntheseGridPanel = function(config) {
    Ext.ux.CaqsFacteurSyntheseGridPanel.superclass.constructor.call(this, config);
};

Ext.extend(Ext.ux.CaqsFacteurSyntheseGridPanel, Ext.grid.GridPanel, {
    header:		false,
    enableColumnHide : 	false,
    enableColumnMove : 	false,
    enableHdMenu:	false,
    height:		350,
    //width:		755,
    autoWidth:          true,
    //autoHeight:         true,
    frame:		false,
    loadMask: 		true,
    enableColumnResize:         false,
    gridStore:                  undefined,
    factorComment:              undefined,
    factorCommentButton:        undefined,
    gridBottomBar:              undefined,
    factorCommentWindow:        undefined,
    criterionCommentWindow:     undefined,
    editedCriterionCommentId:   undefined,
    factorBeanId:           undefined,
    facteursynthese:    undefined,


    initCriterionCommentWindow: function() {
        this.criterionCommentWindow = new Ext.ux.CaqsCommentEditWindow({
            title:          getI18nResource("caqs.facteursynthese.editcomment"),
            parentElement:  this,
            updateValue: function(val) {
                var newValue = val;
                var record = this.parentElement.store.getById(this.parentElement.editedCriterionCommentId);
                record.data.commentValue = newValue;
                record.data.iconImgClass = (newValue=='')?'icon-comment-edit':'icon-has-comment';
                this.parentElement.getView().refresh();
                Ext.Ajax.request({
                    url: requestContextPath+'/FacteurSyntheseManageComment.do',
                    params: {
                        'idCrit':  	record.data.idCrit,
                        'comment':      newValue
                    },
                    success: function() {
                        //this.gridStore.reload();
                    },
                    scope:  this.parentElement
                });
            }
        });
    },

    editComment: function(record) {
        if(canAccessFunction("TOP_DOWN_FACTOR_COMMENT")) {
            if(this.criterionCommentWindow==undefined) {
                this.initCriterionCommentWindow();
            }
            this.editedCriterionCommentId = record.data.idCrit;
            this.criterionCommentWindow.setComment(record.data.commentValue);
            this.criterionCommentWindow.show(this);
        }
    },

    initFactorCommentWindow: function() {
        this.factorCommentWindow = new Ext.ux.CaqsCommentEditWindow({
            title:          getI18nResource("caqs.facteursynthese.editcomment"),
            parentElement:  this,
            updateValue: function(val) {
                var newValue = val;
                Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
                Ext.Ajax.request({
                    url: requestContextPath+'/FacteurSyntheseManageComment.do',
                    params: {
                        'idFact':  	this.parentElement.factorBeanId,
                        'comment':      newValue
                    },
                    success: function() {
                        this.parentElement.factorComment = newValue;
                        this.parentElement.factorCommentButton.setTooltip(newValue);
                        var iconPath = (newValue=='') ? requestContextPath+'/images/comment.gif':requestContextPath+'/images/hasComment.gif';
                        this.parentElement.factorCommentButton.setIcon(iconPath);
                        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
                    },
                    scope:  this
                });
            }
        });
    },

    editFactorComment: function() {
        if(canAccessFunction("TOP_DOWN_FACTOR_COMMENT")) {
            if(this.factorCommentWindow==undefined) {
                this.initFactorCommentWindow();
            }

            this.factorCommentWindow.setComment(this.factorComment);
            this.factorCommentWindow.show(this);
        }
    },

    setTooltips: function() {
        var msg = getI18nResource("caqs.facteursynthese.legende")+"<BR />";
        msg += '<IMG src="images/red.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource("caqs.facteursynthese.notede", "1")+'<BR />';
        msg += '<IMG src="images/orange.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource("caqs.facteursynthese.notede", "2")+'<BR />';
        msg += '<IMG src="images/darkgreen.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource("caqs.facteursynthese.notede", "3")+'<BR />';
        msg += '<IMG src="images/green.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+getI18nResource("caqs.facteursynthese.notede", "4");
        addTooltip('repartitionHelp',
            "<img src=\""+requestContextPath+"/images/help.gif\" />&nbsp;"+getI18nResource("caqs.facteursynthese.repartition.tooltip"),
            msg, 0, 450);
    },

    renderTrend: function(value, p, record){
        p.attr = 'ext:qtip="'+record.data.tendancePopup+'"';
        addTooltip('trend'+record.data.idCrit, '', record.data.tendancePopup);
        var retour = '<IMG id="trend'+ record.data.idCrit +'" src="images/note_'+record.data.tendanceLabel+'.gif" />';
        return retour;
    },

    critereDetailRenderer: function(val, cell, record) {
        var retour = '';
        if(record.data.isIncludedInActionPlan) {
            addTooltip('includedCriterion'+record.data.idCrit,
                '<img src=\''+requestContextPath+'/images/info.gif\' style=\'vertical-align:middle\'>&nbsp;&nbsp;'+getI18nResource("caqs.facteursynthese.actionPlanPopupTitle"),
                getI18nResource("caqs.facteursynthese.actionPlanCost") + record.data.totalCost+' '+record.data.actionPlanUnit
                );
            retour += '<img id="includedCriterion'+record.data.idCrit+'" src="'+requestContextPath+'/images/flag_red.gif" />';
        }
        if(canAccessFunction("Criterion_Detail")) {
            retour += '<span style="cursor: pointer;">'+record.data.libCrit+'</span>';
        } else {
            retour += record.data.libCrit;
        }
        return retour;
    },

    noteDetailRenderer: function(val, cell, record, rowIndex, colIndex, store) {
        var retour = '';
        cell.attr = '';
        if(record.data.hasJustification) {
            if(record.data.justStatus=='DEMAND') {
		if(canAccessFunction("Justification")) {
                    cell.attr = "ext:qtip='<img src=\""+requestContextPath
                        +"/images/info.gif\">&nbsp;&nbsp;"+getI18nResource("caqs.facteursynthese.justif")+"'";
                }
                retour = '<span style="cursor:pointer;" class="demandJustif">'+record.data.formattedMark+'</span>';
            } else if(record.data.justStatus=='REJET') {
                cell.attr = "ext:qtitle='<img src=\""+requestContextPath
                +"/images/info.gif\">&nbsp;&nbsp;"+getI18nResource("caqs.facteursynthese.demanderejetee")+"'";
                cell.attr += " ext:qtip=\""+record.data.justDesc+"\" ";
                addTooltip('justifCTooltip'+record.data.idCrit,
                    '<img src=\"'+requestContextPath
                    +'/images/info.gif\">&nbsp;&nbsp;'+getI18nResource("caqs.facteursynthese.demanderejetee"),
                    record.data.justDesc
                    );
                retour = '<span id="justifCTooltip'+record.data.idCrit+'" >';
                retour += record.data.formattedMark+'</span>';
            } else if(record.data.justStatus=='VALID') {
                cell.attr = "ext:qtitle='<img src=\""+requestContextPath
                +"/images/info.gif\">&nbsp;&nbsp;"+getI18nResource("caqs.facteursynthese.accepte")+"'";
                cell.attr += " ext:qtip=\""+record.data.justDesc+"\" ";
                addTooltip('justifBTooltip'+record.data.idCrit,
                    '<img src=\"'+requestContextPath
                    +'/images/info.gif\">&nbsp;&nbsp;'+getI18nResource("caqs.facteursynthese.accepte"),
                    record.data.justDesc
                    );
                retour = '<span id="justifBTooltip'+record.data.idCrit+'" class="validJustif">';
                retour += record.data.formattedJustMark+'</span>';
            }
        } else {
            if(record.data.note<3 && record.data.note>0) {
                if(canAccessFunction("Justification")) {
                    cell.attr = "ext:qtip='<img src=\""+requestContextPath
                    +"/images/info.gif\">&nbsp;&nbsp;"+getI18nResource("caqs.facteursynthese.justif")+"'";
                    addTooltip('justifATooltip'+record.data.idCrit,
                        '',
                        '<img src=\"'+requestContextPath
                        +'/images/info.gif\">&nbsp;&nbsp;'+getI18nResource("caqs.facteursynthese.justif")
                        );
                    retour = '<span id="justifATooltip'+record.data.idCrit
                    + '" class="toJustif" style="cursor:pointer;" >'+record.data.formattedMark+'</span>';
                } else {
                    retour = '<SPAN class="toJustif">' + record.data.formattedMark + '</SPAN>';
                }
            } else {
                if(record.data.note!=0) {
                    retour = record.data.formattedMark;
                } else {
                    retour = '&nbsp;-&nbsp;';
                }
            }
        }
        return retour;
    },

    agregationRenderer: function(val, cell, record, rowIndex, colIndex, store) {
        cell.attr = "ext:qtip='"+record.data.libTelt+"'";
        return record.data.idTelt;
    },

    weightRenderer: function(val, cell, record) {
        return record.data.formattedWeight;
    },

    justificationRenderer: function(val, cell, record, rowIndex, colIndex, store) {
        var retour = '&nbsp;';
        cell.attr = '';
        if(record.data.hasJustification) {
            var title = "<IMG src=\""+requestContextPath+"/images/info.gif\" />&nbsp;&nbsp;";
            var imgName = '';
            if(record.data.justStatus=='DEMAND') {
                title += getI18nResource("caqs.facteursynthese.demandecours");
                imgName = 'encours';
            } else if(record.data.justStatus=='REJET') {
                title += getI18nResource("caqs.facteursynthese.demanderejetee");
                imgName = 'delete';
            } else {
                title += getI18nResource("caqs.facteursynthese.accepte");
                imgName = 'tick';
            }
            retour = '<IMG id="justifImg'+rowIndex+'" src="'+requestContextPath
            +'/images/' + imgName + '.gif" />';
            cell.attr = "ext:qtitle='"+title+"' ";
            cell.attr += "ext:qtip=\""+record.data.justDesc+"\"";
            addTooltip('justifImg'+rowIndex, title, record.data.justDesc);
        }
        return retour;
    },

    renderRepartition: function(val, p, record) {
        var result = '&nbsp;';

        if(record.data.pct0 > 0) {
            addTooltip(record.data.idCrit+'0', '',
                '<IMG src="'+requestContextPath
                +'/images/red.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'
                +record.data.repartitionPopup0);
            result += '<IMG id="'+record.data.idCrit+'0" src="'+requestContextPath+'/images/red.gif" height="15" border="0" width="'
            + 1.7*record.data.pct0+'" />';
        }
        if(record.data.pct1 > 0) {
            addTooltip(record.data.idCrit+'1', '',
                '<IMG src="'+requestContextPath+'/images/orange.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'
                +record.data.repartitionPopup1);
            result += '<IMG id="'+record.data.idCrit+'1" src="'
            +requestContextPath+'/images/orange.gif" height="15" border="0" width="'
            + 1.7*record.data.pct1+'" />';
        }
        if(record.data.pct2 > 0) {
            addTooltip(record.data.idCrit+'2', '',
                '<IMG src="'+requestContextPath+'/images/darkgreen.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.repartitionPopup2);
            result += '<IMG id="'+record.data.idCrit+'2" src="'
            +requestContextPath+'/images/darkgreen.gif" height="15" border="0" width="'
            + 1.7*record.data.pct2+'" />';
        }
        if(record.data.pct3 > 0) {
            addTooltip(record.data.idCrit+'3', '',
                '<IMG src="'+requestContextPath+'/images/green.gif" style="height:10px; border: 0; width: 10px;" />&nbsp;'+record.data.repartitionPopup3);
            result += '<IMG id="'+record.data.idCrit+'3" src="'
            +requestContextPath+'/images/green.gif" height="15" border="0" width="'
            + 1.7*record.data.pct3+'" />';
        }

        return result;
    },
    
    cellClick: function(grid, rowIndex, columnIndex, evt) {
        var record = grid.getStore().getAt(rowIndex);
        if(columnIndex == 2) {
            if( ((record.data.hasJustification && record.data.justStatus=='DEMAND')
                || (record.data.note<3 && record.data.note>0))
            && canAccessFunction("Justification")) {
                var justificationDemandWnd = new Ext.ux.CaqsJustificationDemandWindow({
                    parentGridElement: this
                });
                justificationDemandWnd.refresh(record.data.idElt,
                    record.data.idBline, record.data.idCrit, record.data.justId,
                    record.data.note, record.data.eltLib, record.data.libCrit);
                justificationDemandWnd.show();
            }
        } else if(columnIndex == 1) {
            if(canAccessFunction("Criterion_Detail")) {
                this.facteursynthese.criterionDetail.load(record.data.idCrit);
                this.facteursynthese.setActiveItem(1);
            }
        }
    },

    reloadGrid: function(goalId) {
        this.factorBeanId = goalId;
        this.gridStore.load({
            params: {
                'start':		0,
                'id_fact':		this.factorBeanId,
                'limit':		-1
            }
        });
    },

    initComponent : function(){
        Ext.ux.CaqsFacteurSyntheseGridPanel.superclass.initComponent.apply(this, arguments);

        var fullRepartitionTitle = getI18nResource("caqs.facteursynthese.repartition")
        +"&nbsp;<img id=\"repartitionHelp\" src=\""
        +requestContextPath+"/images/help.gif\" />";
        this.gridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/FacteurSyntheseListAjax.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 'criteres',
                totalProperty: 'totalCount',
                id: 'idCrit',
                goalComment: 'goalComment',
                goalScore: 'goalScore',
                fields: [
                'totalCost', 'isIncludedInActionPlan', 'actionPlanUnit',
                'libCrit', 'descCrit', 'complCrit',
                'idCrit', 'formattedMark', 'hasJustification',
                'justStatus', 'justId', 'justDesc',
                'formattedJustMark', 'justNote', 'note',
                'formattedWeight', 'idTelt', 'libTelt',
                'fatherId', 'idElt', 'eltLib',
                'idBline', 'projectId', 'factorBeanId',
                'tendanceLabel', 'tendancePopup',
                'repartitionPopup0', 'pct0',
                'repartitionPopup1', 'pct1',
                'repartitionPopup2', 'pct2',
                'repartitionPopup3', 'pct3',
                'all', 'subElt', 'typeElt',
                'dispatching', 'trend', 'weight', 'telt',
                'commentValue', 'iconImgClass'
                ]
            }),
            remoteSort: true
        });
        this.gridStore.setDefaultSort('note', 'asc');
        this.gridStore.on('beforeload', function() {
            Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        }, this);
        this.gridStore.on('load', function() {
            this.factorComment = this.gridStore.reader.jsonData.goalComment;
            var iconPath = (this.factorComment=='' || this.factorComment==undefined) ? requestContextPath+'/images/comment.gif':requestContextPath+'/images/hasComment.gif';
            this.factorCommentButton.setIcon(iconPath);
            this.factorCommentButton.setTooltip(this.factorComment);
            var bbarMsg = '<span style="font-size: 14px;">'+getI18nResource("caqs.facteursynthese.moyenne")
                +'&nbsp;' + this.gridStore.reader.jsonData.goalScore+'</span>';
            Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
            this.gridBottomBar.setText(bbarMsg);
        }, this);

        var myTemplate = new Ext.XTemplate(
            '<p><b>'+getI18nResource("caqs.facteursynthese.lib")+':</b> {libCrit}</p>',
            '<BR/><p><b>'+getI18nResource("caqs.facteursynthese.desc")+':</b> {descCrit}</p>',
            '<tpl if="complCrit != \'\'">',
            '<BR/><p><b>'+getI18nResource("caqs.facteursynthese.compl")+':</b> {complCrit}</p>',
            '</tpl>'
            );
        myTemplate.compile();

        var expander = new Ext.grid.RowExpander({
            tpl : myTemplate
        });

        var action = new Ext.ux.grid.RowActions({
            header:        '',
            align:         'center',
            actions:[
            {
                //qtip:      'mon tooltip',
                qtipIndex: 'commentValue',
                iconIndex: 'iconImgClass'
            }
            ]
        });

        action.on({
            scope:  this,
            action: function(grid, record, action, row, col) {
                this.editComment(record);
            }
        }, this);

        this.gridBottomBar = new Ext.Toolbar.TextItem({
            text:       '&nbsp;'
        });
        var iconPath = (this.factorComment=='') ? requestContextPath+'/images/comment.gif':requestContextPath+'/images/hasComment.gif';
        this.factorCommentButton = new Ext.Button({
            text:	'',
            tooltip:	this.factorComment,
            cls:	"x-btn-icon",
            icon:	iconPath,
            handler:	this.editFactorComment,
            scope:      this
        });

        var config = {
            store: 		this.gridStore,
            autoExpandColumn:   'libelle',
            cm: new Ext.grid.ColumnModel([
                expander,
                {
                    header: 	getI18nResource("caqs.facteursynthese.critere"),
                    width: 	255,
                    sortable: 	true,
                    dataIndex: 	'libCrit',
                    id:         'libelle',
                    renderer: 	this.critereDetailRenderer
                },
                {
                    header: 	getI18nResource("caqs.facteursynthese.note"),
                    width: 	50,
                    sortable: 	true,
                    dataIndex: 	'note',
                    align:	'right',
                    renderer: 	this.noteDetailRenderer
                },
                {
                    header: 	getI18nResource("caqs.facteursynthese.justification"),
                    width: 	30,
                    align:	'center',
                    tooltip:	getI18nResource("caqs.facteursynthese.justification.popup"),
                    renderer:	this.justificationRenderer,
                    dataIndex: 	'j'
                },
                {
                    header: 	getI18nResource("caqs.facteursynthese.poids"),
                    width: 	50,
                    sortable: 	true,
                    align:	'right',
                    dataIndex: 	'weight',
                    renderer:	this.weightRenderer
                },
                {
                    header: 	getI18nResource("caqs.facteursynthese.agregation"),
                    width: 	30,
                    sortable: 	true,
                    align:	'center',
                    tooltip:	getI18nResource("caqs.facteursynthese.agregation.popup"),
                    renderer:	this.agregationRenderer,
                    dataIndex: 	'telt'
                },
                {
                    header: 	getI18nResource("caqs.facteursynthese.tendance"),
                    width: 		70,
                    align:		'center',
                    sortable:	true,
                    tooltip:	getI18nResource("caqs.critere.tendance.popup"),
                    renderer:	this.renderTrend,
                    dataIndex: 	'trend'
                },
                {
                    header: 	fullRepartitionTitle,
                    width: 		180,
                    align:		'center',
                    sortable: 	true,
                    renderer:	this.renderRepartition,
                    dataIndex: 	'dispatching'
                },
                action
                ]),
            viewConfig: {
                forceFit:true
            },
            bbar: [
            this.gridBottomBar,
            this.factorCommentButton
            ],
            plugins: [
            expander,
            action
            ],
            collapsible: false,
            iconCls: 'icon-grid'
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsFacteurSyntheseGridPanel.superclass.initComponent.apply(this, arguments);

        this.getView().on('refresh', function() {
            putTooltips();
        }, this);
        this.on('cellclick', this.cellClick, this);
    },

    onRender : function(ct, position){
        Ext.ux.CaqsFacteurSyntheseGridPanel.superclass.onRender.apply(this, arguments);
        var t = this.getBottomToolbar().el;
        var c = t.createChild("<center></center>");
        c.appendChild(t.child("table"));
        this.setTooltips();
        putTooltips();
    }
});
