<% String pendingSelected = "";
   String overdueSelected = "";
   String dueTodaySelected = "";
   String highPrioritySelected = "";
   String doneTodaySelected = "";
   String userInitSelected = "";
   
   if ("userInit".equals(filter)) {
      userInitSelected = "selected";
   } else if ("overdue".equals(filter)) {
      overdueSelected = "selected";
   } else if ("dueToday".equals(filter)) {
      dueTodaySelected = "selected";
   } else if ("highPriority".equals(filter)) {
      highPrioritySelected = "selected";
   } else if ("doneToday".equals(filter)) {
      doneTodaySelected = "selected";
   } else {
      pendingSelected = "selected";
   }
%>
                     <select name="filter" class="editControl" onChange="document.forms[0].action='tasklist.do' + location.search; document.forms[0].submit()">
                        <option value="pending" <%=pendingSelected%>><bean:message key="tasklist.pending"/>
                        <option value="overdue" <%=overdueSelected%>><bean:message key="tasklist.overdue"/>
                        <option value="dueToday" <%=dueTodaySelected%>><bean:message key="tasklist.dueToday"/>
                        <option value="highPriority" <%=highPrioritySelected%>><bean:message key="tasklist.highPriority"/>
                        <option value="doneToday" <%=doneTodaySelected%>><bean:message key="tasklist.doneToday"/>
                     </select>
