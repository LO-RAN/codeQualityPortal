/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) Frederic DUBOIS<p>
 * Soci�t� : Software & Process<p>
 * @author Frederic DUBOIS
 * @version 1.0
 */
package com.compuware.caqs.presentation.process.servlets;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.dbms.DataAccessCache;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.analysis.AnalysisConfig;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;
import com.compuware.caqs.domain.dataschemas.calcul.impl.CalculationConfig;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.process.CalculationSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

public class CalculServlet extends ProcessServlet {

    /**
     *
     */
    private static final long serialVersionUID = 8359395015826115883L;
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_CALCUL_LOGGER_KEY);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    //Traiter la requete HTTP Post
    /**
     * When used from client browser, the following parameters are interpreted :
     * <ul>
     * <li>id_pro   : project Id</li> 
     * <li>id_bline : baseline Id</li>
     * <li>metrics	: one of {true,false} (false when recalculating EA)</li> 
     * </ul>
     * ex: http://myWebSite/CalculServlet?id_pro=20050101010101&id_bline=20050101010102&metrics=false
     * <p/>
     * @param request
     * @param response
     * @throws ServletException
     * @throws java.io.IOException
     */
    protected AnalysisResult doProcessRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);

        response.setContentType("text/html");

        AnalysisConfig analysisParameters = extractAnalisysConfig(request);

        boolean calculateMetrics = RequestUtil.getBooleanParam(request, "metrics", true);
        boolean success = false;
        String message = null;
        boolean showMsg = RequestUtil.getBooleanParam(request, "showMsg", false);
        String idUser = request.getParameter("id_user");
        if (idUser == null) {
            showMsg = false;
        }

        String idBline = analysisParameters.getBaselineId();
        String projectId = analysisParameters.getProjectId();
        String idEa = request.getParameter("id_ea");
        String libEa = request.getParameter("libElt");
        String prjLib = request.getParameter("libPrj");

        if (projectId != null && idBline != null) {
            if (idEa == null) {
                clearCache(idBline, projectId);
                try {
                    CalculationSvc calculationSvc = CalculationSvc.getInstance();
                    ICalculationConfig calculationConfig = new CalculationConfig();
                    calculationConfig.setNeedMetricCalculation(calculateMetrics);
                    calculationConfig.setNeedCriterionCalculation(calculateMetrics);
                    calculationConfig.setNeedTrendUpdate(true);
                    calculationSvc.calculate(analysisParameters, calculationConfig);

                    success = true;
                    message = resources.getString("caqs.calculservlet.reussi");
                } catch (CaqsException e) {
                    logger.error("Error during calcul", e);
                    success = false;
                    message = message + resources.getString("caqs.calculservlet.echec") + e.toString();
                }
                clearCache(idBline, projectId);
            } else {
                ElementBean eltBean = new ElementBean();
                eltBean.setId(idEa);
                eltBean.setLib(libEa);
                BaselineBean bbean = new BaselineBean();
                bbean.setId(idBline);
                ProjectBean projectBean = new ProjectBean();
                projectBean.setId(projectId);
                projectBean.setLib(prjLib);
                bbean.setProject(projectBean);
                eltBean.setProject(projectBean);
                eltBean.setBaseline(bbean);
                success = CalculationSvc.getInstance().calculate(eltBean, idBline, idUser, projectId, calculateMetrics, showMsg);
                if(success) {
                    message = resources.getString("caqs.calculservlet.reussi");
                } else {
                    message = message + resources.getString("caqs.calculservlet.echec");
                }
            }
        }
        else  {
            logger.error("Error during calcul: invalid baseline or project");
            success = false;
            message = resources.getString("caqs.calculservlet.echec") + "invalid baseline or project";
        }

        //Sends the response to the scheduller
        AnalysisResult result = new AnalysisResult();
        result.setSuccess(success);
        result.setMessage(message);
        return result;

    }

    private void clearCache(String idBline, String idPro) {
        DataAccessCache dataCache = DataAccessCache.getInstance();
        dataCache.clearCache(idBline);
        dataCache.clearCache(idPro);
        ImageUtil.getInstance().clearKiviatImage(idBline);
    }
}
