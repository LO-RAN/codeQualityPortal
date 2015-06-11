<%@ page language="java" import="java.util.*,com.compuware.optimal.flow.*,com.compuware.optimalview.tasklist.*,com.compuware.optimalview.util.*" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<% User user = (User) request.getAttribute("user");
   String filter = request.getParameter("filter");
   if (filter == null || filter.length() == 0) {
      filter = request.getParameter("detailFilter");
   }
   String view = request.getParameter("view");
   String mode = request.getParameter("mode");
   String queryString = (request.getQueryString() != null) ? request.getQueryString() : "";
   Date now = new Date();
%>
<html:html locale="true">
<head>
   <title><bean:message key="tasklist.title"/></title>
   <link rel="stylesheet" href="ovtasklist.css" type="text/css">
   <script src="OFCommon.js"></script>
   <script language="JavaScript">
   <!--
   var intervalId;

   function initPage() {
      preloadImages("images/releasehover.gif", "images/reservehover.gif",
         "images/aborthover.gif", "images/executehover.gif",
         "images/expand.gif", "images/collapse.gif",
         "images/detailshover.gif", "images/tasklisthover.gif",
         "images/detailssmallhover.gif");
      intervalId = window.setInterval("doRefresh()", 60000);
      return (0);
   }
   
   function exitPage () {
      window.clearInterval(intervalId);
      return (0);
   }
   
   function doRefresh () {
      submitAction("", "", "<%=queryString%>");
   }
   
   function switchMode (mode, taskURI) {
      var view;
      view = getCookie("view");
      if (view == null) {
         view = "collapse";
      }
      document.taskListForm.URI.value = taskURI;
      
      document.taskListForm.action = "tasklist.do";
      if (taskURI.charAt(0) != '?') {
         document.taskListForm.action = document.taskListForm.action + "?";
      }
      document.taskListForm.action = document.taskListForm.action + taskURI +
         "&mode=" + mode + "&view=" + view;
         
      document.taskListForm.submit();
      return (0);
   }
   
   function switchView (view) {
      document.cookie = "view=" + view;
      switchMode("showTaskList", "");
   }
   
   function submitAction (action, taskURI, queryString) {
      if (action == "cancelTask" || action == "abortTask") {
         startStopTask(action, taskURI);
      }
      
      document.taskListForm.formAction.value = action;
      document.taskListForm.URI.value = taskURI;
      
      if (queryString != "" && queryString != null) {
         document.taskListForm.action = "tasklist.do?" + queryString;
      }
      document.taskListForm.submit();
      return (0);
   }
   
   function startStopTask (action, taskURI) {
      var idStart, idEnd;
      var taskExecURL, idString;
      var taskExecWindow;
      
      if (startStopTask.arguments < 2) {
         action = "start";
      }
      
      if (taskURI.charAt(0) == '?') {
         taskExecURL = "taskexec.do" + taskURI + "&userId=<%=user.getUserId()%>&action=" + action + "&r=" + new Date().getMilliseconds();
      } else {
         taskExecURL = taskURI;
      }
      
      if (document.all && window.parent && window.parent.OFTaskExec != null) {
         taskExecWindow = window.parent.open(taskExecURL, "OFTaskExec");
      } else {
         idStart = taskURI.indexOf("identifier=") + 11;
         idEnd = taskURI.indexOf("&ActorID=");
         idString = taskURI.slice(idStart, idEnd);
         openerWindow = window;
         if (document.all && window.parent) {
            openerWindow = window.parent;
         }
         taskExecWindow = openerWindow.open(taskExecURL, idString, 
            "height=370, width=500, resizable=yes, scrollbars=yes");
      }
      
      if (action == "start") {
         taskExecWindow.focus();
      } else {
         taskExecWindow.close();
      }
   }
    
   function startTask (taskURI) {
      startStopTask("start", taskURI);
   }
   
   //-->
   </script>
   <html:base/>
</head>
<body onLoad="initPage()" onUnload="exitPage()">

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<form name="taskListForm" method="post" action="tasklist.do">
   <input type="hidden" name="userId" value="<%=user.getUserId()%>">
   <input type="hidden" name="URI" value="">
   <input type="hidden" name="formAction" value="">
<% if ("showTaskDetails".equals(mode)) {
%>
<%@ include file="includes/tasklist/showtaskdetails.jsp" %>
<% } else {
%>
<%@ include file="includes/tasklist/showtasklist.jsp" %>
<% }
%>
</form>
</body>
</html:html>
