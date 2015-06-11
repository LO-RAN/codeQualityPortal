package com.compuware.caqs.presentation.consult.actions.evolution;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementEvolutionSummaryBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.EvolutionSvc;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.dbms.JdbcDAOUtils;

public class ElementsGridListAjax extends ExtJSGridAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 10);
        String idElt = request.getParameter("idElt");
        String idBline = request.getParameter("idBline");
        String previousIdBline = request.getParameter("idPreviousBline");
        boolean fromSession = RequestUtil.getBooleanParam(request, "fromSession", true);
        int target = RequestUtil.getIntParam(request, "target", 0);
        ElementsCategory category = ElementsCategory.fromCode(target);

        JSONObject root = new JSONObject();
        List<ElementEvolutionSummaryBean> result = null;
        if (fromSession) {
            result = (List<ElementEvolutionSummaryBean>) request.getSession().getAttribute("ElementsGridListAjaxResult");
        }
        if (result == null) {
            ElementBean ea = ElementSvc.getInstance().retrieveAllElementDataById(idElt);
            BaselineBean previousBaseline = BaselineSvc.getInstance().getRealBaselineForEA(ea, previousIdBline);
            EvolutionSvc evolutionSvc = EvolutionSvc.getInstance();
            String filterDesc = RequestHelper.retrieve(request, request.getSession(), "filter", JdbcDAOUtils.DATABASE_STRING_NOFILTER);
            request.getSession().setAttribute("filter", filterDesc);
            String filterTypeElt = request.getParameter("typeElt");
            if (filterTypeElt == null) {
                filterTypeElt = ElementType.ALL;
            }
            request.getSession().setAttribute("typeElt", filterTypeElt);
            FilterBean filter = new FilterBean(filterDesc, filterTypeElt);
            if (ElementsCategory.NEW_BAD.equals(category)) {
                result = evolutionSvc.retrieveNewAndBadElements(idElt, idBline, previousBaseline, filter);
            } else if (ElementsCategory.OLD_WORST.equals(category)) {
                result = evolutionSvc.retrieveOldAndWorstElements(idElt, idBline, previousBaseline, filter);
            } else if (ElementsCategory.OLD_BETTER.equals(category)) {
                result = evolutionSvc.retrieveOldAndBetterElements(idElt, idBline, previousBaseline, filter);
            } else if (ElementsCategory.OLD_BETTER_WORST.equals(category)) {
                result = evolutionSvc.retrieveOldBetterAndWorstElements(idElt, idBline, previousBaseline, filter);
            } else if (ElementsCategory.BAD_STABLE.equals(category)) {
                result = evolutionSvc.retrieveBadAndStableElements(idElt, idBline, previousBaseline, filter);
            }
            request.getSession().setAttribute("ElementsGridListAjaxResult", result);
        }

        List<JSONObject> l = new ArrayList<JSONObject>();
        if (result != null) {
            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean asc = "asc".equals(sens.toLowerCase());
                if (asc) {
                    this.sortAsc(result, sort);
                } else {
                    this.sortDesc(result, sort);
                }
            }

            int taille = result.size();
            for (int i = startIndex; i < (startIndex + limitIndex) && i
                    < taille; i++) {
                ElementEvolutionSummaryBean elt = result.get(i);
                JSONObject o = this.elementEvolutionSummaryToJSONObject(elt, category);
                l.add(o);
            }
            root.put("totalCount", result.size());
        } else {
            root.put("totalCount", 0);
        }
        root.put("elements", l.toArray(new JSONObject[0]));

        return root;
    }

    private JSONObject elementEvolutionSummaryToJSONObject(ElementEvolutionSummaryBean elt, ElementsCategory category) {
        JSONObject retour = new JSONObject();
        retour.put("desc", elt.getDesc());
        retour.put("id", elt.getId());
        retour.put("lib", elt.getLib());
        retour.put("target", category.getCode());
        return retour;
    }
}
