<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!-- This JSP is used when a failure like a WorkflowException occurs in a
     Action class. Control is passed to this JSP when such an exception
     occurs.
  -->
<html:html locale="true">
<head>
  <title><bean:message key="failure.title"/></title>
  <html:base/>
</head>
<body bgcolor="white">

<html:errors/>

</body>
</html:html>
