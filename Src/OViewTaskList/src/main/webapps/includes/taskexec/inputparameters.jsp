<% Iterator paramIterator = task.inputParamIterator();
   TaskParameter param = null;
   String paramId = null;
   String paramName = null;
   Object paramValue = null;
   String paramValueString = null;
   int paramType = 0;
   while (paramIterator.hasNext()) {
      param = (TaskParameter) paramIterator.next();
      if (param.isOutput()) {
         continue;
      }
      paramName = param.getName();
      paramValueString = I18N.getParameterValueString(param.getValue(), user);
	  paramValue = param.getValue();
	  paramId = param.getId();
%>
   <tr valign="top">
      <td>
         <p><b><%=paramName%>:</b></p>
      </td>
      <td colspan="4">
      	<p>
	<% if (paramName.equalsIgnoreCase("projectid")) { %>
			<logic:iterate id="project" name="<%=RetrieveProjectAction.PROJECT_LIST_KEY%>" scope="request" type="com.compuware.carscode.dbms.ProjectBean">
              <logic:equal name="project" property="id" value="<%=(String)paramValue%>">
			    <bean:write name="project" property="lib" />
              </logic:equal>
			</logic:iterate>
	<% } else { %>
	  <%=paramValueString%>
	<% } %>
	</p>
      </td>
   </tr>
<% }
%>
