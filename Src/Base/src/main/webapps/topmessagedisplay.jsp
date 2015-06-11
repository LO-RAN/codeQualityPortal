<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<HTML>

<HEAD>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
	<meta http-equiv="pragma" content="no-cache">
	<link href="css/carscode.css" rel="stylesheet" type="text/css" />
</HEAD>
<script type="text/javascript">
if (window.top != window.self) {
  // breakout to top window
  window.top.location = 'topmessagedisplay.jsp?target=<%=request.getParameter("target")%>'
}
</script>
<BODY style='background-color:transparent'>
	<div class="centeredMessage">
	<H2>
		<bean:message key='<%= "caqs." + request.getParameter("target") + ".body" %>' />
	</H2>
	</div>
</BODY>

</HTML>