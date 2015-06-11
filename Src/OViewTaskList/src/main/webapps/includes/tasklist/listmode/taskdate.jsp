               <span class="<%=(task.isLate(now)) ? "critical" : "uncritical"%>">
<%    if (!task.isComplete()) {
%>               
                  <bean:message key="tasklist.dueTime" arg0="<%=I18N.getDateAsString(task.getDueTime(), user)%>"/>
<%    } else {
%>               
                  <bean:message key="tasklist.endTime" arg0="<%=I18N.getDateAsString(task.getEndTime(), user)%>"/>
<%    }
%>               
               </span>
