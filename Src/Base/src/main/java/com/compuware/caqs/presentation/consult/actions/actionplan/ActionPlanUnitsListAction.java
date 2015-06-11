package com.compuware.caqs.presentation.consult.actions.actionplan;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import java.util.List;
import java.util.Locale;

public class ActionPlanUnitsListAction extends ExtJSAjaxAction {

    private JSONArray getElementChildrenListJSON(Collection<ActionPlanUnit> elts,
            Locale loc) {
        JSONObject[] objects = new JSONObject[elts.size()];
        int i = 0;
        for (ActionPlanUnit apu : elts) {
            objects[i] = new JSONObject();
            objects[i].put("id_apu", apu.getId());
            objects[i].put("lib_apu", apu.getLib(loc));
            objects[i].put("nb_apu", apu.getNbUnits());
            objects[i].put("display_field", apu.getNbUnits()+" "+apu.getLib(loc));
            i++;
        }
        return JSONArray.fromObject(objects);
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        List<ActionPlanUnit> elts = ActionPlanSvc.getInstance().retrieveAllActionPlanUnits();
        JSONArray array = this.getElementChildrenListJSON(elts, RequestUtil.getLocale(request));
        this.putArrayIntoObject(array, obj);
        return obj;
    }
}
