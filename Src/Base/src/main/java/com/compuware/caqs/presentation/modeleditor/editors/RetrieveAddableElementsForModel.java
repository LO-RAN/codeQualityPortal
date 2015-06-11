package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparator;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparatorFilterEnum;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.CriterionSvc;
import com.compuware.caqs.service.FactorSvc;
import com.compuware.toolbox.util.resources.Internationalizable;
import java.util.Collections;
import java.util.Locale;
import net.sf.json.JSONObject;

public class RetrieveAddableElementsForModel extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        Locale loc = RequestUtil.getLocale(request);
        String modelId = request.getParameter("modelId");
        String alreadyAddedButNotSaved = request.getParameter("alreadyAddedButNotSaved");
        String[] alreadyAddedButNotSavedIds = null;
        if (alreadyAddedButNotSaved != null) {
            alreadyAddedButNotSavedIds = alreadyAddedButNotSaved.split(";");
        }
        JSONObject root = new JSONObject();
        //ElementCriterionForm
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 10);
        String elementType = request.getParameter("elementType");
        List coll = null;
        int collSize = 0;
        if (elementType != null) {
            String filterId = request.getParameter("filterid");
            String filterLib = request.getParameter("filterlib");
            String idLoc = RequestUtil.getLocale(request).getLanguage();
            if ("goals".equals(elementType)) {
                coll = FactorSvc.getInstance().retrieveGoalsNotAssociatedToModel(modelId, filterId, filterLib,
                        idLoc);
            } else if ("criterions".equals(elementType)) {
                String goalId = request.getParameter("goalId");
                coll = CriterionSvc.getInstance().retrieveCriterionsNotAssociatedToGoalAndModel(modelId, goalId, filterId, filterLib,
                        idLoc);
            }
            if (coll != null) {
                if (request.getParameter("sort") != null) {
                    String sort = request.getParameter("sort");
                    String sens = request.getParameter("dir");
                    boolean desc = "desc".equals(sens.toLowerCase());
                    Collections.sort(coll, new I18nDefinitionComparator(desc, I18nDefinitionComparatorFilterEnum.fromString(sort), loc));
                }
                collSize = coll.size();
            }
        }
        List<JSONObject> l = new ArrayList<JSONObject>();
        root.put("totalCount", collSize);
        for (int i = startIndex; i < (startIndex + limitIndex) && i <
                collSize; i++) {
            Internationalizable elt = (Internationalizable) coll.get(i);
            boolean add = true;
            if (alreadyAddedButNotSavedIds != null &&
                    this.isAlreadyAdded(elt, alreadyAddedButNotSavedIds)) {
                add = false;
            }
            if (add) {
                JSONObject o = this.elementBeanToJSONObject(elt, loc);
                l.add(o);
            }
        }
        root.put("elements", l.toArray(new JSONObject[0]));

        return root;
    }

    private boolean isAlreadyAdded(Internationalizable fb, String[] ids) {
        boolean retour = false;
        for (int i = 0; i < ids.length && !retour; i++) {
            if (ids[i] != null && ids[i].equals(fb.getId())) {
                retour = true;
            }
        }
        return retour;
    }

    private JSONObject elementBeanToJSONObject(Internationalizable elt, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("id", elt.getId());
        retour.put("lib", elt.getLib(loc));
        retour.put("desc", elt.getDesc(loc));
        retour.put("compl", elt.getComplement(loc));
        return retour;
    }
}
