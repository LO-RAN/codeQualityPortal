<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>

<!-- This JSP iterates through the beans build by the Action class OrganizationListAction.

     The Action class builds up the complete tree of Units, containing Actors, Actors containing
     Roles.
     
     This JSP iterates through the objects.
-->

<logic:iterate id="unit" name="unitlist" property="organizationList" type="com.compuware.optimal.flow.webconsole.OrganizationItemBean">
  <html:link forward="showOrganizationDetails" paramId="UnitID" paramName="unit"
             paramProperty="id" paramScope="page" target="view_frame">
    <bean:write name="unit" property="name"/>
  </html:link>
  <br>
  <logic:iterate id="actor" name="unit" property="orgItemList" type="com.compuware.optimal.flow.webconsole.OrganizationItemBean">
    &nbsp&nbsp<html:link forward="showOrganizationDetails" paramId="ActorID" paramName="actor"
                         paramProperty="id" paramScope="page" target="view_frame">
                <bean:write name="actor" property="name"/>
              </html:link>
    </br>
    <logic:iterate id="role" name="actor" property="orgItemList" type="com.compuware.optimal.flow.webconsole.OrganizationItemBean">
      &nbsp&nbsp&nbsp&nbsp<html:link forward="showOrganizationDetails" paramId="RoleID" paramName="role"
                                     paramProperty="id" paramScope="page" target="view_frame">
                            <bean:write name="role" property="name"/>
                          </html:link>
      <br>
    </logic:iterate>
  </logic:iterate>
</logic:iterate>

</html:html>
