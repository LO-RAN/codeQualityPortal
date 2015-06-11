/*
 * ScatterPlotServlet.java
 *
 * Created on 24 mars 2004, 13:44
 */

package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.compuware.caqs.business.chart.xml.XmlDatasetGenerator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BottomUpSyntheseSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author cwfr-fdubois
 */
public class RepartitionAction extends Action {

	private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        LoggerManager.pushContexte("RepartitionRetrieve");

        response.setContentType("text/html;charset=" + Constants.GLOBAL_CHARSET);
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String filterDesc = (String) session.getAttribute("filter");
        String type = request.getParameter("type");
        String lastTypeElt = (String)session.getAttribute("typeElt");
        logger.debug("lastTypeElt="+lastTypeElt);
        if(lastTypeElt.equals("ALL")) {
            lastTypeElt = "%";
        }

        Collection repartition = null;
        PrintWriter out = response.getWriter();
        BottomUpSyntheseSvc bottomUpSyntheseSvc = BottomUpSyntheseSvc.getInstance(); 
        if (type.equals("CRIT")) {
            repartition = bottomUpSyntheseSvc.retrieveRepartitionByCriterion(eltBean, filterDesc, lastTypeElt);
        } else {
            repartition = bottomUpSyntheseSvc.retrieveRepartitionByFactor(eltBean, filterDesc, lastTypeElt);
        }
        StringBuffer xmlout = new StringBuffer();
        XmlDatasetGenerator.writeRepartitionData(xmlout, repartition, RequestUtil.getLocale(request));
        out.println(xmlout.toString());
        out.flush();
        LoggerManager.popContexte();
        return null;
    }

}
