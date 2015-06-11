package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.FactorDefinitionBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.CriterionSvc;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveModelsGoalsForCriterionAction extends ExtJSGridAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject root = new JSONObject();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        String idCrit = request.getParameter("idCrit");
        Map<String, List<FactorDefinitionBean>> result = null;
        if (idCrit != null) {
            result = CriterionSvc.getInstance().retrieveAssociatedModelsAndGoalsForCriterion(idCrit);
        } else {
            retour = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        if (result != null) {
            Locale loc = RequestUtil.getLocale(request);
            int gridSize = result.size();
            List<JSONObject> l = new ArrayList<JSONObject>();
            for (Map.Entry<String, List<FactorDefinitionBean>> entry : result.entrySet()) {
                UsageBean model = new UsageBean(entry.getKey());
                List<FactorDefinitionBean> goals = entry.getValue();
                for (FactorDefinitionBean goal : goals) {
                    JSONObject obj = new JSONObject();
                    obj.put("modelId", model.getId());
                    obj.put("modelLib", model.getLib(loc));
                    obj.put("goalId", goal.getId());
                    obj.put("goalLib", goal.getLib(loc));
                    l.add(obj);
                }
            }
            root.put("totalCount", gridSize);
            root.put("elements", l.toArray(new JSONObject[0]));
        }
        this.fillJSONObjectWithReturnCode(root, retour);
        return root;
    }
}
