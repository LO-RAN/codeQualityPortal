package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.service.ArchitectureSvc;
import com.compuware.caqs.util.RequestHelper;

public class LinkSelectAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

	    String id_elt = RequestHelper.getParameterAndForward(request, "id_elt");
	    String id_bline = RequestHelper.getParameterAndForward(request, "id_bline");
	    String state = request.getParameter("state");
	    
	    ArchitectureSvc architectureSvc = ArchitectureSvc.getInstance();
	    Collection links = architectureSvc.retrieveLinks(id_elt, id_bline, Integer.parseInt(state));
	    request.setAttribute("links", links);
	    
	    RequestHelper.forwardParameter(request, "desc_elt");
	    RequestHelper.forwardParameter(request, "id_crit");
		
		ActionForward forward = null;
		if ("20".equals(state)) {
			forward = mapping.findForward("successCopy");			
		}
		else {
			forward = mapping.findForward("successArchi");			
		}

		return forward;

	}

	
}
