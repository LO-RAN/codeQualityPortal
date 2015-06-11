package com.compuware.caqs.presentation.modeleditor;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.modeleditor.LangueBean;
import com.compuware.caqs.domain.dataschemas.comparators.LangueBeanComparator;
import com.compuware.caqs.presentation.modeleditor.common.RetrieveSearchScreenResultsAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.LangueSvc;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveLangueSearchScreenResultsAction extends RetrieveSearchScreenResultsAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String id = this.retrieveSearchParameter("id", true, request);
        String lib = this.retrieveSearchParameter("lib", true, request);

        List<LangueBean> collection = LangueSvc.getInstance().retrieveLanguesByIdLib(id, lib);
        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            String sens = request.getParameter("dir");
            boolean desc = "desc".equals(sens.toLowerCase());
            LangueBeanComparator comparator = new LangueBeanComparator(sort,
                    desc);
            this.sort(collection, comparator);
        }
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
                LangueBean langue = collection.get(i);
                JSONObject obj = this.convertToJSONObject(langue);
                l.add(obj);
            }
            retour.put("datas", l.toArray(new JSONObject[0]));
        }
        this.fillJSONObjectWithReturnCode(retour, codeRetour);
        return retour;
    }

    private JSONObject convertToJSONObject(LangueBean langue) {
        JSONObject retour = new JSONObject();
        retour.put("id", langue.getId());
        retour.put("lib", langue.getLib());
        retour.put("desc", langue.getDesc());
        return retour;
    }
}
