<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="com.compuware.optimal.flow.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>

<% Locale locale = Locale.getDefault(); %>

<html:html>

<table border="1" width="100%">

  <tr>
    <th align="center" width="20%">
      <bean:message key="activityList.name"/>
    </th>
    <th align="center" width="20%">
      <bean:message key="activityList.extendedtype"/>
    </th>
    <th align="center" width="20%">
      <bean:message key="activityList.extendedstate"/>
    </th>
    <th align="center" width="40%">
      <bean:message key="activityList.endtime"/>
    </th>
  </tr>
  
<!-- This JSP displays the activity instances that are related to the selected
     process instance. The process instance was selected on the
     showProcessSelectList.jsp JSP. The list of activities was build by the 
     Action class ActivityInstanceListAction.java.
  -->
<logic:iterate id="activity" name="activitylist" property="top" scope="request"
               type="com.compuware.optimal.flow.Activity">

  <tr>
    <td align="center" width="20%">
      <bean:write name="activity" property="name"/>
    </td>
    <td align="center" width="20%">
      <bean:write name="activity" property="extendedType"/>
    </td>
    <td align="center" width="20%">
      <bean:write name="activity" property="extendedState"/>
    </td>
    <td align="center" width="40%">
        <logic:present name="activity" property="endTime">
            <%= java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT, locale).format(activity.getEndTime()) %>
        </logic:present>
    </td>
  </tr>
  
</logic:iterate>

</table>

</html:html>
