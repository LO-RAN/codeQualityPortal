package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.FactorSvc;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveTreeMapGoalsList extends ExtJSAjaxAction {

    private JSONArray getTreeMapMetrics(String idDomain,
            Locale loc, HttpServletRequest request) {
        List<ElementBean> elements = (List<ElementBean>) request.getSession().getAttribute("allEAsFor" + idDomain);
        List<FactorBean> factors = FactorSvc.getInstance().retrieveAllFactorsForElements(elements);
        
        JSONObject[] objects = new JSONObject[factors.size() + 1];
        //tous les objectifs
        MessageResources resources = RequestUtil.getResources(request);
        objects[0] = new JSONObject();
        objects[0].put("idFac", Constants.ALL_FACTORS);
        objects[0].put("libFac",
                resources.getMessage(loc, "caqs.domainsynthese.treemap.all_factors"));

        //les objectifs un par un
        int i = 1;
        for (FactorBean fb : factors) {
            objects[i] = new JSONObject();
            objects[i].put("idFac", fb.getId());
            objects[i].put("libFac", fb.getLib(loc));
            i++;
        }

        return JSONArray.fromObject(objects);
    }

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String idDomain = request.getParameter("idDomain");
        JSONArray array =  this.getTreeMapMetrics(idDomain,
                RequestUtil.getLocale(request), request);
        JSONObject retour = new JSONObject();
        this.putArrayIntoObject(array, retour);
        return retour;
    }
}
