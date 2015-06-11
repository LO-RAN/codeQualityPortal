package com.compuware.caqs.presentation.modeleditor.common;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementTypeSvc;
import java.util.List;
import java.util.Locale;
import org.apache.struts.util.MessageResources;

public class ElementTypeListAction extends ExtJSAjaxAction {

    private JSONArray getElementChildrenListJSON(Collection<ElementType> elts,
            Locale loc, boolean addElementTypeAll, MessageResources resources) {
        int nbElts = (addElementTypeAll)?elts.size()+1:elts.size();
        JSONObject[] objects = new JSONObject[nbElts];
        if(addElementTypeAll) {
            objects[0] = new JSONObject();
            objects[0].put("id_telt", "ALL");
            objects[0].put("lib_telt", resources.getMessage(loc, "caqs.modeleditor.modelEdition.allElementTypes"));
        }
        int i = (addElementTypeAll)?1:0;
        for (ElementType et : elts) {
            objects[i] = new JSONObject();
            objects[i].put("id_telt", et.getId());
            objects[i].put("lib_telt", et.getLib(loc));
            i++;
        }
        return JSONArray.fromObject(objects);
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        boolean addElementTypeAll = RequestUtil.getBooleanParam(request, "addElementTypeAll", false);
        List<ElementType> elts = ElementTypeSvc.getInstance().retrieveAllElementTypes();
        JSONArray array = this.getElementChildrenListJSON(elts, RequestUtil.getLocale(request), addElementTypeAll, RequestUtil.getResources(request));
        this.putArrayIntoObject(array, obj);
        return obj;
    }
}
