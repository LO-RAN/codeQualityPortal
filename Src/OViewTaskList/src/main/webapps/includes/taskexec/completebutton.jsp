      <td>
         <div align="right">
            <input type="image" name="complete" src="images/completebutton.gif"
               value="completeTask"
               onMouseUp="restoreImage()" onMouseOut="restoreImage()"
               onMouseDown="swapImage('complete','','images/completebuttonpressed.gif',1)"
               onClick="submitAction('completeTask','<%=task.getURI()%>','<%=request.getQueryString()%>');return false;">
         </div>
      </td>
