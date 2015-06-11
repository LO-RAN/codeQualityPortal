<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="com.compuware.caqs.domain.dataschemas.LabelResume" %>
<%@ page import="com.compuware.caqs.security.auth.Users" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="errorPage.jsp" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <meta http-equiv="pragma" content="no-cache">
    <link href="css/carscode.css" rel="stylesheet" type="text/css" />
    <link href="css/just_lab.css" rel="stylesheet" type="text/css" />
    <title><bean:message key="caqs.labellisation.title" /></title>

<jsp:include page="/ext/common/extjsFramework.jsp" />
<jsp:include page="/common/javascriptResources.jsp" />
<jsp:include page="/ext/common/includes.jsp" />

</head>

<body>
<%
    Locale locale = RequestUtil.getLocale(request);
    ResourceBundle resources =  ResourceBundle.getBundle("com.compuware.caqs.Resources.resources", locale);

    Users user = (Users)session.getAttribute("user");

    String req = (String)request.getAttribute("req");
    List allLab = (List)request.getAttribute("labelList");
    int iTab = 0;
    if("VALID".equals(req)) {
    	iTab = 1;
    } else if("VALID_RES".equals(req)) {
    	iTab = 2;
    } else if("REJET".equals(req)) {
    	iTab = 3;
    }
%>

<div id="tabsDiv"></div>

<script language="javascript">

var porteeDetailRenderer = function(val) {
	var retour = '';
	retour = '<A href="LabelSelect.do?id_label='+val[0];
	retour += '&id_elt='+val[1];
	retour += '&id_bline='+val[2];
	retour += '&req='+val[3]+'" >';
	retour += val[4]+'</A>';
	return retour;
};

Ext.data.SortTypes.porteeSort = function(val) {
	var retour = '';
	retour = String(val[4].toLowerCase());
	return retour;
};

function beforeTabChangeFn(panel, newTab, currentTab) {
	if(newTab.id=='attente') {
		location.href='LabelListRetrieve.do?req=DEMAND';
	}
	if(newTab.id=='validees') {
		location.href='LabelListRetrieve.do?req=VALID';
	}
	if(newTab.id=='reserve') {
		location.href='LabelListRetrieve.do?req=VALID_RES';
	}
	if(newTab.id=='rejet') {
		location.href='LabelListRetrieve.do?req=REJET';
	}
}

caqsOnReady = function(){
    Caqs.Portal.setCurrentScreen('labellisation');
    var tabs = undefined;
	tabs = new Ext.TabPanel({
        id:					'centerPanel',
		renderTo:			'tabsDiv',
        activeTab:			<%= iTab%>,
        border:				false,
        plain:				true,
        items:[
        	{
        		id:			"attente",
        		title: 		'<bean:message key="caqs.labellisation.attente" />',
        		border: 	false
        	}
        	,{
        		id:			"validees",
        		title: 		'<bean:message key="caqs.labellisation.validees" />',
        		border: 	false
        	}
        	,{
        		id:			"reserve",
        		title: 		'<bean:message key="caqs.labellisation.reserve" />',
        		border: 	false
        	}
        	,{
        		id:			"rejet",
        		title: 		'<bean:message key="caqs.labellisation.rejet" />',
        		border: 	false
        	}
        ]
    });
	
	tabs.on('beforetabchange', beforeTabChangeFn);
	
<% if (allLab != null && allLab.size() > 0) { %>
    
    var xg = Ext.grid;

    // shared reader
    var reader = new Ext.data.ArrayReader({}, [
       {name: 'projet'},
       {name: 'demandeDe'},
       {name: 'baseline'},
       {name: 'type'},
       {name: 'portee', sortType:Ext.data.SortTypes.porteeSort},
       {name: 'libelle'},
       {name: 'lastMaj'}
    ]);

Ext.grid.data = [

    <%
        Iterator i = allLab.iterator();
		boolean first = true;
    	while (i.hasNext()) {
            LabelResume label = (LabelResume)i.next();        
    %>
    	<%if(!first) {%>,<%} first = false;%>[
<%
String projetLabel = label.getEscapedProjetLabel(true, false, false);
%>	    	'<%= projetLabel%>',
<%
String auteurLabel = label.getEscapedLabelAuteur(true, false, false);
%>			'<%= auteurLabel %>',
<%
String baselineLabel = label.getEscapedBaselineLabel(true, false, false);
%>			'<%= baselineLabel %>',
<%
String elementType = label.getEscapedElementType(true, false, false, request.getLocale());
%>			'<%=elementType%>',
			[
				'<%=label.getLabelId()%>',
				'<%=label.getElementId()%>',
				'<%=label.getBaselineId()%>',
				"<%=req%>",
<%
String elementLabel = label.getEscapedElementLabel(true, false, false);
%>				'<%=elementLabel%>'						
			],
<%
String libelleLabel = label.getEscapedLabelLibelle(true, false, false);
%>			'<%=libelleLabel%>',
			'<%=java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT, locale).format(label.getLabelDinst())%>'
		]
    <%  }
    %>
];
    var grid = new xg.GridPanel({
        style:	'margin-top:5px;',
        store: 	new Ext.data.GroupingStore({
            reader: reader,
            data: xg.data,
            sortInfo:{field: 'projet', direction: "ASC"},
            groupField:'projet'
        }),
        columns: [
            {id:'projet',header: '<bean:message key="caqs.projet" />', width: 100, sortable: true, dataIndex: 'projet'},
            {header: "<%= req.equals("DEMAND") ? resources.getString("caqs.labellisation.demandede") : resources.getString("caqs.labellisation.validepar") %>", 
            	width: 80, sortable: true, dataIndex: 'demandeDe'},
            {header: "<bean:message key="caqs.baseline" />", width: 100, sortable: true,
            	 dataIndex: 'baseline'},
            {header: "<bean:message key="caqs.labellisation.type" />", width: 100, sortable: true, dataIndex: 'type'},
            {header: "<bean:message key="caqs.labellisation.portee" />", width: 200, sortable: true,
            	renderer: porteeDetailRenderer, dataIndex: 'portee'},
            {header: "<bean:message key="caqs.labellisation.libelle" />", width: 180, sortable: true, dataIndex: 'libelle'},
            {header: "<bean:message key="caqs.labellisation.lastmaj" />", width: 120, sortable: true, dataIndex: 'lastMaj'}
        ],

        view: new Ext.grid.GroupingView({
            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "<bean:message key="caqs.labellisations" />" : "<bean:message key="caqs.labellisation" />"]})'
        }),

        enableColumnHide : 	false,
        enableColumnMove : 	false,
        frame:				true,
        width: 				900,
        height: 			450,
        collapsible: 		false,
        header: 			false,
        iconCls: 			'icon-grid'
    });

	grid.render(document.body);
};
</script>

	<% } else { %>
};
</script>
    <CENTER>
        <H2><bean:message key="caqs.labellisation.nolabel" /></H2>
    </CENTER>
<% } %>

</body>
</html>
