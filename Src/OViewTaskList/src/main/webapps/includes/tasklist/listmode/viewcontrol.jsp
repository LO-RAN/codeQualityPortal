<% if (filter == null || !filter.equals("userInit")) {
      if (view != null && view.equals("expand")) {
%>
                     <a href="javascript:switchView('collapse','')" onMouseOver="swapImage('collapse', '', 'images/collapse.gif', 1)" onMouseOut="restoreImage()">
                        <img name="collapse" width="20" height="20" border="0" src="images/expand.gif" alt="<bean:message key="tasklist.collapse"/>">
                     </a>
<%    } else {  
%>
                     <a href="javascript:switchView('expand','')" onMouseOver="swapImage('expand', '', 'images/expand.gif', 1)" onMouseOut="restoreImage()">
                        <img name="expand" width="20" height="20" border="0" src="images/collapse.gif" alt="<bean:message key="tasklist.expand"/>">
                     </a>
<%    }
   } else {  
%>
                     &nbsp;
<% }
%>
