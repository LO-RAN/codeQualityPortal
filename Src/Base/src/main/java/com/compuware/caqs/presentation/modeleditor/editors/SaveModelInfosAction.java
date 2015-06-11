package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.presentation.util.RequestUtil;
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
public class SaveModelInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        UsageBean model = this.modelBeanFromRequest(request);
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            this.fillDatesFromRequest(model, request);
            retour = ModelSvc.getInstance().saveModelBean(model);
            String ifpug = request.getParameter("ifpug");
            String idTelt = request.getParameter("ifpugIdTelt");
            if(ifpug!=null) {
                CaqsQualimetricModelManager man = CaqsQualimetricModelManager.getQualimetricModelManager(model.getId());
                if(man != null) {
                    man.saveIFPUGToDisk(ifpug, idTelt);
                }
            }
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.saveI18nInfosFromJSON(request, model);
            }
        } else if ("delete".equals(action)) {
            retour = ModelSvc.getInstance().deleteQualityModel(model.getId());
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.deleteI18nInfosFromJSON(request, model);
            }
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

    private UsageBean modelBeanFromRequest(HttpServletRequest request) {
        UsageBean retour = new UsageBean();
        retour.setId(request.getParameter("id"));
        retour.setLowerLimitLongRun(RequestUtil.getDoubleParam(request, "longRun", 20.0));
        retour.setLowerLimitMediumRun(RequestUtil.getDoubleParam(request, "mediumRun", 10.0));
        return retour;
    }
}
