<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page errorPage="errorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.compuware.caqs.domain.dataschemas.*" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil,
				com.compuware.caqs.util.CaqsConfigUtil,
				com.compuware.caqs.constants.Constants,
				com.compuware.toolbox.io.PropertiesReader" %>
<%@ taglib uri="/WEB-INF/resources.tld" prefix="check" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/html-ext.tld" prefix="ext" %>
<%@ taglib uri="/WEB-INF/auth.tld" prefix="auth" %>
<HTML>
<auth:checkElement/>
<jsp:useBean id="currentElement" scope="session" class="com.compuware.caqs.domain.dataschemas.ElementBean" />
<HEAD>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<link href="css/carscode.css" rel="stylesheet" type="text/css" />
<link href="css/synthese.css" rel="stylesheet" type="text/css" />
    <title><bean:message key="caqs.scatterplot.title" /></title>
<script language="javascript">
window.moveTo(0, 0);
window.resizeTo(screen.width, screen.height);
</script>
</HEAD>
<BODY>
<%
    Locale locale = RequestUtil.getLocale(request);
    ResourceBundle resources =  ResourceBundle.getBundle("com.compuware.caqs.Resources.resources", locale);

    Properties prop = (Properties)session.getAttribute("webResources");
    DialecteBean dialecte = currentElement.getDialecte();
    String idLang = (dialecte != null ? dialecte.getLangage().getId() : "obj");
    String methodLib = resources.getString("caqs."+prop.getProperty(idLang+".method"));

    
    String dynamicPropFileName = CaqsConfigUtil.getLocalizedCaqsFile(Constants.CAQS_DYNAMIC_CONFIG_FILE_PATH);
    Properties dynProp = PropertiesReader.getProperties(dynamicPropFileName, this, false);
    
    String defaultElementType = dynProp.getProperty(Constants.DEFAULT_ELEMENT_TYPE+idLang);
    String metH = dynProp.getProperty(Constants.SCATTERPLOT_X+idLang);
    String metV = dynProp.getProperty(Constants.SCATTERPLOT_Y+idLang);
    String centerMetH = dynProp.getProperty(Constants.SCATTERPLOT_CENTER_X+idLang);
    String centerMetV = dynProp.getProperty(Constants.SCATTERPLOT_CENTER_Y+idLang);

    if (currentElement.getTypeElt().equals("EA")) {
%>
   	<ext:applet code="com.compuware.caqs.presentation.applets.scatterplot.ScatterPlot" 
    	type="application/x-java-applet"
	    codebase="./applets/"
    	archive="scatterplot-applet-%CAQS_VERSION%.jar, jcommon-%JCOMMON_VERSION%.jar, applet-factory-%CAQS_VERSION%.jar, jfreechart-%JFREECHART_VERSION%.jar"
	    width="100%"
    	height="100%"
	    message="This browser does not have a Java Plug-in.">
    <PARAM name="centerH" value="<%= centerMetH%>" />
    <PARAM name="centerV" value="<%= centerMetV%>" />
    <PARAM name="metH" value='<%= metH%>' />
    <PARAM name="metV" value='<%= metV%>' />
    <PARAM name="RAZ" value='<bean:message key="caqs.scatterplot.RAZ" />' />
    <PARAM name="MF" value='<bean:message key="caqs.scatterplot.mf" arg0='<%= methodLib%>' />' />
    <PARAM name="ELTS" value='<bean:message key="caqs.scatterplot.elts" />' />
    <PARAM name="STATUS" value='<bean:message key="caqs.scatterplot.status" />' />
    <PARAM name="defaultEltType" value='<%= defaultElementType%>' />
    <PARAM name="serverAdress" value='<%= request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf("/"))%>/' />
    <PARAM name="LOCALE" value='<%= RequestUtil.getLocale(request)%>' />
</ext:applet>
<% } %>
</BODY>
</HTML>
