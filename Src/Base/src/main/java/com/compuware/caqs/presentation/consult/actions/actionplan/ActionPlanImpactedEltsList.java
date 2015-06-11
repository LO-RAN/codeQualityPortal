package com.compuware.caqs.presentation.consult.actions.actionplan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanImpactedElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanImpactedElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanMap;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;

public class ActionPlanImpactedEltsList extends ExtJSGridAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String idBline = eltBean.getBaseline().getId();

        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 5);
        JSONObject root = new JSONObject();

        ActionPlanImpactedElementBeanCollection liste = this.getImpactedElementsList(eltBean, idBline, eltBean.getUsage().getId(), request);

        if (liste != null) {
            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean asc = "asc".equalsIgnoreCase(sens.toLowerCase());
                if (asc) {
                    this.sortAsc(liste, sort);
                } else {
                    this.sortDesc(liste, sort);
                }
            }

            Locale loc = RequestUtil.getLocale(request);

            int size = liste.size();
            List<JSONObject> l = new ArrayList<JSONObject>();
            for (int i = startIndex; i < (startIndex + limitIndex) && i <
                    liste.size(); i++) {
                ActionPlanImpactedElementBean elt = liste.get(i);
                JSONObject o = this.elementBeanToJSONObject(elt, loc);
                l.add(o);
            }
            root.put("totalCount", size);

            root.put("elements", l.toArray(new JSONObject[0]));
        }

        return root;
    }

    private JSONObject elementBeanToJSONObject(ActionPlanImpactedElementBean elt, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("idElt", elt.getIdElt());
        retour.put("descElt", elt.getDescElt());
        retour.put("idTelt", elt.getIdTelt());
        retour.put("telt", (new ElementType(elt.getIdTelt())).getLib(loc));

        StringBuffer criterions = new StringBuffer();
        boolean first = true;
        for (ActionPlanCriterionBean criterion : elt.getCriterions()) {
            if (!first) {
                criterions.append("<BR />");
            }
            criterions.append("- ").append(criterion.getInternationalizableProperties().getLib(loc));
            first = false;
        }
        retour.put("criterions", criterions.toString());

        return retour;
    }

    private ActionPlanImpactedElementBeanCollection getImpactedElementsList(ElementBean eltBean, String idBline, String idUsa, HttpServletRequest request) {
        ActionPlanImpactedElementBeanCollection liste = (ActionPlanImpactedElementBeanCollection) request.getSession().getAttribute(Constants.ACTION_PLAN_IMPACTED_ELTS_LIST);
        if (liste != null) {
            if (!liste.getIdEa().equals(eltBean.getId()) ||
                    !liste.getIdBline().equals(idBline)) {
                liste = null;
            }
        }

        ApplicationEntityActionPlanBean actionplan = (ApplicationEntityActionPlanBean)ActionPlanSvc.getInstance().getCompleteActionPlan(eltBean, idBline, false, request);
        if (actionplan.isNeedsImpactedElementsRecompte()) {
            liste = null;
        }

        if (liste == null) {
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> allCriterions = actionplan.getCorrectedElements();
            ActionPlanElementBeanMap elementMap = ActionPlanSvc.getInstance().getElementsImpactedByActionPlan(allCriterions, idUsa);

            liste = new ActionPlanImpactedElementBeanCollection(elementMap.getIdEa(), elementMap.getIdBline());
            if (elementMap != null) {
                for (ActionPlanImpactedElementBean elt : elementMap.values()) {
                    liste.add(elt);
                }
            }
            request.getSession().setAttribute(Constants.ACTION_PLAN_IMPACTED_ELTS_LIST, liste);
            actionplan.setNeedsImpactedElementsRecompute(false);
        }
        return liste;
    }
}
