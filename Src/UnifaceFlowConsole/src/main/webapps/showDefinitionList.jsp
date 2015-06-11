<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>

<!-- This JSP iterates through the beans build by the Action class ProcessInstanceListAction.

     The Action class builds up the complete tree of Packages, Process Definitions until no
     children exist anymore.
     
     This JSP iterates through the first 4 levels.
-->

<logic:iterate id="packsprocesses" name="ppbeans" property="packageProcessDefs" scope="request" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemBean">
  <bean:write name="packsprocesses" property="name"/>(<bean:write name="packsprocesses" property="entityName"/>)
  <br>
  <logic:present name="packsprocesses" property="ppbeans">
    <bean:define id="ppbeans1" name="packsprocesses" property="ppbeans" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemsBean"/>
    <logic:iterate id="ppb1" name="ppbeans1" property="packageProcessDefs" scope="page" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemBean">
      <logic:equal name="ppb1" property="entityName" scope="page" value="ProcessDef">
        &nbsp&nbsp<html:link forward="showProcessInstances" paramId="processDefID"
                             paramName="ppb1" paramProperty="id" paramScope="page"
                             target="view_frame">
                    <bean:write name="ppb1" property="name"/>(<bean:write name="ppb1" property="entityName"/>)
                  </html:link>
      </logic:equal>
      <logic:notEqual name="ppb1" property="entityName" scope="page" value="ProcessDef">
        &nbsp&nbsp&nbsp&nbsp<bean:write name="ppb1" property="name"/>(<bean:write name="ppb1" property="entityName"/>)
      </logic:notEqual>
      <br>
      <logic:present name="ppb1" property="ppbeans">
        <bean:define id="ppbeans2" name="ppb1" property="ppbeans" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemsBean"/>
        <logic:iterate id="ppb2" name="ppbeans2" property="packageProcessDefs" scope="page" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemBean">
          <logic:equal name="ppb2" property="entityName" scope="page" value="ProcessDef">
           &nbsp&nbsp&nbsp&nbsp<html:link forward="showProcessInstances" paramId="processDefID"
                                          paramName="ppb2" paramProperty="id" paramScope="page"
                                          target="view_frame">
                                 <bean:write name="ppb2" property="name"/>(<bean:write name="ppb2" property="entityName"/>)
                               </html:link>
          </logic:equal>
          <logic:notEqual name="ppb2" property="entityName" scope="page" value="ProcessDef">
            &nbsp&nbsp&nbsp&nbsp<bean:write name="ppb2" property="name"/>(<bean:write name="ppb2" property="entityName"/>)
          </logic:notEqual>
          <br>
          <logic:present name="ppb2" property="ppbeans">
            <bean:define id="ppbeans3" name="ppb2" property="ppbeans" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemsBean"/>
            <logic:iterate id="ppb3" name="ppbeans3" property="packageProcessDefs" scope="page" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemBean">
              &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<bean:write name="ppb3" property="name"/>(
              <bean:write name="ppb3" property="entityName"/>)
              <br>
            </logic:iterate>
          </logic:present>
        </logic:iterate>
      </logic:present>
    </logic:iterate>
  </logic:present>
</logic:iterate>

</html:html>
