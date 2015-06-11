package com.compuware.caqs.presentation.consult.actions.actionplan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.business.locking.LockManager;
import com.compuware.caqs.business.locking.Locks;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;

public class RemoveActionPlanLock extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
		Users user = RequestUtil.getConnectedUser(request);
		if(eltBean!=null) {
			boolean hasAccess = LockManager.getInstance().hasAccess(Locks.ACTION_PLAN_MODIFICATION, user.getId(), eltBean.getId(), user.getCookie());
			if(hasAccess) {
				LockManager.getInstance().removeLock(Locks.ACTION_PLAN_MODIFICATION, user.getId(), eltBean.getId(), user.getCookie());
			}
		}
		return null;
	}
}
