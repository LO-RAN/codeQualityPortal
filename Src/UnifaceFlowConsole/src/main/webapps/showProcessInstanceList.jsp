<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>

<table border="1" width="100%">

  <tr>
    <th align="center" width="12%">
      <bean:message key="processList.extendedtype"/>
    </th>
    <th align="center" width="12%">
      <bean:message key="processList.extendedstate"/>
    </th>
    <th align="center" width="38%">
      <bean:message key="processList.creationtime"/>
    </th>
    <th align="center" width="38%">
      <bean:message key="processList.endtime"/>
    </th>
  </tr>

<!-- This JSP fills in the details when a user selects a process definition
     in the JSP showDefinitionList.jsp. The process was saved in request scope
     by the Action class ProcessInstanceListAction, its details are displayed
     by this JSP in the details frame, see also packageProcessDef.html which
     has the frame definition.
  -->
<logic:iterate id="process" name="processlist" property="top" scope="request"
               type="com.compuware.optimal.flow.Process">

  <tr>
    <td align="center" width="12%">
      <bean:write name="process" property="extendedType"/>
    </td>
    <td align="center" width="12%">
      <bean:write name="process" property="extendedState"/>
    </td>
    <td align="center" width="38%">
      <bean:write name="process" property="creationTime"/>
    </td>
    <td align="center" width="38%">
      <bean:write name="process" property="endTime"/>
    </td>
  </tr>
  
</logic:iterate>

</table>

</html:html>
