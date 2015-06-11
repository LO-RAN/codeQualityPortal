package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.service.UserRightsSvc;
import net.sf.json.JSONObject;

public class UserRightsSaveAction extends ExtJSAjaxAction {

	public JSONObject retrieveDatas(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
        JSONObject retour = new JSONObject();
		String roleId = request.getParameter("roleId");
        String rightId = request.getParameter("rightId");
        String newValue = request.getParameter("newValue");

        boolean b = Boolean.parseBoolean(newValue);

        UserRightsSvc.getInstance().saveRightsChange(rightId, roleId, b);
		
		return retour;
	}
}
