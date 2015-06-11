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
    <body>
        <table>
            <tr>
                <th>ID</th>
                <th>CONTENT</th>
            </tr>
            <logic:present name="result">
                <logic:iterate id="resultForm" name="result" type="com.compuware.form.ResultForm">
                    <tr>
                        <td><bean:write name="resultForm" property="id" /></td>
                        <td><bean:write name="resultForm" property="content" /></td>
                    </tr>
                </logic:iterate>
            </logic:present>
            <logic:notPresent name="result">
                <tr><td colspan="2">Aucun r&eacute;sultat</td></tr>
            </logic:notPresent>
        </table>
    </body>
</html>