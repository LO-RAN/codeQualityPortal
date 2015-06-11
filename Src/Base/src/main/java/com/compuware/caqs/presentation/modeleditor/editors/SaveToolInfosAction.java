package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
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
public class SaveToolInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        OutilBean tool = this.outilsBeanFromRequest(request);
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            retour = OutilsSvc.getInstance().saveOutilsBean(tool);
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.saveI18nInfosFromJSON(request, tool);
            }
        } else if ("delete".equals(action)) {
            retour = OutilsSvc.getInstance().deleteOutilsBean(tool.getId());
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.deleteI18nInfosFromJSON(request, tool);
            }
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

    private OutilBean outilsBeanFromRequest(HttpServletRequest request) {
        OutilBean retour = new OutilBean();
        retour.setId(request.getParameter("id"));
        return retour;
    }
}
