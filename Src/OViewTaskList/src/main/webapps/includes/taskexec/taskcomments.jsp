   <tr><td class="thinLine" colspan="5">
      <p>
<% String comments = task.getComments();
   if (comments == null || comments.length() == 0) {
      comments = task.getDescription();
      if (comments == null || comments.length() == 0) {
         comments = task.getName();      
      }
   }
%>
      <%=comments%>   <br>&nbsp;
      </p>
   </td></tr>
