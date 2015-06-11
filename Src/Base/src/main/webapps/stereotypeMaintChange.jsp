<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-bean.tld" prefix="bean" %>

<%@ page import="com.compuware.caqs.presentation.admin.forms.StereotypeForm" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="errorPage.jsp" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<link href="css/carscode.css" rel="stylesheet" type="text/css" />
<jsp:useBean id="user" scope="session" class="com.compuware.caqs.security.auth.Users" />
</head>
<BODY>
<BR>
<html:form action="/StereotypeUpdate.do" method="post">
<H2><bean:message key="caqs.stereotype.saisie" /></H2>
<TABLE>
    <TR>
        <TD class='eltHeader'><bean:message key="caqs.stereotype.id" /></TD>
        <TD>:&nbsp;<html:text name="stereotypeForm" property="id"  /></TD>
    </TR>
    <TR>
        <TD class='eltHeader'><bean:message key="caqs.stereotype.lib" /></TD>
        <TD>:&nbsp;<html:text name="stereotypeForm" property="lib"  /></TD>
    </TR>
    <TR>
        <TD class='eltHeader'><bean:message key="caqs.stereotype.desc" /></TD>
        <TD>:&nbsp;<html:text name="stereotypeForm" property="desc" /></TD>
    </TR>
</TABLE>
<BR/>
<html:submit property="action"><bean:message key="caqs.update" /></html:submit>
<html:cancel property="action"><bean:message key="caqs.annuler" /></html:cancel>
</html:form>
</BODY>
</html>
