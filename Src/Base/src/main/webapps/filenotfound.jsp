<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page 	
		isErrorPage='true' 
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
    <link href="css/error.css" rel="stylesheet" type="text/css" />
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <title><bean:message key="caqs.errorpage.title" /></title>
</head>

<% if (session == null) { %>
<jsp:forward page="sessiontimedout.jsp"/>
<%
 } else { 
 %>
<body>
<div class="error">
    <H2><bean:message key="caqs.errorpage.filenotfound" /> :</H2>
    <bean:write name="dummy" property="filePath" />
</div>
</body>
<% } %>
</html>
