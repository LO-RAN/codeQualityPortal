/**
 * 
 */
package com.compuware.carscode.security.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.security.auth.SessionManager;
import com.compuware.caqs.security.auth.UserData;

/**
 * @author cwfr-fdubois
 *
 */
public class RetrieveSecurityInfoAction extends Action {
	
	public static final String TOTAL_USER_ATTR_KEY = "totalUser";
	public static final String USER_LIST_ATTR_KEY = "userList";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// Extract attributes we will need

		// ActionErrors needed for error passing
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();

		ActionForward forward = null;

		SessionManager sessionManager = SessionManager.getInstance();
		List<UserData> userList = sessionManager.getUserList();
		
		request.setAttribute(TOTAL_USER_ATTR_KEY, userList.size());
		request.setAttribute(USER_LIST_ATTR_KEY, userList);
		
		return mapping.findForward("success");

	}
}
