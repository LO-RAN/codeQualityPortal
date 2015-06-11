package com.compuware.caqs.presentation.consult.actions;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.JustificatifResume;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.JustificationSvc;
import com.compuware.caqs.util.RequestHelper;

public final class JustificationListRetrieveAction extends Action {


    // --------------------------------------------------------- Public Methods


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

    	ActionForward forward = mapping.findForward("success");
    	
        Users user = RequestUtil.getConnectedUser(request);

        String req = RequestHelper.getParameterAndForward(request, "req");
        if (req == null || req.length() == 0) {
            req = "DEMAND";
        }
    	
        try {
	    	JustificationSvc justifiSvc = JustificationSvc.getInstance();
	        Collection<JustificatifResume> justifColl = justifiSvc.getAllJustifications(req, user.getId());	        
	        request.setAttribute("allJust", justifColl);
        }
        catch (CaqsException e) {
        	forward = mapping.findForward("failure");
        }
        
        return forward;

    }

}
