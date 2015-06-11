<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page 	errorPage="/errorPage.jsp" %>
<%@ taglib 	uri="/WEB-INF/resources.tld" prefix="check" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<HTML>
<HEAD>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<link href="css/carscode.css" rel="stylesheet" type="text/css" />
<link href="css/synthese.css" rel="stylesheet" type="text/css" />
<TITLE>
<bean:message key="caqs.intlupload.title" />
</TITLE>
</HEAD>
<BODY style='background-color:transparent'>
<check:checkResources/>
<CENTER>
<%
    String result = request.getParameter("result");
if (result != null){%>
	<bean:message key="caqs.intlupload.telechargement" />
<% } %>
<H2><bean:message key="caqs.intlupload.title" /></H2>
<form action="InternationalizationUpdate.do" name="intlUploadForm" type="com.compuware.caqs.presentation.admin.forms.i18n.InternationalizationUploadForm" scope="request" enctype="MULTIPART/FORM-DATA" method=POST>
<Table>
	<TR>
		<TD>
			<bean:message key="caqs.intlupload.label" /> :
		</TD>
		<TD>
			<html:file name="intlUploadForm" property="file"/>
		</TD>
    </TR>
    <TR>
		<TD colspan='2' align='center'>
			<html:submit property="submit">
				<bean:message key='caqs.telecharger' />
			</html:submit>
			<html:submit property="refresh">
				<bean:message key='caqs.intlupload.refresh' />
			</html:submit>
		</TD>
	</TR>
</TAble>
<HR/>
<TABLE>
	<TR>
		<TD><bean:message key="caqs.intlupload.getmetrics" /></TD>
		<TD>
			<A href="InternationalizationExtract.do?datatype=metriques">
				<IMG src='images/csv.gif' border=0 width=32 title='<bean:message key="caqs.intlupload.getmetrics" />'/>
			</A>
		</TD>
	</TR>
	<TR>
		<TD><bean:message key="caqs.intlupload.getcriterions" /></TD>
		<TD>
			<A href="InternationalizationExtract.do?datatype=criteres">
				<IMG src='images/csv.gif' border=0 width=32 title='<bean:message key="caqs.intlupload.getcriterions" />'/>
			</A>
		</TD>
	</TR>
	<TR>
		<TD><bean:message key="caqs.intlupload.getfactors" /></TD>
		<TD>
			<A href="InternationalizationExtract.do?datatype=facteurs">
				<IMG src='images/csv.gif' border=0 width=32 title='<bean:message key="caqs.intlupload.getfactors" />'/>
			</A>
		</TD>
	</TR>
</TABLE>
</form>
</CENTER>
</BODY>
</HTML>

