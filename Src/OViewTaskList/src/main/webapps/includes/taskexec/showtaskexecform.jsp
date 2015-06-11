<form name="taskExecForm" method="post" action="taskexec.do">
<input type="hidden" name="userID" value="<%=user.getUserId()%>">
<input type="hidden" name="formAction" value="">
<table width="100%" border="0">
   <tr>
<% if (exceptionList == null) {   
%>
      <td colspan="5" class="thickLine">
<% } else {
%>
      <td colspan="5">
<% }
%>
<%@ include file="detailslink.jsp" %>
<%@ include file="taskname.jsp" %>
      </td>
   </tr>
<%@ include file="validationexception.jsp" %>
<%@ include file="taskcomments.jsp" %>
<%@ include file="taskparameters.jsp" %>
   <tr>
      <td colspan="5">&nbsp;</td>
   </tr>
   <tr valign="top">
      <td>
         <input type="hidden" name="URI" value="<%=task.getURI()%>">
      </td>
<%@ include file="cancelbutton.jsp" %>
      <td>&nbsp;</td>
<%@ include file="completebutton.jsp" %>
      <td>&nbsp;</td>
   </tr>
</table>
</form>
