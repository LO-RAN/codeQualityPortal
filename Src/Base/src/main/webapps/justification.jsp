<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.compuware.caqs.domain.dataschemas.JustificatifResume" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<title><bean:message key="caqs.justification.title" /></title>
<link href="css/carscode.css" rel="stylesheet" type="text/css" />
<link href="css/just_lab.css" rel="stylesheet" type="text/css" />

<jsp:include page="/ext/common/extjsFramework.jsp" />
<jsp:include page="/common/javascriptResources.jsp" />
<jsp:include page="/ext/common/includes.jsp" />

</head>

<body>

<%
    Locale locale = RequestUtil.getLocale(request);
    ResourceBundle resources =  ResourceBundle.getBundle("com.compuware.caqs.Resources.resources", locale);

    String req = request.getParameter("req");
    if (req == null || req.length() == 0) {
        req = "DEMAND";
    }
    
    Collection<JustificatifResume> allJust = (Collection)request.getAttribute("allJust");
    int iTab = 0;
    if("VALID".equals(req)) {
    	iTab = 1;
    } else if("REJET".equals(req)) {
    	iTab = 2;
    }
%>

<div id="tabsDiv">

  <div id="contentTabEl">
    <div id="contentGridFactorEl"></div>
    <div id="contentGridCriterionEl"></div>
  </div>

</div>

<script language="javascript">
var elementsWidth = 1000;

var porteeDetailRenderer = function(val) {
	var retour = '';
        val[4] = val[4].replace(/ /gi,'%20');
	retour = '<A href="JustificationRetrieve.do?id_just='+val[0];
	retour += '&id_elt='+val[1];
	retour += '&lib_elt='+val[2];
	retour += '&id_critfac='+val[3];
	//retour += '&lib_critfac='+val[4];
	retour += '&id_bline='+val[5];
	retour += '&dbtable='+val[6];
	retour += '&req='+val[7]+'" >';
	retour += val[2]+'</A>';
	return retour;
};

Ext.data.SortTypes.porteeSort = function(val) {
	var retour = '';
	retour = String(val[2].toLowerCase());
	return retour;
};

function beforeTabChangeFn(panel, newTab, currentTab) {
	if(newTab.id=='attente') {
		location.href='JustificationListRetrieve.do?req=DEMAND';
	}
	if(newTab.id=='validees') {
		location.href='JustificationListRetrieve.do?req=VALID';
	}
	if(newTab.id=='rejet') {
		location.href='JustificationListRetrieve.do?req=REJET';
	}
}

