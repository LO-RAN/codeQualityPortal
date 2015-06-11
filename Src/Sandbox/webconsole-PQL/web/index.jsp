<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!-- This is the JSP that is displayed when the user starts the Web Console
     demonstration application.
     The application is started by typing http://localhost:8181/webconsole
     in a browser window.
  -->

<html:html locale="true">
<head>
<title><bean:message key="index.title"/></title>
<html:base/>
</head>
<body bgcolor="white">

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<h3><bean:message key="index.heading"/></h3>

<ul>
<li><html:link page="/logon.jsp"><bean:message key="index.logon"/></html:link></li>
<li><html:link page="/logonNoPassword.jsp"><bean:message key="index.logonNoPassword"/></html:link></li>
</ul>

<html:img page="/struts_power.gif" alt="Powered by Struts"/>
<br>
<html:img page="/of.gif" alt="OptimalFlow"/>

</body>
</html:html>
