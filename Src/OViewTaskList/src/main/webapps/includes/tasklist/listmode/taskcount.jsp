<%
long numTasks = ("userInit".equals(filter)) ? userInitTaskList.size() : assignedTaskList.size(filter);
%>
                     <p id="hitText" align="right"><bean:message key="tasklist.hitText" arg0="<%=String.valueOf(numTasks)%>"/></p>
