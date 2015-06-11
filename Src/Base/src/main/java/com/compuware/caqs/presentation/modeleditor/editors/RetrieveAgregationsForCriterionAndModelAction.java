package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.Agregation;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.util.MessageResources;

public class RetrieveAgregationsForCriterionAndModelAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject root = new JSONObject();
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        String modelId = request.getParameter("modelId");
        String idCrit = request.getParameter("critId");
        List<JSONObject> l = new ArrayList<JSONObject>();
        List<Agregation> agregations = null;
        boolean firstLoad = true;
        String sFirstLoad = request.getParameter("firstLoad");
        if (sFirstLoad != null) {
            firstLoad = Boolean.parseBoolean(sFirstLoad);
        }
        if (idCrit != null && modelId != null) {
            agregations = (List<Agregation>) request.getSession().getAttribute("agregationsFor" +
                    modelId + idCrit);
            if (agregations == null || firstLoad) {
                CaqsQualimetricModelManager manager = CaqsQualimetricModelManager.getQualimetricModelManager(modelId);
                if (manager != null) {
                    if (manager.setCritere(idCrit)) {
                        agregations = manager.getAggregations();
                        request.getSession().setAttribute("agregationsFor" +
                                modelId + idCrit, agregations);
                    }
                }
            }

            for (Iterator<Agregation> it = agregations.iterator(); it.hasNext();) {
                Agregation elt = it.next();
                JSONObject o = this.agregationFormToJSONObject(elt, resources, loc);
                l.add(o);
            }
        }
        root.put("elements", l.toArray(new JSONObject[0]));

        return root;
    }

    private JSONObject agregationFormToJSONObject(Agregation elt, MessageResources resources, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("id", elt.getId());
        retour.put("lib", elt.getLib(resources, loc));
        retour.put("elementType", elt.getIdTelt());
        Set<Map.Entry<String, String>> paramSet = elt.getParamsSet();
        for(Map.Entry<String, String> param : paramSet) {
            retour.put(param.getKey(), param.getValue());
        }
        return retour;
    }
}
