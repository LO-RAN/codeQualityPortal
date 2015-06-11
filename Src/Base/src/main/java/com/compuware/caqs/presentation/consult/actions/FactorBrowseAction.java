package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.comparators.FactorBeanComparator;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.FactorSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.Collections;
import java.util.List;

public final class FactorBrowseAction extends ElementSelectedActionAbstract {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    // --------------------------------------------------------- Public Methods
    public ActionForward doExecute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        request.setAttribute("idElt", request.getParameter("id_elt"));
        request.setAttribute("idBline", request.getParameter("id_bline"));
        return mapping.findForward("success");
    }

}
