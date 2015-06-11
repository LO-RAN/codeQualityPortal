            <span class="taskName">
<%    if (task.isExecutable()) {
%>
               <a href="javascript:startStopTask('start','<%=task.getURI()%>')">
                  <%=task.getName()%>
               </a>
<%    } else {
%>
               <%=task.getName()%>
<%    }
%>
            </span>
