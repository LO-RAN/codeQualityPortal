/*
 * SourceManagerServlet.java
 *
 * Created on 23 d�cembre 2002, 15:45
 */

package com.compuware.caqs.presentation.process.servlets;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.business.analysis.SourceManager;
import com.compuware.caqs.business.util.AnalysisFileLogger;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.analysis.AnalysisConfig;
import com.compuware.caqs.domain.dataschemas.analysis.SystemCallResult;
import org.apache.log4j.Logger;

/**
 * Source management entry point for project or EA.
 *  Parameters are:
 *  One of the two following ones:
 *      - projectId : the id of the project to manage.
 *      - eaId      : the id of the ea to manage.
 *  And the goal to execute (if no parameter then the default action is executed)
 *      - goal      : values from SourceManager.java.
 * @author  cwfr-fdubois
 * @version
 */
public class SourceManagerServlet extends ProcessServlet {
    
    private static final String PARAM_TARGET = "target";
	private static final String PARAM_GOAL = "goal";
	private static final String PARAM_EA_ID = "eaId";

	/**
	 * 
	 */
	private static final long serialVersionUID = -1585463859779573847L;

	/** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * Nested parameters: "projectId" for project extraction or "eaId" for EA extraction.
     * @param request servlet request
     * @param response servlet response
     */
    protected AnalysisResult doProcessRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        response.setContentType("text/xml");

        // Getting request parameters.
        AnalysisConfig config = extractAnalisysConfig(request);

        String eaId = request.getParameter(PARAM_EA_ID);
        String goal = request.getParameter(PARAM_GOAL);
        String target = request.getParameter(PARAM_TARGET);
        boolean prjTarget = false;
        if (ElementType.PRJ.equalsIgnoreCase(target)) {
        	prjTarget = true;
        }
        Properties parameters = extractExtraParameters(request);

        AnalysisResult result = new AnalysisResult();
        SystemCallResult executionResult = new SystemCallResult();

        Logger analysisLogger = AnalysisFileLogger.createLogger(config.getBaselineId());
        SourceManager manager = new SourceManager(analysisLogger);

        try {
            if (config.getProjectId() != null && config.getProjectId().length() > 0) {
                // Extracting the project sources.
            	executionResult = manager.manage(config, goal, prjTarget, parameters);
            }
            else {
                if (eaId != null && eaId.length() > 0) {
                    // Extracting the EA sources.
                	executionResult = manager.manageEa(eaId, config.getBaselineId(), null, goal, parameters);
                }
            }
            result.setSuccess(executionResult.getResultCode() == 0);
        }
        catch (java.io.IOException ex) {
            // Extraction failed: print 0;error description.
        	logger.error("Error executing ant task", ex);
            result.setSuccess(false);
            result.setMessage(ex.toString());
        }
        finally {
            AnalysisFileLogger.shutdownHierarchy(analysisLogger);
        }
        return result;
    }

    private Properties extractExtraParameters(HttpServletRequest request) {
    	Properties result = new Properties();
    	Enumeration parameterNamesEnum = request.getParameterNames();
    	String currentParameterName = null;
    	if (parameterNamesEnum != null) {
    		while (parameterNamesEnum.hasMoreElements()) {
    			currentParameterName = (String)parameterNamesEnum.nextElement();
    			if (!currentParameterName.equals(ProcessServlet.PROJECT_ID_PARAMETER_KEY)
    					&& !currentParameterName.equals(ProcessServlet.BASELINE_ID_PARAMETER_KEY)
    					&& !currentParameterName.equals(PARAM_EA_ID)
    					&& !currentParameterName.equals(PARAM_GOAL)
    					&& !currentParameterName.equals(PARAM_TARGET)
    					&& !currentParameterName.equals(EA_LIST_PARAMETER_KEY)
    					&& !currentParameterName.equals(EA_OPTION_LIST_PARAMETER_KEY)) {
    				result.put(currentParameterName, request.getParameter(currentParameterName));
    			}
    		}
    	}
    	return result;
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
