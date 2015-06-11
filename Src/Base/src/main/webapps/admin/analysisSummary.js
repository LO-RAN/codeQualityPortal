Ext.ux.CaqsAnalysisSummaryWnd = function(config) {
    // call parent constructor
    Ext.ux.CaqsAnalysisSummaryWnd.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsAnalysisSummaryWnd, Ext.Window, {
    modal:          true,
    maximizable:    true,
    resizable:      true,
    minimizable:    false,
    plain:          true,
    shadow:         false,
    title:          getI18nResource("caqs.admin.launchSummary.launchedFor"),
    width:          550,
    height:         500,
    layout:         'fit',
    autoScroll:     true,
    
    initComponent : function(){
        var config = {
            buttons:    [
                new Ext.Button({
                    scope:      this,
                    handler:    function() {
                        this.close();
                    },
                    text:       getI18nResource('caqs.close')
                })
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsAnalysisSummaryWnd.superclass.initComponent.call(this);
    },

    setLaunchAnalysisResults: function(response) {
        if(response!=null && response.responseText!=null) {
            var json = Ext.util.JSON.decode(response.responseText);
            var child = null;
            if(json.ok) {
                var reader = new Ext.data.ArrayReader({}, [
                   {name: 'lib'},
                   {name: 'desc'},
                   {name: 'weight'},
                   {name: 'src'},
                   {name: 'modele'},
                   {name: 'dialecte'}
                ]);

                var myTemplate = new Ext.Template(
                        '<p><b>'+getI18nResource("caqs.admin.launchSummary.srcDir")+':</b> {src}</p><br />',
                        '<p><b>'+getI18nResource("caqs.admin.launchSummary.modele")+':</b> {modele}</p><br />',
                        '<p><b>'+getI18nResource("caqs.admin.launchSummary.dialecte")+':</b> {dialecte}</p>'
                    );
                myTemplate.compile();

                var expander = new Ext.grid.RowExpander({
                    tpl : myTemplate
                });

                // create the Grid
                child = new Ext.grid.GridPanel({
                    store: new Ext.data.Store({
                        reader: reader,
                        data:   json.dataArray,
                        sortInfo:{
                            field:'lib',
                            direction:'DESC'
                        }
                    }),
                    enableColumnHide : 	false,
                    enableColumnMove : 	false,
                    enableHdMenu:	false,
                    frame:		false,
                    cm: new Ext.grid.ColumnModel([
                        expander,
                        {id:'lib', header: getI18nResource("caqs.admin.launchSummary.lib"), width: 200, sortable: true, dataIndex: 'lib'},
                        {header: getI18nResource("caqs.admin.launchSummary.desc"), width: 200, sortable: true, dataIndex: 'desc'},
                        {header: getI18nResource("caqs.admin.launchSummary.weight"), width: 50, sortable: true, dataIndex: 'weight'}
                    ]),
                    plugins:            expander,
                    collapsible:        false,
                    iconCls:            'icon-grid',
                    //autoHeight:         true,
                    autoExpandColumn:   'lib',
                    title:  getI18nResource("caqs.admin.launchSummary.launchedFor")
                });
            } else {
                //lancement de l'analyse echoue
                var msg = '';
                if(json.errorMsgKey!=null && json.errorMsgKey.length>0) {
                    msg = '<img src="'+requestContextPath+'/images/exclamation.gif"/>&nbsp;'
                        + getI18nResource("caqs.admin.launchSummary.ko") +
                        ':<BR/>&nbsp;-&nbsp;' + getI18nResource(json.errorMsgKey);
                } else {
                    msg = '<img src="'+requestContextPath
                        + '/images/exclamation.gif"/>&nbsp;'
                        + getI18nResource("caqs.admin.launchSummary.ko");
                }
                child = {
                    xtype:      'statictextfield',
                    htmlEncode: false,
                    autoHeight:	true,
                    value:      msg
        	};
            }
            this.setTitle(getI18nResource("caqs.admin.launchSummary.title")+'&nbsp;'+json.projectName);
            this.add(child);
            this.doLayout();
        }
        Caqs.Messages.checkMessages();
    }
});

