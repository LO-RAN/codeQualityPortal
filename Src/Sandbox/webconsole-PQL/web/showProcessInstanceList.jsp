<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="com.compuware.optimal.flow.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>

<% Locale locale = Locale.getDefault(); %>

<meta http-equiv="refresh" content="10"/>
<link href="css/border.css" rel="stylesheet" type="text/css" />

<html:html>

<%
    com.compuware.optimal.flow.webconsole.UserDrivenItemBean task = (com.compuware.optimal.flow.webconsole.UserDrivenItemBean)request.getAttribute("manualTask");
%>
<FORM method="POST" action="startProcess.jsp" target='_blank'>
    <B>Projet:</B>&nbsp;<%=request.getAttribute("projectId")%>
    <INPUT type=SUBMIT name='Submit' value='Nouvelle Analyse'>
    <INPUT type=HIDDEN name='projectId' value='<%=request.getAttribute("projectId")%>'>
    <INPUT type=HIDDEN name='packageId' value='<%=request.getParameter("packageId")%>'>
    <INPUT type=HIDDEN name='taskId' value='<%=task.getTaskDefId()%>'>
</FORM>
<table cellpadding='10' cellspacing='0' width="100%">

  <tr bgcolor='yellow'>
    <th class='bordertopright' align="center" width="12%">
      <bean:message key="processList.extendedtype"/>
    </th>
    <th class='bordertopright' align="center" width="12%">
      <bean:message key="processList.extendedstate"/>
    </th>
    <th class='bordertopright' align="center" width="20%">
      <bean:message key="processList.creationtime"/>
    </th>
    <th class='bordertopright' align="center" width="20%">
      <bean:message key="processList.endtime"/>
    </th>
    <th class='bordertop' align="center" width="20%">
      Variables
    </th>
  </tr>

<!-- This JSP fills in the details when a user selects a process definition
     in the JSP showDefinitionList.jsp. The process was saved in request scope
     by the Action class ProcessInstanceListAction, its details are displayed
     by this JSP in the details frame, see also packageProcessDef.html which
     has the frame definition.
  -->
<logic:iterate id="process" name="processlist" scope="request"
               type="com.compuware.optimal.flow.Process">

  <tr>
    <td class='bordertopright' align="center" width="12%">
      <a href="showActivityInfoOfProcessIns.do?action=ShowActivities&processID=<%=process.getID()%>">
        <bean:write name="process" property="name"/>
      </a>
    </td>
    <td class='bordertopright' align="center" width="12%">
      <bean:write name="process" property="extendedState"/>
    </td>
    <td class='bordertopright' align="center" width="12%">
        <logic:present name="process" property="creationTime">
            <%= java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT, locale).format(process.getCreationTime()) %>
        </logic:present>
    </td>
    <td class='bordertopright' align="center" width="12%">
        <logic:present name="process" property="endTime">
            <%= java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT, locale).format(process.getEndTime()) %>
        </logic:present>
    </td>
    <td class='bordertop' width='52%'>
    <bean:define id="params" name="process" property="actualVariables" type="com.compuware.optimal.flow.ParameterData"/>
    	<%
            String projectId = (String)params.get("vp_projectid");
            if (projectId == null)
                    projectId = "Non saisi";
            String baselineName = (String)params.get("vp_baselinename");
            if (baselineName == null)
                    baselineName = "Non récupérée";
            String error = (String)params.get("vp_error");
            if (projectId == null)
                    projectId = "Pas d'erreur";
    	%>
        <TEXTAREA cols='60' rows='5' readonly>
Project Id&nbsp;:&nbsp;<%= projectId %>
Baseline&nbsp;:&nbsp;<%= baselineName %>
Erreur&nbsp;:&nbsp;<%= error %>
        </TEXTAREA>
    </td>
  </tr>

</logic:iterate>

</table>

</html:html>
