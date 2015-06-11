package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.service.MetricSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class RetrieveMetricsAsCsvAction extends ElementSelectedActionAbstract {


    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    // --------------------------------------------------------- Public Methods


    public ActionForward doExecute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {

        return retrieveData(mapping, request, response);

    }
    
    protected ActionForward retrieveData(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {

    	HttpSession session = request.getSession();
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        MetricSvc metricSvc = MetricSvc.getInstance();
    	String data = metricSvc.retrieveAllMetricsAsCsv(eltBean, eltBean.getBaseline());

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			// Set your response headers here
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment; filename=\"DonneesBrutes.csv\"");
			response.setContentLength(data.length());

			out.write(data.getBytes(Constants.GLOBAL_CHARSET));
		} catch (IOException ioe) {
			return mapping.findForward("failure");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe) {
					//ignore subtly
                	logger.error("Error during closing stream", ioe);
				}
			}
		}

		return null;
	}
    
}
