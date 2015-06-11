/*
 * AnalyseStatiqueServlet.java
 *
 * @author  cwfr-fxalbouy
 * @version
 *
 * Created on 7 novembre 2002, 17:50
 */


package com.compuware.caqs.presentation.process.servlets;

import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.business.analysis.StaticAnalysisConfig;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.StaticAnalysisSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.log4j.Logger;

public class AnalyseStatiqueServlet extends ProcessServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7898742396954826565L;
	
	protected static Logger mLog = LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {

    }

    /**
     * When used from client browser, the following parameters are interpreted :
     * <ul>
     * <li>projectId  : project Id</li> 
     * <li>baselineId : baseline Id</li>
     * <li>tool		  : one of {mccabe,checkstyle,optimaladvisor,cast,flawfinder,pqc,uml,csmetricgeneration}</li> 
     * <li>step		  : one of {analyse,load,analyseandload}</li>
     * <li>master     : true/false (indique si le parseur est le principal pour la p�remption)</li>
     * </ul>
     * ex: http://myWebSite/AnalyseStatiqueServlet?projectId=20050101010101&baselineId=20050101010102&tool=checkstyle&step=analyseandload
     * <p/>
     * @param request
     * @param response
     * @throws ServletException
     * @throws java.io.IOException
     */
     protected AnalysisResult doProcessRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);

        StaticAnalysisConfig config = extractStaticAnalisysConfig(request);
        AnalyseStatiqueServlet.mLog.info("********************************************************");
        AnalyseStatiqueServlet.mLog.info("Start Static Analysis for project : " + config.getProjectId());
        AnalysisResult result = null;

        if (config.isAnalyse() || config.isLoad()) {
        	StaticAnalysisSvc analysisSvc = StaticAnalysisSvc.getInstance();
        	result = analysisSvc.execute(config, resources);
        }

        AnalyseStatiqueServlet.mLog.info("End of Static Analysis of project");
        if(result!=null) {
        	result.addParam("tool", config.getToolId());
        	result.addParam("analysis", "" + config.isAnalyse());
        	result.addParam("load", "" + config.isLoad());
            AnalyseStatiqueServlet.mLog.info("Result=" + result.isSuccess());
        }
        AnalyseStatiqueServlet.mLog.info("********************************************************");
        LoggerManager.popContexte();
        LoggerManager.removeContexte();
        return result;
    }

    public String getServletInfo() {
        return "Static Analysis Servlet";
    }

}
