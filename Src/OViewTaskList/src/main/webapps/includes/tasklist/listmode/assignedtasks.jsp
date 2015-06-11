<% TaskDataBean task = null;
   Iterator paramIterator = null;
   boolean firstParam = true;
   TaskParameter param = null;
   String paramName = null;
   String paramValueString = null;
   StringBuffer paramBuf = null;
   String paramString = null;
   String descriptionString = null;
   int totalCount = 0;
   String priorityDescriptor = null;
   
   Iterator it = assignedTaskList.iterator(filter);
   while (it.hasNext()) {
      task = (TaskDataBean) it.next();
      totalCount++;
      if ("expand".equals(view)) {
%>
<%@ include file="expandedview.jsp" %>

<%    } else {
%>
<%@ include file="collapsedview.jsp" %>
<%    }
   }
%>
