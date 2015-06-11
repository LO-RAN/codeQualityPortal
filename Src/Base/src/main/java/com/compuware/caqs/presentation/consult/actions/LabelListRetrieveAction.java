package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.LabelResume;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.LabelSvc;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class LabelListRetrieveAction extends Action {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    // --------------------------------------------------------- Public Methods

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException, ServletException {

        // ActionErrors needed for error passing
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        ActionForward forward = null;

        forward = doRetrieve(mapping, request, session, errors, response);

        if (forward == null) {
			forward = mapping.findForward("success");
		}

        return forward;
    }

    private ActionForward doRetrieve(ActionMapping mapping,
                                     HttpServletRequest request,
                                     HttpSession session,
                                     ActionErrors errors,
                                     HttpServletResponse response) {
    	ActionForward forward = null;
    	LabelSvc labelSvc = LabelSvc.getInstance();
    	Users user = RequestUtil.getConnectedUser(request);
    	String req = RequestHelper.retrieve(request, session, "req", "DEMAND");
    	try {
			List<LabelResume> labelList = labelSvc.retrieveAllLabels(req, user.getId());
			request.setAttribute("labelList", labelList);
			request.setAttribute("req", req);
		}
    	catch (CaqsException e) {
			logger.error("Error during label list retrieve", e);
			forward = mapping.findForward("failure");
		}
        return forward;
    }

}
