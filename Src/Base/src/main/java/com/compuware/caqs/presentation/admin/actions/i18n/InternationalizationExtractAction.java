package com.compuware.caqs.presentation.admin.actions.i18n;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.service.InternationalizationSvc;

public class InternationalizationExtractAction extends Action {
	
	public static final String METRIC_DATA_TYPE = "metriques";
	public static final String CRITERION_DATA_TYPE = "criteres";
	public static final String FACTOR_DATA_TYPE = "facteurs";

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
    	throws IOException, ServletException {
    	
		String datatype = request.getParameter("datatype");
    	InternationalizationSvc internationalisationSvc = InternationalizationSvc.getInstance();
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment; filename=\""+datatype+".csv\"");
		if (datatype.equalsIgnoreCase(METRIC_DATA_TYPE)) {
	    	internationalisationSvc.extractMetricsAsCsv(response.getWriter());
		}
		else if (datatype.equalsIgnoreCase(CRITERION_DATA_TYPE)) {
	    	internationalisationSvc.extractCriterionsAsCsv(response.getWriter());
		}
		else if (datatype.equalsIgnoreCase(FACTOR_DATA_TYPE)) {
	    	internationalisationSvc.extractFactorsAsCsv(response.getWriter());
		}
    	response.flushBuffer();
    	return null;
    }	
}
