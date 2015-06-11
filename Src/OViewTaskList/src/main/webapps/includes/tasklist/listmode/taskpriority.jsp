<% switch (task.getPriorityDescriptor()) {
      case TaskDataBean.HIGH_PRIORITY:
         priorityDescriptor = I18N.getString("tasklist.highPri", user);
         break;
      case TaskDataBean.MEDIUM_PRIORITY:
         priorityDescriptor = I18N.getString("tasklist.mediumPri", user);
         break;
      default:
         priorityDescriptor = I18N.getString("tasklist.lowPri", user);
   }
%>
   
               <img src="images/priority_<%=task.getPriority()%>.gif" width="51" height="11" alt="<bean:message key="tasklist.priority" arg0="<%=task.getPriority()%>" arg1="<%=priorityDescriptor%>"/>">
