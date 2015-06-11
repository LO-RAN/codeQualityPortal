Ext.ux.CaqsLaunchAnalysisWnd = function(config) {
    // call parent constructor
    Ext.ux.CaqsLaunchAnalysisWnd.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsLaunchAnalysisWnd, Ext.Window, {
    idPro:                      undefined,
    layout:                     'anchor',
    modal:                      true,
    maximizable:                true,
    resizable:                  false,
    minimizable:                false,
    plain:                      true,
    shadow:                     false,
    analysisTreeLoader:         undefined,
    title:                      getI18nResource("caqs.admin.launchAnalysis.title"),
    width:                      550,
    analyseBaselineName:        undefined,
    projectName:                undefined,
    treePanel:                  undefined,
    records:                    undefined,
    analysisWindowSubmitBtn:    undefined,
    generateBatchParametersBtn: undefined,
    checkedEltsId:              undefined,
    analysisLaunchFormPanel:    undefined,
    parent:                     undefined,


    getEAList: function() {
        var eaList = '';
        if(this.checkedEltsId!=undefined) {
            for(var i=0; i < this.checkedEltsId.length; i++) {
                eaList += this.checkedEltsId[i];
                if(i<(this.checkedEltsId.length-1)) {
                    eaList += ',';
                }
            }
        }
        return eaList;
    },

    getOptionList: function() {
        var eaOptionList = '';
        if(this.checkedEltsId!=undefined) {
            for(var i=0; i<this.checkedEltsId.length; i++) {
                var elt = document.getElementById(this.checkedEltsId[i]+'OptionField');
                if(elt!=undefined) {
                    eaOptionList += elt.value;
                }
                if(i<(this.checkedEltsId.length-1)) {
                    eaOptionList += ',';
                }
            }
        }
        return eaOptionList;
    },

    launchAnalyse: function() {
        var eaList = this.getEAList();
        var eaOptionList = this.getOptionList();
        var baselineName = this.analyseBaselineName.getValue();
        Caqs.Portal.getCaqsPortal().getAdministrationActivity().showMask();
        Ext.Ajax.request({
            url:		requestContextPath+'/AdminProjectAnalysisLaunchAnalyse.do',
            params:     {
                eaList:         eaList,
                baselineName:   baselineName,
                projectId:      this.idPro,
                eaOptionList:   eaOptionList,
                projectName:    Ext.getCmp('adminPRJlib').getValue()
            },
            success:	function(response) {
                Caqs.Portal.getCaqsPortal().getAdministrationActivity().hideMask();
                this.close();
                var resultWnd = new Ext.ux.CaqsAnalysisSummaryWnd();
                resultWnd.setLaunchAnalysisResults(response);
                resultWnd.show();
            },
            scope:      this
        });
    },

    openWindow: function(bl, eaList, eaOptionList){
	var pro = this.idPro;
	var proUpcase = pro.toUpperCase();
	var blUpcase = bl.toUpperCase();
	var url = requestContextPath+"/as.jsp?projectId="+proUpcase+"&baselineId="+blUpcase+'&eaList='+eaList+'&eaOptionList='+eaOptionList;
	PopupCentrer(url, 640,480,"menubar=yes,statusbar=yes,scrollbars=yes,resizable=yes");
    },

    launchManualAnalyse: function() {
        var eaList = this.getEAList();
        var eaOptionList = this.getOptionList();
        var baselineName = this.analyseBaselineName.getValue();
        if(baselineName=='') {
            Ext.MessageBox.alert('', getI18nResource("caqs.admin.launchAnalysis.needBaselineName"));
            this.analyseBaselineName.focus();
        } else {
            this.setVisible(false);
            this.openWindow(baselineName, eaList, eaOptionList);
        }
    },

    generateBatchParameters: function() {
        var eaList = this.getEAList();
        var eaOptionList = this.getOptionList();
        var baselineName = this.analyseBaselineName.getValue();
        Ext.Ajax.request({
            url:		requestContextPath+'/AdminPrjGenerateBatchParameters.do',
            params:     {
                eaList:         eaList,
                baselineName:   baselineName,
                projectId:      this.idPro,
                eaOptionList:   eaOptionList,
                projectName:    Ext.getCmp('adminPRJlib').getValue()
            },
            success:	function(response) {
                if(response.responseText!='' && response.responseText!='[]') {
                    var json = Ext.util.JSON.decode(response.responseText);
                    Ext.MessageBox.alert('', json.batchLaunchParameters);
                }
            },
            scope:      this
        });
    },

    initComponent : function(){
        this.records = new Array();
        this.checkedEltsId = new Array();

        this.treePanel = new Ext.tree.ColumnTree({
            anchor:             '95%',
            height:             300,
            maxHeight:          500,
            rootVisible: 	false,
            autoScroll:		true,
            title: 		getI18nResource("caqs.admin.launchAnalysis.treeTitle"),
            style:		'margin-bottom:5px;',
            loader:             new Ext.tree.TreeLoader({
                uiProviders:{
                    'col': Ext.tree.ColumnNodeUI
                }
            }),
            columns:[
            {
                header:'Element',
                width:300,
                dataIndex:'task'
            },
            new Ext.grid.TextFieldColumn({
                header: 	getI18nResource("caqs.admin.launchAnalysis.optionClm"),
                width: 	100
            }),
            new Ext.grid.CheckColumn({
                header:         "",
                dataIndex: 	'checked',
                scope:          this,
                width: 		55
            })
            ]
        });
        this.analyseBaselineName = new Ext.form.TextField({
            id:         'analyseBaselineName',
            name:       'analyseBaselineName',
            width:      225,
            maxLength : 32
        });

        this.treePanel.setRootNode(new Ext.tree.AsyncTreeNode({
            text:	'rootNode',
            draggable:	false,
            //expanded:	true,
            id:		'root',
            children:   this.parent.analysisTree
        }));

        var analysisLaunchFormPanelItems = new Array();
        analysisLaunchFormPanelItems[analysisLaunchFormPanelItems.length] = this.treePanel;
        analysisLaunchFormPanelItems[analysisLaunchFormPanelItems.length] = new Ext.Panel({
                header:         false,
                bodyStyle:      "background-color:transparent",
                labelWidth:     170,
                style:          'margin: 5px 0px 0px 0px;',
                autoHeight:     true,
                border:         false,
                anchor:         '95%',
                layout: 	'table',
                layoutConfig: {
                    columns : 	2
                },
                items: [
                    {
                        baseCls:    'x-form-item',
                        style:      'margin: 0px 5px 0px 0px;',
                        html:       getI18nResource("caqs.admin.launchAnalysis.baselineName")+':'
                    }
                    , this.analyseBaselineName
                ]
            });

        this.manualAnalysisWindowSubmitBtn = new Ext.Button({
            text:       getI18nResource("caqs.admin.launchAnalysis.manualAnalysis"),
            disabled:   true,
            handler:    this.launchManualAnalyse,
            scope:      this
        });
        if(caqsUserRights["MANUAL_ANALYSIS"]) {
            analysisLaunchFormPanelItems[analysisLaunchFormPanelItems.length] = new Ext.Panel({
                header:         false,
                bodyStyle:      "background-color:transparent",
                labelWidth:     170,
                style:          'margin: 5px 0px 0px 0px;',
                autoHeight:     true,
                border:         false,
                anchor:         '95%',
                layout: 	'table',
                layoutConfig: {
                    columns : 	3
                },
                items: [
                    {
                        baseCls:    'x-form-item',
                        style:      'margin: 0px 5px 0px 0px;',
                        html:       '<label style="width:200px;" >'+getI18nResource("caqs.infosPRJ.serveur")+': </label>'
                    }
                    ,{
                        xtype:      'textfield',
                        style:      'margin: 0px 5px 0px 0px;',
                        name:       'hostURL',
                        id:         'hostURL',
                        allowBlank: false,
                        value:      requestContextPath
                    }
                    , this.manualAnalysisWindowSubmitBtn
                ]
            });
        }
        this.analysisWindowSubmitBtn = new Ext.Button({
            text:       getI18nResource("caqs.admin.launchAnalysis.launch"),
            disabled:   true,
            formBind:   true,
            scope:      this,
            handler:    this.launchAnalyse
        });
        this.generateBatchParametersBtn = new Ext.Button({
            text:       getI18nResource('caqs.admin.launchAnalysis.generatebatchParametersBtn'),
            disabled:   true,
            handler:    this.generateBatchParameters,
            scope:      this
        });
        this.analysisLaunchFormPanel = new Ext.form.FormPanel({
            labelWidth:     170,
            monitorValid:   true,
            bodyStyle:      'padding:5px 5px 5px 5px;',
            items:          analysisLaunchFormPanelItems
            , buttons: [
                this.analysisWindowSubmitBtn
                ,   this.generateBatchParametersBtn
                , {
                    text:       getI18nResource("caqs.annuler"),
                    handler: function(){
                        this.hide();
                    },
                    scope:      this
                }
            ]
	});
        var config = {
            height:     (caqsUserRights["MANUAL_ANALYSIS"]) ? 480 : 430,
            items:      [this.analysisLaunchFormPanel]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsLaunchAnalysisWnd.superclass.initComponent.call(this);
        this.on('show', this.onShow, this);
    },
    
    onShow: function(cmpt) {
        Ext.ux.CaqsLaunchAnalysisWnd.superclass.onShow.call(this, cmpt);
        this.treePanel.expandAll();
    }
});

