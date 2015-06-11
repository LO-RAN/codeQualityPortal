               <tr align="left" >
                  <td class="thinLine" colspan="2">
                     <p><b><bean:message key="taskdetails.label.input"/></b></p>
                  </td>
               </tr>
<% Iterator paramIterator = task.inputParamIterator();
   TaskParameter param = null;
   String paramName = null;
   String paramValueString = null;
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
