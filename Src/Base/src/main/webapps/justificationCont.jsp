<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page errorPage="errorPage.jsp" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<STYLE>
    BODY {background-color:transparent}
    A {text-decoration: none;color: blue}
</STYLE>
</head>
<body>
<IFRAME NAME="justification" SRC="<%=request.getContextPath()%>/JustificationListRetrieve.do" WIDTH="100%" HEIGHT="500" FRAMEBORDER=0 allowTransparency="true" style="background-color:transparent">
</IFRAME>
</body>
</html>