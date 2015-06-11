package com.compuware.caqs.presentation.consult.actions.actionplan;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.comparator.ActionPlanCriterionBeanComparator;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import java.util.Collections;

public class ActionPlanCriterionList extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 5);
        JSONObject root = new JSONObject();

        ElementBean ea = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        ApplicationEntityActionPlanBean actionPlanBean = (ApplicationEntityActionPlanBean) ActionPlanSvc.getInstance().getCompleteActionPlan(ea, ea.getBaseline().getId(), true, request);
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions = actionPlanBean.getElementsWithProblematicElement();

        ActionPlanElementBeanCollection<ActionPlanCriterionBean> filteredCriterions = (ActionPlanElementBeanCollection<ActionPlanCriterionBean>) request.getSession().getAttribute("FilteredActionPlanCriteres");
        if (filteredCriterions == null) {
            filteredCriterions = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(criterions);
            request.getSession().setAttribute("FilteredActionPlanCriteres", filteredCriterions);
        }

        FactorBeanCollection selectedFactors = (FactorBeanCollection) request.getSession().getAttribute("factors");
        String idFact = request.getParameter("factor");
        if (idFact != null && !"".equals(idFact)) {
            boolean checked = RequestUtil.getBooleanParam(request, "checked", true);
            if (checked) {
                FactorBean fb = new FactorBean();
                fb.setId(idFact);
                selectedFactors.add(fb);
                filteredCriterions.clear();
                filteredCriterions.addAll(criterions);
            } else {
                for (Iterator<FactorBean> it = selectedFactors.iterator(); it.hasNext();) {
                    FactorBean fb = it.next();
                    if (fb.getId().equals(idFact)) {
                        it.remove();
                        break;
                    }
                }
            }
            this.filterCriterions(filteredCriterions, selectedFactors);
        }

        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);

        if (filteredCriterions != null) {
            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean asc = "asc".equalsIgnoreCase(sens.toLowerCase());
                Collections.sort(filteredCriterions, new ActionPlanCriterionBeanComparator(RequestUtil.getLocale(request), sort, asc));
            }

            Locale loc = RequestUtil.getLocale(request);

            int size = filteredCriterions.size();
            int gridSize = size;
            List<JSONObject> l = new ArrayList<JSONObject>();
            for (int i = startIndex; i < (startIndex + limitIndex) && i < size; i++) {
                ActionPlanCriterionBean elt = filteredCriterions.get(i);
                if (this.isCriterionUsedByFactors(elt, selectedFactors)) {
                    JSONObject o = this.elementCriterionBeanToJSONObject(elt, loc, resources, ea);
                    l.add(o);
                } else {
                    limitIndex++;
                }
            }
            root.put("totalCount", gridSize);
            root.put("actionPlanComment", actionPlanBean.getActionPlanComment());

            root.put("criteres", l.toArray(new JSONObject[0]));
        }

        return root;
    }

    private JSONObject elementCriterionBeanToJSONObject(ActionPlanCriterionBean elt, Locale loc, ResourceBundle resources, ElementBean eltBean) {
        JSONObject retour = new JSONObject();
        retour.put("id", elt.getId());
        retour.put("lib", elt.getInternationalizableProperties().getLib(loc));
        retour.put("desc", elt.getInternationalizableProperties().getDesc(loc));
        retour.put("priority", elt.getPriority());
        retour.put("compl", elt.getInternationalizableProperties().getComplement(loc));
        retour.put("tendance", elt.getTendance());
        retour.put("idTelt", elt.getTypeElt());
        retour.put("comment", elt.getComment());
        retour.put("iconImgClass", (elt.getComment() == null || elt.getComment().length()
                == 0) ? "icon-comment-edit" : "icon-has-comment");
        retour.put("telt", ((new ElementType(elt.getTypeElt())).getLib(loc)));
        NumberFormat nfpct = StringFormatUtil.getMarkFormatter(loc);
        retour.put("note", nfpct.format(elt.getScore() - 0.005));
        String agg = "N/A";
        if (elt.getAggregation() != null) {
            agg = elt.getAggregation().getId();
        }
        retour.put("aggregation", agg);
        retour.put("nbElts", elt.getNumberElt());
        retour.put("indiceGravite", elt.getIndiceGravite());
        retour.put("repartition", "");
        retour.put("pct1", elt.getRepartition().getPct(0));
        retour.put("pct2", elt.getRepartition().getPct(1));
        retour.put("pct3", elt.getRepartition().getPct(2));
        retour.put("pct4", elt.getRepartition().getPct(3));
        retour.put("elementMaster", (elt.getElementMaster() == null) ? "" : elt.getElementMaster());
        String tooltipMessage = resources.getString("caqs.facteursynthese.popup");
        String format = MessageFormat.format(tooltipMessage,
                new Object[]{elt.getRepartition().getValue(0), elt.getRepartition().getPct(0)});
        retour.put("tooltip1", format);
        format = MessageFormat.format(tooltipMessage,
                new Object[]{elt.getRepartition().getValue(1), elt.getRepartition().getPct(1)});
        retour.put("tooltip2", format);
        format = MessageFormat.format(tooltipMessage,
                new Object[]{elt.getRepartition().getValue(2), elt.getRepartition().getPct(2)});
        retour.put("tooltip3", format);
        format = MessageFormat.format(tooltipMessage,
                new Object[]{elt.getRepartition().getValue(3), elt.getRepartition().getPct(3)});
        retour.put("tooltip4", format);
        retour.put("selected", elt.isCorrected());
        NumberFormat nf = StringFormatUtil.getMarkFormatter(loc);
        retour.put("cost", nf.format(elt.getCost()));
        retour.put("elementBeanId", eltBean.getId());//sert a comparaison avec l'element master
        String factorsList = "";
        FactorBean fb = new FactorBean();
        boolean first = true;
        for (String fId : elt.getFactors().keySet()) {
            fb.setId(fId);
            if (!first) {
                factorsList += ", ";
            }
            factorsList += fb.getLib(loc);
            first = false;
        }
        retour.put("factorsList", factorsList);
        return retour;
    }

    private void filterCriterions(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions, FactorBeanCollection selectedFactors) {
        for (Iterator<ActionPlanCriterionBean> it = criterions.iterator(); it.hasNext();) {
            ActionPlanCriterionBean criterion = it.next();
            if (!this.isCriterionUsedByFactors(criterion, selectedFactors)) {
                it.remove();
            }
        }
    }

    private boolean isCriterionUsedByFactors(ActionPlanCriterionBean criterion, FactorBeanCollection selectedFactors) {
        boolean retour = false;
        for (String fb : criterion.getFactors().keySet()) {
            if (selectedFactors.containsByObject(fb)) {
                retour = true;
                break;
            }
        }
        return retour;
    }
}
