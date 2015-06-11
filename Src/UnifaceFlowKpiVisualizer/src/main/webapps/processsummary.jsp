<%@ page language="java" import="com.compuware.carscode.unifaceflow.kpi.action.*" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
<title><bean:message key="process.summary.title"/></title>
<link rel="stylesheet" href="css/ovtasklist.css" type="text/css">
<html:base/>
</head>
<body bgcolor="white">
<h3><bean:message key="process.summary.title" /></h3>
<table cellpadding='0' cellspacing='0'>
    <tr>
        <th class="tableHeading"><bean:message key="process.summary.processname" /></th>
        <th class="tableHeading"><bean:message key="process.summary.nbexecutions" /></th>
        <th class="tableHeading"><bean:message key="process.summary.avgexecutiontime" /></th>
        <th class="tableHeading"><bean:message key="process.summary.avgwaitingtime" /></th>
        <th class="tableHeading"><bean:message key="process.summary.avgglobaltime" /></th>
    </tr>
    <logic:iterate id="processSummaryBean" name="<%=RetrieveAggregatedProcessDataAction.PROCESS_SUMMARY_BEAN_COLLECTION_KEY%>" type="com.compuware.carscode.unifaceflow.kpi.dao.ProcessSummaryBean">
    <tr>
        <td class="cellProcess">
            <a href="retrieveProcessInstanceData.do?processId=<bean:write name="processSummaryBean" property="id" filter="true" />"><bean:write name="processSummaryBean" property="name" filter="true" /></a>
        </td>
        <td class="cell"><bean:write name="processSummaryBean" property="nbExecutions" filter="true" /></td>
        <td class="cell">
            <logic:notEqual name="processSummaryBean" property="avgExecutionDays" value="0">
                <bean:write name="processSummaryBean" property="avgExecutionDays" filter="true" />j&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processSummaryBean" property="avgExecutionHrs" value="0">
                <bean:write name="processSummaryBean" property="avgExecutionHrs" filter="true" />h&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processSummaryBean" property="avgExecutionMin" value="0">
                <bean:write name="processSummaryBean" property="avgExecutionMin" filter="true" />min&nbsp;
            </logic:notEqual>
            <bean:write name="processSummaryBean" property="avgExecutionSec" filter="true" />s
        </td>
        <td class="cell">
            <logic:notEqual name="processSummaryBean" property="avgWaitingDays" value="0">
                <bean:write name="processSummaryBean" property="avgWaitingDays" filter="true" />j&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processSummaryBean" property="avgWaitingHrs" value="0">
                <bean:write name="processSummaryBean" property="avgWaitingHrs" filter="true" />h&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processSummaryBean" property="avgWaitingMin" value="0">
                <bean:write name="processSummaryBean" property="avgWaitingMin" filter="true" />min&nbsp;
            </logic:notEqual>
            <bean:write name="processSummaryBean" property="avgWaitingSec" filter="true" />s
        </td>
        <td class="cell">
            <logic:notEqual name="processSummaryBean" property="avgGlobalDays" value="0">
                <bean:write name="processSummaryBean" property="avgGlobalDays" filter="true" />j&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processSummaryBean" property="avgGlobalHrs" value="0">
                <bean:write name="processSummaryBean" property="avgGlobalHrs" filter="true" />h&nbsp;
            </logic:notEqual>
            <logic:notEqual name="processSummaryBean" property="avgGlobalMin" value="0">
                <bean:write name="processSummaryBean" property="avgGlobalMin" filter="true" />min&nbsp;
            </logic:notEqual>
            <bean:write name="processSummaryBean" property="avgGlobalSec" filter="true" />s
        </td>
    </tr>
    </logic:iterate>
</table>
</body>
</html:html>
