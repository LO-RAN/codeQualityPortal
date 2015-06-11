/*
 * UpdateBaselineServlet.java
 *
 * Created on 16 d�cembre 2002, 12:05
 * @author  cwfr-fxalbouy
 * @version
 */

package com.compuware.caqs.presentation.process.servlets;

import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.servlets.ResultServlet;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;

public class BaselineServlet extends ResultServlet {

	private static final long serialVersionUID = -1928302713876938119L;

    public static final int ACTION_CREATE = 0;
    public static final int ACTION_UPDATE = 1;
    public static final int ACTION_DELETE = 2; //logical deletion
    public static final int ACTION_PURGE = 3;  //physical deletion

	/**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /**
     * Destroys the servlet.
     */
    public void destroy() {

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected AnalysisResult doProcessRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        String action = request.getParameter("action");
        AnalysisResult result = process(request, action);

       return result;
    }

    public AnalysisResult process(HttpServletRequest request, String action) {
        AnalysisResult result = null;

        Locale locale = RequestUtil.getLocale(request);
        String projectId = request.getParameter("projectId");
        String baselineName = request.getParameter("baselineName");
        String baselineId = request.getParameter("baselineId");
        String forcedId = request.getParameter("forcedId");

        try {
            Integer actionInteger = new Integer(action);
            int actionInt = actionInteger.intValue();
        	BaselineSvc baselineSvc = BaselineSvc.getInstance();
            switch (actionInt) {
                case BaselineServlet.ACTION_CREATE:
                	result = baselineSvc.create(projectId, forcedId);
                    break;
                case BaselineServlet.ACTION_DELETE:
                	result = baselineSvc.delete();
                    break;
                case BaselineServlet.ACTION_PURGE:
                	baselineSvc.purge(projectId, baselineId);
                    break;
                case BaselineServlet.ACTION_UPDATE:
                	result = baselineSvc.update(baselineName, baselineId, true);
                    break;
                default:
                    logger.error("Baseline : unknown case " + actionInt);
            }
        }
        catch (CaqsException e) {
        	result = new AnalysisResult();
        	result.setSuccess(false);
        	result.setMessage(e.toString());
            logger.error("Error processing baseline", e);
        }

        return result;
    }

    
    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Baseline Servlet";
    }

}
