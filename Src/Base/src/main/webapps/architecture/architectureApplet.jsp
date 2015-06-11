<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/html-ext.tld" prefix="ext" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<HTML>
<HEAD>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
	<STYLE>
        BODY{	background-color: transparent;
        	font-size: 10pt;
        	font-family: verdana,helvetica;}

        A {	text-decoration: none;
        	color: blue}
        A:active {	text-decoration: none;
        	color: white}
    </STYLE>
</HEAD>
<BODY>
<%
java.util.Locale locale = RequestUtil.getLocale(request);
String idElement = request.getParameter("id_elt");
String idBaseline = request.getParameter("id_bline");
%>

<%if( (idElement!=null) && (idBaseline!=null) ){%>
<ext:applet code="com.compuware.caqs.presentation.applets.architecture.AppletArchitecture" 
    	type="application/x-java-applet"
	    codebase="../applets/"
    	archive="architecture-applet-%CAQS_VERSION%.jar"
	    width="100%"
    	height="100%"
	    message="This browser does not have a Java Plug-in.">
	<PARAM name="idElement" value="<%=idElement%>" />
	<PARAM name="idBaseline" value="<%=idBaseline%>" />	
	<PARAM name="strArchitectureLink" value='<bean:message key="caqs.architecture.archi" />' />
	<PARAM name="strRealLink" value='<bean:message key="caqs.architecture.reel" />' />
	<PARAM name="language" value="<%= locale.getLanguage() %>" />	
</ext:applet>
<%}%>
</BODY>
</HTML>
