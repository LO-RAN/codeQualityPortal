<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<app:checkLogon/>

<html:html>
<head>
<title><bean:message key="historyTaskList.title"/></title>
<html:base/>
</head>

<body bgcolor="white">

<html:errors/>

<div align="center">
<h3><bean:message key="historyTaskList.heading"/>
<jsp:getProperty name="user" property="username"/></h3>
</div>

<!-- This JSP displays the history Tasks for the user that is logged on.
     See the Action class HistoryTaskListAction that builds the historytasklist,
     when the user clicks on the Top, Bottom, Next, Previous button.
     The historytasklist object is saved in request scope and each task in this
     list is displayed by this JSP.
     This JSP also provides the possibility to set a Filter, Order and Stepsize
     on the list that will be returned. It is only possible to set a Filter, Order
     and/or Stepsize initially or after pressing the Reset button.
  -->
<html:form action="/showHistoryTaskList">

<logic:present name="position">
  <bean:define id="posexists" value="true"/>
</logic:present>
<logic:notPresent name="position">
  <bean:define id="posexists" value="false"/>
</logic:notPresent>

<div align="center">
<table border="0" width="80%">

  <tr>
    <th align="right">
      <bean:message key="prompt.stepsize"/>
    </th>
    <td align="left">
      <html:text property="stepsize" size="3" maxlength="10"
                 disabled='<%= (new java.lang.Boolean((String)pageContext.findAttribute("posexists"))).booleanValue() %>'/>
    </td>
    
    <th align="right">
      <bean:message key="prompt.filterselect"/>
    </th>
    <td align="left">
      <html:select name="historyTaskListForm" property="filterselect" multiple="true" size="3"
                   disabled='<%= (new java.lang.Boolean((String)pageContext.findAttribute("posexists"))).booleanValue() %>'>
        <html:option key="historyTaskList.filteroption.none" value="none"/>
        <html:option key="historyTaskList.filteroption.name.equals" value="nameequals"/>
        <html:option key="historyTaskList.filteroption.state.equals" value="stateequals"/>
      </html:select>
    </td>
    
    <th align="right">
      <bean:message key="prompt.filtervalue"/>
    </th>
    <td align="left">
      <html:text property="filtervalue" size="15" maxlength="50"
                 disabled='<%= (new java.lang.Boolean((String)pageContext.findAttribute("posexists"))).booleanValue() %>'/>
    </td>
    
    <th align="right">
      <bean:message key="prompt.sortoption"/>
    </th>
    <td align="left">
      <html:select name="historyTaskListForm" property="sortselect" multiple="true" size="5"
                   disabled='<%= (new java.lang.Boolean((String)pageContext.findAttribute("posexists"))).booleanValue() %>'>
        <html:option key="historyTaskList.sortoption.none" value="none"/>
        <html:option key="historyTaskList.sortoption.name.asc" value="nameasc"/>
        <html:option key="historyTaskList.sortoption.name.desc" value="namedesc"/>
        <html:option key="historyTaskList.sortoption.state.asc" value="stateasc"/>
        <html:option key="historyTaskList.sortoption.state.desc" value="statedesc"/>
      </html:select>
    </td>
    
  </tr>

</table>

<br>

<html:submit property="action" value="Top"/>
<logic:present name="position">
  <html:submit property="action" value="Next"/>
  <html:submit property="action" value="Previous"/>
</logic:present>
<html:submit property="action" value="Bottom"/>
<html:submit property="action" value="Reset"/>

</div>

<br>

<logic:present name="historytasklist">
<table border="1" width="100%">

  <tr>
    <th align="center" width="10%">
      <bean:message key="historyTaskList.name"/>
    </th>
    <th align="center" width="10%">
      <bean:message key="historyTaskList.state"/>
    </th>
    <th align="center" width="5%">
      <bean:message key="historyTaskList.priority"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="historyTaskList.var1label"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="historyTaskList.var1value"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="historyTaskList.creationtime"/>
    </th>
    <th align="center" width="8%">
      <bean:message key="historyTaskList.comments"/>
    </th>
    <th align="center" width="27%">
      <bean:message key="historyTaskList.actions"/>
    </th>
  </tr>

<logic:iterate id="task" collection='<%= request.getAttribute("historytasklist") %>'
               type="com.compuware.optimal.flow.Task">

  <tr>
    <td align="center" width="10%">
      <bean:write name="task" property="name"/>
    </td>
    <td align="center" width="10%">
      <bean:write name="task" property="state"/>
    </td>
    <td align="center" width="4%">
      <bean:write name="task" property="priority"/>
    </td>
    <td align="center" width="8%">
      <bean:write name="task" property="var1Label"/>&nbsp
    </td>
    <td align="center" width="8%">
      <bean:write name="task" property="var1"/>&nbsp
    </td>
    <td align="center" width="20%">
      <bean:write name="task" property="creationTime"/>
    </td>
    <td align="center" width="20%">
      <bean:write name="task" property="comments"/>&nbsp
    </td>
    <td align="center" width="20%">
    links to be added
    </td>
  </tr>
</logic:iterate>
</logic:present>

</table>
</html:form>

<br>
<li><html:link forward="success"><bean:message key="historyTaskList.mainMenu"/></html:link></li>

</body>
</html:html>
