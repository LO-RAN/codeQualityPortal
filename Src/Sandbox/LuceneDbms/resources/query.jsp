<%--
  Created by IntelliJ IDEA.
  User: cwfr-fdubois
  Date: 21 nov. 2005
  Time: 17:32:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<html>
  <head><title>Search query</title></head>
  <body>
      <html:form action="/Search.do" name="searchForm" type="com.compuware.form.SearchForm" scope="request" method="post">
        <html:text name="searchForm" property="filter" size="40" />
        <html:submit property="action">Search</html:submit>
      </html:form>
  </body>
</html>