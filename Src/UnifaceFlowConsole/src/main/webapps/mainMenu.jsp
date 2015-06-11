<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<app:checkLogon/>
<jsp:useBean id="user" scope="session" type="com.compuware.optimal.flow.webconsole.User"/>

<html:html>
<head>
<title><bean:message key="mainMenu.title"/></title>
<html:base/>
</head>
<body bgcolor="white">

<html:errors/>

<!-- This is the main Menu of the application, the user selects a link 
     and the related Action class will be given control. See also the
     struts-config.xml file.
  -->
<h3><bean:message key="mainMenu.heading"/>
<jsp:getProperty name="user" property="username"/></h3>
<ul>
  <li>
    <html:link page="/showUserDrivenTaskList.do" target="_top">
      <bean:message key="mainMenu.userdriventasklist"/>
    </html:link>
  </li>
  <li>
    <html:link page="/showWorkflowDrivenTaskList.do" target="_top">
      <bean:message key="mainMenu.workflowdriventasklist"/>
    </html:link>
  </li>
  <li>
    <html:link page="/showHistoryTaskList.do" target="_top">
      <bean:message key="mainMenu.historytasklist"/>
    </html:link>
  </li>
  <li>
    <html:link page="/packageProcessDef.html" target="_top">
      <bean:message key="mainMenu.processInstanceInfo"/>
    </html:link>
  </li>
  <li>
    <html:link page="/packageProcessInstances.html" target="_top">
      <bean:message key="mainMenu.activityInstanceInfo"/>
    </html:link>
  </li>
  <li>
    <html:link page="/organizationDiagram.html" target="_top">
      <bean:message key="mainMenu.organization.diagram"/>
    </html:link>
  </li>
  <li>
    <html:link forward="logoff" target="_top">
      <bean:message key="mainMenu.logoff"/>
    </html:link>
  </li>
</ul>

</body>
</html:html>
