package com.compuware.caqs.service;

import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import java.io.File;
import java.util.Collection;
import java.util.List;

import com.compuware.caqs.business.consult.Evolution;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.BottomUpDetailBean;
import com.compuware.caqs.domain.dataschemas.CriterionRepartitionBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementEvolutionSummaryBean;
import com.compuware.caqs.domain.dataschemas.EvolutionBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import java.util.Iterator;
import java.util.Map;

public class EvolutionSvc {

    private static final EvolutionSvc instance = new EvolutionSvc();

    private EvolutionSvc() {
    }

    public static EvolutionSvc getInstance() {
        return instance;
    }
    Evolution evolution = new Evolution();

    public File retrieveDashboardEvolutionImage(ElementBean elt, GraphImageConfig imgConfig) {
        return evolution.retrieveDashboardEvolutionImage(elt, imgConfig);
    }

    public List<ElementEvolutionSummaryBean> retrieveNewAndBadElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveNewAndBadElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveOldAndWorstElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveOldAndWorstElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveOldAndBetterElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveOldAndBetterElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveOldBetterAndWorstElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveOldBetterAndWorstElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveStableElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveStableElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveBadAndStableElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveBadAndStableElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveNewAndBadElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveNewAndBadElements(idElt, idBline, previousBaseline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveOldAndWorstElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveOldAndWorstElements(idElt, idBline, previousBaseline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveOldAndBetterElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveOldAndBetterElements(idElt, idBline, previousBaseline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveOldBetterAndWorstElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveOldBetterAndWorstElements(idElt, idBline, previousBaseline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveStableElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveStableElements(idElt, idBline, previousBaseline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveBadAndStableElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveBadAndStableElements(idElt, idBline, previousBaseline, filter);
    }

    public Collection retrieveRepartitionNewAndBadElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveRepartitionNewAndBadElements(idElt, idBline, filter);
    }

    public Collection retrieveRepartitionOldAndWorstElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveRepartitionOldAndWorstElements(idElt, idBline, filter);
    }

    public Collection retrieveRepartitionOldAndBetterElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveRepartitionOldAndBetterElements(idElt, idBline, filter);
    }

    public Collection retrieveRepartitionOldBetterWorstElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveRepartitionOldBetterWorstElements(idElt, idBline, filter);
    }

    public Collection retrieveRepartitionBadStableElements(String idElt, String idBline, FilterBean filter) {
        return evolution.retrieveRepartitionBadStableElements(idElt, idBline, filter);
    }

    public Collection<FactorBean> retrieveFactorEvolution(ElementBean eltBean, BaselineBean bline) {
        return evolution.retrieveFactorEvolution(eltBean, bline);
    }

    public Collection<BaselineBean> retrieveBaselines(ElementBean eltBean) {
        return evolution.retrieveBaselines(eltBean);
    }

    public Collection<EvolutionBean> retrieveEvolution(ElementBean eltBean, String target) {
        return evolution.retrieveEvolution(eltBean, target);
    }

    public Volumetry retrieveVolumetry(ElementBean eltBean) {
        return evolution.retrieveVolumetry(eltBean);
    }

    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getCorrectedCriterionsFromPreviousActionPlan(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlan,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> badCriterions) {
        return evolution.getCorrectedCriterionsFromPreviousActionPlan(previousActionPlan, badCriterions);
    }

    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getStableCriterionsFromPreviousActionPlan(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlan,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> badCriterions) {
        return evolution.getStableCriterionsFromPreviousActionPlan(previousActionPlan, badCriterions);
    }

    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getDeterioratedCriterionsFromPreviousActionPlan(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlan,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> badCriterions) {
        return evolution.getDeterioratedCriterionsFromPreviousActionPlan(previousActionPlan, badCriterions);
    }

    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getPartiallyCorrectedCriterionsFromPreviousActionPlan(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlan,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> badCriterions) {
        return evolution.getPartiallyCorrectedCriterionsFromPreviousActionPlan(previousActionPlan, badCriterions);
    }

    /**
     * 
     * @param idElt
     * @param bline
     * @param prevBline
     * @return
     */
    public List<Volumetry> retrieveVolumetryBetweenBaselines(String idElt, BaselineBean bline, BaselineBean prevBline) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveVolumetryBetweenBaselines(idElt, bline, prevBline);
    }

    /**
     *
     * @param idElt
     * @param bline
     * @param prevBline
     * @return
     */
    public Volumetry retrieveSumVolumetryBetweenBaselines(String idElt, BaselineBean bline, BaselineBean prevBline) {
        return this.summarize(DaoFactory.getInstance().getEvolutionDao().retrieveVolumetryBetweenBaselines(idElt, bline, prevBline));
    }

    /**
     * 
     * @param volumetryList
     * @return
     */
    private Volumetry summarize(List volumetryList) {
        Volumetry result = new Volumetry();
        Iterator i = volumetryList.iterator();
        Volumetry current = null;
        while (i.hasNext()) {
            current = (Volumetry) i.next();
            result.setCreated(result.getCreated() + current.getCreated());
            result.setDeleted(result.getDeleted() + current.getDeleted());
        }
        return result;
    }

    /**
     *
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionBadStableElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveRepartitionBadStableElements(idElt, idBline, previousIdBline, filter);
    }

    /**
     *
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionOldBetterWorstElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveRepartitionOldBetterWorstElements(idElt, idBline, previousIdBline, filter);
    }

    /**
     *
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection retrieveRepartitionNewAndBadElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveRepartitionNewAndBadElements(idElt, idBline, previousIdBline, filter);
    }

    /**
     *
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionOldAndWorstElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveRepartitionOldAndWorstElements(idElt, idBline, previousIdBline, filter);
    }

    /**
     *
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionOldAndBetterElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveRepartitionOldAndBetterElements(idElt, idBline, previousIdBline, filter);
    }

    /**
     * 
     * @param idBline
     * @param idPreviousBline
     * @param idElt
     * @param idPro
     * @return
     */
    public List<BottomUpDetailBean> retrieveCriterionBottomUpDetail(
            String idBline, String idPreviousBline, String idElt, String idPro) {
        return DaoFactory.getInstance().getEvolutionDao().retrieveCriterionBottomUpDetail(idBline, idPreviousBline, idElt, idPro);
    }

    /**
     *
     * @param idBline
     */
    public void clearBaselineEvolutionDatas(String idBline) {
       DaoFactory.getInstance().getEvolutionDao().clearBaselineEvolutionDatas(idBline);
    }

    /**
     * retrieves metrics synthesis volumetry evolution
     * @param eltBean
     * @return
     */
    public Map<String, Map<String, Double>> retrieveVolumetryMetricsEvolution(ElementBean eltBean) {
        return DaoFactory.getInstance().getMetriqueDao().retrieveVolumetryMetricsEvolution(eltBean);
    }
}
