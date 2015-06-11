<%@page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<%     	
    int nbDemandees = ((Integer)request.getAttribute("DEMAND")).intValue();
    int nbValidees  = ((Integer)request.getAttribute("VALID")).intValue();
    int nbRefusees  = ((Integer)request.getAttribute("REJET")).intValue();
%>
	var demandesValue = '';
<%
if(nbDemandees>0) {
%>
	demandesValue += '<a href="javascript:loadJustificationList(\'DEMAND\', \'<bean:message key="caqs.synthese.justifDemandList" />\');">';
<%
}	
%>
	demandesValue += '<%= nbDemandees%>';
<%
if(nbDemandees>0) {
%>
	demandesValue += '<img src="<%= request.getContextPath()%>/images/loupe.gif" />';
	demandesValue += '</a>';
<%
} else {
%>
	demandesValue += '<img src="<%= request.getContextPath()%>/images/empty16.gif" />';
<%
}
%>
	var validesValue  = '';
<%
if(nbValidees>0) {
%>
	validesValue += '<a href="javascript:loadJustificationList(\'VALID\', \'<bean:message key="caqs.synthese.justifValidList" />\');">';
<%
}	
%>

	validesValue += '<%= nbValidees%>';

<%
if(nbValidees>0) {
%>
	validesValue += '<img src="<%= request.getContextPath()%>/images/loupe.gif" />';
	validesValue += '</a>';
<%
} else {
%>
	validesValue += '<img src="<%= request.getContextPath()%>/images/empty16.gif" />';
<%
}
%>
	var refusValue    = '';
<%
if(nbRefusees>0) {
%>
	refusValue += '<a href="javascript:loadJustificationList(\'REJET\', \'<bean:message key="caqs.synthese.justifRejetList" />\');">';
<%
}	
%>
	refusValue += '<%= nbRefusees%>';
<%
if(nbRefusees>0) {
%>
	refusValue += '<img src="<%= request.getContextPath()%>/images/loupe.gif" />';
	refusValue += '</a>';
<%
} else {
%>
	refusValue += '<img src="<%= request.getContextPath()%>/images/empty16.gif" />';
<%
}
%>
	var value = '<div style="text-align:right;"><span><bean:message key="caqs.synthese.nbJustifDemand" /> '+demandesValue+'</span><br />'
		+'<span><bean:message key="caqs.synthese.nbJustifValid" /> '+validesValue+'</span><br />'
		+'<span><bean:message key="caqs.synthese.nbJustifRejet" /> '+refusValue+'</span></div>';
	
    	var just = new Ext.ux.form.StaticTextField({
	    		fieldLabel:			'<bean:message key="caqs.synthese.nbJustif" />',
    			parentClassName: 	'smallerStaticField',
    			smallLine:			true,
    			htmlEncode: 		false,
    			//autoHeight:			true,
    			height:				60,
    			value:				value
    	});
		volumetryPanel.add(just);
