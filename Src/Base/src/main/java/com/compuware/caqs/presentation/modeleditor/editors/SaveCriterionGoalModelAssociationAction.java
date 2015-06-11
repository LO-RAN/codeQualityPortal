package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
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
public class SaveCriterionGoalModelAssociationAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        String idCrit = request.getParameter("idCrit");
        String idUsa = request.getParameter("modelId");
        String goalId = request.getParameter("goalId");
        retour = CriterionSvc.getInstance().associateCriterionToGoalAndModel(idCrit, goalId, idUsa);
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

}
