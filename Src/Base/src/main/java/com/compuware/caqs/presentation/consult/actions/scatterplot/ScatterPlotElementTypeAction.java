/*
 * ScatterPlotServlet.java
 *
 * Created on 24 mars 2004, 13:44
 */

package com.compuware.caqs.presentation.consult.actions.scatterplot;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author cwfr-fdubois
 */
public class ScatterPlotElementTypeAction extends Action {

	private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        LoggerManager.pushContexte("ScatterPlotElementTypeRetrieve");
		ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
		
		String idEa = null;
		if(eltBean==null) {
			idEa = request.getParameter("idEa");
		} else {
			idEa = eltBean.getId();
		}

		List<ElementType> l = ElementSvc.getInstance().retrieveAllElementTypesForApplicationEntity(idEa);
		logger.debug("Number of element types: "+l.size());

		StringBuffer buff = new StringBuffer();
		for(ElementType et : l) {
			buff.append(et.getId()).append(';').append(et.getLib(RequestUtil.getLocale(request))).append('\n');
		}

		logger.debug("Sending data: " + new Date());
		GZIPOutputStream zout = new GZIPOutputStream(response.getOutputStream());
		zout.write(buff.toString().getBytes());
		zout.flush();
		zout.close();
		logger.debug("Data sent: " + new Date());
		LoggerManager.popContexte();
        return null;
	}

}
