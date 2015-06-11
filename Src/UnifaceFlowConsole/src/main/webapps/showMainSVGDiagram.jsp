<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html>
  <head>
    <title>diagram.svg</title>
    <link rel="stylesheet" type="text/css" href="SVGStylesheet.css"></link>
    <META HTTP-EQUIV="Refresh" CONTENT="60" />
  </head>
  <body>
    <logic:present name="activity">
      <div>
        <table border="1" width="100%">
        <tr>
          <th align="center" width="20%" class='PropertyName'>
            <bean:message key="activityList.name"/>
          </th>
          <th align="center" width="10%" class='PropertyName'>
            <bean:message key="activityList.extendedtype"/>
          </th>
          <th align="center" width="10%" class='PropertyName'>
            <bean:message key="activityList.extendedstate"/>
          </th>
          <th align="center" width="30%" class='PropertyName'>
            <bean:message key="activityList.starttime"/>
          </th>
          <th align="center" width="30%" class='PropertyName'>
            <bean:message key="activityList.endtime"/>
          </th>
        </tr>
        <!-- This part displays the activity details that are related to the displayed
             svgDiagram. The activity was selected on the showProcessSelectList.jsp JSP.
             The activity object was build by the Action class ActivityInstanceListAction.java.
         -->
        <tr>
          <td align="center" width="20%" class='PropertyValue'>
            <bean:write name="activity" property="name" scope="session"/>
          </td>
          <td align="center" width="10%" class='PropertyValue'>
            <bean:write name="activity" property="extendedType" scope="session"/>
          </td>
          <td align="center" width="10%" class='PropertyValue'>
            <bean:write name="activity" property="extendedState" scope="session"/>
          </td>
          <td align="center" width="30%" class='PropertyValue'>
            <bean:write name="activity" property="startTime" scope="session"/>
          </td>
          <td align="center" width="30%" class='PropertyValue'>
            <bean:write name="activity" property="endTime" scope="session"/>&nbsp
          </td>
        </tr>
        </table>

      </div>
    </logic:present>

    <logic:present name="processdef">
      <div>
        <table border="1" width="100%">
        <tr>
          <th align="center" width="15%">
            <bean:message key="processDefList.name"/>
          </th>
          <th align="center" width="40%">
            <bean:message key="processDefList.description"/>
          </th>
        </tr>


        <!-- This part displays the process definition details that are related to the displayed
             svgDiagram. The process definition was selected on the SVGDiagram.
             The process definition object was build by the Action class ActivityInstanceListAction.java.
         -->

        <tr>
          <td align="center" width="15%">
            <bean:write name="processdef" property="name" scope="session"/>
          </td>
          <td align="center" width="40%">
            <bean:write name="processdef" property="description" scope="session"/>&nbsp
          </td>
        </tr>
        </table>

      </div>
    </logic:present>

    <br>

    <div>
      <!-- Display of the breadcrumb trail.
       -->
      <logic:present name="breadcrumb">
        <logic:iterate id="breadcrumbitem" name="breadcrumb" property="breadCrumb"
                       type="com.compuware.optimal.flow.webconsole.BreadCrumbItemBean">

          <html:link forward="showBreadCrumbInstances" paramId="processID" paramName="breadcrumbitem"
                             paramProperty="id" paramScope="page">
            <bean:write name="breadcrumbitem" property="name"/>
          </html:link>
          -->
        </logic:iterate>
      </logic:present>

      <logic:present name="activity">
        <bean:write name="activity" property="name"/>
      </logic:present>
      <logic:present name="processdef">
        <bean:write name="processdef" property="name"/>
      </logic:present>

    </div>

    <br>

    <script language="JavaScript" src="svgJavaScript.js"></script>

    <embed name="defineSVG"
           type="image/svg+xml"
           width="800" height="800"
           src="contentMainSVGDiagram.jsp?ielikes=dummy.svg">
    </embed>

  </body>

</html:html>
