<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<meta http-equiv="pragma" content="no-cache">
<%@ include file="commonHeaderElement.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/auth.tld" prefix="auth" %>

<auth:notAccess function="Criterion_Detail">
	<jsp:forward page="noaccess.jsp"/>
</auth:notAccess>

<%@ page import="com.compuware.caqs.domain.dataschemas.*" %>
<%@ page import="com.compuware.caqs.presentation.consult.forms.*" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="currentElement" scope="session" class="com.compuware.caqs.domain.dataschemas.ElementBean" />
<jsp:useBean id="critereList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="criterionDefinition" scope="session" class="com.compuware.caqs.domain.dataschemas.CriterionDefinition" />

<%
Locale locale = RequestUtil.getLocale(request);

String lastTypeElt = (String)session.getAttribute("typeElt");

String all = (String)request.getAttribute("all");
String full = (String)request.getAttribute("full");

NumberFormat nf = NumberFormat.getInstance(locale);
nf.setMaximumFractionDigits(2);
nf.setMinimumFractionDigits(2);
    
boolean allInfos = true;
int nbCols=0;
int nbLines=0;

%>
<html>
<head>
        <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<link href="css/carscode.css" rel="stylesheet" type="text/css" />
<link href="css/synthese.css" rel="stylesheet" type="text/css" />
<link href="css/critere.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />

<link type="text/css" rel="stylesheet" href="css/columnlist.css" />
<title></title>
</head>
<BODY scroll="auto">

<%  nf.setMaximumFractionDigits(0);
    nf.setMinimumFractionDigits(0);
%>
<P>
<H2>
<bean:message key="caqs.projet" /> : <%=currentElement.getProject().getLib()%><BR />
<bean:message key="caqs.baseline" /> : <%=currentElement.getBaseline().getLib()%>
</H2>

<center>
<H2><%= criterionDefinition.getLib(locale) %> : <%= nf.format(critereList.size()) %>&nbsp;<bean:message key="caqs.critere.elements" />&nbsp;</H2>
</center>
</P>
<%  nf.setMaximumFractionDigits(0);
    nf.setMinimumFractionDigits(0);
%>

    <TABLE cellpadding='0' cellspacing='0' width='100%' border='1'>
        <thead>
            <th>
                <bean:message key="caqs.critere.nom" />
            </th>

            <%
                Collection metriquesDef = criterionDefinition.getMetriquesDefinitions();
                if (metriquesDef != null && (criterionDefinition.getIdTElt().equals(lastTypeElt) || lastTypeElt.equals("ALL"))) {
                    Iterator metIter = metriquesDef.iterator();
                    while (metIter.hasNext()) {
                        MetriqueDefinitionBean def = (MetriqueDefinitionBean)metIter.next();
                        ++nbCols;
                         %>
            <th>
                <%= def.getLib(locale) %>
            </th>
                <% }
                } %>
            <th>
                <%= criterionDefinition.getLib(locale) %>
            </th>
            <th>
                <bean:message key="caqs.critere.tendance.popup" />
            </th>
        </thead>

<%
 if (critereList != null && critereList.size() > 0) {
%>
        <tbody class="webfx-columnlist-bodyVI">
        
        <%
        int index = 0;
        int taille = critereList.size();
        ListIterator critIter = critereList.listIterator();
        while ( (critIter.hasNext()) && (index<taille) ) {
            index++;
            ElementCriterionForm crit = (ElementCriterionForm)critIter.next();
            ElementForm eltBean = crit.getElement();
            String elementDisplayed = eltBean.getLib();
            if (full != null) {
                elementDisplayed = eltBean.getDesc();
                }                
            ++nbLines;
            String classTR = "odd";
            if((index%2)==0) classTR="even";
            %>
            <TR class='<%= classTR%>'>
                <TD class='left'>
                    <%= elementDisplayed %>
                </TD>
            <%
                metriquesDef = criterionDefinition.getMetriquesDefinitions();
                if (metriquesDef != null && (criterionDefinition.getIdTElt().equals(lastTypeElt) || lastTypeElt.equals("ALL"))) {
					String[] metrics = crit.getMetrics();
                    for (int metIdx = 0; metIdx < metrics.length; metIdx++) { %>
                        <TD class='inside'>
                             <%
                                String valbrute = metrics[metIdx];
                                if (valbrute != null) { %>
                                    <%= valbrute %>
                             <% } else { %>
                                    &nbsp;-&nbsp;
                             <% } %>
                        &nbsp;</TD>
                 <% }
                } %>

                    <TD class='inside'>
            <%
                JustificationForm just = crit.getJustificatif();
                if (just != null) {
                    if ((just.getStatus()).equals("DEMAND")) { %>
                            <IMG src='images/encours.gif' title='<bean:message key="caqs.critere.demandecours" />' />
                                <%= crit.getNote() %>
                    &nbsp;
                 <% } else {
                        if ((just.getStatus()).equals("REJET")) { %>
                                <IMG src='images/delete.gif' title='<bean:message key="caqs.critere.rejet" />' />
                                <span class='rejetJustif'><%= crit.getNote() %></span>
                        &nbsp;
                     <% } else { %>
                                <IMG src='images/tick.gif' title='<bean:message key="caqs.critere.accepte" />' />
                                <span class='validJustif'><%=just.getNote()%></span>
                        &nbsp;
                     <% }
                    } %>
             <% } else { %>
                    <%=crit.getNote()%>
                &nbsp;
             <% } %>
            </TD>
            <TD class='right'><center>
                &nbsp;<IMG src='images/note_<%=crit.getTendance()%>.gif' /></center>
            </TD>

        </TR>
     <% } %>
 </TABLE>

<% } else { %>
            <BR/>
            <% if (all != null && all.equals("true")) { %>
            <H2><bean:message key="caqs.critere.noelement" /></H2>
            <% } else { %>
            <H2><bean:message key="caqs.critere.noproblem" /></H2>
            <% } %>
<% } %>

<center>
<H2><bean:message key="caqs.vi.dictio" /></H2>
</center>

<%
    metriquesDef = criterionDefinition.getMetriquesDefinitions();
    if (metriquesDef != null && (criterionDefinition.getIdTElt().equals(lastTypeElt) || lastTypeElt.equals("ALL"))) {
        Iterator metIter = metriquesDef.iterator();
        %>
        <UL>
        <%
        while (metIter.hasNext()) {
            MetriqueDefinitionBean def = (MetriqueDefinitionBean)metIter.next(); %>
            <LI>
                <B><%= def.getLib(locale) %></B> : <%= def.getDesc(locale)%>
            </LI>
     <% }
        %>
        </UL>
        <%
    } %>

<center>
<H2><bean:message key="caqs.vi.defcrit" arg0='<%=criterionDefinition.getLib(locale)%>' /></H2>
</center>
<%= criterionDefinition.getDesc(locale)%>
</body>
</html>
