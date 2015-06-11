<%@ page 	
		isErrorPage='true' 
		contentType="text/html"
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<link href="css/error.css" rel="stylesheet" type="text/css" />
<head>
    <title><bean:message key="caqs.errorpage.title" /></title>
</head>
<body>
	<div class="errorPanel">
    <H1><bean:message key="caqs.errorpage.erreur" /> : </H1>
    <bean:message key="caqs.errorpage.message" />

<% if (exception != null) { %>
	<br/>
	<code><%= exception.getLocalizedMessage() %></code>
<% } %>
	</div>
</body>
</html>
