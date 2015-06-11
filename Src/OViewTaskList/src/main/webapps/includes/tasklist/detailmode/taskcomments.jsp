      <tr valign="top">
         <td colspan="3">
            <table width="100%" border="0">
               <tr>
                  <td class="sectionHeading"><bean:message key="taskdetails.comments"/></td>
               </tr>
               <tr>
                  <td>
                     <p>
<% if (task.getComments() == null || task.getComments().length() == 0) {
%>
                        <bean:message key="taskdetails.none"/>
<% } else {
%>
                        <%=task.getComments()%>
<% }
%>
                     </p>
                  </td>
               </tr>
            </table>
         </td>
      </tr>
