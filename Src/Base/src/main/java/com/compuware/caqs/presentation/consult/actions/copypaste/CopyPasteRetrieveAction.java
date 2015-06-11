package com.compuware.caqs.presentation.consult.actions.copypaste;

import java.io.IOException;
import java.util.List;

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
import com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.consult.actions.ElementSelectedActionAbstract;
import com.compuware.caqs.service.copypaste.CopyPasteSvc;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class CopyPasteRetrieveAction extends ElementSelectedActionAbstract {

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
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        List<CopyPasteBean> result = null;
        ActionForward retour = null;

        try {
        	String idElt = RequestHelper.getParameterAndForward(request, "id_elt");
        	CopyPasteSvc copyPasteSvc = CopyPasteSvc.getInstance();
        	result = copyPasteSvc.retrieveCopyPaste(idElt, eltBean.getBaseline().getId());
        	request.setAttribute("copypastelist", result);
        }
        catch (CaqsException e) {
            logger.error("Error getting copy/paste elements.", e);
            retour = mapping.findForward("failure");
        }

        return retour;
    }

}
