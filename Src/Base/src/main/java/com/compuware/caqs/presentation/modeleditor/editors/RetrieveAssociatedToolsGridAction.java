package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.domain.dataschemas.comparators.OutilBeanComparator;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.OutilsSvc;
import java.util.Collections;
import java.util.Locale;
import net.sf.json.JSONObject;

public class RetrieveAssociatedToolsGridAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        Locale loc = RequestUtil.getLocale(request);
        String modelId = request.getParameter("modelId");
        JSONObject root = new JSONObject();
        //ElementCriterionForm
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 10);

        List<OutilBean> coll = OutilsSvc.getInstance().retrieveToolsWithMetricCount();
        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            String sens = request.getParameter("dir");
            boolean desc = "desc".equals(sens.toLowerCase());
            Collections.sort(coll, new OutilBeanComparator(sort, desc, loc));
        }
        List<OutilBean> associated = OutilsSvc.getInstance().retrieveOutilsByModelWithMetricCount(modelId);

        List<JSONObject> l = new ArrayList<JSONObject>();
        root.put("totalCount", coll.size());
        for (int i = startIndex; i < (startIndex + limitIndex) && i <
                coll.size(); i++) {
            OutilBean elt = coll.get(i);
            JSONObject o = this.outilBeanToJSONObject(elt, loc, associated);
            l.add(o);
        }
        root.put("elements", l.toArray(new JSONObject[0]));
        return root;
    }

    private JSONObject outilBeanToJSONObject(OutilBean elt, Locale loc, List<OutilBean> associated) {
        JSONObject retour = new JSONObject();
        retour.put("id", elt.getId());
        retour.put("lib", elt.getLib(loc));
        retour.put("desc", elt.getDesc(loc));
        retour.put("compl", elt.getComplement(loc));
        retour.put("nbAssociatedMetrics", elt.getNbMetricsAssociated());
        retour.put("associated", associated.contains(elt));
        return retour;
    }
}
