<% UserInitTaskDataBean task = null;
   int totalCount = 0;
   
   Iterator it = userInitTaskList.iterator();
   while (it.hasNext()) {
      task = (UserInitTaskDataBean) it.next();
      totalCount++;
%>
      <tr>
         <td colspan="4" class="thinLine" valign="top">
            <span class="taskName">
               <a href="javascript:startStopTask('start','<%=task.getURI()%>')">
                  <%=task.getName()%>
               </a>
            </span>               
         </td>
      </tr>
<%
   }
%>
