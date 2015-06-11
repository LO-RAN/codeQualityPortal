package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class SaveActionPlanUnitInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        ActionPlanUnit apu = this.actionPlanUnitBeanFromRequest(request);
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            retour = ActionPlanSvc.getInstance().saveActionPlanUnit(apu);
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.saveI18nInfosFromJSON(request, apu);
            }
        } else if ("delete".equals(action)) {
            retour = ActionPlanSvc.getInstance().deleteActionPlanUnitBean(apu.getId());
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.deleteI18nInfosFromJSON(request, apu);
            }
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

    private ActionPlanUnit actionPlanUnitBeanFromRequest(HttpServletRequest request) {
        ActionPlanUnit retour = new ActionPlanUnit(request.getParameter("id"));
        retour.setNbUnits(RequestUtil.getIntParam(request, "nbApu", 1));
        return retour;
    }
}
