package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.presentation.util.StringFormatUtil;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.consult.forms.LabelForm;
import com.compuware.caqs.service.LabelSvc;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.util.logging.LoggerManager;

//TODO simplifier cette méthode car elle ne sert plus pour la partie synthèse (faite dorénavant en Ajax)

public final class LabelUpdateAction extends ElementSelectedActionAbstract {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    // --------------------------------------------------------- Public Methods


    public ActionForward doExecute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException, ServletException {

        // ActionErrors needed for error passing
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        ActionForward forward = null;
        LabelForm formBean = (LabelForm) form;
        
        String action = request.getParameter("action");

        if (!"cancel".equals(action)) {
	        forward = doUpdate(mapping, request, session, errors, response, formBean);
        }
        
        if (forward == null) {
	        String req = RequestHelper.getParameterAndForward(request, "req");
	        if (req != null) {
	        	forward = mapping.findForward("successLabel");
	        }
	        else {
	        	forward = mapping.findForward("successSynthesis");
	        }
		}
        
        return forward;

    }

    private ActionForward doUpdate(ActionMapping mapping,
                                   HttpServletRequest request,
                                   HttpSession session,
                                   ActionErrors errors,
                                   HttpServletResponse response,
                                   LabelForm formBean) {
    	ActionForward forward = null;
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        LabelSvc labelSvc = LabelSvc.getInstance();
        try {
        	labelSvc.store(formToBean(formBean, eltBean, getStatusFromRequest(request, formBean)), eltBean);
        }
        catch (CaqsException e) {
        	logger.error("Error during label store", e);
        	request.setAttribute("id_elt", eltBean.getId());
        	request.setAttribute("id_label", formBean.getId());
        	request.setAttribute("id_bline", eltBean.getBaseline().getId());
        	forward = mapping.findForward("failure");
        }
        return forward;
    }

    private LabelBean formToBean(LabelForm labelForm, ElementBean eltBean, String status) {
    	LabelBean result = null;
    	if (labelForm != null) {
    		result = new LabelBean();
    		result.setId(labelForm.getId());
    		result.setLib(labelForm.getLib());
            String labelFormDesc = labelForm.getDesc();
            if(labelFormDesc != null) {
                labelFormDesc = StringFormatUtil.replaceCarriageReturnByHTML(labelFormDesc);
            }
    		result.setDesc(labelFormDesc);
    		result.setStatut(status);
    		result.setUser(labelForm.getUser());
    		if (labelForm.getDemand() != null) {
    			LabelBean link = new LabelBean();
    			link.setId(labelForm.getDemand().getId());
    			result.setLabelLink(link);
    		}
    		result.setBaseline(eltBean.getBaseline());
    	}
    	return result;
    }
    
    private String getStatusFromRequest(HttpServletRequest request, LabelForm labelForm) {
    	String result = "DEMAND";
    	String action = request.getParameter("action");
    	if ("valid".equals(action)) {
    		result = "VALID";
    	}
    	else if ("validReserve".equals(action)) {
    		result = "VALID_RES";
    	}
    	else if ("rejet".equals(action)) {
    		result = "REJET";
    	} else if("changeLib".equals(action)) {
    		result = labelForm.getStatus();
    	}
    	return result;
    }
    
}
