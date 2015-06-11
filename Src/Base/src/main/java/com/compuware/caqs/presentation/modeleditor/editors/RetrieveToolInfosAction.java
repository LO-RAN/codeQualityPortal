/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.OutilsSvc;
import java.lang.String;
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
public class RetrieveToolInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String id = request.getParameter("id");
        boolean success = false;
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        if (id != null && !"".equals(id)) {
            OutilBean tool = OutilsSvc.getInstance().retrieveToolById(id);
            if (tool != null) {
                success = true;
                retour.put("id", tool.getId());
                retour.put("nbMetricsAssociated", tool.getNbMetricsAssociated());
                retour.put("nbModelsAssociated", tool.getNbModelsUsingIt());
                this.addLanguagesFieldsToJSON(tool, retour, new String[]{"lib", "desc", "compl"}, resources, loc);
            } else {
                code = MessagesCodes.DATABASE_ERROR;
            }
        } else {
            retour.put("id", "");
            retour.put("nbMetricsAssociated", 0);
            retour.put("nbModelsAssociated", 0);
            this.addLanguagesFieldsToJSON(null, retour, new String[]{"lib", "desc", "compl"}, resources, loc);
            success = true;
        }

        if (!success) {
            mLog.error("no tool retrieved for id " + id);
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
