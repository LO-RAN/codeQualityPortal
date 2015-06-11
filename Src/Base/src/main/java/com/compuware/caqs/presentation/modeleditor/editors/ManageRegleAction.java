package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.service.MetricSvc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class ManageRegleAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        String action = request.getParameter("action");
        String idMet = request.getParameter("idMet");
        String idUsa = request.getParameter("modelId");
        String idCrit = request.getParameter("critId");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            retour = MetricSvc.getInstance().createRegle(idMet, idUsa, idCrit);
        } else if ("delete".equals(action)) {
            retour = MetricSvc.getInstance().deleteRegle(idMet, idUsa, idCrit);
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

}
