package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.service.UserSvc;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;

public class UserSaveAction extends ExtJSAjaxAction {

   @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        UserBean ub = this.userBeanFromRequest(request);
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            retour = UserSvc.getInstance().saveUserBean(ub);
        } else if ("delete".equals(action)) {
            retour = UserSvc.getInstance().deleteUserBean(ub.getId());
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

    private UserBean userBeanFromRequest(HttpServletRequest request) {
        UserBean ub = new UserBean();
        ub.setId(request.getParameter("id"));
        ub.setLastName(request.getParameter("lastname"));
        ub.setFirstName(request.getParameter("firstname"));
        ub.setEmail(request.getParameter("email"));
        ub.setPassword(request.getParameter("password"));

        String[] rolesList = request.getParameter("roles").split(",");

        List<RoleBean> roles=new ArrayList<RoleBean>();

        for (int i=0;i<rolesList.length;i++){
            RoleBean rb= new RoleBean();
            rb.setId(rolesList[i]);
            roles.add(rb);
        }

        ub.setRoles(roles);

        return ub;
    }

}
