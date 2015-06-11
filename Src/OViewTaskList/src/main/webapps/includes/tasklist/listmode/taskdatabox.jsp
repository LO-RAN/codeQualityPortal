   <table class="databox" align="center">
      <tr>
         <td width="25%"><p align="center"><img src="images/todo_dark.gif" width="9" height="9" border="0"> <bean:message key="tasklist.counts.toDo"/></p></td>
         <td width="25%"><p align="center"><img src="images/late9x9.gif" width="9" height="9" border="0"> <bean:message key="tasklist.counts.late"/></p></td>
         <td width="25%"><p align="center"><img src="images/onalert9x9.gif" width="9" height="9" border="0"> <bean:message key="tasklist.counts.onAlert"/></p></td>
         <td width="25%"><p align="center"><img src="images/done9x9.gif" width="9" height="8" border="0"> <bean:message key="tasklist.counts.done"/></p></td>
      </tr>
      <tr valign="middle" align="center">
         <td width="25%">
            <p align="center"><b><%=assignedTaskList.getToDoCount()%></b></p>
         </td>
         <td width="25%">
            <p align="center"><font color="#FF0000"><%=assignedTaskList.getLateCount()%></font></p>
         </td>
         <td width="25%">
            <p align="center"><b><font color="#FF0000"><%=assignedTaskList.getOnAlertCount()%></font></b></p>
         </td>
         <td width="25%">
            <p align="center"><%=assignedTaskList.getDoneCount()%></p>
         </td>
      </tr>
   </table>
