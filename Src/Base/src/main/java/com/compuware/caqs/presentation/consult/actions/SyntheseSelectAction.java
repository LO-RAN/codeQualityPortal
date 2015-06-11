package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.service.TaskSvc;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.business.report.Reporter;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.LabelDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class SyntheseSelectAction extends ElementSelectedActionAbstract {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
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
        ActionForward forward = null;
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String idBline = eltBean.getBaseline().getId();
        if (eltBean != null) {
            //recuperation de la presence ou non du rapport pour cet element
            //le test n'est pas fait sur le type EA pour le cas ou un rapport pourra etre genere sur tous les types
            Reporter reporter = new Reporter();
            boolean reportAvailable = reporter.reportAlreadyExists(eltBean, RequestUtil.getLocale(request));
            session.setAttribute("reportWordAlreadyExists", new Boolean(reportAvailable));

            boolean generatingReport = TaskSvc.getInstance().reportGeneratingForElement(eltBean.getId(), idBline);
            session.setAttribute("generatingReport", new Boolean(generatingReport));
            request.setAttribute("id_elt", eltBean.getId());

            BaselineSvc baselineSvc = BaselineSvc.getInstance();
            boolean lastBaseline = baselineSvc.isLastBaseline(idBline, eltBean);
            request.setAttribute("lastBaseline", new Boolean(lastBaseline));

        } else {
            forward = mapping.findForward("sessiontimeout");
        }
        return forward;
    }

}
