package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.domain.dataschemas.UsageBean;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ModelSvc;
import java.util.Locale;

public class RetrieveAllModelsListAction extends ExtJSAjaxAction {

    private JSONArray getElementChildrenListJSON(Collection<UsageBean> elts, Locale loc) {
        int nbElts = elts.size();
        JSONObject[] objects = new JSONObject[nbElts];
        int i = 0;
        for (UsageBean et : elts) {
            objects[i] = new JSONObject();
            objects[i].put("id", et.getId());
            objects[i].put("lib", et.getLib(loc));
            i++;
        }
        return JSONArray.fromObject(objects);
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        Collection<UsageBean> elts = ModelSvc.getInstance().retrieveAllModels();
        JSONArray array = this.getElementChildrenListJSON(elts, RequestUtil.getLocale(request));
        this.putArrayIntoObject(array, obj);
        return obj;
    }
}
