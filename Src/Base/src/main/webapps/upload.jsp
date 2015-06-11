<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page 	errorPage="errorPage.jsp" %>
<%@ page	import="java.util.*,
                    com.compuware.caqs.domain.dataschemas.DialecteBean,
					com.compuware.caqs.service.upload.UploadFileType"
%>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ taglib uri="/WEB-INF/resources.tld" prefix="check" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<HTML>
<jsp:useBean id="currentElement" scope="session" class="com.compuware.caqs.domain.dataschemas.ElementBean" />
<HEAD>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<link href="css/carscode.css" rel="stylesheet" type="text/css" />
<link href="css/synthese.css" rel="stylesheet" type="text/css" />
<TITLE>
<bean:message key="caqs.upload.title" />
</TITLE>
<jsp:include page="/ext/common/extjsFramework.jsp" />
<jsp:include page="/common/javascriptResources.jsp" />
<jsp:include page="/ext/common/includes.jsp" />

<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/uploadDialogBox.js">
</script>

</HEAD>
<BODY style='background-color:transparent'>
<div id="errorDivPanel" class="x-panel">
    <div class="x-panel-body" >
        <html:errors />
    </div>
</div>

<logic:equal parameter="result" scope="request" value="success">
<div id="successDivPanel" class="x-panel">
    <div class="x-panel-body" >
        <bean:message key="caqs.upload.success" />
    </div>
</div>
<script language="javascript">
    new Ext.Panel({
            style:      'margin-top: 5px; margin-left: 5px; margin-bottom: 5px; margin-right: 5px;',
            frame:      true,
            applyTo:    'successDivPanel'
    });
</script>
</logic:equal>
</BODY>
<script language="javascript">
caqsOnReady = function(){
<logic:present name="<%=org.apache.struts.Globals.ERROR_KEY%>">
    new Ext.Panel({
            style:      'margin-top: 5px; margin-left: 5px; margin-bottom: 5px; margin-right: 5px;',
            frame:      true,
            applyTo:    'errorDivPanel'
    });
</logic:present>

new Ext.ux.CaqsUploadDialogBox({
        renderTo:           Ext.getBody()
    });
};
</script>
</HTML>

