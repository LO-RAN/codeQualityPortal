<% paramIterator = task.outputParamIterator();
   while (paramIterator.hasNext()) {
      param = (TaskParameter) paramIterator.next();
      paramId = param.getId();
      paramName = param.getName();
      paramValue = param.getValue();
      paramValueString = I18N.getParameterValueString(paramValue, user);
      paramType = param.getType();
%>
   <tr valign="top">
      <td>
         <p><b><%=paramName%>:</b></p>
      </td>
      <td colspan="4">
<%    switch (paramType) {
         case TaskParameter.BOOLEAN:
%>
         <p><input type="checkbox" class="editControl" name="<%=paramId%>" value="true" <%=( (request.getParameter(paramId) != null && "true".equals(request.getParameter(paramId))) || (paramValue != null && ((Boolean) paramValue).booleanValue())) ? "CHECKED" : ""%>></p>
<%          break;
         default:
%>         
         <p>
		<% if (paramId.equalsIgnoreCase("projectid")) { %>
		<select class="editControl" name="<%=paramId%>" >
			<logic:iterate id="project" name="<%=RetrieveProjectAction.PROJECT_LIST_KEY%>" scope="request" type="com.compuware.carscode.dbms.ProjectBean">
              <logic:equal name="project" property="id" value="<%=(String)paramValue%>">
			    <option value="<bean:write name="project" property="id" />" selected><bean:write name="project" property="lib" /></option>
              </logic:equal>
              <logic:notEqual name="project" property="id" value="<%=(String)paramValue%>">
			    <option value="<bean:write name="project" property="id" />"><bean:write name="project" property="lib" /></option>
              </logic:notEqual>
			</logic:iterate>
		</select>
		<% } else { %>
		<input type="text" size="40" class="editControl" name="<%=paramId%>" value="<%=(request.getParameter(paramId) == null) ? paramValueString : request.getParameter(paramId)%>">
		<% } %>
		 </p>
<%          break;
         }
%>         
      </td>
   </tr>
<% }
%>
