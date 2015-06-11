<% UserInitTaskList userInitTaskList = (UserInitTaskList) request.getAttribute("userInitTaskList");
   AssignedTaskList assignedTaskList = (AssignedTaskList) request.getAttribute("assignedTaskList");
%>
   <table width="100%" border="0">
      <tr valign="baseline">
         <td colspan="4" class="ThickLine">
            <table width="10" border="0" align="right">
               <tr>
                  <td nowrap>
<%@ include file="listmode/viewcontrol.jsp" %>
<%@ include file="listmode/filtercontrol.jsp" %>
<%@ include file="listmode/taskcount.jsp" %>
                  </td>
               </tr>
            </table>
<%@ include file="listmode/refreshlink.jsp" %>
         </td>
      </tr>
<% if ("userInit".equals(filter)) {
%>
<%@ include file="listmode/userdriventasks.jsp" %>
<% } else {
%>
<%@ include file="listmode/assignedtasks.jsp" %>
<% }
%>
   </table>
<%@ include file="listmode/taskdatabox.jsp" %>
<%@ include file="listmode/taskfooter.jsp" %>
   
