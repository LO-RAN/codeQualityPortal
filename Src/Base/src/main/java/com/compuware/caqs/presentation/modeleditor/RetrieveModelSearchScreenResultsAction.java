package com.compuware.caqs.presentation.modeleditor;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.presentation.modeleditor.common.RetrieveSearchScreenResultsAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ModelSvc;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
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
public class RetrieveModelSearchScreenResultsAction extends RetrieveSearchScreenResultsAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String id = this.retrieveSearchParameter("id", true, request);
        String lib = this.retrieveSearchParameter("lib", true, request);

        Locale loc = RequestUtil.getLocale(request);

        List<UsageBean> collection = ModelSvc.getInstance().retrieveModelsByIdAndLib(id, lib, loc);
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
                UsageBean usage = collection.get(i);
                JSONObject obj = this.convertToJSONObject(usage, loc);
                l.add(obj);
            }
            retour.put("datas", l.toArray(new JSONObject[0]));
        }
        this.fillJSONObjectWithReturnCode(retour, codeRetour);
        return retour;
    }

    private JSONObject convertToJSONObject(UsageBean usage, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("id", usage.getId());
        retour.put("lib", usage.getLib(loc));
        retour.put("desc", usage.getDesc(loc));
        boolean hasError = false;
        CaqsQualimetricModelManager model = CaqsQualimetricModelManager.getQualimetricModelManager(usage.getId());
        if(model != null) {
            hasError = model.hasAFormulaWithError();
        }
        retour.put("formulaWithError", hasError);
        return retour;
    }
}
