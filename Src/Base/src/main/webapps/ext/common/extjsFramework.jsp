<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="com.compuware.caqs.domain.dataschemas.ElementType" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/adapter/jquery/jquery.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/adapter/jquery/ext-jquery-adapter.js">
</script>

<!-- LIBS -->
<script type="text/javascript" src="<%= request.getContextPath() %>/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/ext/ext-all-debug.js"></script>
<script type="text/javascript" charset="UTF-8" src="<%= request.getContextPath() %>/ext/locale/ext-lang-<%=request.getLocale().getLanguage()%>.js"></script>

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ext/resources/css/ext-all.css" ></link>
<link id="extJSTheme" rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ext/resources/css/x<%= com.compuware.caqs.presentation.util.RequestUtil.getUsedThemeForConnectedUser(request)%>.css">
</link>
