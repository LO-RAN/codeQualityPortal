<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<app:checkLogon/>
<jsp:useBean id="user" scope="session" type="com.compuware.optimal.flow.webconsole.User"/>

<html:html>
<head>
<title><bean:message key="mainMenu.title"/></title>
<html:base/>
</head>
<body bgcolor="white">

<html:errors/>

<!-- This is the main Menu of the application, the user selects a link 
     and the related Action class will be given control. See also the
     struts-config.xml file.
  -->
<h3><bean:message key="mainMenu.heading"/>
<jsp:getProperty name="user" property="username"/></h3>
<IFRAME src='http://cwfr-d070:8181/webconsole/packageProcessDef.html' width='100%' height='500'></IFRAME>
</body>
</html:html>
