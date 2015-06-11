<% if (!task.isUserInitiated()) {
%>
         <table width="1%" class="box" align="right">
            <tr>
               <td><p>
                  <a href="javascript:showDetails('<%=task.getURIQueryString()%>')">
                     [<bean:message key="tasklist.details"/>]</a></p>
               </td>
            </tr>
         </table>
<% }
%>
