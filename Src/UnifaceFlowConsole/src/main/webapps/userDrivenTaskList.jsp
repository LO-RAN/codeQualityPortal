<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<app:checkLogon/>

<html:html>
<head>
<title><bean:message key="userDrivenTaskList.title"/></title>
<html:base/>
</head>

<body bgcolor="white">
<html:errors/>

<div align="center">
  <h3><bean:message key="userDrivenTaskList.heading"/>
  <!-- Display the name of the user that is logged on in the heading -->
  <jsp:getProperty name="user" property="username"/></h3>

  <table border="1" width="50%">

    <tr>
      <th align="center" width="20%">
        <bean:message key="userDrivenTaskList.name"/>
      </th>
      <th align="center" width="10%">
        <bean:message key="userDrivenTaskList.releaseName"/>
      </th>
      <th align="center" width="10%">
        <bean:message key="userDrivenTaskList.releaseState"/>
      </th>
      <th align="center" width="10%">
        <bean:message key="userDrivenTaskList.releaseVersion"/>
      </th>
    </tr>

  <!-- the User-Driven tasks (Workflow Connector object AvailableTask) are
       retrieved and saved in request scope by the Action class
       UserDrivenTaskListAction. This JSP iterates through that list and each
       item of that list is a UserDrivenItemBean object. From the bean
       several properties are displayed. When the user clicks on the link
       (a link is created from the name property) the Action class
       ProcessTaskAction will be started.
    -->
       
  <logic:iterate id="userdriventask" name="userdrivenitemsbean" property="tasks" scope="request" type="com.compuware.optimal.flow.webconsole.UserDrivenItemBean">
    <tr>
      <td align="center" width="20%">
        <logic:equal name="userdriventask" property="taskType" value="Manual">
          <html:link forward="processUserDriven" paramId="taskID" paramName="userdriventask" paramProperty="taskId" paramScope="page">
            <bean:write name="userdriventask" property="name"/>
          </html:link>
        </logic:equal>
        <logic:equal name="userdriventask" property="taskType" value="Interactive">
          <html:link forward="processUserDriven" paramId="taskID" paramName="userdriventask" paramProperty="taskId" paramScope="page" target="_blank">
            <bean:write name="userdriventask" property="name"/>
          </html:link>
        </logic:equal>
      </td>
      <td align="center" width="10%">
        <bean:write name="userdriventask" property="releaseName"/>
      </td>
      <td align="center" width="10%">
        <bean:write name="userdriventask" property="releaseState"/>
      </td>
      <td align="center" width="10%">
        <bean:write name="userdriventask" property="releaseVersion"/>&nbsp
      </td>
    </tr>
  </logic:iterate>

  </table>
</div>

<br>

<!-- Possible to return to the mainMenu -->
<li><html:link forward="success"><bean:message key="userdriventasklist.mainMenu"/></html:link></li>

</body>
</html:html>
