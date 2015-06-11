package com.compuware.caqs.presentation.consult.actions.actionplan;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.BaselineSvc;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveActionPlanGlobalPropertiesAction extends ExtJSAjaxAction {

    private void retrieveActionPlanCriterionsPriorities(JSONObject retour, HttpServletRequest request) {
        JSONArray array = new JSONArray();
        ActionPlanPriority[] priorites = ActionPlanPriority.getValidValues();
        for (int i = 0; i < priorites.length; i++) {
            JSONObject obj = new JSONObject();
            obj.put("idPriority", priorites[i].toString());
            obj.put("lib", priorites[i].toString(request));
            array.add(obj);
        }
        retour.put("priorities", array);
    }

    private void retrieveFactorsList(JSONObject retour, HttpServletRequest request, ElementBean ea) {
        JSONArray array = new JSONArray();
        FactorBeanCollection real = ActionPlanSvc.getInstance().getRealKiviatDatas(ea, ea.getBaseline().getId(), request);
        FactorBeanCollection factors = new FactorBeanCollection(real);
        request.getSession().setAttribute("factors", factors);
        Locale loc = RequestUtil.getLocale(request);
        for (FactorBean fb : factors) {
            JSONObject obj = new JSONObject();
            obj.put("id", fb.getId());
            obj.put("text", fb.getLib(loc));
            array.add(obj);
        }
        retour.put("factors", array);
    }

    private void computeCost(JSONObject retour, HttpServletRequest request, ApplicationEntityActionPlanBean actionPlanBean, ElementBean ea) {
        double cost = 0.0;
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> criteres = actionPlanBean.getCorrectedElements();
        for (ActionPlanCriterionBean criterion : criteres) {
            cost += criterion.getCost();
        }
        retour.put("totalCost", new Double(cost));
/*        retour.put("totalCostWorkUnit", new Double(cost *
                ea.getUsage().getActionPlanUnit().getNbUnits()));
        retour.put("nbActionPlanUnit", new Double(ea.getUsage().getActionPlanUnit().getNbUnits()));
        retour.put("actionPlanUnitLabel", ea.getUsage().getActionPlanUnit().getLib(RequestUtil.getLocale(request)));*/
    }

    private void setReadOnlyFlag(JSONObject retour, HttpServletRequest request, ElementBean ea) {
        BaselineSvc baselineSvc = BaselineSvc.getInstance();
        boolean readOnly = false;
        readOnly = !baselineSvc.isLastBaseline(ea.getBaseline().getId(), ea);

        Users user = RequestUtil.getConnectedUser(request);
        String idUser = user.getId();

        if (!readOnly) {
            /*if (!LockManager.getInstance().hasAccess(Locks.ACTION_PLAN_MODIFICATION, idUser, ea.getId(), user.getCookie())) {
                readOnly = true;
                request.setAttribute("concurrentModification", "true");
            }*/
        }

        boolean hasAccess = user.access("ACTION_PLAN_EDITION");
        if (!hasAccess) {
            readOnly = true;
        }/* else if (!readOnly && hasAccess) {
            LockManager.getInstance().setLock(Locks.ACTION_PLAN_MODIFICATION, idUser, ea.getId(), user.getCookie());
        }*/
        retour.put("readOnly", readOnly);
    }

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        ElementBean ea = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        this.setReadOnlyFlag(retour, request, ea);
        this.retrieveFactorsList(retour, request, ea);
        this.retrieveActionPlanCriterionsPriorities(retour, request);

        ApplicationEntityActionPlanBean actionPlanBean = (ApplicationEntityActionPlanBean) ActionPlanSvc.getInstance().getCompleteActionPlan(ea, ea.getBaseline().getId(), true, request);

        this.computeCost(retour, request, actionPlanBean, ea);
        request.getSession().removeAttribute("FilteredActionPlanCriteres");
        request.getSession().removeAttribute("ActionPlanAllCriterions");

        return retour;
    }
}
