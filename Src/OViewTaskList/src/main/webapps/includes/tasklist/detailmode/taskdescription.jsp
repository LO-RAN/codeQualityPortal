               <tr>
                  <td nowrap>
                     <p><b><bean:message key="taskdetails.label.description"/></b></p>
                  </td>
                  <td nowrap>
<% if (task.getDescription().length() > 0) {                  
%>
                     <p><%=task.getDescription()%></p>
<% } else {
%>
                     <p><bean:message key="taskdetails.none"/></p>
<% }
%>
                  </td>
               </tr>
