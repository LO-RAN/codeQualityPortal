/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveActionPlanUnitInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String id = request.getParameter("id");
        boolean success = false;
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        if (id != null && !"".equals(id)) {
            ActionPlanUnit apu = ActionPlanSvc.getInstance().retrieveActionPlanUnitById(id);
            if (apu != null) {
                success = true;
                retour.put("id", apu.getId());
                retour.put("nbApu", apu.getNbUnits());
                this.addLanguagesFieldsToJSON(apu, retour, new String[]{"lib"}, resources, loc);
            } else {
                code = MessagesCodes.DATABASE_ERROR;
            }
        } else {
            retour.put("id", "");
            retour.put("nbApu", 1);
            this.addLanguagesFieldsToJSON(null, retour, new String[]{"lib"}, resources, loc);
            success = true;
        }

        if (!success) {
            mLog.error("no action plan unit retrieved for id " + id);
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
