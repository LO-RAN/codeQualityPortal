<%@ page language="java" import="com.compuware.optimalview.tasklist.*" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
<title><bean:message key="exception.title"/></title>
<link rel="stylesheet" href="ovtasklist.css" type="text/css">
<html:base/>
</head>
<body bgcolor="white">

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<% Exception ex = (Exception) request.getAttribute("exception");
   String key = "exception.unknownException";
   if (ex instanceof UnknownUserException) {
      key = "exception.unknownUser";
   } else if (ex instanceof UserNotFoundException) {
      key = "exception.userNotFound";
   } else if (ex instanceof ConnectionFailedException) {
      key = "exception.connectFailed";
   } else if (ex instanceof TaskNotFoundException) {
      key = "exception.taskNotFound";
   } else if (ex instanceof TaskDetailsNotFoundException) {
      key = "exception.detailsNotFound";
   } else {
      key = "exception.unknownException";
   }
   String headerKey = key + ".header";
   String messageKey = key + ".message";
%>
<h3><bean:message key="<%=headerKey%>"/></h3>
<p class="exceptionBox"><bean:message key="<%=messageKey%>"/></p>
</body>
</html:html>
