      <tr valign="top">
         <td colspan="3">
            <table width="100%" border="0">
               <tr align="left">
                  <th colspan="2" class="sectionHeading" nowrap><bean:message key="taskdetails.stateOverview"/></th>
               </tr>
               <tr>
                  <td class="thinLine" nowrap colspan="2">
<%@ include file="taskpriority.jsp" %>
                     <p>
<%@ include file="../common/taskstatebox.jsp" %>                    
                     </p>
                  </td>
               </tr>
               <tr>
                  <td></td>
                  <td></td>
               </tr>
<%@ include file="taskdescription.jsp" %>
<%@ include file="taskcreatetime.jsp" %>
<%@ include file="taskstarttime.jsp" %>
<%@ include file="taskendtime.jsp" %>
<%@ include file="taskduetime.jsp" %>
<%@ include file="taskalerttime.jsp" %>
               <tr>
                  <td colspan="2">&nbsp;</td>
               </tr>
<%@ include file="taskparameters.jsp" %>
            </table>
         </td>
      </tr>
