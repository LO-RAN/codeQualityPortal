<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html:html locale="true">
<head>
<title><bean:message key="logonNoPassword.title"/></title>
<html:base/>
</head>
<body bgcolor="white">

<html:errors/>

<!-- Action class LogonNoPasswordAction will be given control when the form 
     is submitted. That Action class will create the WorkflowConnector object
     and the User object, both objects are saved in session scope and used
     thru the whole application by the other Action classes.
  -->
<html:form action="/logonNoPassword" focus="username">
<table border="0" width="100%">

  <tr>
    <th align="right">
      <bean:message key="prompt.username"/>
    </th>
    <td align="left">
      <html:text property="username" size="16" maxlength="16"/>
    </td>
  </tr>

  <tr>
    <td align="right">
      <html:submit property="submit" value="Submit"/>
    </td>
    <td align="left">
      <html:reset/>
    </td>
  </tr>

</table>

</html:form>

</body>
</html:html>
