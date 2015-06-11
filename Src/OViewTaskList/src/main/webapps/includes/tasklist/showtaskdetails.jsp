   <% TaskDataBean task = (TaskDataBean) request.getAttribute("detailTask");
      int totalCount = 1;
      String priorityDescriptor = null;
   %>
   <table width="100%" border="0">
      <tr>
         <td colspan="3">
<%@ include file="detailmode/returnlink.jsp" %>
<%@ include file="detailmode/refreshlink.jsp" %>
<%@ include file="detailmode/taskname.jsp" %>
         </td>
      </tr>
<%@ include file="detailmode/taskstateoverview.jsp" %>
      <tr>
         <td colspan="3">&nbsp;</td>
      </tr>
<%@ include file="detailmode/taskcomments.jsp" %>
      
   </table>
   




