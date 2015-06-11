<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>

<html:html>

<link href="css/border.css" rel="stylesheet" type="text/css" />

<!-- This JSP iterates through the beans build by the Action class ProcessInstanceListAction.

     The Action class builds up the complete tree of Packages, Process Definitions until no
     children exist anymore.
     
     This JSP iterates through the first 4 levels.
-->

<% Locale locale = request.getLocale(); %>

<TABLE cellpadding='10' cellspacing='0'>
<logic:iterate id="packsprocesses" name="ppbeans" property="packageProcessDefs" scope="request" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemBean">
    <TR bgcolor='yellow'>
        <TH class='bordertopright'>
            <bean:write name="packsprocesses" property="name"/>
        </TH>
        <TH class='bordertop'>Prochaine Analyse</TH>
    </TR>
  <logic:present name="packsprocesses" property="ppbeans">
    <bean:define id="ppbeans1" name="packsprocesses" property="ppbeans" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemsBean"/>
    <logic:iterate id="ppb1" name="ppbeans1" property="packageProcessDefs" scope="page" type="com.compuware.optimal.flow.webconsole.PackageProcessDefItemBean">
        <TR>
      <logic:equal name="ppb1" property="entityName" scope="page" value="ProcessDef">
            <TD class='bordertopright'>
              &nbsp&nbsp<A href="showProcessInstancesOfProcessDef.do?action=ShowProcesses&processDefID=<bean:write name="ppb1" property="id"/>&packageId=<bean:write name="packsprocesses" property="id"/>"
                             target="view_frame">
                    <bean:write name="ppb1" property="name"/>
                  </A>
            </TD>
            <TD class='bordertop'>&nbsp;
                <logic:present name="ppb1" property="scheduledTime">
                    <%= java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT, locale).format(ppb1.getScheduledTime()) %>
                </logic:present>
            </TD>
      </logic:equal>
      <logic:notEqual name="ppb1" property="entityName" scope="page" value="ProcessDef">
            <TD>
        &nbsp&nbsp&nbsp&nbsp<bean:write name="ppb1" property="name"/>
            </TD>
            <TD></TD>
            <TD></TD>
      </logic:notEqual>
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
      </TR>
</logic:iterate>
</TABLE>
</html:html>
