<%    if ("Scheduled".equals(task.getState())) {
%>
                        <img src="images/scheduled.gif" width="16" height="16" alt="<bean:message key="tasklist.scheduled"/>">
                        <img src="images/next.gif" width="16" height="14">
                        <input type="image" value="reserveTask" name="reserve<%=totalCount%>" alt="<bean:message key="tasklist.reserve"/>"
                           src="images/reserve.gif" width="16" height="16"
                           onMouseOut="restoreImage()"
                           onMouseOver="swapImage('reserve<%=totalCount%>','','images/reservehover.gif',1)"
                           onClick="submitAction('reserveTask','<%=task.getURI()%>','<%=queryString%>')">
                        <img src="images/next.gif" width="16" height="14">
                        <a href="javascript:startStopTask('start','<%=task.getURI()%>')"
                           onMouseOut="restoreImage()"
                           onMouseOver="swapImage('execute<%=totalCount%>','','images/executehover.gif',1)">
                           <img name="execute<%=totalCount%>" border="0"
                              src="images/execute.gif" width="22" height="16" alt="<bean:message key="tasklist.execute"/>"></a>
<%    } else if ("Reserved".equals(task.getState())) {
%>
                        <input type="image" value="releaseTask" name="release<%=totalCount%>" alt="<bean:message key="tasklist.release"/>"
                           src="images/release.gif" width="16" height="16"
                           onMouseOut="restoreImage()"
                           onMouseOver="swapImage('release<%=totalCount%>','','images/releasehover.gif',1)"
                           onClick="submitAction('releaseTask','<%=task.getURI()%>','<%=queryString%>')">
                        <img src="images/prev.gif" width="16" height="14">
                        <img src="images/reserved.gif" width="16" height="16" alt="<bean:message key="tasklist.reserved"/>">
                        <img src="images/next.gif" width="16" height="14">
                        <a href="javascript:startStopTask('start','<%=task.getURI()%>')"
                           onMouseOut="restoreImage()"
                           onMouseOver="swapImage('execute<%=totalCount%>','','images/executehover.gif',1)">
                           <img name="execute<%=totalCount%>" border="0"
                              src="images/execute.gif" width="22" height="16" alt="<bean:message key="tasklist.execute"/>"></a>
<%    } else if ("Executing".equals(task.getState())) {
%>
                        <input type="image" value="cancelTask" name="cancel<%=totalCount%>" alt="<bean:message key="tasklist.cancel"/>"
                           src="images/reserve.gif" width="16" height="16"
                           onMouseOut="restoreImage()"
                           onMouseOver="swapImage('cancel<%=totalCount%>','','images/reservehover.gif',1)"
                           onClick="submitAction('cancelTask','<%=task.getURI()%>','<%=queryString%>')">
                        <img src="images/prev.gif" width="16" height="14">
                        <a href="javascript:startStopTask('start','<%=task.getURI()%>')"
                           onMouseOut="restoreImage()"
                           onMouseOver="swapImage('execute<%=totalCount%>','','images/executehover.gif',1)">
                           <img name="execute<%=totalCount%>" border="0"
                              src="images/executing.gif" width="22" height="16" alt="<bean:message key="tasklist.resumeExec"/>"></a>
                        <img src="images/next.gif">
                        <input type="image" value="abortTask" name="abort<%=totalCount%>" alt="<bean:message key="tasklist.abort"/>"
                           src="images/abort.gif" width="16" height="16"
                           onMouseOut="restoreImage()"
                           onMouseOver="swapImage('abort<%=totalCount%>','','images/aborthover.gif',1)"
                           onClick="submitAction('abortTask','<%=task.getURI()%>','<%=queryString%>')">

<%    } else if ("Aborted".equals(task.getState())) {
%>
                        <img src="images/aborted.gif" width="16" height="16" alt="<bean:message key="tasklist.aborted"/>">
<%    } else if ("Failed".equals(task.getState())) {
%>
                        <input type="image" value="reserveTask" name="reserve<%=totalCount%>" alt="<bean:message key="tasklist.release"/>"
                           src="images/reserve.gif" width="16" height="16"
                           onMouseOut="restoreImage()"
                           onMouseOver="swapImage('reserve<%=totalCount%>','','images/reservehover.gif',1)"
                           onClick="submitAction('reserveTask','<%=task.getURI()%>','<%=queryString%>')">
                        <img src="images/prev.gif" width="16" height="14">
                        <img src="images/failed.gif" width="16" height="16" alt="<bean:message key="tasklist.failed"/>">
<%    } else if (task.isComplete()) {
%>
                        <img src="images/completed.gif" width="16" height="16" alt="<bean:message key="tasklist.completed"/>">
<%    }
%>
