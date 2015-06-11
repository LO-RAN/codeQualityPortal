<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page errorPage="errorPage.jsp" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">

<%@ page language="java"%>
<%@ page import="java.net.*" %>
<%

String idEa = (String) request.getParameter("idEA");
String idBl = (String) request.getParameter("idBL");
String idMethod = (String) request.getParameter("idMethod");

String protocol = "http://"+request.getServerName()+":"+request.getServerPort();
String contextPath = request.getContextPath();

String pathTxtFile=protocol+contextPath+"/Data/"+idEa+"/"+idBl+"/m"+idMethod+"_source.txt";
String pathPdfFile=protocol+contextPath+"/Data/"+idEa+"/"+idBl+"/m"+idMethod+"_flow.pdf";



int responseCodeTxt = 404;
int responseCodePdf = 404;
URL urlTextFile = new URL(pathTxtFile);
URL urlPdfFile = new URL(pathPdfFile);
%>
<title><bean:message key="caqs.graphlisting.message01" />:<%=idMethod%></title>
<style>
   BODY {background-color: transparent; font-size: 10pt; font-family: verdana,helvetica}
   TABLE {font-size: 10pt; font-family: verdana,helvetica}
</style>
</head>

<%	
try {	
	HttpURLConnection httpConnectionTxt = (HttpURLConnection) urlTextFile.openConnection();
	responseCodeTxt = httpConnectionTxt.getResponseCode();
	HttpURLConnection httpConnectionPdf = (HttpURLConnection) urlPdfFile.openConnection();
	responseCodePdf = httpConnectionPdf.getResponseCode();
}
catch (Exception e){
}
%>
<frameset cols="*,*" frameborder="NO" border="0" framespacing="0">
<%if(responseCodePdf == 404){%>
  		<frame src="nogen.jsp" name="mainFrame" scrolling="AUTO">
<%}else {%>
  		<frame src="<%=urlPdfFile%>" name="mainFrame" scrolling="AUTO">	
<%}%>

<%if (responseCodeTxt == 404) {%>
		<frame src="nogen.jsp" name="topFrame"  scrolling="AUTO">
<%}
else{%>
  		<frame src="<%=urlTextFile%>" name="topFrame" scrolling="AUTO">
<%}%>

</frameset>
</HTML>