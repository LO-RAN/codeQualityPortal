package com.compuware.caqs.presentation.modeleditor;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.comparators.MetriqueDefinitionBeanComparator;
import com.compuware.caqs.presentation.modeleditor.common.RetrieveSearchScreenResultsAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.MetricSvc;
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
public class RetrieveMetricSearchScreenResultsAction extends RetrieveSearchScreenResultsAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String id = this.retrieveSearchParameter("id", true, request);
        String lib = this.retrieveSearchParameter("lib", true, request);
        String tool = this.retrieveSearchParameter("tool", false, request);

        Locale loc = RequestUtil.getLocale(request);

        List<MetriqueDefinitionBean> collection = MetricSvc.getInstance().retrieveMetricsByIdLibTool(id, lib, tool, loc);
        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            String sens = request.getParameter("dir");
            boolean desc = "desc".equals(sens.toLowerCase());
            MetriqueDefinitionBeanComparator comparator = new MetriqueDefinitionBeanComparator(desc,
                    sort, loc);
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
                MetriqueDefinitionBean metric = collection.get(i);
                JSONObject obj = this.convertToJSONObject(metric, loc);
                l.add(obj);
            }
            retour.put("datas", l.toArray(new JSONObject[0]));
        }
        this.fillJSONObjectWithReturnCode(retour, codeRetour);
        return retour;
    }

    private JSONObject convertToJSONObject(MetriqueDefinitionBean metric, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("id", metric.getId());
        retour.put("lib", metric.getLib(loc));
        retour.put("desc", metric.getDesc(loc));
        if(metric.getOutil() != null) {
            retour.put("tool", metric.getOutil().getLib(loc));
        }
        return retour;
    }
}
