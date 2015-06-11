<%@ page errorPage="/errorPage.jsp" %>
<jsp:useBean id="user" scope="session" class="com.compuware.caqs.security.auth.Users" />
<% 
if (session == null || user == null || user.getId() == null || user.getId().equals("")) { 
%>
<jsp:forward page="sessiontimedout.jsp"/>
<%
 } 
%>
