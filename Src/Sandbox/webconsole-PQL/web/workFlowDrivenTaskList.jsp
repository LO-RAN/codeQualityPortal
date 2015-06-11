<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<app:checkLogon/>

<html:html>
<head>
<title><bean:message key="workFlowDrivenTaskList.title"/></title>
<html:base/>
</head>

<body bgcolor="white">
<html:errors/>

<div align="center">
<h3><bean:message key="workFlowDrivenTaskList.heading"/>
<jsp:getProperty name="user" property="username"/></h3>
</div>

<table border="1" width="100%">

  <tr>
    <th align="center" width="10%">
      <bean:message key="workFlowDrivenTaskList.name"/>
    </th>
    <th align="center" width="10%">
      <bean:message key="workFlowDrivenTaskList.state"/>
    </th>
    <th align="center" width="5%">
      <bean:message key="workFlowDrivenTaskList.priority"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="workFlowDrivenTaskList.var1label"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="workFlowDrivenTaskList.var1value"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="workFlowDrivenTaskList.var2label"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="workFlowDrivenTaskList.var2value"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="workFlowDrivenTaskList.var3label"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="workFlowDrivenTaskList.var3value"/>
    </th>
    <th align="center" width="27%">
      <bean:message key="workFlowDrivenTaskList.actions"/>
    </th>
  </tr>

<!-- Iteration through the workflowitembean objects that where created by the
     action class WorkFlowDrivenTaskListAction. From each task the propeties
     are displayed. Dependend on the state of the task links are created.
     Each link will initiate an action on the task, this action upon the task
     is processed by the ProcessWorkFlowDrivenTaskAction class. Together with
     the action, that is passed as a request parameter to the Action class,
     also the taskid is passed as a request parameter to the Action class.
  -->
<logic:iterate id="workflowitembean" name="workflowitemsbean" property="tasks" scope="request" type="com.compuware.optimal.flow.webconsole.WorkFlowItemBean">
  <tr>
    <td align="center" width="10%">
      <bean:write name="workflowitembean" property="name"/>
    </td>
    <td align="center" width="10%">
      <bean:write name="workflowitembean" property="state"/>
    </td>
    <td align="center" width="5%">
      <bean:write name="workflowitembean" property="priority"/>
    </td>
    <td align="center" width="8%">
      <bean:write name="workflowitembean" property="var1Label"/>
    </td>
    <td align="center" width="8%">
      <bean:write name="workflowitembean" property="var1"/>
    </td>
    <td align="center" width="8%">
      <bean:write name="workflowitembean" property="var2Label"/>
    </td>
    <td align="center" width="8%">
      <bean:write name="workflowitembean" property="var2"/>
    </td>
    <td align="center" width="8%">
      <bean:write name="workflowitembean" property="var3Label"/>
    </td>
    <td align="center" width="8%">
      <bean:write name="workflowitembean" property="var3"/>
    </td>
    <td align="center" width="27%">
      <logic:equal name="workflowitembean" property="state" scope="page" value="Scheduled">
        <html:link forward="executeworkflowdriven" paramId="taskID" paramName="workflowitembean" paramProperty="id" paramScope="page" target="_blank">
          <bean:message key="workFlowDrivenTaskList.execute"/>
        </html:link>
        &nbsp
        <html:link forward="reserveworkflowdriven" paramId="taskID" paramName="workflowitembean" paramProperty="id" paramScope="page">
          <bean:message key="workFlowDrivenTaskList.reserve"/>
        </html:link>
        &nbsp
        <html:link forward="abortworkflowdriven" paramId="taskID" paramName="workflowitembean" paramProperty="id" paramScope="page">
          <bean:message key="workFlowDrivenTaskList.abort"/>
        </html:link>
      </logic:equal>
      <logic:equal name="workflowitembean" property="state" scope="page" value="Reserved">
        <html:link forward="executeworkflowdriven" paramId="taskID" paramName="workflowitembean" paramProperty="id" paramScope="page" target="_blank">
          <bean:message key="workFlowDrivenTaskList.execute"/>
        </html:link>
        &nbsp
        <html:link forward="cancelreservationworkflowdriven" paramId="taskID" paramName="workflowitembean" paramProperty="id" paramScope="page">
          <bean:message key="workFlowDrivenTaskList.cancelreservation"/>
        </html:link>
        &nbsp
      </logic:equal>
      <logic:equal name="workflowitembean" property="state" scope="page" value="Executing">
        <html:link forward="abortworkflowdriven" paramId="taskID" paramName="workflowitembean" paramProperty="id" paramScope="page">
          <bean:message key="workFlowDrivenTaskList.abort"/>
        </html:link>
        &nbsp
      </logic:equal>
    </td>
  </tr>
</logic:iterate>

</table>
</html:form>

<br>
<li><html:link forward="success"><bean:message key="workFlowDrivenTaskList.mainMenu"/></html:link></li>

</body>
</html:html>
