package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.Agregation;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import java.util.ArrayList;
import java.util.List;
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
public class SaveAgregationsForCriterionAndModelAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String critId = request.getParameter("critId");
        String modelId = request.getParameter("modelId");
        String agregations = request.getParameter("agregations");
        if (critId != null && modelId != null && !"".equals(critId) &&
                !"".equals(modelId) && agregations!=null && !"".equals(agregations)) {
            JSONArray array = JSONArray.fromObject(agregations);
            List<Agregation> liste = new ArrayList<Agregation>();
            for(int i=0; i<array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Agregation agg = this.convertJSONToAgregation(obj);
                if(agg!=null) {
                    liste.add(agg);
                }
            }
            CaqsQualimetricModelManager manager = CaqsQualimetricModelManager.getQualimetricModelManager(modelId);
            if (manager != null) {
                manager.setCritere(critId);
                manager.saveAgregationsToDisk(liste);
            }
        } else {
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }

    public Agregation convertJSONToAgregation(JSONObject obj) {
        Agregation retour = Agregation.getAgregationFromId(obj.getString("id"));
        if(retour != null) {
            retour.setParamsFromJSON(obj);
            retour.setIdTelt(obj.getString("elementType"));
        }
        return retour;
    }
}
