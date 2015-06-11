package com.compuware.caqs.presentation.consult.actions.actionplan;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import java.text.NumberFormat;
import net.sf.json.JSONArray;

/**
 * renvoie les informations necessaires a l'affichage du tableau d'impact d'un
 * plan d'action niveau projet.
 * ce tableau affiche pour chaque ea ayant un plan d'action :
 * - son nom
 * - le cout de son plan d'action
 * - le nombre de criteres inclus
 * - apres ouverture de la ligne par le '+' :
 *   - kiviat simule
 *   - liste des criteres inclus
 * @author cwfr-dzysman
 */
public class ActionPlanProjectCostList extends ExtJSGridAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject root = new JSONObject();
        JSONArray array = new JSONArray();
        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        double totalCost = 0.0;
        if (eltBean != null) {
            List<ElementBean> eas = ElementSvc.getInstance().retrieveAllApplicationEntitiesForProject(eltBean);
            if (eas != null) {
                for (ElementBean ea : eas) {
                    //cette ea a-t-elle un plan d'action defini ?
                    ApplicationEntityActionPlanBean ap = (ApplicationEntityActionPlanBean) ActionPlanSvc.getInstance().getCompleteActionPlan(ea,
                            ea.getBaseline().getId(), false, request);
                    if (ap != null) {
                        totalCost += ap.getCorrectionCost();
                        array.add(this.elementBeanToJSONObject(ea, ap, RequestUtil.getLocale(request)));
                    }
                }
            }
        }
        root.put("totalCost", totalCost);
        root.put("eas", array);
        root.put("totalCount", array.size());
        return root;
    }

    private JSONObject elementBeanToJSONObject(ElementBean eltBean, ApplicationEntityActionPlanBean ap, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("id", eltBean.getId());
        retour.put("lib", eltBean.getLib());
        retour.put("idBline", eltBean.getBaseline().getId());
        if (eltBean.getUsage() != null) {
            retour.put("libUsage", eltBean.getUsage().getLib(loc));
        }
        if (eltBean.getDialecte() != null) {
            retour.put("libDialecte", eltBean.getDialecte().getLib());
        }
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> correctedElementsList = (ActionPlanElementBeanCollection<ActionPlanCriterionBean>) ap.getCorrectedElements();
        retour.put("nbCriterionsIncluded", correctedElementsList.size());
        JSONArray criterions = new JSONArray();
        double actionPlanCost = 0.0;
        NumberFormat nf = StringFormatUtil.getMarkFormatter(loc);
        for (ActionPlanCriterionBean criterion : correctedElementsList) {
            JSONObject obj = new JSONObject();
            obj.put("criterionId", criterion.getId());
            obj.put("criterionCost", nf.format(criterion.getCost()));
            obj.put("criterionLib", criterion.getInternationalizableProperties().getLib(loc));
            actionPlanCost += criterion.getCost();
            criterions.add(obj);
        }
        retour.put("criterions", criterions);
        retour.put("totalCost", nf.format(actionPlanCost));
        return retour;
    }
}
