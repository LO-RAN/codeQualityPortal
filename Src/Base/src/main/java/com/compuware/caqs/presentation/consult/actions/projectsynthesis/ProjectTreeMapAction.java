package com.compuware.caqs.presentation.consult.actions.projectsynthesis;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.treemap.TreeMapElementBean;
import com.compuware.caqs.domain.dataschemas.treemap.TreeMapMetrics;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.TreeMapSvc;
import java.util.ArrayList;
import java.util.List;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import net.sf.json.JSONArray;
import org.apache.struts.util.MessageResources;

public class ProjectTreeMapAction extends ExtJSAjaxAction {

    private double getValue(TreeMapElementBean elt, TreeMapMetrics met) {
        double retour = 0.0;
        if (TreeMapMetrics.ALL_CODE.equals(met)) {
            retour = TreeMapSvc.getInstance().getProjectAllCodeValue(elt);
        } else if (TreeMapMetrics.IFPUG.equals(met)) {
            retour = TreeMapSvc.getInstance().getProjectIFPUG(elt);
        } else if (TreeMapMetrics.NB_ELTS_FILE.equals(met)) {
            retour = TreeMapSvc.getInstance().getProjectNbFileElements(elt);
        } else if (TreeMapMetrics.ACTION_PLAN_COST.equals(met)) {
            retour = TreeMapSvc.getInstance().getProjectActionPlansCosts(elt);
        }/* else if (TreeMapMetrics.AVG_VG.equals(met)) {
        retour = TreeMapSvc.getInstance().getAverageVGValue(elt, TREE_MAP_LEAF);
        }*/
        return retour;
    }

    private JSONObject getJSONInfos(TreeMapElementBean eb,
            TreeMapMetrics met, String libFac, Locale loc, MessageResources resources) {
        JSONObject retour = null;
        double mark = eb.getMark();
        if (eb != null && mark > 0.0) {
            retour = new JSONObject();
            retour.put("id", eb.getId());
            retour.put("name", eb.getLib());
            List<JSONObject> children = new ArrayList<JSONObject>();
            for (TreeMapElementBean child : eb.getChildren()) {
                JSONObject c = this.getJSONInfos(child, met,
                        libFac, loc, resources);
                if(c != null) {
                    children.add(c);
                }
            }
            JSONArray array = JSONArray.fromObject(children.toArray(new JSONObject[children.size()]));
            retour.put("children", array);

            JSONObject data = new JSONObject();
            data.put("$area", this.getValue(eb, met));
            data.put("$color", mark);
            data.put("scoreLabel", libFac);
            data.put("areaLabel", resources.getMessage(loc, "caqs.domainsynthese.treemap." + met));
            retour.put("id", eb.getId());
            retour.put("data", data);
        }
        return retour;
        /*JSONObject retour = null;
        double mark = eb.getMark();
        if (eb != null && mark > 0.0) {
            retour = new JSONObject();
            retour.put("id", eb.getId());

            StringBuffer nameBf = new StringBuffer((new ElementType(eb.getTypeElt())).getLib(loc));
            if (eb.getLib() != null) {
                nameBf.append(" : ");
                nameBf.append(eb.getLib());
            }
            retour.put("name", nameBf.toString());

            List<JSONObject> children = new ArrayList<JSONObject>();
            for (TreeMapElementBean child : eb.getChildren()) {
                JSONObject c = this.getJSONInfos(child, met,
                        libFac, loc, resources);
                if (c != null) {
                    children.add(c);
                }
            }
            JSONArray array = JSONArray.fromObject(children.toArray(new JSONObject[0]));
            retour.put("children", array);

            retour.put("id", eb.getId());

            retour.put("markLabel", libFac);

            retour.put("valueLabel", resources.getMessage(loc, "caqs.domainsynthese.treemap." +
                    met));

            JSONObject[] datas = new JSONObject[2];
            JSONObject value = new JSONObject();
            value.put("value", this.getValue(eb, met));
            value.put("key", "value");
            datas[0] = value;

            JSONObject jsonMark = new JSONObject();
            jsonMark.put("value", mark);
            jsonMark.put("key", "mark");
            datas[1] = jsonMark;
            JSONArray datasArray = JSONArray.fromObject(datas);
            retour.put("data", datasArray);
        }
        return retour;*/
    }

    private JSONObject getTreeMap(ElementBean eltBean, TreeMapMetrics met,
            String idFac, String idUser,
            Locale loc, MessageResources resources) {
        JSONObject retour = null;
        TreeMapElementBean eb = TreeMapSvc.getInstance().retrieveProjectMarkedTree(
                eltBean.getId(), eltBean.getBaseline(), idFac, idUser);

        if (eb != null) {
            String libFac = "";
            if (Constants.ALL_FACTORS.equals(idFac)) {
                libFac = resources.getMessage(loc, "caqs.domainsynthese.treemap.avgmark");
            } else {
                FactorBean fb = new FactorBean();
                fb.setId(idFac);
                libFac = fb.getLib(loc);
            }
            retour = this.getJSONInfos(eb, met, libFac, loc, resources);
        }

        return retour;
    }

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String idFac = request.getParameter("idFac");
        String met = request.getParameter("met");
        JSONObject obj = null;
        if (eltBean != null && met != null) {
            obj = this.getTreeMap(eltBean, TreeMapMetrics.valueOf(met),
                    idFac,
                    RequestUtil.getConnectedUserId(request),
                    RequestUtil.getLocale(request),
                    RequestUtil.getResources(request));
        }

        return obj;
    }
}
