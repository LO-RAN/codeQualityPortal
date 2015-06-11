/*
 * DomainSynthesisKiviatServlet.java
 *
 * Created on 24 mars 2004, 13:44
 */
package com.compuware.caqs.presentation.consult.actions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.business.consult.Synthese;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import com.compuware.caqs.presentation.consult.actions.actionplan.util.ActionPlanKiviatUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.Locale;
import org.apache.struts.util.MessageResources;

/**
 * @author cwfr-dzysman
 */
public class DomainSynthesisKiviatAction extends Action {

    /**
     * Logger.
     */
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        response.setContentType("image/png");

        LoggerManager.pushContexte("DomainSynthesisKiviatServlet");
        //on recupere tous les projets
        List<ElementBean> elts = (List<ElementBean>) request.getSession().getAttribute("domainSynthesisProjectsList");

        int width = 400;
        String widthStr = request.getParameter("width");

        if (widthStr != null && widthStr.matches("[0-9]+")) {
            width = Integer.parseInt(widthStr);
        }

        GraphImageConfig imgConfig = new GraphImageConfig();
        imgConfig.setWidth(width);
        imgConfig.setHeight(350);
        Locale locale = getLocale(request);
        MessageResources resources = this.getResources(request);

        imgConfig.setLocale(locale);
        imgConfig.setResources(resources);


        File kiviatFile = null;
        boolean displayActionPlan = RequestUtil.getBooleanParam(request, "displayActionPlan", false);
        if (displayActionPlan) {
            String apPriority = request.getParameter("actionPlanPriority");
            kiviatFile = ActionPlanKiviatUtils.getInstance().retrieveDomainActionPlanKiviatImage(elts, ActionPlanPriority.valueOf(apPriority), request);
        } else {
            Synthese synthese = new Synthese();
            kiviatFile = synthese.retrieveDomainKiviatImage(elts, imgConfig);
        }
        if (kiviatFile != null && kiviatFile.exists()) {
            try {
                ServletOutputStream out = response.getOutputStream();
                byte[] result = ImageUtil.getInstance().getImageBytes(kiviatFile);
                out.write(result);
                out.flush();
                kiviatFile.delete();
            } catch (IOException e) {
                logger.error("Error reading file: " + kiviatFile.getName(), e);
            }
        }

        LoggerManager.popContexte();
        return null;
    }
}
