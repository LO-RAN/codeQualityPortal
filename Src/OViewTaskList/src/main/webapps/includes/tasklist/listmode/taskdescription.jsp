<%    descriptionString = StringParser.extractContentFromHTML(task.getDescription()).trim();
      if (descriptionString.length() == 0) {
%>
               <bean:message key="tasklist.noDescription"/><br>
<%    } else {
%>
               <bean:message key="tasklist.description" arg0="<%=descriptionString%>"/><br>
<%    }
%>