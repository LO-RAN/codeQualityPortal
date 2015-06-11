package com.compuware.caqs.presentation.report.servlets;

import com.compuware.caqs.service.ElementSvc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.presentation.common.servlets.ResultServlet;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.report.ReportSvc;

public final class ReportServlet extends ResultServlet {

    // --------------------------------------------------------- Public Methods
    /**
     *
     */
    private static final long serialVersionUID = -5531308267994037614L;

    public AnalysisResult doProcessRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Locale loc = RequestUtil.getLocale(request);

        String language = request.getParameter("language");
        if (language != null && !"".equals(language)) {
            loc = new Locale(language);
        }

        String idUser = request.getParameter("id_user");
        boolean showMessages = ( idUser != null && !"".equals(idUser) );

        MessageResources resources = (org.apache.struts.util.PropertyMessageResources) this.getServletContext().getAttribute(Globals.MESSAGES_KEY);

        Collection<ElementBean> eaCollection = retrieveEaCollection(request);

        boolean success = false;
        String failedEaLib = null;
        if (eaCollection != null) {
            for (ElementBean eltBean : eaCollection) {
                success = ReportSvc.getInstance().generateReportFor(eltBean, idUser, loc, resources, showMessages);
                if (!success) {
                    failedEaLib = eltBean.getLib();
                    break;
                }
            }
        }
        AnalysisResult result = new AnalysisResult();
        result.setSuccess(success);
        if (!success) {
            if (!eaCollection.isEmpty()) {
                String failureMsg = resources.getMessage("caqs.report.servlet.failureMessage", failedEaLib);
                result.setMessage(failureMsg);
            } else {
                result.setMessage(resources.getMessage("caqs.report.servlet.failureMessage.noEa"));
            }
        }
        return result;

    }

    protected Collection<ElementBean> retrieveEaCollection(HttpServletRequest request) {
        String idBline = request.getParameter("id_bline");
        String idPro = request.getParameter("id_pro");
        String idEa = request.getParameter("id_ea");
        Collection<ElementBean> result = new ArrayList<ElementBean>();
        
        // we need at least a project Id
        if (idPro != null && !"".equals(idPro)) {
            // Acces en batch...
            DaoFactory daoFactory = DaoFactory.getInstance();
            BaselineDao baselineFacade = daoFactory.getBaselineDao();
            BaselineBean baselineBean;
            if (idBline != null && !"".equals(idBline)) {
                baselineBean = baselineFacade.retrieveBaselineById(idBline);
            } else {
                baselineBean = baselineFacade.retrieveLastBaseline(idPro);
            }

            if (null != baselineBean) {
                ProjectDao projectFacade = daoFactory.getProjectDao();
                ProjectBean projectBean = projectFacade.retrieveProjectById(idPro);
                baselineBean.setProject(projectBean);
                if (idEa == null || "".equals(idEa)) {
                    ReportSvc reportSvc = ReportSvc.getInstance();
                    result = reportSvc.retrieveAllEa(idPro, baselineBean);
                    Iterator<ElementBean> i = result.iterator();
                    ElementBean current = null;
                    while (i.hasNext()) {
                        current = i.next();
                        current.setBaseline(baselineBean);
                        current.setProject(projectBean);
                    }
                } else {
                    result = new ArrayList<ElementBean>();
                    ElementBean eltBean = ElementSvc.getInstance().retrieveElementById(idEa);
                    if (eltBean != null) {
                        eltBean.setProject(projectBean);
                        eltBean.setBaseline(baselineBean);
                        result.add(eltBean);
                    }
                }
            }
        }
        return result;
    }
}
