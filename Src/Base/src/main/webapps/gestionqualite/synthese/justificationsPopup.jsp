<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*,
                 com.compuware.caqs.domain.dataschemas.JustificatifResume"
%>
<%@page import="com.compuware.caqs.presentation.util.RequestUtil"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script language="javascript">

Ext.override(Ext.grid.GridView, {
    layout : function(){
        if(!this.mainBody){
            return;
        }
        var g = this.grid;
        var c = g.getGridEl(), cm = this.cm,
                expandCol = g.autoExpandColumn,
                gv = this;
        var csize = c.getSize(true);
        var vw = csize.width;
        if(vw < 20 || csize.height < 20){
            return;
        }
        if(g.autoHeight){
	        csize.height = this.mainHd.getHeight() + this.mainBody.getHeight() + 20;
	        if (!this.forceFit) {
	        	csize.height += this.scrollOffset;
	        }
        }
        this.el.setSize(csize.width, csize.height);
        var hdHeight = this.mainHd.getHeight();
        var vh = csize.height - (hdHeight);
        this.scroller.setSize(vw, vh);
        if(this.innerHd){
            this.innerHd.style.width = (vw)+'px';
        }
        if(this.forceFit){
            if(this.lastViewWidth != vw){
                this.fitColumns(false, false);
                this.lastViewWidth = vw;
            }
        }else {
            this.autoExpand();
        }
        this.onLayout(vw, vh);
    }
});

    //creation de la fenetre d'affichage des justifs en demande
    var justificationStore = new Ext.data.Store({
	    proxy: new Ext.data.HttpProxy({
	        url: requestContextPath+'/SyntheseJustificationsPopup.do'
	    }),

	    // create reader that reads the Topic records
	    reader: new Ext.data.JsonReader({
	        root: 			'justifs',
	        totalProperty: 	'totalCount',
	        id: 			'id',
	        fields: [
	            'id', 'lib_just', 'dmaj', 'lib_crit', 'auteur', 'desc', 'eltLib',
	            'before', 'note', 'type'
	            ]
	    }),
	    remoteSort: false
	});

    var myTemplate = new Ext.Template(
            	'<tpl if="type != \'DEMAND\'">',
                    '<p><b><bean:message key="caqs.synthese.justificationNoteAvant" />:</b> {before}</p>',
                '<tpl if="type != \'VALID\'">',
                    '<p><b><bean:message key="caqs.synthese.justificationAuteurValid" />:</b> {auteur}</p>',
                    '<p><b><bean:message key="caqs.synthese.justificationDescValid" />:</b> {desc}</p>',
                '</tpl>',
                '<tpl if="type != \'REJET\'">',
                    '<p><b><bean:message key="caqs.synthese.justificationAuteurRejet" />:</b> {auteur}</p>',
                    '<p><b><bean:message key="caqs.synthese.justificationDescRejet" />:</b> {desc}</p>',
                '</tpl>',
            	'<tpl if="type == \'DEMAND\'">',
                    '<p><b><bean:message key="caqs.synthese.justificationAuteur" />:</b> {auteur}</p>',
                    '<p><b><bean:message key="caqs.synthese.justificationDescDemande" />:</b> {desc}</p>',
                '</tpl>'
        );
    myTemplate.compile();

	var expanderCritere = new Ext.grid.RowExpander({
        tpl : myTemplate
    });

    // create the Grid
    var gridCriteres = new Ext.grid.GridPanel({
        store:                  justificationStore,
        enableColumnHide : 	false,
        enableColumnMove : 	false,
        enableHdMenu:		false,
        frame:			false,
        cm: new Ext.grid.ColumnModel([
        	expanderCritere,
            {
                id:'lib_just',
                header: "<bean:message key="caqs.synthese.justificationLib" />",
                width: 180,
                sortable: true,
                dataIndex: 'lib_just'
            },
            {
                header: "<bean:message key="caqs.synthese.justificationLibCrit" />",
                width: 200,
                sortable: true,
                dataIndex: 'lib_crit'
            },
            {
                header: "<bean:message key="caqs.synthese.justificationEltLib" />",
                width: 200,
                sortable: true,
                dataIndex: 'eltLib'
            },
            {
                header: "<bean:message key="caqs.synthese.justificationDMaj" />",
                width: 120,
                sortable: true,
                dataIndex: 'dmaj'
            },
            {
                header: "<bean:message key="caqs.synthese.justificationNote" />",
                width: 100,
                sortable: true,
                dataIndex: 'note'
            }
        ]),
        collapsible: 	false,
        iconCls: 		'icon-grid',
        plugins: 		expanderCritere,
        autoHeight:		true
    });

	var justificationListView = undefined;

	function loadJustificationList(type, title) {
        if(justificationListView == undefined) {
            justificationListView = new Ext.Window({
                modal: 			true,
                maximizable: 	true,
                resizable:		true,
                layout:			'fit',
                minimizable: 	false,
                width:			850,
                autoHeight:     true,
                shadow:			false,
                plain:			true,
                x:				20,
                y:				20,
                items:	[
                                gridCriteres
                ]
            });
            justificationListView.on('beforeclose', function(wnd) {
                justificationListView.hide();
                return false;
            });
        }
        var width = 740;
        if(type == 'DEMAND') {
            width += 100;
        }
        var colIndex = gridCriteres.getColumnModel().findColumnIndex('note');
        if(colIndex >= 0) {
            gridCriteres.getColumnModel().setHidden(colIndex, (type == 'DEMAND'));
        }
        justificationListView.setTitle(title);
        justificationListView.setWidth(width);
        justificationStore.load({
                    params:{
                        type:    			type
                    }
                });
		justificationListView.show();
	}
</script>
