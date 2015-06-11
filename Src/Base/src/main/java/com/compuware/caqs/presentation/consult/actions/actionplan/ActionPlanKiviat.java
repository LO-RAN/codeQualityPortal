package com.compuware.caqs.presentation.consult.actions.actionplan;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.presentation.consult.actions.actionplan.util.ActionPlanKiviatUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

public class ActionPlanKiviat extends Action {

    /**
     * Logger.
     */
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final long serialVersionUID = 7904005326944053926L;

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();

        ElementBean eltBean = null;
        String idElt = request.getParameter("id_elt");
        if(idElt != null) {
            eltBean = ElementSvc.getInstance().retrieveElementById(idElt);
        } else {
            eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        }
        LoggerManager.pushContexte("ActionPlanKiviatServlet");

        response.setContentType("image/png");

        String idBline = request.getParameter("baselineId");
        if (idBline == null) {
            idBline = eltBean.getBaseline().getId();
        }

        String widthStr = request.getParameter("width");
        String heightStr = request.getParameter("height");
        String title = request.getParameter("title");
        int width = 300;
        int height = 350;

        if (widthStr != null && widthStr.matches("[0-9]+")) {
            width = Integer.parseInt(widthStr);
        }
        if (heightStr != null && heightStr.matches("[0-9]+")) {
            height = Integer.parseInt(heightStr);
        }

        boolean showLegend = RequestUtil.getBooleanParam(request, "showLegend", true);
        boolean showTitle = RequestUtil.getBooleanParam(request, "showTitle", true);

        File kiviatFile = ActionPlanKiviatUtils.getInstance().getKiviatTemporaryFile(eltBean, idBline,
                ActionPlanPriority.LONG_TERM, title, width, height, showTitle, showLegend, request);

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
