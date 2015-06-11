package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.service.FactorSvc;
import com.compuware.caqs.service.OutilsSvc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class SaveGoalInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        FactorBean factor = this.factorBeanFromRequest(request);
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            this.fillDatesFromRequest(factor, request);
            retour = FactorSvc.getInstance().saveFactorBean(factor);
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.saveI18nInfosFromJSON(request, factor);
            }
        } else if ("delete".equals(action)) {
            retour = FactorSvc.getInstance().deleteFactorBean(factor.getId());
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.deleteI18nInfosFromJSON(request, factor);
            }
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

    private FactorBean factorBeanFromRequest(HttpServletRequest request) {
        FactorBean retour = new FactorBean();
        retour.setId(request.getParameter("id"));
        return retour;
    }
}
