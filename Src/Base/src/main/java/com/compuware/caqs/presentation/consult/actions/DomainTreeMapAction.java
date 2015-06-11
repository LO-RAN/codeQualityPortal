package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
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
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import net.sf.json.JSONArray;
import org.apache.struts.util.MessageResources;

public class DomainTreeMapAction extends ExtJSGridAjaxAction {

    private static final String TREE_MAP_LEAF = ElementType.PRJ;

    private double getValue(TreeMapElementBean elt, TreeMapMetrics met,
            List<ElementBean> eas) {
        double retour = 0.0;
        if (TreeMapMetrics.ALL_CODE.equals(met)) {
            retour = TreeMapSvc.getInstance().getAllCodeValue(elt, TREE_MAP_LEAF);
        } else if (TreeMapMetrics.IFPUG.equals(met)) {
            retour = TreeMapSvc.getInstance().getIFPUG(elt, TREE_MAP_LEAF, eas);
        } else if (TreeMapMetrics.NB_ELTS_FILE.equals(met)) {
            retour = TreeMapSvc.getInstance().getNbFileElements(elt, TREE_MAP_LEAF, eas);
        } else if (TreeMapMetrics.ACTION_PLAN_COST.equals(met)) {
            retour = TreeMapSvc.getInstance().getActionPlansCosts(elt, TREE_MAP_LEAF, eas);
        }/* else if (TreeMapMetrics.AVG_VG.equals(met)) {
            retour = TreeMapSvc.getInstance().getAverageVGValue(elt, TREE_MAP_LEAF);
        }*/
        return retour;
    }

    private JSONObject getJSONInfos(TreeMapElementBean eb, List<ElementBean> eas,
            TreeMapMetrics met, String libFac, Locale loc, MessageResources resources) {
        JSONObject retour = null;
        double mark = eb.getMark();
        if (eb != null && mark > 0.0) {
            retour = new JSONObject();
            retour.put("id", eb.getId());

            if(ElementType.ENTRYPOINT.equals(eb.getId())) {
                retour.put("name", "<I>"+resources.getMessage(loc, "caqs.allProject.racine")+"</I>");
            } else {
                retour.put("name", eb.getLib());
            }

            List<JSONObject> children = new ArrayList<JSONObject>();
            for (TreeMapElementBean child : eb.getChildren()) {
                JSONObject c = this.getJSONInfos(child, eas, met,
                        libFac, loc, resources);
                if(c != null) {
                    children.add(c);
                }
            }
            JSONArray array = JSONArray.fromObject(children.toArray(new JSONObject[children.size()]));
            retour.put("children", array);

            JSONObject data = new JSONObject();
            data.put("$area", this.getValue(eb, met, eas));
            data.put("$color", mark);
            data.put("scoreLabel", libFac);
            data.put("areaLabel", resources.getMessage(loc, "caqs.domainsynthese.treemap." + met));
            retour.put("id", eb.getId());
            retour.put("data", data);
        }
        return retour;
    }

    private JSONObject getTreeMap(String rootId, TreeMapMetrics met,
            String idFac, List<ElementBean> eas, String idUser,
            Locale loc, MessageResources resources) {
        JSONObject retour = null;
        TreeMapElementBean eb = TreeMapSvc.getInstance().retrieveMarkedTree(
                rootId, TREE_MAP_LEAF, idFac, idUser);

        if (eb != null) {
            String libFac = "";
            if(Constants.ALL_FACTORS.equals(idFac)) {
                libFac = resources.getMessage(loc, "caqs.domainsynthese.treemap.avgmark");
            } else {
                FactorBean fb = new FactorBean();
                fb.setId(idFac);
                libFac = fb.getLib(loc);
            }
            retour = this.getJSONInfos(eb, eas, met, libFac, loc, resources);
        }

        return retour;
    }

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        String domainId = request.getParameter("domainId");
        String idFac = request.getParameter("idFac");
        String met = request.getParameter("met");
        JSONObject obj = null;
        if (domainId != null && met != null) {
            List<ElementBean> elements = (List<ElementBean>) request.getSession().getAttribute("allEAsFor" + domainId);
            obj = this.getTreeMap(domainId, TreeMapMetrics.valueOf(met),
                    idFac, elements,
                    RequestUtil.getConnectedUserId(request),
                    RequestUtil.getLocale(request),
                    RequestUtil.getResources(request));
        }

        return obj;
    }
}
