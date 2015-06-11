package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.domain.dataschemas.DialecteBean;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.DialecteSvc;

public class RetrieveAllDialectsListAction extends ExtJSAjaxAction {

    private JSONArray getElementChildrenListJSON(Collection<DialecteBean> elts) {
        int nbElts = elts.size();
        JSONObject[] objects = new JSONObject[nbElts];
        int i = 0;
        for (DialecteBean et : elts) {
            objects[i] = new JSONObject();
            objects[i].put("id", et.getId());
            objects[i].put("lib", et.getLib());
            i++;
        }
        return JSONArray.fromObject(objects);
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        Collection<DialecteBean> elts = DialecteSvc.getInstance().retrieveAllDialectes();
        JSONArray array = this.getElementChildrenListJSON(elts);
        this.putArrayIntoObject(array, obj);
        return obj;
    }
}
