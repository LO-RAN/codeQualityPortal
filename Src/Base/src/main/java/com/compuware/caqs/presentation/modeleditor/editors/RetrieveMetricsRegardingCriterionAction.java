/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparator;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparatorFilterEnum;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.MetricSvc;
import com.compuware.toolbox.util.resources.Internationalizable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveMetricsRegardingCriterionAction extends ExtJSGridAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        Locale loc = RequestUtil.getLocale(request);

        String idCrit = request.getParameter("critId");
        String modelId = request.getParameter("modelId");
        List<MetriqueDefinitionBean> coll = null;
        String type = request.getParameter("associated");
        boolean associated = true;
        if (type != null) {
            associated = Boolean.parseBoolean(type);
        }
        if (associated) {
            coll = MetricSvc.getInstance().retrieveMetriqueDefinitionByIdCrit(idCrit, modelId);
        } else {
            String toolId = request.getParameter("toolId");
            String filterId = request.getParameter("filterId");
            String filterLib = request.getParameter("filterLib");
            String idLoc = RequestUtil.getLocale(request).getLanguage();
            coll = MetricSvc.getInstance().retrieveMetriqueDefinitionNotAssociatedToCriterionAndModelByTool(idCrit, modelId, toolId, filterId, filterLib, idLoc);
        }
        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            String sens = request.getParameter("dir");
            boolean desc = "desc".equals(sens.toLowerCase());
            Collections.sort(coll, new I18nDefinitionComparator(desc, I18nDefinitionComparatorFilterEnum.fromString(sort), loc));
        }
        int collSize = coll.size();
        List<JSONObject> l = new ArrayList<JSONObject>();
        retour.put("totalCount", collSize);
        int start = RequestUtil.getIntParam(request, "start", 0);
        int limit = RequestUtil.getIntParam(request, "limit", collSize);
        for (int i = start; i < (start + limit) && i < collSize; i++) {
            MetriqueDefinitionBean elt = coll.get(i);
            JSONObject o = this.elementBeanToJSONObject((Internationalizable) elt, loc);
            if (elt.getOutil() != null) {
                o.put("tool", elt.getOutil().getLib(loc));
            }
            l.add(o);
        }
        retour.put("elements", l.toArray(new JSONObject[0]));

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
