<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page contentType="text/html"%>
<%@ page import="com.compuware.caqs.security.auth.UserData" %>
<%@ page import="java.util.List" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta HTTP-EQUIV="Refresh" CONTENT="30;URL=RetrieveSecurityInfo.do" />
	<link href="css/carscode.css" rel="stylesheet" type="text/css" />
	<SCRIPT>
		function setUserCookie(target) {
			document.forms[0].userCookie.value=target;
		}
	</SCRIPT>
</head>
<body>
	<form action="CleanSession.do">
		<html:hidden property="userCookie" value="" />
		<H2>
			<bean:message key="caqs.security.display.totalUser" />:&nbsp;<bean:write name="<%= com.compuware.carscode.security.web.action.RetrieveSecurityInfoAction.TOTAL_USER_ATTR_KEY %>" filter="true"/>
			<a href="RetrieveSecurityInfo.do" title="<bean:message key='caqs.action.refresh' />">
				<img src="images/refresh.gif" border="0" />
			</a>
			<html:image src="images/delete.gif" altKey="caqs.action.cleansessions" property="cleanAllSessions">
			</html:image>
		</H2>
		<BR/>
		<TABLE border='0' >
			<THEAD>
			<TR>
				<TH><bean:message key="caqs.security.display.userList.name" /></TH>
				<TH><bean:message key="caqs.security.display.userList.loginDateTime" /></TH>
				<TH><bean:message key="caqs.security.display.userList.lastAccessDate" /></TH>
				<TH></TH>
			</TR>
			</THEAD>
			<TBODY>
			<logic:iterate id="userList" name="<%= com.compuware.carscode.security.web.action.RetrieveSecurityInfoAction.USER_LIST_ATTR_KEY %>" type="com.compuware.caqs.security.auth.UserData">
				<TR>
					<TD>
						<bean:write name="userList" property="id" filter="true"/>
					</TD>
					<TD>
						<bean:write name="userList" property="loginDateTime" formatKey="format.datetime.short" filter="true"/>
					</TD>
					<TD>
						<bean:write name="userList" property="lastAccessDate" formatKey="format.datetime.short" filter="true"/>
					</TD>
					<TD>
						<html:image src="images/delete.gif" altKey="caqs.action.delete" property="deleteSession" onclick='<%= "setUserCookie(\'" + userList.getCookie() + "\');" %>'>
						</html:image>
					</TD>
				</TR>
			</logic:iterate>
			</TBODY>
		</TABLE>
	</form>
</body>
</html>
