package com.compuware.caqs.presentation.consult.actions.evolution;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanImpactedElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanImpactedElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.ElementSvc;
import java.util.Locale;
import java.util.Map;

public class ActionsPlanElementsGridListAjax extends ExtJSGridAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        MessagesCodes mc = MessagesCodes.CAQS_GENERIC_ERROR;
        Locale loc = RequestUtil.getLocale(request);
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 10);

        JSONObject root = new JSONObject();

        List<JSONObject> l = new ArrayList<JSONObject>();

        Map<String, ActionPlanImpactedElementBeanCollection> result = null;
        int category = RequestUtil.getIntParam(request, "category", -1);
        if (category != -1) {
            String idElt = request.getParameter("idElt");
            String idBline = request.getParameter("idBline");
            String previousIdBline = request.getParameter("previousIdBline");
            ElementBean eltBean = ElementSvc.getInstance().retrieveElementById(idElt);
            BaselineBean bb = new BaselineBean();
            bb.setId(idBline);
            eltBean.setBaseline(bb);
            ElementsCategory cat = ElementsCategory.fromCode(category);
            if (ElementsCategory.CORRECTED_ELEMENTS.equals(cat)) {
                result = ActionPlanSvc.getInstance().getCorrectedElementsByCriterions(
                        eltBean, previousIdBline);
            } else if (ElementsCategory.STABLE_ELEMENTS.equals(cat)) {
                result = ActionPlanSvc.getInstance().getStableElementsByCriterions(
                        eltBean, previousIdBline);
            } else if (ElementsCategory.PARTIALLY_CORRECTED_ELEMENTS.equals(cat)) {
                result = ActionPlanSvc.getInstance().getPartiallyCorrectedElementsByCriterions(
                        eltBean, previousIdBline);
            } else if (ElementsCategory.DEGRADED_ELEMENTS.equals(cat)) {
                result = ActionPlanSvc.getInstance().getDegradedElementsByCriterions(
                        eltBean, previousIdBline);
            } else if (ElementsCategory.SUPPRESSED_ELEMENTS.equals(cat)) {
                result = ActionPlanSvc.getInstance().getCorrectedBecauseSuppressedElementsByCriterions(
                        eltBean, previousIdBline);
            }
        }

        if (result != null) {
            String idCrit = request.getParameter("idCrit");
            ActionPlanImpactedElementBeanCollection collection = result.get(idCrit);
            if (collection != null) {
                if (request.getParameter("sort") != null) {
                    String sort = request.getParameter("sort");
                    String sens = request.getParameter("dir");
                    boolean asc = "asc".equals(sens.toLowerCase());
                    if (asc) {
                        this.sortAsc(collection, sort);
                    } else {
                        this.sortDesc(collection, sort);
                    }
                }

                int taille = collection.size();
                for (int i = startIndex; i < (startIndex + limitIndex) && i
                        < taille; i++) {
                    ActionPlanImpactedElementBean elt = collection.get(i);
                    JSONObject o = this.actionPlanImpactedElementToJSONObject(elt, loc);
                    l.add(o);
                }
                root.put("totalCount", collection.size());
                mc = MessagesCodes.NO_ERROR;
            }
        } else {
            root.put("totalCount", 0);
        }

        root.put("elements", l.toArray(new JSONObject[0]));

        this.fillJSONObjectWithReturnCode(root, mc);

        return root;
    }

    private JSONObject actionPlanImpactedElementToJSONObject(ActionPlanImpactedElementBean elt, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("descElt", elt.getDescElt());
        retour.put("id", elt.getIdElt());
        retour.put("idTelt", elt.getIdTelt());
        retour.put("lib_telt", (new ElementType(elt.getIdTelt())).getLib(loc));
        return retour;
    }
}