caqsOnReady = function(){
    Caqs.Portal.setCurrentScreen('justification');
    var tabs = undefined;
	tabs = new Ext.TabPanel({
        id:					'centerPanel',
		renderTo:			'tabsDiv',
        activeTab:			<%= iTab%>,
        border:				false,
        height:				500,
        plain:				true,
        width:				elementsWidth+20,
        defaults:	{
				autoScroll:	true
        },
        items:[
        	{
        		id:			"attente",
        		title: 		'<bean:message key="caqs.justification.enAttente" />',
        		<%
        	    if("DEMAND".equals(req)) {
        	    %>
        	    contentEl:	'contentTabEl',
        	    <%
        	    }
        	    %>
        		border: 	false
        	}
        	,{
        		id:			"validees",
        		title: 		'<bean:message key="caqs.justification.validees" />',
        		<%
        	    if("VALID".equals(req)) {
        	    %>
        	    contentEl:	'contentTabEl',
        	    <%
        	    }
        	    %>
        		border: 	false
        	}
        	,{
        		id:			"rejet",
        		title: 		'<bean:message key="caqs.justification.rejetees" />',
        		<%
        	    if("REJET".equals(req)) {
        	    %>
        	    contentEl:	'contentTabEl',
        	    <%
        	    }
        	    %>
        		border: 	false
        	}
        ]
    });
	
	tabs.on('beforetabchange', beforeTabChangeFn);

    // shared reader
    var reader = new Ext.data.ArrayReader({}, [
       {name: 'projet'},
       {name: 'demandeDe'},
       {name: 'baseline'},
       {name: 'aJustifier'},
       {name: 'type'},
       {name: 'portee', sortType:Ext.data.SortTypes.porteeSort},
       {name: 'libelle'},
       {name: 'lastMaj'},
       {name: 'dinst'}
    ]);


 <% if (allJust != null) { %>
var critereData = [
<%      Iterator<JustificatifResume> i = allJust.iterator();
		boolean first = true;
        while (i.hasNext()) {
            JustificatifResume just = i.next();
            if(just==null) continue;
            String critFactLabel = just.getEscapedCritfacLabel(true, false, false, RequestUtil.getLocale(request));
    %>
    	<%if(!first) {%>,<%} first = false;%>[
<%
String projetLabel = just.getEscapedProjetLabel(true, false, false);
%>	    	'<%=projetLabel%>',
<%
String justificatifAuteur = just.getEscapedJustificatifAuteur(true, false, false);
%>			'<%= justificatifAuteur %>',
<%
String baselineLabel = just.getEscapedBaselineLabel(true, false, false);
%>			'<%= baselineLabel %>',
			'<%= critFactLabel%>', 
<%
String elementType = just.getEscapedElementType(true, false, false, request.getLocale());
%>			'<%=elementType%>',
			[
				'<%=just.getJustificatifId()%>',
				'<%=just.getElementId()%>',
<%
String elementLabel = just.getEscapedElementLabel(true, false, false);
%>				'<%=elementLabel%>',
				'<%=just.getCritfacId()%>',
				'<%=critFactLabel%>',
				'<%=just.getBaselineId()%>',
				'CRIT',
				"<%=req%>"
			],
<%
String justLib = just.getEscapedJustificatifLibelle(true, false, false);
%>			'<%=justLib%>',
			'<%=java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT, locale).format(just.getLastModifDate())%>',
			'<%=java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT, locale).format(just.getJustificatifDinst())%>'
		]
    <%  }
    %>
];
    var gridCritere = new Ext.grid.GridPanel({
        store: 		new Ext.data.GroupingStore({
                            reader: reader,
                            data:   critereData,
                            sortInfo:{field: 'projet', direction: "ASC"},
                            groupField:'projet'
			}),        
        renderTo:	'contentGridCriterionEl',
        view: 		new Ext.grid.GroupingView({
			       	forceFit:	true,
                                emptyText:      getI18nResource('caqs.justification.noJustif'),
			        groupTextTpl: 	'{text} ({[values.rs.length]} {[values.rs.length > 1 ? "<bean:message key="caqs.justifications" />" : "<bean:message key="caqs.justification" />"]})'
			}),
        enableColumnHide : 	false,
        enableColumnMove : 	false,
        frame:		true,
        width: 		elementsWidth,
        height:		450,
        collapsible: 	false,
        header:         false,
        iconCls: 	'icon-grid',

        columns: [
            {id:'projet',header: '<bean:message key="caqs.projet" />', width: 100, sortable: true, dataIndex: 'projet'},
<%
	String columnName = "";
    if("DEMAND".equals(req)) {
    	columnName = resources.getString("caqs.justification.demandeDe");
    } else if("VALID".equals(req)) {
    	columnName = resources.getString("caqs.justification.validePar");
    } else if("REJET".equals(req)) {
    	columnName = resources.getString("caqs.justification.rejetPar");
    }
%>            {header: "<%= columnName %>", width: 80, sortable: true, dataIndex: 'demandeDe'},
            {header: "<bean:message key="caqs.baseline" />", width: 100, sortable: true,
            	 dataIndex: 'baseline'},
            {header: "<bean:message key="caqs.justification.aJustifier" arg0='<%=resources.getString("caqs.critere")%>' />",
            	 width: 150, sortable: true, dataIndex: 'aJustifier'},
            {header: "<bean:message key="caqs.justification.type" />", width: 50, sortable: true, dataIndex: 'type'},
            {header: "<bean:message key="caqs.justification.portee" />", width: 200, sortable: true, 
            	renderer: porteeDetailRenderer, dataIndex: 'portee'},
            {header: "<bean:message key="caqs.justification.libelle" />", width: 180, sortable: true, dataIndex: 'libelle'},
<%    if("DEMAND".equals(req)) { %>
            {header: "<bean:message key="caqs.justification.lastMAJ" />", width: 120, sortable: true, dataIndex: 'dinst'}
<%
	} else {
%>
            {header: "<bean:message key="caqs.justification.lastMAJ" />", width: 120, sortable: true, dataIndex: 'lastMaj'}
<%	
	}
%>
        ]
    });

<% } %>
};
</script>
</body>
</html>
