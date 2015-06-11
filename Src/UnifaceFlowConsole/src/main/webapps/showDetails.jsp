<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/app.tld"    prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html>

  <head>
    <title>diagram.svg</title>
    <link rel="stylesheet" type="text/css" href="SVGStylesheet.css"></link>
  </head>

  <body>
    
    <logic:present name="activitydef">
      <div>
        <caption>
          Activity properties
        </caption>
        <table border="1" width="100%">
        <thead>
          <tr>
            <th>Property</th><th>Value</th>
          </tr>
        </thead>
        
        <tbody>
          <tr>
            <th align="left" width="10%" class='PropertyName'>
              Name
            </th>
	    <td align="left" width="14%" class='PropertyValue'>
	      <bean:write name="activitydef" property="name" scope="request"/>
	    </td>
          </tr>
      

          <logic:notPresent name="activity" scope="request">
	  <tr>
            <th align="left" width="10%" class='PropertyName'>
              State
            </th>
	    <td align="left" width="14%" class='PropertyValue'>
	      Definition (not instantiated)
	    </td>
	  </tr>
          </logic:notPresent>
          
          <logic:present name="activity" scope="request">
	    <tr>
              <th align="left" width="10%" class='PropertyName'>
                State
              </th>
	      <td align="left" width="14%" class='PropertyValue'>
	        <bean:write name="activity" property="state" scope="request"/>
	      </td>
	    </tr>
	    <logic:equal name="activity" property="entityName" value="Task">
	    <tr>
              <th align="left" width="10%" class='PropertyName'>
                Parameters
              </th>
	      <td align="left" width="14%" class='PropertyValue'>
	  	<bean:write name="activity" property="parameters" scope="request"/>
	      </td>
	    </tr>

	    <tr>
              <th align="left" width="10%" class='PropertyName'>
                Priority
              </th>
	      <td align="left" width="14%" class='PropertyValue'>
  	        <bean:write name="activity" property="priority" scope="request"/>&nbsp
	      </td>
	    </tr>
  	    </logic:equal>
          </logic:present>

	  <logic:equal name="activitydef" property="entityName" value="FlowControlDef">
	    <logic:equal name="activitydef" property="flowControlType" value="Condition" scope="request">
	    <tr>
              <th align="left" width="10%" class='PropertyName'>
                Condition
              </th>
	      <td align="left" width="14%" class='PropertyValue'>
	        <bean:write name="activitydef" property="condition" scope="request"/>
	      </td>
	    </tr>
	    </logic:equal>
	  </logic:equal>
	  
          <logic:present name="processor" scope="request">
	    <tr>
              <th align="left" width="10%" class='PropertyName'>
                Actor
              </th>
	      <td align="left" width="14%" class='PropertyValue'>
	        <bean:write name="processor" property="displayName" scope="request"/>
	      </td>
	    </tr>
          </logic:present>
          
          <logic:present name="recurrencecomponent" scope="request">
            <tr>
              <th align="left" width="10%" class='PropertyName'>
                StartRange
              </th>
              <td align="left" width="14%" class='PropertyValue'>
                <bean:write name="recurrencecomponent" property="startRange" scope="request"/>
              </td>
            </tr>
            <tr>
              <th align="left" width="10%" class='PropertyName'>
                EndRange
              </th>
              <td align="left" width="14%" class='PropertyValue'>
                <bean:write name="recurrencecomponent" property="endRange" scope="request"/>
              </td>
            </tr>
            <tr>
              <th align="left" width="10%" class='PropertyName'>
                ScheduledTime
              </th>
              <td align="left" width="14%" class='PropertyValue'>
                <bean:write name="recurrencecomponent" property="scheduledTime" scope="request"/>
              </td>
            </tr>
          </logic:present>

	</tbody>	
        </table>
    
      </div>
    </logic:present>

  </body>
  
</html:html>
