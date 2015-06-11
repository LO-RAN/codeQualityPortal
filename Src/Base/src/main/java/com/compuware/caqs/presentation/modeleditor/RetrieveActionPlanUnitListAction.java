package com.compuware.caqs.presentation.modeleditor;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveActionPlanUnitListAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        Locale loc = RequestUtil.getLocale(request);
        
        List<ActionPlanUnit> collection = ActionPlanSvc.getInstance().retrieveAllActionPlanUnits();


        List<JSONObject> liste = new ArrayList<JSONObject>();
        JSONObject obj = null;
        for (ActionPlanUnit apu : collection) {
            obj = new JSONObject();
            obj.put("id", apu.getId());
            obj.put("lib", apu.getLib(loc));
            liste.add(obj);
        }

        this.putArrayIntoObject(JSONArray.fromObject(liste.toArray(new JSONObject[0])), retour);

        return retour;
    }
}
