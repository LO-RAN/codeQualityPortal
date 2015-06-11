<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page errorPage="errorPage.jsp"
		import="java.io.*,
                java.util.Locale,
                java.util.ResourceBundle"
%>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" href="styles.css" type="text/css">
</head>
<body topmargin="0">
<font face="Verdana, Arial, Helvetica, sans-serif" size="4">
<%
           Locale locale = RequestUtil.getLocale(request);
            ResourceBundle resources =  ResourceBundle.getBundle("com.compuware.caqs.Resources.resources", locale);
    String base = (String)session.getAttribute("baseline");
    String path = application.getRealPath("/"+resources.getString("caqs.devpartner")+"/" + base);
    File dir = new File(path);
    if (!dir.exists())
        dir.mkdir();
    String[] list;
    list = dir.list();

    String cmd = request.getParameter("cmd");
    if (cmd != null) {
        if (cmd.equals("delete")) {
            File[] file_list = dir.listFiles();
            for (int i = 0; i < file_list.length; i++) {
                file_list[i].delete();
            }
            list = dir.list();
        }    
    }
%>
<H3><bean:message key="caqs.showdir.title" />:</H3>
<TABLE>
    <% for (int i = 0; i < list.length; i++) { %>
    <TR>
        <TD>
            <IMG src='/images/csv.gif' width='24' height='24' />
        </TD>
        <TD>
            <%= list[i] %>
        </TD>
    </TR>
    <% } %>
</TABLE>
</font>
</body>
</html>

