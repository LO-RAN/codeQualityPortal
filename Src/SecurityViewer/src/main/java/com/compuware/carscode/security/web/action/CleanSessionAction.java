/**
 * 
 */
package com.compuware.carscode.security.web.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.security.auth.SessionManager;
import com.compuware.caqs.security.auth.UserData;


/**
 * @author cwfr-fdubois
 *
 */
public class CleanSessionAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// Extract attributes we will need
		String cleanAll = request.getParameter("cleanAllSessions.x");
		if (cleanAll != null) {
			cleanAll();
		}
		else {
			String deleteSession = request.getParameter("deleteSession.x");
			String userCookie = request.getParameter("userCookie");
			if (deleteSession != null && userCookie != null) {
				deleteSession(userCookie);
			}
		}
		
		return mapping.findForward("success");

	}
	
	private void deleteSession(String userCookie) {
		SessionManager sessionManager = SessionManager.getInstance();
		sessionManager.removeUser(userCookie);
	}
	
	private void cleanAll() {
		SessionManager sessionManager = SessionManager.getInstance();
		List<UserData> userList = sessionManager.getUserList();

		if (userList != null) {
			Iterator<UserData> i = userList.iterator();
			UserData currentUser = null;
			while (i.hasNext()) {
				currentUser = i.next();
				sessionManager.removeUser(currentUser.getCookie());
			}
		}
	}
	
}
