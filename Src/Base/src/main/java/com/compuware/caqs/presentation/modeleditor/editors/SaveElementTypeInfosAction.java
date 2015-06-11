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
import java.sql.Timestamp;
import java.text.ParseException;
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
public class SaveElementTypeInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        ElementTypeBean et = this.elementTypeBeanFromRequest(request);
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            retour = ElementTypeSvc.getInstance().saveElementTypeBean(et);
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.saveI18nInfosFromJSON(request, et);
            }
        } else if ("delete".equals(action)) {
            retour = ElementTypeSvc.getInstance().deleteElementTypeBean(et.getId());
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.deleteI18nInfosFromJSON(request, et);
            }
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

    private ElementTypeBean elementTypeBeanFromRequest(HttpServletRequest request) {
        ElementTypeBean retour = new ElementTypeBean();
        this.fillDatesFromRequest(retour, request);
        retour.setHasSource(Boolean.parseBoolean(request.getParameter("hasSource")));
        retour.setIsFile(Boolean.parseBoolean(request.getParameter("isFile")));
        retour.setId(request.getParameter("id"));
        return retour;
    }
}
