<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>

<%@ page import="com.compuware.caqs.domain.dataschemas.rights.RoleBean,
	             java.util.*" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ page import="com.compuware.caqs.domain.dataschemas.rights.RightBean" %>

<script language="javascript">


var userRightsGridReader = new Ext.data.JsonReader({
                    root:           'elements',
                    totalProperty:  'totalCount',
                    id: 'id',
                    fields: [
                    'id', 'rightName'
<%
		List<RoleBean> roleList = (List<RoleBean>)request.getAttribute("rolesList");
       if(roleList!=null) {
       		for(Iterator<RoleBean> it = roleList.iterator(); it.hasNext(); ) {
       			RoleBean rb = it.next();
%>
		,{
			name: 		'<%= rb.getId()%>',
			type: 		'bool',
			editor: 	Ext.form.Checkbox
		}
<%
       		}
       }
%>

                    ]
            });

var userRightsGridCheckColumns = new Array();

<%
	for(Iterator<RoleBean> it = roleList.iterator(); it.hasNext(); ) {
		RoleBean rb = it.next();
%>
userRightsGridCheckColumns[userRightsGridCheckColumns.length] = new Ext.grid.UserRightsCheckColumn({
 id:			'<%= rb.getId()%>',
 header: 		"<%= rb.getLib()%>",
 width: 		90,
 sortable: 		true,
 dataIndex: 	'<%= rb.getId()%>'
})
<%
	}
%>
</script>
