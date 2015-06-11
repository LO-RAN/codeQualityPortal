package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Collection;
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
public class RetrieveBaselinesForProjectAction  extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        String idPro = request.getParameter("idPro");
        JSONArray array = null;
        if(idPro != null) {
            BaselineSvc baselineSvc = BaselineSvc.getInstance();
        	Collection<BaselineBean> baselineCollection = baselineSvc.retrieveAllTerminatedBaselinesByProjectId(idPro);
            array = this.retrieveJSONArrayFromCollection(baselineCollection, request);
        }

        return this.putArrayIntoObject(array, null);
    }

    private JSONArray retrieveJSONArrayFromCollection(Collection<BaselineBean> baselines,
            HttpServletRequest request) {
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, loc);

        JSONObject[] objects = new JSONObject[baselines.size()];

        int i=0;
        for(BaselineBean baseline : baselines) {
            objects[i] = new JSONObject();
            objects[i].put("baselineId", baseline.getId());
            String lib = resources.getMessage(loc, "caqs.purgeBL.libelle",
                    baseline.getLib(), df.format(baseline.getDinst()));
            objects[i].put("baselineLib", lib);
            i++;
        }
        return JSONArray.fromObject(objects);
    }

}
