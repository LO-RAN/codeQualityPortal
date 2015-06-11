/**
 * 
 */
package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;

/**
 * @author cwfr-fdubois
 * 
 */
public abstract class ElementSelectedActionAbstract extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ActionForward result = null;
		if (isCurrentElementSet(request.getSession())) {
			result = doExecute(mapping, form, request, response);
		} else {
			result = mapping.findForward("sessiontimeout");
		}
		return result;
	}

	protected abstract ActionForward doExecute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException;

	private boolean isCurrentElementSet(HttpSession session) {
		return session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY) != null;
	}

}
