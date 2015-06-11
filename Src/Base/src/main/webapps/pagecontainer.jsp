<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<HTML>

<HEAD>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
</HEAD>

<BODY style='background-color:transparent'>
	<IFRAME name='<%=request.getParameter("name")%>' src='<%=request.getContextPath()%>/<%=request.getParameter("frameUrl")%>?id_user=<%=request.getParameter("id_user")%>' width='<%=request.getParameter("width")%>' height='<%=request.getParameter("height")%>' FRAMEBORDER=0 allowTransparency="true" style="background-color:transparent">
	</IFRAME>
</BODY>

</HTML>