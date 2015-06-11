<%@ page language="java" import="com.compuware.carscode.unifaceflow.kpi.action.*" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
<title><bean:message key="process.instances.title"/></title>
<link rel="stylesheet" href="css/ovtasklist.css" type="text/css">
<html:base/>
</head>
<body bgcolor="white">
<h3><bean:message key="process.instances.title" /></h3>
<table cellpadding='0' cellspacing='0'>
    <tr>
        <th class="tableHeading"><bean:message key="process.instances.processname" /></th>
        <th class="tableHeading"><bean:message key="process.instances.createtime" /></th>
        <th class="tableHeading"><bean:message key="process.instances.starttime" /></th>
        <th class="tableHeading"><bean:message key="process.instances.endtime" /></th>
        <th class="tableHeading"><bean:message key="process.instances.executiontime" /></th>
        <th class="tableHeading"><bean:message key="process.instances.waitingtime" /></th>
        <th class="tableHeading"><bean:message key="process.instances.globaltime" /></th>
    </tr>
    <logic:iterate id="processTaskBean" name="<%=RetrieveProcessInstanceDataAction.PROCESS_BEAN_COLLECTION_KEY%>" type="com.compuware.carscode.unifaceflow.kpi.dao.ProcessTaskBean">
    <tr>
        <td class="cellProcess">
            <a href="retrieveProcessTaskData.do?processId=<bean:write name="processTaskBean" property="id" filter="true" />" target="taskfrm"><bean:write name="processTaskBean" property="name" filter="true" /></a>
        </td>
        <td class="cell">
            <bean:write name="processTaskBean" property="createTimeStr" filter="true" />
        </td>
        <td class="cell">
            <bean:write name="processTaskBean" property="startTimeStr" filter="true" />
        </td>
        <td class="cell">
            <bean:write name="processTaskBean" property="endTimeStr" filter="true" />
        </td>
        <td class="cell">
            <logic:notEqual name="processTaskBean" property="executionDays" value="0">
                <bean:write name="processTaskBean" property="executionDays" filter="true" />j&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processTaskBean" property="executionHrs" value="0">
                <bean:write name="processTaskBean" property="executionHrs" filter="true" />h&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processTaskBean" property="executionMin" value="0">
                <bean:write name="processTaskBean" property="executionMin" filter="true" />min&nbsp;
            </logic:notEqual>
            <bean:write name="processTaskBean" property="executionSec" filter="true" />s
        </td>
        <td class="cell">
            <logic:notEqual name="processTaskBean" property="waitingDays" value="0">
                <bean:write name="processTaskBean" property="waitingDays" filter="true" />j&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processTaskBean" property="waitingHrs" value="0">
                <bean:write name="processTaskBean" property="waitingHrs" filter="true" />h&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processTaskBean" property="waitingMin" value="0">
                <bean:write name="processTaskBean" property="waitingMin" filter="true" />min&nbsp;
            </logic:notEqual>
            <bean:write name="processTaskBean" property="waitingSec" filter="true" />s
        </td>
        <td class="cell">
            <logic:notEqual name="processTaskBean" property="globalDays" value="0">
                <bean:write name="processTaskBean" property="globalDays" filter="true" />j&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processTaskBean" property="globalHrs" value="0">
                <bean:write name="processTaskBean" property="globalHrs" filter="true" />h&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processTaskBean" property="globalMin" value="0">
                <bean:write name="processTaskBean" property="globalMin" filter="true" />min&nbsp;
            </logic:notEqual>
            <bean:write name="processTaskBean" property="globalSec" filter="true" />s
        </td>
    </tr>
    </logic:iterate>
</table>
<iframe name="taskfrm" id="taskfrm" src="blank.htm" width="100%" height="300" border="0"></iframe>
<a href="retrieveAggregatedProcessData.do">
    <bean:message key="process.instances.back" />
</a>
</body>
</html:html>
