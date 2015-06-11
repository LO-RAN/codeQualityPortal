<%@ page language="java" import="java.util.*,com.compuware.optimal.flow.*,com.compuware.optimalview.tasklist.*,com.compuware.optimalview.util.*,com.compuware.carscode.action.RetrieveProjectAction" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<% TaskDataBean task = (TaskDataBean) request.getAttribute("task");
   User user = (User) request.getAttribute("user");
   ValidationExceptionList exceptionList = (ValidationExceptionList) request.getAttribute("exception");
%>
<html:html locale="true">
<head>
   <title><bean:message key="taskexec.title"/></title>
   <link rel="stylesheet" href="ovtasklist.css" type="text/css">
   <script language="JavaScript" src="OFCommon.js"></script>
   <script language="JavaScript" type="text/javascript">
<!--
      var taskListWindow;

      function initPage() {
         preloadImages("images/completebuttonpressed.gif", "images/resetbuttonpressed.gif", "images/cancelbuttonpressed.gif");
         reloadTaskList();
         return(0);
      }

      function exitPage() {
         return(0);
      }

      function submitAction(action, taskURI, queryString) {
         if (action == "resetTask") {
            document.taskExecForm.reset();
            //- Restore task URI and query string for next action
            document.taskExecForm.URI.value = taskURI;
            document.taskExecForm.action = "taskexec.do?" + queryString;
         }
         else {
            document.taskExecForm.formAction.value = action;
            document.taskExecForm.URI.value = taskURI;
            document.taskExecForm.action = "taskexec.do?" + queryString;
            document.taskExecForm.submit();
            return(0);
         }
      }

      function showDetails(taskURI) {
         if (parent && parent.OFTaskList && parent.OFTaskList.switchMode) {
            parent.OFTaskList.switchMode("showTaskDetails", taskURI);
         } else if (opener && opener.switchMode) {
            opener.switchMode("showTaskDetails", taskURI);
         } else if (opener && opener.OFTaskList && opener.OFTaskList.switchMode) {
            opener.OFTaskList.switchMode("showTaskDetails", taskURI);
         } 
      }

//-->
   </script>
<%@ include file="customexec.jsp" %>
   <html:base/>
</head>
<body onLoad="initPage()">

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<%@ include file="includes/taskexec/showtaskexecform.jsp" %>
</body>
</html:html>

