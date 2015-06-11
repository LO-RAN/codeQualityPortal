package com.compuware.caqs.presentation.consult.actions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionNoteRepartition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.comparators.CriterionBeanComparator;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.SyntheseObjectifsSvc;

/**
 * Liste des crit�res pour un objectif.
 * Pour ExtJS.
 * @author cwfr-dzysman
 */
public class FacteurSyntheseListAjaxAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 10);

        return this.doRetrieve(request, request.getSession(), startIndex, limitIndex);
    }

    private JSONObject doRetrieve(HttpServletRequest request,
            HttpSession session,
            int startIndex, int limitIndex) {
        ElementBean eaBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        SyntheseObjectifsSvc syntheseObjectifsSvc = SyntheseObjectifsSvc.getInstance();

        String idFact = request.getParameter("id_fact");

        FactorBean factorBean = retrieveFactorInfos(request, eaBean, syntheseObjectifsSvc);
        List<CriterionBean> criterions = retrieveEaSynthese(request, eaBean, syntheseObjectifsSvc);

        Map repartition = syntheseObjectifsSvc.retrieveCriterionNoteRepartition(eaBean, idFact);
        request.setAttribute("criterionNoteRepartition", repartition);


        JSONObject root = new JSONObject();
        int taille = 0;
        List<JSONObject> l = new ArrayList<JSONObject>();

        if (criterions != null && factorBean != null) {
            Locale loc = RequestUtil.getLocale(request);

            NumberFormat markFormatter = StringFormatUtil.getMarkFormatter(loc);

            NumberFormat weightFormatter = StringFormatUtil.getIntegerFormatter(loc);

            NumberFormat actionPlanCostFormatter = StringFormatUtil.getFormatter(loc, 2, 2);

            MessageResources messageResources = RequestUtil.getResources(request);

            taille = criterions.size();
            if (limitIndex == -1) {
                limitIndex = taille;
            }
            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean desc = "desc".equals(sens.toLowerCase());
                Collections.sort(criterions, new CriterionBeanComparator(desc, sort, repartition, loc));
            }

            for (int i = startIndex; i < (startIndex + limitIndex) && i <
                    taille; i++) {
                CriterionBean elt = criterions.get(i);
                JSONObject o = this.elementCriterionBeanToJSONObject(factorBean, elt, loc,
                        markFormatter, weightFormatter, actionPlanCostFormatter, eaBean, messageResources, repartition);
                l.add(o);
            }
            NumberFormat nf = StringFormatUtil.getMarkFormatter(loc);
            root.put("goalScore", nf.format(factorBean.getNote() - 0.005));
            root.put("goalComment", factorBean.getComment());
        }
        root.put("totalCount", taille);
        root.put("criteres", l.toArray(new JSONObject[taille]));

        return root;
    }

    private FactorBean retrieveFactorInfos(HttpServletRequest request,
            ElementBean eltBean,
            SyntheseObjectifsSvc syntheseObjectifsSvc) {
        String idFact = request.getParameter("id_fact");
        FactorBean factorBean = syntheseObjectifsSvc.retrieveFactorInfos(eltBean, idFact);
        return factorBean;
    }

    private List<CriterionBean> retrieveEaSynthese(HttpServletRequest request,
            ElementBean eltBean,
            SyntheseObjectifsSvc syntheseObjectifsSvc) {
        String idFact = request.getParameter("id_fact");
        List<CriterionBean> result = syntheseObjectifsSvc.retrieveCriterionSummary(eltBean, idFact);
        //recuperation du plan d'action
        if (result != null) {
            ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
            ActionPlanBean ap = actionPlanSvc.getSavedActionPlan(eltBean, eltBean.getBaseline().getId());
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> actionPlan = ap.getCorrectedElements();
            if (!actionPlan.isEmpty()) {
                for (ActionPlanCriterionBean apcb : actionPlan) {
                    for (CriterionBean cb : result) {
                        if (cb.getCriterionDefinition().getId().equals(apcb.getId())) {
                            cb.setIncludedInActionPlan(true);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    private JSONObject elementCriterionBeanToJSONObject(FactorBean fb, CriterionBean crit, Locale loc,
            NumberFormat markFormatter, NumberFormat weightFormatter, NumberFormat actionPlanCostFormatter, ElementBean eaBean,
            MessageResources messageResources, Map repartition) {
        String idCrit = crit.getCriterionDefinition().getId();
        JSONObject retour = new JSONObject();
        retour.put("commentValue", crit.getComment());
        retour.put("totalCost", actionPlanCostFormatter.format(crit.getCost()));
        retour.put("isIncludedInActionPlan", crit.isIncludedInActionPlan());
        retour.put("libCrit", crit.getCriterionDefinition().getLib(loc));
        retour.put("descCrit", crit.getCriterionDefinition().getDesc(loc));
        retour.put("complCrit", crit.getCriterionDefinition().getComplement(loc));
        retour.put("idCrit", idCrit);
        retour.put("formattedMark", markFormatter.format(crit.getNote() - 0.005));
        retour.put("hasJustification", (crit.getJustificatif() != null));
        if (crit.getJustificatif() != null) {
            retour.put("justStatus", crit.getJustificatif().getStatut());
            retour.put("justId", crit.getJustificatif().getId());
            retour.put("justDesc", crit.getJustificatif().getDesc());
            retour.put("formattedJustMark", markFormatter.format(crit.getJustNote() -
                    0.005));
        }
        retour.put("justNote", crit.getJustNote());
        retour.put("note", crit.getNote());
        retour.put("formattedWeight", weightFormatter.format(crit.getWeight()));
        retour.put("idTelt", crit.getCriterionDefinition().getIdTElt());
        retour.put("libTelt", (new ElementType(crit.getCriterionDefinition().getIdTElt())).getLib(loc));

        String typeElt = "AUTO";
        String all = "false";
        boolean subElt = true;

        //TODO a-t-on encore besoin de only_crit ????

        //TODO en a t'on encore besoin ??
        if (idCrit.equals("MAIN") || idCrit.equals("NETSTD_PRIO") ||
                idCrit.equals("ANTI_NETSTD") || idCrit.equals("ANTI_ARCHI_CS") ||
                idCrit.equals("ANTI_ARCHI_NP")) {
            subElt = false;
            typeElt = "ALL";
            all = "true";
        }
        retour.put("all", all);
        retour.put("subElt", subElt);
        retour.put("typeElt", typeElt);

        retour.put("fatherId", eaBean.getId());
        retour.put("idElt", eaBean.getId());
        retour.put("eltLib", eaBean.getLib());
        retour.put("eltIdTelt", eaBean.getTypeElt());
        retour.put("idBline", eaBean.getBaseline().getId());
        retour.put("projectId", eaBean.getProject().getId());
        retour.put("factorBeanId", fb.getId());

        String tendanceLabel = StringFormatUtil.getTendanceLabel(crit.getTendance(), crit.getNote());
        retour.put("tendanceLabel", tendanceLabel);
        retour.put("tendancePopup", messageResources.getMessage(loc, "caqs.critere." +
                tendanceLabel));
        retour.put("iconImgClass", (crit.getComment()==null || crit.getComment().length()==0) ? "icon-comment-edit" : "icon-has-comment");

        CriterionNoteRepartition nr = (CriterionNoteRepartition) repartition.get(crit.getCriterionDefinition().getId());
        if (nr != null) {
            for (int i = 0; i < 4; i++) {
                String pctPopup = messageResources.getMessage(loc, "caqs.facteursynthese.popup", nr.getValue(i), nr.getPct(i));
                retour.put("repartitionPopup" + i, pctPopup);
                retour.put("pct" + i, nr.getPct(i));
            }
        }

        return retour;
    }
}
