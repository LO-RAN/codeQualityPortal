               <tr>
                  <td nowrap>
                     <p><b><bean:message key="taskdetails.label.dueTime"/></b>
<% if (task.isLate()) {
%>
                        <img src="images/late.gif" width="16" height="16" alt="<bean:message key="tasklist.late"/>">
<% }
%>
                     </p>
                  </td>
                  <td nowrap>
<% if (task.isLate()) {
%>
                        <p class="criticalInline"><%=I18N.getDateAsString(task.getDueTime(), user)%></p>
<% } else {
%>
                        <p><%=I18N.getDateAsString(task.getDueTime(), user)%></p>
<% }
%>
                  </td>
               </tr>
