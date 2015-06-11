package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.CriterionSvc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class SaveGoalCriterionModificationAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        boolean success = false;
        String modelId = request.getParameter("modelId");
        String idCrit = request.getParameter("idCrit");
        if (modelId != null && idCrit!=null) {
            if ("saveWeight".equals(action)) {
                String goalId = request.getParameter("goalId");
                String sWeight = request.getParameter("weight");
                if (goalId != null && idCrit != null && sWeight != null) {
                    try {
                        int weight = Integer.parseInt(sWeight);
                        retour = CriterionSvc.getInstance().updateCriterionWeightForGoal(idCrit, goalId, modelId, weight);
                        success = (retour == MessagesCodes.NO_ERROR);
                    } catch (NumberFormatException exc) {
                        mLog.error(exc);
                    }
                }
            } else if ("saveTelt".equals(action)) {
                String idTelt = request.getParameter("idTelt");
                if (idCrit != null && idTelt != null) {
                    retour = CriterionSvc.getInstance().updateCriterionTEltForModel(idCrit, modelId, idTelt);
                    success = (retour == MessagesCodes.NO_ERROR);
                }
            }
        }
        if (!success) {
            retour = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }
}
