package com.compuware.caqs.presentation.modeleditor;

import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.OutilsSvc;
import java.util.ArrayList;
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
public class RetrieveToolsListAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        boolean addAllToolsElement = true;
        String sAddAll = request.getParameter("addAllElement");
        if (sAddAll != null) {
            addAllToolsElement = Boolean.parseBoolean(sAddAll);
        }
        JSONObject retour = new JSONObject();
        Locale loc = RequestUtil.getLocale(request);
        MessageResources resources = RequestUtil.getResources(request);
        String modelId = request.getParameter("modelId");
        List<OutilBean> collection = null;
        if(modelId!=null) {
            collection = OutilsSvc.getInstance().retrieveOutilsByModelWithMetricCount(modelId);
        } else {
            collection = OutilsSvc.getInstance().retrieveTools();
        }


        List<JSONObject> liste = new ArrayList<JSONObject>();
        JSONObject obj = null;
        //on ajoute la possibilite de filtrer sur tous les outils
        if (addAllToolsElement) {
            obj = new JSONObject();
            obj.put("id", "%");
            obj.put("lib", resources.getMessage(loc, "caqs.modeleditor.allToolsCB"));
            liste.add(obj);
        }

        for (OutilBean outil : collection) {
            obj = new JSONObject();
            obj.put("id", outil.getId());
            obj.put("lib", outil.getLib(loc));
            liste.add(obj);
        }

        this.putArrayIntoObject(JSONArray.fromObject(liste.toArray(new JSONObject[0])), retour);

        return retour;
    }
}
