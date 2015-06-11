package com.compuware.caqs.presentation.report.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.consult.actions.ElementSelectedActionAbstract;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.report.ReportSvc;

public final class ReportAction extends ElementSelectedActionAbstract {
    // --------------------------------------------------------- Public Methods


    public ActionForward doExecute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {        
        HttpSession session = request.getSession();        
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        ReportSvc.getInstance().forwardToReportGeneratorServer(eltBean, RequestUtil.getConnectedUserId(request), request);
        return null;
    }
}
