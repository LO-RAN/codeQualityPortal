<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>
  
<!-- When the user selects a process instance on the form that was displayed
     by JSP showOrganizationTree.jsp this JSP will check if the details of a
     Unit, Actor or Role are requested. This check is done by checking the 
     presence of a request scope object unitdetails, actordetails or
     roledetails. The object saved was actually a Workflow Connector object
     Unit, Actor or Role.
  -->
<logic:present name="unitdetails" scope="request">
  <bean:define id="unit" name="unitdetails" scope="request" type="com.compuware.optimal.flow.Unit"/>

<table border="1" width="80%">

  <tr>
    <th align="center" width="20%">
      <bean:message key="unit.details.name"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="unit" property="name"/>
    </td>
  </tr>
  
  <tr>
    <th align="center" width="20%">
      <bean:message key="unit.details.unittype"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="unit" property="unitType"/>
    </td>
  </tr>
  
  <tr>
    <th align="center" width="20%">
      <bean:message key="unit.details.description"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="unit" property="description"/>
    </td>
  </tr>
  
  <tr>
    <th align="center" width="20%">
      <bean:message key="unit.details.comments"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="unit" property="comments"/>
    </td>
  </tr>
  
</table>

</logic:present>

<logic:present name="actordetails" scope="request">
  <bean:define id="actor" name="actordetails" scope="request" type="com.compuware.optimal.flow.Actor"/>

<table border="1" width="80%">

  <tr>
    <th align="center" width="20%">
      <bean:message key="actor.details.name"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="actor" property="name"/>
    </td>
  </tr>
  
  <tr>
    <th align="center" width="20%">
      <bean:message key="actor.details.displayName"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="actor" property="displayName"/>
    </td>
  </tr>
  
  <tr>
    <th align="center" width="20%">
      <bean:message key="actor.details.availableon"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="actor" property="availableOn"/>
    </td>
  </tr>

  <tr>
    <th align="center" width="20%">
      <bean:message key="actor.details.location"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="actor" property="location"/>
    </td>
  </tr>

  <tr>
    <th align="center" width="20%">
      <bean:message key="actor.details.timeavailable"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="actor" property="timeAvailable"/>
    </td>
  </tr>

  <tr>
    <th align="center" width="20%">
      <bean:message key="actor.details.description"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="actor" property="description"/>
    </td>
  </tr>

  <tr>
    <th align="center" width="20%">
      <bean:message key="actor.details.comments"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="actor" property="comments"/>
    </td>
  </tr>

</table>

</logic:present>

<logic:present name="roledetails" scope="request">
  <bean:define id="role" name="roledetails" scope="request" type="com.compuware.optimal.flow.Role"/>

<table border="1" width="100%">

  <tr>
    <th align="center" width="20%">
      <bean:message key="role.details.name"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="role" property="name"/>
    </td>
  </tr>

  <tr>
    <th align="center" width="20%">
      <bean:message key="role.details.description"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="role" property="description"/>
    </td>
  </tr>
  
  <tr>
    <th align="center" width="20%">
      <bean:message key="role.details.comments"/>
    </th>
    <td align="center" width="80%">
      <bean:write name="role" property="comments"/>
    </td>
  </tr>
  
</table>

</logic:present>

</html:html>
