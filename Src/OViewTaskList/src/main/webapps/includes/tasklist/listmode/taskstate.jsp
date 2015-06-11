<% if (!"Executing".equals(task.getState())) {
%>
            <img src="images/dot.gif" width="2" height="3">
<% }
   if ("Scheduled".equals(task.getState())) {
%>
            <img src="images/scheduled.gif" width="16" height="16" alt="<bean:message key="tasklist.scheduled"/>">
<% } else if ("Reserved".equals(task.getState())) {
%>
            <img src="images/reserved.gif" width="16" height="16" alt="<bean:message key="tasklist.reserved"/>">
<% } else if ("Executing".equals(task.getState())) {
%>
            <img src="images/executing.gif" width="22" height="16" alt="<bean:message key="tasklist.executing"/>">
<% } else if ("Aborted".equals(task.getState())) {
%>
            <img src="images/aborted.gif" width="16" height="16" alt="<bean:message key="tasklist.aborted"/>">
<% } else if ("Failed".equals(task.getState())) {
%>
            <img src="images/failed.gif" width="16" height="16" alt="<bean:message key="tasklist.failed"/>">
<% } else if (task.isComplete()) {
%>
            <img src="images/completed.gif" width="16" height="16" alt="<bean:message key="tasklist.completed"/>">
<% }
%>
