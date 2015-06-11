<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page errorPage="errorPage.jsp" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.compuware.caqs.domain.dataschemas.CriterionDefinition" %>
<%@ page import="com.compuware.caqs.domain.dataschemas.DialecteBean" %>
<%@ page import="com.compuware.caqs.domain.dataschemas.LinkInfos" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ page import="java.util.*" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <title><bean:message key="caqs.copy.copypaste" /></title>
    <link href="css/carscode.css" rel="stylesheet" type="text/css" />
    <link href="css/just_lab.css" rel="stylesheet" type="text/css" />
</head>
<body>
<jsp:useBean id="currentElement" scope="session" class="com.compuware.caqs.domain.dataschemas.ElementBean" />
<%
    Locale locale = RequestUtil.getLocale(request);
    ResourceBundle resources =  ResourceBundle.getBundle("com.compuware.caqs.Resources.resources", locale);

    Properties prop = (Properties)session.getAttribute("webResources");
    DialecteBean dialecte = currentElement.getDialecte();
    String methodLib = prop.getProperty(dialecte.getLangage().getId()+".method");

    String id_elt = (String)request.getAttribute("id_elt");
    String desc_elt = (String)request.getAttribute("desc_elt");
    String id_bline = (String)request.getAttribute("id_bline");
    String id_crit = request.getParameter("id_crit");
	CriterionDefinition crit = new CriterionDefinition();
	crit.setId(id_crit);
    String lib_crit = crit.getLib(locale);
    Collection links = (Collection)request.getAttribute("links");
    String name = resources.getString("caqs."+methodLib);
%>
    <H2><%=lib_crit%></H2>
    <B><bean:message key="caqs.copy.source" arg0='<%=name%>'/> : </B>&nbsp;<%=desc_elt%>
    <BR><BR>
    <% if (links != null && links.size() > 0) { %>
    <TABLE border='0' cellpadding='0' cellspacing='0'>
        <TR>
            <TH class='borderleftright'><CENTER><bean:message key="caqs.copy.complement" arg0='<%=name %>' /></CENTER></TH>
        </TR>
        <%  Iterator i = links.iterator();
            while (i.hasNext()) {
                LinkInfos li = (LinkInfos)i.next(); %>
                <TR>
                    <TD class='borderleftright'><%=li.getDescEltTo()%></TD>
                </TR>
        <% } %>
    </TABLE>
    <% } else { %>
            <H3><bean:message key="caqs.copy.nodata" /><H3>
    <% } %>
</body>
</html>
