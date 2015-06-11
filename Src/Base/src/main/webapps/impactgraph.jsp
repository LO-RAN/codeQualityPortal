<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/html-ext.tld" prefix="ext" %>
<%@ taglib uri="/WEB-INF/auth.tld" prefix="auth" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page errorPage="errorPage.jsp" %>
<html>
<head>
<auth:checkElement/>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<title><bean:message key="caqs.label.title" /></title></head>
<link href="css/carscode.css" rel="stylesheet" type="text/css" />
<link href="css/just_lab.css" rel="stylesheet" type="text/css" />

<jsp:useBean id="user" scope="session" class="com.compuware.caqs.security.auth.Users" />

<%
    if (session == null || user == null || user.getId() == null || user.getId().equals("")) { %>
		<jsp:forward page="sessiontimedout.jsp"/>
<%  }
    else {
%>
<BODY>
<BR>
<ext:applet code="com.compuware.caqs.presentation.applets.graphapplet.GraphApplet" 
    	type="application/x-java-applet"
	    codebase="./applets/"
    	archive="graph-applet-%CAQS_VERSION%.jar, org-netbeans-api-visual-2.0.jar, org-openide-util-2.0.jar"
	    width="100%"
    	height="90%"
	    message="This browser does not have a Java Plug-in.">
    <PARAM name="SERVLET" value='../CallRetrieve.do' />
    <PARAM name="ID_ELT" value='<%=request.getParameter("id_elt")%>' />
    <PARAM name="ID_BLINE" value='<%=request.getParameter("id_bline")%>' />
    <PARAM name="CENTER_DESC" value='<%=request.getParameter("desc_elt")%>' />
    <% if (request.getParameter("nbIn") != null) { %>
    <PARAM name="NB_IN" value='<%=request.getParameter("nbIn")%>' />
    <% } %>
    <% if (request.getParameter("nbOut") != null) { %>
    <PARAM name="NB_OUT" value='<%=request.getParameter("nbOut")%>' />
    <% } %>
</ext:applet>
<BR/>
<%
	String urlEnd = "";
	if (request.getParameter("nbIn") != null) {
		urlEnd += "&nbIn=" + request.getParameter("nbIn");
	}
	else {
		urlEnd += "&nbIn=1";
	}
	if (request.getParameter("nbOut") != null) {
		urlEnd += "&nbOut=" + request.getParameter("nbOut");
	}
	else {
		urlEnd += "&nbOut=1";
	}
%>
<a href="http://www.yworks.com" title="yWorks.com" target="_blank"><IMG src="images/yEd.png" border="0"/></a><a href='ExtractYEdGraphML.do?idBline=<%=request.getParameter("id_bline")%>&idElt=<%=request.getParameter("id_elt")%><%= urlEnd %>'>Export as GraphML for yEd editor</a>
</body>
<% } %>
</html>
