/**
 * 
 */
package com.compuware.caqs.presentation.common.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.log4j.Logger;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class ResultServlet extends HttpServlet {

    protected static Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final int MAX_STACK_LENGTH = 200;
	
	public void printResult(AnalysisResult result, PrintWriter output) {
		output.append("<result>");
		output.append("<success>").append("" + result.isSuccess()).append("</success>");
		if (result.getParamMap() != null) {
			printResultMap(result.getParamMap(), output);
		}
		if (result.getMessage() != null && result.getMessage().length() > 0) {
			output.append("<message>").append(result.getMessage()).append("</message>");
		}
		output.append("</result>");
	}
	
	private void printResultMap(Map<String, String> paramMap, PrintWriter output) {
		if (paramMap != null && paramMap.size() > 0) {
			Set<String> paramKeySet = paramMap.keySet();
			Iterator<String> paramKeyIter = paramKeySet.iterator();
			String paramKey = null;
			while (paramKeyIter.hasNext()) {
				paramKey = paramKeyIter.next();
				output.append('<').append(paramKey).append('>');
				output.append(paramMap.get(paramKey));
				output.append("</").append(paramKey).append('>');
			}
		}
	}
	
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
    	AnalysisResult result = null;
    	try {
    		result = doProcessRequest(request, response);
    	}
    	catch (Exception e) {
    		logger.error("Error during process task", e);
    		result = new AnalysisResult();
        	result.setSuccess(false);
        	result.setMessage("Error during process task: " + getStackTraceAsString(e));
    	}
        response.setContentType("text/xml");
        PrintWriter output = response.getWriter();
    	printResult(result, output);
    	output.flush();
        output.close();
    }	

    private String getStackTraceAsString(Exception e) {
    	String result = null;
    	StringWriter sw = new StringWriter();
    	try {
	    	if (e != null) {
	    		e.printStackTrace(new PrintWriter(sw));
	    	}
	    	sw.flush();
	    	result = sw.toString();
	    	sw.close();
    	}
    	catch (IOException ex) {
    		logger.error("Cannot print stack trace", ex);
    	}
        if (result != null && result.length() > MAX_STACK_LENGTH) {
            result = result.substring(0,MAX_STACK_LENGTH);
        }
    	return result;
    }
	
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected abstract AnalysisResult doProcessRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException;
	
	
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected final void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

}
