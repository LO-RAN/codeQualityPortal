package com.compuware.caqs.presentation.modeleditor;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.ModelSvc;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class DuplicateModelAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String usaSrc = request.getParameter("usaSrc");
        String usaTarget = request.getParameter("usaTarget");
        if (usaSrc != null && usaTarget != null && !"".equals(usaSrc) &&
                !"".equals(usaTarget)) {
            code = ModelSvc.getInstance().duplicateModel(usaSrc, usaTarget);
            if (MessagesCodes.NO_ERROR.equals(code)) {
                code = CaqsQualimetricModelManager.duplicateModel(usaSrc, usaTarget);
            }
        } else {
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
