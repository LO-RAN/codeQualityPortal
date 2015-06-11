               <tr align="left">
                  <td colspan="2" class="thinLine">
                     <p><b><bean:message key="taskdetails.label.output"/></b></p>
                  </td>
               </tr>
<% paramIterator = task.outputParamIterator();
   while (paramIterator.hasNext()) {
      param = (TaskParameter) paramIterator.next();
      paramName = param.getName();
      paramValueString = I18N.getParameterValueString(param.getValue(), user);
%>
               <tr valign="top">
                  <td>
                     <p class="taskParameter"><%=paramName%></p>
                  </td>
                  <td>
                     <p><%=paramValueString%></p>
                  </td>
               </tr>
<% }
%>
