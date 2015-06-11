package com.compuware.caqs.presentation.modeleditor;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.presentation.modeleditor.common.RetrieveSearchScreenResultsAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.CriterionSvc;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveCriterionSearchScreenResultsAction extends RetrieveSearchScreenResultsAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String id = this.retrieveSearchParameter("id", true, request);
        String lib = this.retrieveSearchParameter("lib", true, request);
        String goal = this.retrieveSearchParameter("goal", true, request);
        String model = this.retrieveSearchParameter("model", true, request);

        Locale loc = RequestUtil.getLocale(request);

        List<CriterionDefinition> collection = CriterionSvc.getInstance().retrieveCriterionsByIdLibGoalModel(id, lib, goal, model, loc);
        this.sortBy(collection, loc, request);
        MessagesCodes codeRetour = MessagesCodes.NO_ERROR;
        if (collection == null) {
            codeRetour = MessagesCodes.DATABASE_ERROR;
        } else {
            retour.put("totalCount", collection.size());
            int startIndex = RequestUtil.getIntParam(request, "start", 0);
            int limitIndex = RequestUtil.getIntParam(request, "limit", 10);
            List<JSONObject> l = new ArrayList<JSONObject>();
            for (int i = startIndex; i < (startIndex + limitIndex) && i <
                    collection.size(); i++) {
                CriterionDefinition criterion = collection.get(i);
                JSONObject obj = this.convertToJSONObject(criterion, loc);
                l.add(obj);
            }
            retour.put("datas", l.toArray(new JSONObject[0]));
        }
        this.fillJSONObjectWithReturnCode(retour, codeRetour);
        return retour;
    }

    private JSONObject convertToJSONObject(CriterionDefinition criterion, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("id", criterion.getId());
        retour.put("lib", criterion.getLib(loc));
        retour.put("desc", criterion.getDesc(loc));
        return retour;
    }
}
