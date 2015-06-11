package com.compuware.caqs.presentation.consult.actions.actionplan;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanFactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ProjectActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.comparator.ActionPlanElementBeanComparator;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import java.util.Collections;

public class ActionPlanGoalsList extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 5);
        JSONObject root = new JSONObject();

        ElementBean prjElt = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        //boolean readOnly = false;
/*
        Boolean bReadOnly = (Boolean) request.getSession().getAttribute(Constants.ACTION_PLAN_READ_ONLY);
        if (bReadOnly != null) {
            readOnly = bReadOnly.booleanValue();
        }
*/
        ProjectActionPlanBean actionPlanBean = (ProjectActionPlanBean) ActionPlanSvc.getInstance().getCompleteActionPlan(prjElt, prjElt.getBaseline().getId(), true, request);
        ActionPlanElementBeanCollection<ActionPlanFactorBean> goals = actionPlanBean.getFactorsWithProblematicElements();
        /*if (readOnly) {
            goals = actionPlanBean.getCorrectedElements();
        }*/

        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            String sens = request.getParameter("dir");
            boolean asc = "asc".equalsIgnoreCase(sens.toLowerCase());
            Collections.sort(goals, new ActionPlanElementBeanComparator(RequestUtil.getLocale(request), sort, asc));
        }

        Locale loc = RequestUtil.getLocale(request);

        int size = goals.size();
        int gridSize = size;
        List<JSONObject> l = new ArrayList<JSONObject>();
        for (int i = startIndex; i < (startIndex + limitIndex) && i < size; i++) {
            ActionPlanFactorBean elt = goals.get(i);
            /*if (!readOnly || (readOnly && (elt.isCorrected() ||
                    !elt.getComment().equals("")))) {*/
                JSONObject o = this.elementFactorBeanToJSONObject(elt, loc, prjElt);
                l.add(o);
            /*} else {
                limitIndex++;
            }*/

        }
        root.put("totalCount", gridSize);
        root.put("actionPlanComment", actionPlanBean.getActionPlanComment());

        root.put("goals", l.toArray(new JSONObject[0]));

        return root;
    }

    private JSONObject elementFactorBeanToJSONObject(ActionPlanFactorBean elt, Locale loc, ElementBean elementBean) {
        JSONObject retour = new JSONObject();
        retour.put("id", elt.getId());
        retour.put("lib", elt.getInternationalizableProperties().getLib(loc));
        retour.put("desc", elt.getInternationalizableProperties().getDesc(loc));
        retour.put("priority", elt.getPriority());
        retour.put("compl", elt.getInternationalizableProperties().getComplement(loc));
        retour.put("tendance", elt.getTendance());
        retour.put("comment", elt.getComment());
        NumberFormat nfpct = StringFormatUtil.getMarkFormatter(loc);
        retour.put("note", nfpct.format(elt.getScore() - 0.005));
        retour.put("indiceGravite", elt.getIndiceGravite());
        retour.put("repartition", "");
        retour.put("selected", elt.isCorrected());
        retour.put("elementBeanId", elementBean.getId());//sert a comparaison avec l'element master
        retour.put("elementMaster", (elt.getElementMaster()==null)?"":elt.getElementMaster());
        retour.put("iconImgClass", (elt.getComment()==null || elt.getComment().length()==0) ? "icon-comment-edit" : "icon-has-comment");
        return retour;
    }
}
