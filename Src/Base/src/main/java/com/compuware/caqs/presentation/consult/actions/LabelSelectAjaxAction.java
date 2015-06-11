package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.LabelSvc;
import com.compuware.caqs.util.IDCreator;
import org.apache.struts.util.MessageResources;

public final class LabelSelectAjaxAction extends ExtJSAjaxAction {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    // --------------------------------------------------------- Public Methods
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {

        JSONObject obj = doRetrieve(request);
        return obj;
    }

    private JSONObject doRetrieve(HttpServletRequest request) {
        return retrieveLabel(request);
    }

    private String getStatusStringfromLabelStatus(String status,
            HttpServletRequest request) {
        String retour = "";
        Locale loc = RequestUtil.getLocale(request);
        MessageResources resources = RequestUtil.getResources(request);
        if ("DEMAND".equals(status)) {
            retour = resources.getMessage(loc, "caqs.label.encours");
        } else if ("VALID".equals(status)) {
            retour = resources.getMessage(loc, "caqs.label.validee");
        } else if ("REJET".equals(status)) {
            retour = resources.getMessage(loc, "caqs.label.rejetee");
        }
        return retour;
    }

    private JSONObject retrieveLabel(HttpServletRequest request) {
        JSONObject retour = new JSONObject();
        String labelId = request.getParameter("id_label");
        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        if (eltBean != null) {
            retour.put("elementLib", eltBean.getLib());
        }
        String user = "";
        String labelLib = "";
        String labelDesc = "";
        String status = "DEMAND";

        if (labelId == null || "".equals(labelId)) {
            user = RequestUtil.getConnectedUserId(request);
            labelId = IDCreator.getID();
        } else {
            try {
                LabelSvc labelSvc = LabelSvc.getInstance();
                LabelBean labelBean = labelSvc.retrieveLabel(labelId);
                if (labelBean != null) {
                    user = labelBean.getUser();
                    labelLib = labelBean.getLib();
                    status = this.getStatusStringfromLabelStatus(labelBean.getStatut(), request);
                    labelDesc = labelBean.getDesc();
                }
            } catch (CaqsException e) {
                logger.error("Error while retrieving labelisation", e);
            }
        }
        retour.put("user", user);
        retour.put("labelLib", labelLib);
        retour.put("status", status);
        retour.put("labelDesc", labelDesc);
        retour.put("labelId", labelId);

        return retour;
    }
}
