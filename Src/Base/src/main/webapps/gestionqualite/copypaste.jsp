<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/auth.tld" prefix="auth" %>
<%@ page errorPage="/errorPage.jsp" %>
<%@page import="com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteElement;"%>
<html>
<auth:checkElement/>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<link href="<%= request.getContextPath()%>/css/carscode.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath()%>/css/synthese.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath()%>/css/main.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%= request.getContextPath()%>/js/boxOver.js"></script>

</head>

<body style="margin:5px 5px;">
	<h2><bean:message key="caqs.copypaste.title" /></h2>
	<logic:iterate id="currentCopyPaste" name="copypastelist" type="com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteBean">
	<TABLE>
		<thead>
			<tr>
				<th><bean:message key="caqs.copypaste.element" /></th>
				<th><bean:message key="caqs.copypaste.line" /></th>
			</tr>
		</thead>
		<tbody>
		<nested:iterate name="currentCopyPaste" property="elements">
			<tr>		
				<td>
					<nested:write property="descElt" filter="true"/>
				</td>
				<td>
					<a href='CopyPasteRetrieveSource.do?id_elt=<nested:write property="idElt" />&line=<nested:write property="line" filter="true"/>&id=<bean:write name="currentCopyPaste" property="id" />#line<nested:write property="line" filter="true"/>' target="sourceCodeDisplay">
						<nested:write property="line" filter="true"/>
					</a>
				</td>
			</tr>
		</nested:iterate>
		</tbody>
	</TABLE>
	</logic:iterate>
	<br/>
	<iframe id="sourceCodeDisplay" name="sourceCodeDisplay" src='blank.html' width="90%" height="400"></iframe>
</body>

</html>
