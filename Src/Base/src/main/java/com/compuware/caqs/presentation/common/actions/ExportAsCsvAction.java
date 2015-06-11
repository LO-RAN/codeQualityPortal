/**
 * 
 */
package com.compuware.caqs.presentation.common.actions;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.formatter.Formatter;
import com.compuware.caqs.domain.dataschemas.formatter.FormatterFactory;

/**
 * @author cwfr-fdubois
 *
 */
public class ExportAsCsvAction extends Action {

    //logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {

		HttpSession session = request.getSession();

		// Extract attributes we will need
    	String sessionAttributeName = request.getParameter("sessionAttributeName");

    	FormatterFactory formatterFactory = FormatterFactory.getInstance();
    	Formatter formatter = formatterFactory.getFormatter(sessionAttributeName);

    	try {
			// Set your response headers here
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=\"BottomUp.csv\"");
    		formatter.format((Collection)session.getAttribute(sessionAttributeName), response.getWriter(), this.getResources(request), this.getLocale(request));
    	}
    	catch (IOException e) {
    		logger.error("Error during csv formatting");
    		logger.error("sessionAttributeName=" + sessionAttributeName);
    		logger.error(e);
    	}
    	
		return null;

	}
}
