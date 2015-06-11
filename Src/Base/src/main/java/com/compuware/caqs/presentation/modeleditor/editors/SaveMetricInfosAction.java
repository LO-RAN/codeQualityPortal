package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.OutilBean;
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
public class SaveMetricInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MetriqueDefinitionBean metric = this.metricBeanFromRequest(request);
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            this.fillDatesFromRequest(metric, request);
            retour = MetricSvc.getInstance().saveMetricBean(metric);
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.saveI18nInfosFromJSON(request, metric);
            }
        } else if ("delete".equals(action)) {
            retour = MetricSvc.getInstance().deleteMetricBean(metric.getId());
            if (retour == MessagesCodes.NO_ERROR) {
                retour = this.deleteI18nInfosFromJSON(request, metric);
            }
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

    private MetriqueDefinitionBean metricBeanFromRequest(HttpServletRequest request) {
        MetriqueDefinitionBean retour = new MetriqueDefinitionBean();
        retour.setId(request.getParameter("id"));
        String outilMet = request.getParameter("outilMet");
        if (outilMet != null) {
            OutilBean ob = new OutilBean();
            ob.setId(outilMet);
            retour.setOutil(ob);
        }
        return retour;
    }
}
