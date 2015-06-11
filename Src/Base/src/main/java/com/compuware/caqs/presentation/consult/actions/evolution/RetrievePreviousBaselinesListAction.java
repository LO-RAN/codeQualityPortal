package com.compuware.caqs.presentation.consult.actions.evolution;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.evolutions.EvolutionBaselineBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrievePreviousBaselinesListAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        MessagesCodes mc = MessagesCodes.CAQS_GENERIC_ERROR;

        JSONObject retour = new JSONObject();
        String baselineId = request.getParameter("currentBaselineId");
        String idElt = request.getParameter("idElt");
        JSONArray array = new JSONArray();

        if (baselineId != null && !"".equals(baselineId) && idElt!=null && !"".equals(idElt)) {
            ElementBean eltBean = ElementSvc.getInstance().retrieveElementById(idElt);
            BaselineBean currentBaseline = BaselineSvc.getInstance().getRealBaselineForEA(eltBean, baselineId);
            if (currentBaseline != null) {
                Collection<EvolutionBaselineBean> baselines = BaselineSvc.getInstance().retrieveAllPreviousValidBaseline(idElt,
                        currentBaseline);
                if (baselines != null) {
                    for (EvolutionBaselineBean baseline : baselines) {
                        JSONObject obj = new JSONObject();
                        obj.put("id", baseline.getId());
                        obj.put("lib", baseline.getLib());
                        obj.put("nb_crit", baseline.getNbCriterionsInActionsPlan());
                        array.add(obj);
                    }
                    mc = MessagesCodes.NO_ERROR;
                }

            }
        }

        this.putArrayIntoObject(array, retour);
        this.fillJSONObjectWithReturnCode(retour, mc);
        return retour;
    }
}
