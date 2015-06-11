<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<div class="error">
<%
	if(session!=null) {
		java.util.ArrayList al = (java.util.ArrayList) session.getAttribute("errors");
		if(al!=null && al.size()>0 ) {
	%>
		<bean:message key="caqs.error.libelle" /><br />
	<%
			for(java.util.Iterator it = al.iterator(); it.hasNext(); ) {
	%>
				<bean:message key='<%= (String)it.next() %>' /><br />
	<%
			}
	%>
	<%
			session.removeAttribute("errors");
		}
	}
%>

</div>