<%@ page language="java" import="java.util.Date,com.compuware.optimalview.tasklist.User,com.compuware.optimalview.util.I18N" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
<title><bean:message key="taskexec.title"/></title>
<link rel="stylesheet" href="ovtasklist.css" type="text/css">
<script language="JavaScript" src="OFCommon.js"></script>
<html:base/>
</head>
<body bgcolor="white" onLoad="reloadTaskList()">

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>
<div class="taskCompleted">
<% User user = (User) request.getAttribute("user");
   String action = request.getParameter("formAction");
   if (action == null || action.length() == 0) {
      action = request.getParameter("action");
   }
   if ("cancelTask".equals(action)) {
%>
<bean:message key="taskcompleted.canceled" arg0="<%=user.getUserId()%>" arg1="<%=I18N.getDateAsString(new Date(), user)%>"/>
<% } else if ("abortTask".equals(action)) {
%>
<bean:message key="taskcompleted.aborted" arg0="<%=user.getUserId()%>" arg1="<%=I18N.getDateAsString(new Date(), user)%>"/>
<% } else { %>
<bean:message key="taskcompleted.completed" arg0="<%=user.getUserId()%>" arg1="<%=I18N.getDateAsString(new Date(), user)%>"/>
<% } %>
</div>
</body>
</html:html>
