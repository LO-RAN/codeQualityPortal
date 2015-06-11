<% paramIterator = task.inputParamIterator();
   paramName = null;
   paramValueString = null;
   paramBuf = new StringBuffer();
   firstParam = true;
   while (paramIterator.hasNext()) {
      param = (TaskParameter) paramIterator.next();
      paramName = param.getName();
      paramValueString = I18N.getParameterValueString(param.getValue(), user);
      if (firstParam) {
         firstParam = false;
      } else {
         paramBuf.append(" - ");
      }
      paramBuf.append(StringParser.extractContentFromHTML(paramValueString));
   }
   if (paramBuf.toString().length() > 160) {
      paramString = paramBuf.toString().substring(0, 160) + "...";
   } else {
      paramString = paramBuf.toString();
   }
   if (paramString.equals("")) {
%>
               <bean:message key="tasklist.noParameters"/><br>
<%    } else {
%>
               <bean:message key="tasklist.parameters" arg0="<%=paramString%>"/><br>
<%    }
%>