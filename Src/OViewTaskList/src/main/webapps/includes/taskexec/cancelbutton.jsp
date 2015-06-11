      <td>
         <div align="left">
            <input type="image" name="cancel" src="images/cancelbutton.gif"
               value="cancelTask"
               onMouseUp="restoreImage()" onMouseOut="restoreImage()"
               onMouseDown="swapImage('cancel','','images/cancelbuttonpressed.gif',1)"
               onClick="submitAction('cancelTask','<%=task.getURI()%>','<%=request.getQueryString()%>'); return false;">
         </div>
      </td>
