package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.UserSvc;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.openide.util.Exceptions;

/**
 *
 * @author cwfr-lizac
 */
public class RetrieveUserInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String id = request.getParameter("id");
        boolean success = false;
        if (id != null && !"".equals(id)) {
            UserBean ub = UserSvc.getInstance().retrieveUserById(id);
            if (ub != null) {
                success = true;
                retour.put("id", ub.getId());
                retour.put("lastname", ub.getLastName());
                retour.put("firstname", ub.getFirstName());
                retour.put("email", ub.getEmail());
                retour.put("password", ub.getPassword());
                retour.put("roles", ub.getRoles());

                Users connectedUser = RequestUtil.getConnectedUser(request);
                retour.put("isConnected", connectedUser.getId().equals(ub.getId()));
            }
        } else {
            retour.put("id", "");
            retour.put("lastname", "");
            retour.put("firstname", "");
            retour.put("email", "");
            retour.put("password", "");
            retour.put("roles", "");
            retour.put("isConnected", false);
            success = true;
        }
        try {
            this.addUsersRolesInfos(id, retour);
        } catch (CaqsException ex) {
            mLog.error("Problem retrieving roles for id " + id);
        }

        if (!success) {
            mLog.error("no user retrieved for id " + id);
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }

    private void addUsersRolesInfos(String userId, JSONObject obj) throws CaqsException {
        // get all available roles
        List<RoleBean> allRoles = UserSvc.getInstance().retrieveAllRoles();

        // get roles already assigned to user
        List<RoleBean> userRoles = UserSvc.getInstance().retrieveUserRoles(userId);

        // strip avaliable roles list, removing roles already assigned to user
        allRoles.removeAll(userRoles);

        // build json structures for user assigned roles
        JSONArray roleCollection = new JSONArray();
        for (RoleBean rb : userRoles) {
            JSONObject role = new JSONObject();
            role.put("id", rb.getId());
            role.put("lib", rb.getLib());
            roleCollection.add(role);
        }
        JSONObject userRolesCollection = new JSONObject();
        userRolesCollection.put("rows", roleCollection);
        userRolesCollection.put("totalCount", roleCollection.size());
        obj.put("userRolesCollection", userRolesCollection);


        // build json structures for remaining available roles
        JSONArray jsonAllRolesCollection = new JSONArray();
        for (RoleBean rb : allRoles) {
            JSONObject role = new JSONObject();
            role.put("id", rb.getId());
            role.put("lib", rb.getLib());
            jsonAllRolesCollection.add(role);
        }
        JSONObject availableRolesCollection = new JSONObject();
        availableRolesCollection.put("rows", jsonAllRolesCollection);
        availableRolesCollection.put("totalCount", jsonAllRolesCollection.size());
        obj.put("availableRolesCollection", availableRolesCollection);
    }

}
