<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="com.compuware.caqs.util.CaqsConfigUtil" %>
<%@ page import="com.compuware.caqs.constants.Constants" %>
<%@ page import="com.compuware.toolbox.io.PropertiesReader" %>
<%@ page import="java.util.Properties" %>


<HTML>

<HEAD>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
	<meta http-equiv="pragma" content="no-cache">
	<TITLE><bean:message key="caqs.timedout.title" /></TITLE>
</HEAD>
<%
        String dynamicPropFileName = CaqsConfigUtil.getLocalizedCaqsFile(Constants.CAQS_DYNAMIC_CONFIG_FILE_PATH);
        Properties dynProp = PropertiesReader.getProperties(dynamicPropFileName, this, false);
        String unifaceviewLogoutUrl = dynProp.getProperty(Constants.UNIFACEVIEW_LOGOUT_URL);
%>
<script type="text/javascript">
if (window.top != window.self) {
  // breakout to top window
  window.top.location = '<%=unifaceviewLogoutUrl%>'
}
</script>
<BODY style='background-color:transparent'>
	<H2>
		<bean:message key="caqs.timedout.body" />
	</H2>
</BODY>

</HTML>