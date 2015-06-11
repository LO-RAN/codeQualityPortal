<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page 	
		isErrorPage='true' 
		import="java.util.*"
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<link href="<%= request.getContextPath()%>/css/error.css" rel="stylesheet" type="text/css" />
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <title><bean:message key="caqs.errorpage.title" /></title>
</head>
<%
    org.apache.log4j.Logger log = com.compuware.toolbox.util.logging.LoggerManager.getLogger("Ihm");

	Exception currentException = (Exception)request.getAttribute("org.apache.struts.action.EXCEPTION");
	if (currentException != null) {
    	log.error("Unexpected Struts Action Error detected", currentException);
	}
	currentException = (Exception)request.getAttribute("javax.servlet.jsp.jspException");
	if (currentException != null) {
    	log.error("Unexpected Servlet/Jsp Error detected", currentException);
	}
%>
<% if (session == null) { %>
<jsp:forward page="sessiontimedout.jsp"/>
<%
 } else { 
 %>
<body>
	<div class="errorPanel">
    <H1><bean:message key="caqs.errorpage.erreur" /> : </H1>
    <bean:message key="caqs.errorpage.message" />
	</div>
</body>
<% } %>
</html>
