<%@ taglib uri="/WEB-INF/auth.tld" prefix="auth" %>

<script language="javascript">
var caqsUserRights = new Array();

caqsUserRights["ADMIN_DRAGDROP"] = false;
<auth:access function="ADMIN_DRAGDROP">
caqsUserRights["ADMIN_DRAGDROP"] = true;
</auth:access>

caqsUserRights["ADMIN_DOMAIN_MODIFICATION"] = false;
<auth:access function="ADMIN_DOMAIN_MODIFICATION">
caqsUserRights["ADMIN_DOMAIN_MODIFICATION"] = true;
</auth:access>

caqsUserRights["ALL_PROJECT_ADMIN"] = false;
<auth:access function="ALL_PROJECT_ADMIN">
caqsUserRights["ALL_PROJECT_ADMIN"] = true;
</auth:access>

caqsUserRights["ADMIN_DRAGDROP"] = false;
<auth:access function="ADMIN_DRAGDROP">
caqsUserRights["ADMIN_DRAGDROP"] = true;
</auth:access>

caqsUserRights["MANUAL_ANALYSIS"] = false;
<auth:access function="MANUAL_ANALYSIS">
caqsUserRights["MANUAL_ANALYSIS"] = true;
</auth:access>

caqsUserRights["IMPORT_EXPORT"] = false;
<auth:access function="IMPORT_EXPORT">
caqsUserRights["IMPORT_EXPORT"] = true;
</auth:access>

caqsUserRights["Criterion_Detail"] = false;
<auth:access function="Criterion_Detail">
caqsUserRights["Criterion_Detail"] = true;
</auth:access>

caqsUserRights["Justification"] = false;
<auth:access function="Justification">
caqsUserRights["Justification"] = true;
</auth:access>

caqsUserRights["TOP_DOWN_FACTOR_COMMENT"] = false;
<auth:access function="TOP_DOWN_FACTOR_COMMENT">
caqsUserRights["TOP_DOWN_FACTOR_COMMENT"] = true;
</auth:access>

caqsUserRights["Calculation"] = false;
<auth:access function="Calculation">
caqsUserRights["Calculation"] = true;
</auth:access>

caqsUserRights["Model_Editor"] = false;
<auth:access function="Model_Editor">
caqsUserRights["Model_Editor"] = true;
</auth:access>

caqsUserRights["ADMINISTRATION_ACCESS"] = false;
<auth:access function="ADMINISTRATION_ACCESS">
caqsUserRights["ADMINISTRATION_ACCESS"] = true;
</auth:access>

caqsUserRights["USER_ADMIN_ACCESS"] = false;
<auth:access function="USER_ADMIN_ACCESS">
caqsUserRights["USER_ADMIN_ACCESS"] = true;
</auth:access>

caqsUserRights["ARCHITECTURE_ACCESS"] = false;
<auth:access function="ARCHITECTURE_ACCESS">
caqsUserRights["ARCHITECTURE_ACCESS"] = true;
</auth:access>

caqsUserRights["DASHBOARD_CONNECTION_TIMELINE"] = false;
<auth:access function="DASHBOARD_CONNECTION_TIMELINE">
caqsUserRights["DASHBOARD_CONNECTION_TIMELINE"] = true;
</auth:access>

caqsUserRights["DELETE_FROM_TRASH"] = false;
<auth:access function="DELETE_FROM_TRASH">
caqsUserRights["DELETE_FROM_TRASH"] = true;
</auth:access>

caqsUserRights["TRANSLATION"] = false;
<auth:access function="TRANSLATION">
caqsUserRights["TRANSLATION"] = true;
</auth:access>

caqsUserRights["LABEL"] = false;
<auth:access function="LABEL">
caqsUserRights["LABEL"] = true;
</auth:access>

caqsUserRights["Justification_Creation"] = false;
<auth:access function="Justification_Creation">
caqsUserRights["Justification_Creation"] = true;
</auth:access>

caqsUserRights["Report"] = false;
<auth:access function="Report">
caqsUserRights["Report"] = true;
</auth:access>

caqsUserRights["Data"] = false;
<auth:access function="Data">
caqsUserRights["Data"] = true;
</auth:access>

caqsUserRights["Upload"] = false;
<auth:access function="Upload">
caqsUserRights["Upload"] = true;
</auth:access>
    
canAccessFunction = function(id) {
    var retour = caqsUserRights[id];
    if(retour==null) {
        retour = false;
    }
    return retour;
}


var caqsUserRoles = new Array();

caqsUserRoles["ADMINISTRATOR"] = false;
<auth:isUserInRoles roles="ADMINISTRATOR">
caqsUserRoles["ADMINISTRATOR"] = true;
</auth:isUserInRoles>


isUserInRole = function(role) {
    var retour = caqsUserRoles[role];
    if(retour==null) {
        retour = false;
    }
    return retour;
}
</script>