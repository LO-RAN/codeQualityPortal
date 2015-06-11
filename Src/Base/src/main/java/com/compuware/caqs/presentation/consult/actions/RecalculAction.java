package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.service.process.CalculationSvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import javax.servlet.http.HttpSession;

public class RecalculAction extends ExtJSAjaxAction {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_CALCUL_LOGGER_KEY);

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        HttpSession session = request.getSession(true);

        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        boolean calculateMetrics = RequestUtil.getBooleanParam(request, "metrics", true);

        CalculationSvc.getInstance().launchRemoteCalculation(eltBean, RequestUtil.getConnectedUserId(request),
                calculateMetrics, request);
       this.fillJSONObjectWithReturnCode(obj, retour);
       return obj;
    }
}
