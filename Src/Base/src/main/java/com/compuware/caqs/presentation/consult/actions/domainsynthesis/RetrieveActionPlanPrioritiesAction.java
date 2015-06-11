package com.compuware.caqs.presentation.consult.actions.domainsynthesis;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
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
public class RetrieveActionPlanPrioritiesAction  extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        
        JSONArray array = new JSONArray();
        for(ActionPlanPriority priority : ActionPlanPriority.getValidValues()) {
            JSONObject obj = new JSONObject();
            obj.put("id", priority.toString());
            obj.put("lib", priority.toString(request));
            array.add(obj);
        }
        
        return this.putArrayIntoObject(array, null);
    }

}
