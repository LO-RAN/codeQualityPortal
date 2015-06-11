package com.compuware.caqs.presentation.consult.actions.actionplan;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.presentation.consult.actions.ElementSelectedActionAbstract;
import com.compuware.caqs.presentation.consult.actions.actionplan.util.ActionPlanSimulateGoalsMark;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.PrintWriter;
import net.sf.json.JSONObject;

public class ActionPlanAutoFillAction extends ElementSelectedActionAbstract {
    private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    @Override
    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String idBline = eltBean.getBaseline().getId();

        ActionPlanSvc.getInstance().autoFillApplicationEntityActionPlan(eltBean, idBline, request);
        JSONObject obj = new JSONObject();
        ActionPlanBean ap = ActionPlanSvc.getInstance().getCompleteActionPlan(eltBean, idBline, false, request);
        if (ap != null) {
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> criteres = ap.getCorrectedElements();
            double cost = 0.0;
            for (ActionPlanCriterionBean criterion : criteres) {
                cost += criterion.getCost();
            }
            obj.put("newCost", cost);
        }
        session.removeAttribute(Constants.ACTION_PLAN_IMPACTED_ELTS_LIST);
        //ActionPlanSimulateGoalsMark.getInstance().calculatePieChartMarks(eltBean, idBline, request);

        try {
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            obj.write(out);
            out.flush();
        } catch (IOException e) {
            mLog.error("ProjectTree : " + e.getMessage());
        }

        return null;
    }
}
