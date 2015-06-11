               <tr>
                  <td nowrap>
                     <p><b><bean:message key="taskdetails.label.alertTime"/></b>
<% if (task.isOnAlert()) {
%>
                        <img src="images/onalert.gif" width="16" height="16" alt="<bean:message key="tasklist.onAlert"/>">
<% }
%>
                     </p>
                  </td>
                  <td nowrap>
<% if (task.isOnAlert()) {
%>
                        <p class="criticalInline"><%=I18N.getDateAsString(task.getAlertTime(), user)%></p>
<% } else {
%>
                        <p><%=I18N.getDateAsString(task.getAlertTime(), user)%></p>
<% }
%>
                  </td>
               </tr>                  
