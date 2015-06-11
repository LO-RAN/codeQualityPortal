package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.FactorSvc;
import java.util.Locale;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RetrieveAssociatedGoalsAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        Locale loc = RequestUtil.getLocale(request);
        String modelId = request.getParameter("modelId");
        JSONObject root = new JSONObject();
        //ElementCriterionForm
        
        List<FactorBean> coll = FactorSvc.getInstance().retrieveGoalsListByModel(modelId);
        
        JSONArray array = new JSONArray();
        for (FactorBean factor : coll) {
            JSONObject o = this.factorBeanToJSONObject(factor, loc);
            array.add(o);
        }
        root.put("datas", array);
        return root;
    }

    private JSONObject factorBeanToJSONObject(FactorBean elt, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("id", elt.getId());
        retour.put("lib", elt.getLib(loc));
        retour.put("desc", elt.getDesc(loc));
        retour.put("compl", elt.getComplement(loc));
        return retour;
    }
}
