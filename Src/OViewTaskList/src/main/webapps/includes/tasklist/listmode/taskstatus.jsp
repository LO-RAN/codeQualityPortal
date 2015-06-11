<%    if (task.isOnAlert(now)) {
%>
            <img src="images/onalert.gif" width="16" height="16" alt="<bean:message key="tasklist.onAlert"/>">
<%    } else if (task.isLate(now)) { 
%>
            <img src="images/late.gif" width="16" height="16" alt="<bean:message key="tasklist.late"/>">
<%    } else { 
%>         
            &nbsp;
<%    }
%>
