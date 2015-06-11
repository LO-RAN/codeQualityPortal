package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.treemap.TreeMapMetrics;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
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
public class RetrieveTreeMapMetrics extends ExtJSAjaxAction {

    private JSONArray getTreeMapMetrics(MessageResources resources, Locale loc) {
        JSONArray retour = null;
        TreeMapMetrics[] tmm = TreeMapMetrics.values();
        if (tmm != null) {
            JSONObject[] objects = new JSONObject[tmm.length];
            for (int i = 0; i < tmm.length; i++) {
                objects[i] = new JSONObject();
                objects[i].put("idTMM", tmm[i].toString());
                objects[i].put("libTMM", resources.getMessage(loc,
                        "caqs.domainsynthese.treemap." + tmm[i].toString()));
            }

            retour = JSONArray.fromObject(objects);
        }
        return retour;
    }

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONArray retour = this.getTreeMapMetrics(RequestUtil.getResources(request),
                RequestUtil.getLocale(request));
        JSONObject obj = new JSONObject();
        this.putArrayIntoObject(retour, obj);
        return obj;
    }
}
