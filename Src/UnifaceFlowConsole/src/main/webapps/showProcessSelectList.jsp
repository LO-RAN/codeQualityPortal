<%@ page import="java.util.HashMap, com.compuware.carscode.dbms.ProjectBean"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>
<br>
 
<!-- This JSP iterates through the beans build by the Action class ActivityInstanceListAction.

     The Action class builds up the complete tree of Packages, 'Top' Process Definitions, Processes 
     until no children exist anymore.
     
     This JSP iterates through the first 4 levels.
-->
<%
    HashMap projectMap = (HashMap)session.getAttribute("projectMap");
%>

<logic:iterate id="packsprocesses" name="ppbeans" property="packageProcesses" scope="request" type="com.compuware.optimal.flow.webconsole.PackageProcessInsItemBean">
  <bean:write name="packsprocesses" property="name"/>
  <br>
  <logic:present name="packsprocesses" property="ppbeans">
    <bean:define id="ppbeans1" name="packsprocesses" property="ppbeans" type="com.compuware.optimal.flow.webconsole.PackageProcessInsItemsBean"/>
    <logic:iterate id="ppb1" name="ppbeans1" property="packageProcesses" scope="page" type="com.compuware.optimal.flow.webconsole.PackageProcessInsItemBean">
      &nbsp&nbsp&nbsp<bean:write name="ppb1" property="name"/>
      <br>
      <logic:present name="ppb1" property="ppbeans">
        <bean:define id="ppbeans2" name="ppb1" property="ppbeans" type="com.compuware.optimal.flow.webconsole.PackageProcessInsItemsBean"/>
        <logic:iterate id="ppb2" name="ppbeans2" property="packageProcesses" scope="page" type="com.compuware.optimal.flow.webconsole.PackageProcessInsItemBean">
          <logic:equal name="ppb2" property="entityName" scope="page" value="Process">
           &nbsp&nbsp&nbsp
           &nbsp&nbsp&nbsp<html:link forward="showActivityInstances" paramId="processID"
                                     paramName="ppb2" paramProperty="id" paramScope="page"
                                     target="view_frame">
                            <bean:write name="ppb2" property="creationTime"/>,&nbsp;
                            <logic:notEqual name="ppb2" property="var1" value="">
                                 <%
                                     ProjectBean prjBean = (ProjectBean)projectMap.get(ppb2.getVar1());
                                 %>
                                   <%= prjBean.getLib() %>
                            </logic:notEqual>
                          </html:link>
          </logic:equal>
          <logic:notEqual name="ppb2" property="entityName" scope="page" value="Process">
            &nbsp&nbsp&nbsp
            &nbsp&nbsp&nbsp<bean:write name="ppb2" property="name"/>(<bean:write name="ppb2" property="entityName"/>)
          </logic:notEqual>
          <br>
          <logic:present name="ppb2" property="ppbeans">
            <bean:define id="ppbeans3" name="ppb2" property="ppbeans" type="com.compuware.optimal.flow.webconsole.PackageProcessInsItemsBean"/>
            <logic:iterate id="ppb3" name="ppbeans3" property="packageProcesses" scope="page" type="com.compuware.optimal.flow.webconsole.PackageProcessInsItemBean">
              <logic:equal name="ppb3" property="entityName" scope="page" value="Process">
                &nbsp&nbsp&nbsp  
                &nbsp&nbsp&nbsp
                &nbsp&nbsp&nbsp<html:link forward="showActivityInstances" paramId="processID"
                                          paramName="ppb3" paramProperty="id" paramScope="page"
                                          target="view_frame">
                                 <bean:write name="ppb3" property="creationTime"/>,&nbsp;
                                 <logic:notEqual name="ppb3" property="var1" value="">
                                 <%
                                     ProjectBean prjBean = (ProjectBean)projectMap.get(ppb3.getVar1());
                                 %>
                                   <%= prjBean.getLib() %>
                                 </logic:notEqual>
                               </html:link>
              </logic:equal>
              <logic:notEqual name="ppb3" property="entityName" scope="page" value="Process">
                &nbsp&nbsp&nbsp
                &nbsp&nbsp&nbsp
                &nbsp&nbsp&nbsp<bean:write name="ppb3" property="name"/>(<bean:write name="ppb3" property="entityName"/>)
              </logic:notEqual>
              <br>
            </logic:iterate>
          </logic:present>
        </logic:iterate>
      </logic:present>
    </logic:iterate>
  </logic:present>
</logic:iterate>
<br>

</html:html>
