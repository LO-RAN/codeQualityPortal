<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html"%>
<%@ page import="com.compuware.caqs.domain.dataschemas.CriterionDefinition" %>
<%@ page import="com.compuware.caqs.domain.dataschemas.LinkInfos" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <title><bean:message key="caqs.archi.liens" /></title>
    <link href="css/carscode.css" rel="stylesheet" type="text/css" />
    <link href="css/just_lab.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
    Locale locale = RequestUtil.getLocale(request);
    String id_crit = (String)request.getAttribute("id_crit");
	CriterionDefinition crit = new CriterionDefinition();
	crit.setId(id_crit);
    String lib_crit = crit.getLib(locale);
    Collection links = (Collection)request.getAttribute("links");
%>
    <H2><%=lib_crit%></H2>
    <% if (links != null && links.size() > 0) { %>
    <TABLE border='0' cellpadding='0' cellspacing='0'>
        <TR bgcolor='yellow'>
            <TD class='borderleft'><CENTER><B><bean:message key="caqs.archi.appelant" /></B></CENTER></TD>
            <TD class='borderleft'><CENTER><B><bean:message key="caqs.archi.package" /></B></CENTER></TD>
            <TD class='borderleft'><CENTER><B><bean:message key="caqs.archi.appele" /></B></CENTER></TD>
            <TD class='borderleftright'><CENTER><B><bean:message key="caqs.archi.package" /></B></CENTER></TD>
        </TR>
        <%  Iterator i = links.iterator();
            while (i.hasNext()) {
                LinkInfos li = (LinkInfos)i.next(); %>
                <TR>
                    <TD title='<%=li.getDescEltFrom()%>' class='borderleft'><%=li.getLibEltFrom()%></TD>
                    <TD class='borderleft'><%=li.getLibPackFrom()%></TD>
                    <TD title='<%=li.getDescEltTo()%>' class='borderleft'><%=li.getLibEltTo()%></TD>
                    <TD class='borderleftright'><%=li.getLibPackTo()%></TD>
                </TR>
        <% } %>
    </TABLE>
    <% } else { %>
            <H3>
            	<bean:message key="caqs.nodata" />
           	</H3>
    <% } %>
</body>
</html>
