package com.compuware.caqs.presentation.common.projecttree;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;

public class BaselineList extends ExtJSAjaxAction {

    private JSONArray getElementChildrenListJSON(Collection<BaselineBean> elts, ResourceBundle resources) {
        JSONObject[] objects = new JSONObject[elts.size()];
        int i = 0;
        String datePattern = resources.getString("caqs.dateFormat.withHour");
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        for (Iterator<BaselineBean> it = elts.iterator(); it.hasNext(); i++) {
            BaselineBean bean = it.next();
            objects[i] = new JSONObject();
            objects[i].put("idBaseline", bean.getId());
            objects[i].put("libBaseline", bean.getLib());
            objects[i].put("lastDMaj", sdf.format(bean.getDmaj()));
        }
        return JSONArray.fromObject(objects);
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);

        String idPro = request.getParameter("id_pro");

        if (idPro != null) {
            BaselineSvc baselineSvc = BaselineSvc.getInstance();
            Collection<BaselineBean> baselineColl = baselineSvc.retrieveValidBaselinesByProjectId(idPro);

            if (baselineColl != null) {
                JSONArray array = this.getElementChildrenListJSON(baselineColl, resources);
                this.putArrayIntoObject(array, obj);
            }

        }
        return obj;
    }
}
