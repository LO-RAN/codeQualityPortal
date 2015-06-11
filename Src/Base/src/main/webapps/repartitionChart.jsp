<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/html-ext.tld" prefix="ext" %>
<%@ page errorPage="/errorPage.jsp" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<title></title>
</head>
<%
	String type = (String)request.getAttribute("type");
%>
<script type="text/javascript">
function reload() {
	var dw = document.width ? document.width : document.documentElement.offsetWidth - 25;
	var dh = document.height ? document.height : document.documentElement.offsetHeight - 25;
	document.location='RepartitionSelect.do?width='+dw+'&height='+dh+'&type=<%=type%>';
}

window.onresize = reload; 
</script>
<body>
<%= (String)request.getAttribute(type + "-piechartImageMap") %>
<%
String fileName = (String)request.getAttribute(type + "-piechartFileName");
%>
<img src='./displaychart?filename=<%= fileName %>' width="100%" height="100%" border="0" usemap="#<%= fileName %>" />
</body>
</html>
