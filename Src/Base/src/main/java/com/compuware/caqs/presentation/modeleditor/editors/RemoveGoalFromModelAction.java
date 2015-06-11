package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.CriterionSvc;
import com.compuware.caqs.service.ModelSvc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RemoveGoalFromModelAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        JSONObject obj = new JSONObject();
        String modelId = request.getParameter("modelId");
        String goalId = request.getParameter("goalId");
        if(goalId!=null && modelId!=null) {
            ModelSvc.getInstance().removeGoalFromModel(modelId, goalId);
        } else {
            retour = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }
}
