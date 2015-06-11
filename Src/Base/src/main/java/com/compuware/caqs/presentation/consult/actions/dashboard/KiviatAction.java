/*
 * KiviatAction.java
 *
 * Created on 24 mars 2004, 13:44
 */
package com.compuware.caqs.presentation.consult.actions.dashboard;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.dashboard.DashboardElementBean;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import com.compuware.caqs.service.SyntheseSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-dzysman
 */
public class KiviatAction extends Action {

    /**
     * Logger.
     */
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        LoggerManager.pushContexte("DashboardKiviatAction");

        String id = request.getParameter("idElt");

        response.setContentType("image/png");


        ElementBean elt = null;
        List<DashboardElementBean> elts = (List<DashboardElementBean>) request.getSession().getAttribute("dashboardElementsBean");
        if (elts != null) {
            for (DashboardElementBean e : elts) {
                if (e.getId().equals(id)) {
                    elt = e;
                    break;
                }
            }
        }

        if (elt != null) {
            GraphImageConfig imgConfig = new GraphImageConfig();
            imgConfig.setWidth(320);
            imgConfig.setHeight(200);

            Locale locale = getLocale(request);
            MessageResources resources = this.getResources(request);

            imgConfig.setLocale(locale);
            imgConfig.setResources(resources);

            SyntheseSvc syntheseSvc = SyntheseSvc.getInstance();
            File kiviatFile = syntheseSvc.retrieveDashboardKiviatImage(elt, elt.getBaseline().getId(), imgConfig);

            if (kiviatFile != null && kiviatFile.exists()) {
                try {
                    ServletOutputStream out = response.getOutputStream();
                    byte[] result = ImageUtil.getInstance().getImageBytes(kiviatFile);
                    out.write(result);
                    out.flush();
                } catch (IOException e) {
                    logger.error("Error reading file: " + kiviatFile.getName(), e);
                }
            }
        }


        LoggerManager.popContexte();
        return null;
    }
}
