package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.MetricSvc;
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
public class RetrieveMetricInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String id = request.getParameter("id");
        boolean success = false;
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        if (id != null && !"".equals(id)) {
            MetriqueDefinitionBean metric = MetricSvc.getInstance().retrieveMetricWithAssociationCountById(id);
            if (metric != null) {
                success = true;
                retour.put("id", metric.getId());
                if(metric.getOutil()!=null) {
                    retour.put("outilMet", metric.getOutil().getId());
                }
                this.addTimestamps(metric, retour, resources, loc);
                retour.put("nbCriterionsAssociated", metric.getNbCriterionsAssociated());
                this.addLanguagesFieldsToJSON(metric, retour, new String[]{"lib", "desc", "compl"}, resources, loc);
                if(MetricSvc.getInstance().isMetricUsedInIFPUGComputation(id)) {
                    retour.put("nbIFPUGAssociated", "1");
                }
            } else {
                code = MessagesCodes.DATABASE_ERROR;
            }
        } else {
            retour.put("id", "");
            retour.put("nbCriterionsAssociated", 0);
            this.addTimestamps(null, retour, resources, loc);
            this.addLanguagesFieldsToJSON(null, retour, new String[]{"lib", "desc", "compl"}, resources, loc);
            success = true;
        }

        if (!success) {
            mLog.error("no metric retrieved for id " + id);
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
