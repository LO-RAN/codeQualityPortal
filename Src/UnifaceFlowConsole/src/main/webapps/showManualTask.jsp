<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<app:checkLogon/>

<html:html>

<script language="JavaScript" src="manualTask.js">
</script>

<head>
<title><bean:message key="showManualTask.title"/></title>
<html:base/>
</head>

<body bgcolor="white" onfocus="focusTask()">
<html:errors/>

<form>
<div align="center">
  <h3><bean:message key="showManualTask.heading"/>
      <!-- Display the name of the task in the heading. -->
      <bean:write name="taskname"/>
      <bean:message key="showManualTask.heading1"/>
  <!-- Display the name of the user that is logged on in the heading. -->
  <jsp:getProperty name="user" property="username"/></h3>

  <% String $taskid$ = (String)session.getAttribute("task"); %>
  <input type=hidden name="taskID" value=<%=$taskid$%>>

<table border="1" width="40%">

  <!-- Iterate through the ParameterItemBean's that were saved in session scope -->
  <logic:iterate id="pitem" name="parameteritems" property="params" scope="session" type="com.compuware.optimal.flow.webconsole.ParameterItemBean">
  <tr>
    <td>
      <bean:write name="pitem" property="name"/>(<bean:write name="pitem" property="dataType"/>)
    </td>
    <td>
      <logic:equal name="pitem" property="inputOnly" value="true">
        <logic:equal name="pitem" property="dataType" value="Boolean">
          <logic:equal name="pitem" property="value" value="true">
            <input type=checkbox disabled="true" name=<bean:write name="pitem" property="name"/> checked
                 value=<bean:write name="pitem" property="value"/>>
          </logic:equal>
          <logic:equal name="pitem" property="value" value="false">
            <input type=checkbox disabled="true" name=<bean:write name="pitem" property="name"/>
                 value=<bean:write name="pitem" property="value"/>>
          </logic:equal>
        </logic:equal>
        <logic:equal name="pitem" property="dataType" value="String">
          <input type=text disabled="true" name=<bean:write name="pitem" property="name"/>
                 value=<bean:write name="pitem" property="value"/>>
        </logic:equal>
        <logic:equal name="pitem" property="dataType" value="Double">
          <input type=text disabled="true" name=<bean:write name="pitem" property="name"/>
                 value=<bean:write name="pitem" property="value"/>>
        </logic:equal>
      </logic:equal>
    
      <logic:equal name="pitem" property="inputOnly" value="false">
        <logic:equal name="pitem" property="dataType" value="Boolean">
          <logic:equal name="pitem" property="value" value="true">
            <input type=checkbox name=<bean:write name="pitem" property="name"/> checked
                 value=<bean:write name="pitem" property="value"/>>
          </logic:equal>
          <logic:equal name="pitem" property="value" value="false">
            <input type=checkbox name=<bean:write name="pitem" property="name"/>
                 value=<bean:write name="pitem" property="value"/>>
          </logic:equal>
        </logic:equal>
        <logic:equal name="pitem" property="dataType" value="String">
          <input type=text name=<bean:write name="pitem" property="name"/>
                 value=<bean:write name="pitem" property="value"/>>
        </logic:equal>
        <logic:equal name="pitem" property="dataType" value="Double">
          <input type=text name=<bean:write name="pitem" property="name"/>
                 value=<bean:write name="pitem" property="value"/>>
        </logic:equal>
      </logic:equal>
    </td>
  </tr>
  </logic:iterate>
  
</table>
  
  <br>
  
  <html:button onclick="completeTask()" property="action" value="Complete">
  </html:button>
  <html:button onclick="cancelTask()" property="action" value="Cancel">
  </html:button>
  
</div>

<br>

</form>

</body>
</html:html>
