/*
 * KiviatServlet.java
 *
 * Created on 24 mars 2004, 13:44
 */
package com.compuware.caqs.presentation.consult.actions;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.SyntheseSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-fdubois
 */
public class KiviatAction extends Action {

    /**
     * Logger.
     */
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final long serialVersionUID = 7904005326944053926L;
    private static final String KIVIAT_TITLE_MSG = "caqs.synthese.kiviat";

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        LoggerManager.pushContexte("KiviatServlet");

        response.setContentType("image/png");
        ElementBean eltBean = null;
        //les recuperations de idelt, idbline, idpro sont specifiques
        //a l'affichage de la synthese du topdown dans la labellisation
        String idElt = request.getParameter("id_elt");
        if(idElt != null) {
            eltBean = new ElementBean();
            eltBean.setId(idElt);
        } else {
            eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        }
        String idPro = request.getParameter("id_pro");
        if(idPro != null) {
            ProjectBean prj = new ProjectBean();
            prj.setId(idPro);
            eltBean.setProject(prj);
        }

        GraphImageConfig imgConfig = new GraphImageConfig();
        imgConfig.setWidth(400);
        imgConfig.setHeight(350);

        String idBline = request.getParameter("baselineId");
        String widthStr = request.getParameter("width");
        String heightStr = request.getParameter("height");

        if (idBline == null && eltBean!=null && eltBean.getBaseline()!=null) {
            idBline = eltBean.getBaseline().getId();
        }

        if (widthStr != null && widthStr.matches("[0-9]+")) {
            imgConfig.setWidth(Integer.parseInt(widthStr));
        }
        if (heightStr != null && heightStr.matches("[0-9]+")) {
            imgConfig.setHeight(Integer.parseInt(heightStr));
        }

        Locale locale = getLocale(request);
        MessageResources resources = this.getResources(request);

        String title = request.getParameter("title");
        if (title == null) {
            boolean displayTitle = RequestUtil.getBooleanParam(request, "displayTitle", true);
            if(displayTitle) {
                title = resources.getMessage(locale, KIVIAT_TITLE_MSG);
            }
        }

        imgConfig.setTitle(title);

        imgConfig.setLocale(locale);
        imgConfig.setResources(resources);

        SyntheseSvc syntheseSvc = SyntheseSvc.getInstance();
        File kiviatFile = null;
        if(eltBean!=null && idBline!=null) {
            kiviatFile = syntheseSvc.retrieveKiviatImage(eltBean, idBline, imgConfig);
        }
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
        LoggerManager.popContexte();
        return null;
    }
}
