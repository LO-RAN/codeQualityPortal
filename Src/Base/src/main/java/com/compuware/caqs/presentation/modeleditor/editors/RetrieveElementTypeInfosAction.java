/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.modeleditor.ElementTypeBean;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementTypeSvc;
import java.lang.String;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
public class RetrieveElementTypeInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String id = request.getParameter("id");
        boolean success = false;
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        if (id != null && !"".equals(id)) {
            ElementTypeBean et = ElementTypeSvc.getInstance().retrieveElementTypeById(id);
            if (et != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(resources.getMessage(loc, "caqs.dateFormat"));
                success = true;
                retour.put("id", et.getId());
                this.addTimestamps(et, retour, resources, loc);
                retour.put("hasSource", et.hasSource());
                retour.put("isFile", et.isFile());
                retour.put("nbCriterionsAssociated", et.getNbCriterionsAssociated());
                this.addLanguagesFieldsToJSON(et, retour, new String[]{"lib", "desc", "compl"}, resources, loc);
            } else {
                code = MessagesCodes.DATABASE_ERROR;
            }
        } else {
            retour.put("id", "");
            this.addTimestamps(null, retour, resources, loc);
            retour.put("hasSource", "false");
            retour.put("isFile", "false");
            retour.put("nbCriterionsAssociated", 0);
            this.addLanguagesFieldsToJSON(null, retour, new String[]{"lib", "desc", "compl"}, resources, loc);
            success = true;
        }

        if (!success) {
            mLog.error("no element type retrieved for id " + id);
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
