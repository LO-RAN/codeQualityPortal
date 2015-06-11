<% if (exceptionList != null) {
%>
   <tr>
      <td colspan="5">
         <div class="exceptionBox" style="padding-top:0px">
            <h3 style="color:black"><bean:message key="exception.validation.title"/></h3>
            <bean:message key="exception.validation.header"/>
            <ul>
<%    Iterator it = exceptionList.iterator();
      ValidationException vex = null;
      while (it.hasNext()) {
         vex = (ValidationException) it.next();
%>
               <li><bean:message key="<%=vex.getTypeKey()%>" arg0="<%=vex.getFieldName()%>"/>
<%    }
%>
            </ul>
         </div>
      </td>
   </tr>
<% } 
%>
