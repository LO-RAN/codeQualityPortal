/*
 * ScatterPlotServlet.java
 *
 * Created on 24 mars 2004, 13:44
 */
package com.compuware.caqs.presentation.consult.actions.scatterplot;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ScatterDataBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.consult.actions.ScatterPlotPrepareAction;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.ScatterPlotSyntheseSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author cwfr-fdubois
 */
public class ScatterPlotAction extends Action {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        LoggerManager.pushContexte("ScatterPlotRetrieve");

        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        try {
            if (eltBean == null) {
                String idElt = request.getParameter("id_elt");
                String idBline = request.getParameter("id_bline");
                ElementSvc elementSvc = ElementSvc.getInstance();
                eltBean = elementSvc.retrieveElement(idElt, idBline);
            }

            String idMetH = request.getParameter("metH");
            String idMetV = request.getParameter("metV");
            String centreH = request.getParameter(ScatterPlotPrepareAction.CENTER_H_KEY);
            String centreV = request.getParameter(ScatterPlotPrepareAction.CENTER_V_KEY);
            String idTelt = request.getParameter("idTelt");

            if (centreH == null) {
                centreH = "10";
            }
            if (centreV == null) {
                centreV = "7";
            }

            logger.debug("idMetH=" + idMetH + ", idMetV=" + idMetV);
            logger.debug("centerH=" + centreH + ", centerV=" + centreV);
            logger.debug("idTelt=" + idTelt);

            logger.debug("Getting connection: " + new Date());
            Properties props = (Properties) session.getAttribute("webResources");
            logger.debug("Starting retrieve: " + new Date());
            ScatterPlotSyntheseSvc scatterPlotSyntheseSvc = ScatterPlotSyntheseSvc.getInstance();
            List<ScatterDataBean> scatterBeans = scatterPlotSyntheseSvc.retrieveScatterPlot(eltBean, idMetH.toUpperCase(), idMetV.toUpperCase(), idTelt);

            if (props != null) {
                ScatterDataBean centre = scatterBeans.get(0);
                if (!centre.getLib().equals("Centre")) {
                    ScatterDataBean bean = new ScatterDataBean(null, "CENTER", "Centre", "Centre");
                    bean.setMetH(centreH);
                    bean.setMetV(centreV);
                    scatterBeans.add(0, bean);
                } else {
                    centre.setMetH(centreH);
                    centre.setMetV(centreV);
                }
            }
            logger.debug("Ending retrieve: " + new Date());

            logger.debug("Sending data: " + new Date());
            GZIPOutputStream zout = new GZIPOutputStream(response.getOutputStream());
            zout.write(scatterPlotSyntheseSvc.getScatterBytes(scatterBeans).getBytes());
            zout.flush();
            zout.close();
            logger.debug("Data sent: " + new Date());
            LoggerManager.popContexte();
        } catch (CaqsException e) {
            throw new ServletException(e);
        }
        return null;
    }
}
