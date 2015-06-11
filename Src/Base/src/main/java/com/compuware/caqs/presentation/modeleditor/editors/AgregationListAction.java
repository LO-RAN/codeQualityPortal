package com.compuware.caqs.presentation.modeleditor.editors;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.Agregation;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationAvg;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationAvgAll;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationAvgExclus;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationAvgExclusSeuil;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationAvgWeightByScore;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationExclus;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationMultiSeuil;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import java.util.Locale;
import org.apache.struts.util.MessageResources;

public class AgregationListAction extends ExtJSAjaxAction {

    private static final Agregation[] agregations = new Agregation[]{
        new AgregationAvg(),
        new AgregationAvgAll(),
        new AgregationAvgExclus(),
        new AgregationAvgExclusSeuil(),
        new AgregationAvgWeightByScore(),
        new AgregationExclus(),
        new AgregationMultiSeuil()
    };

    private JSONArray getElementChildrenListJSON(MessageResources resources, Locale loc) {
        JSONObject[] objects = new JSONObject[agregations.length];
        for (int i=0; i<agregations.length; i++) {
            objects[i] = new JSONObject();
            objects[i].put("id", agregations[i].getId());
            objects[i].put("lib", agregations[i].getLib(resources, loc));
        }
        return JSONArray.fromObject(objects);
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        JSONArray array = this.getElementChildrenListJSON(RequestUtil.getResources(request), RequestUtil.getLocale(request));
        this.putArrayIntoObject(array, obj);
        return obj;
    }
}
