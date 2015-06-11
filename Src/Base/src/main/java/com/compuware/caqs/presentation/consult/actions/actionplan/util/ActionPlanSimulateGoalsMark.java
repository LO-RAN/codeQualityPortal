package com.compuware.caqs.presentation.consult.actions.actionplan.util;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorRepartitionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanFactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.FactorRepartitionBeanList;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionPlanSimulateGoalsMark {

    private static ActionPlanSimulateGoalsMark instance = new ActionPlanSimulateGoalsMark();

    private ActionPlanSimulateGoalsMark() {
    }

    public static ActionPlanSimulateGoalsMark getInstance() {
        return instance;
    }

    public FactorRepartitionBeanList calculatePieChartMarks(ElementBean ea, String idBline, HttpServletRequest request) {
        FactorRepartitionBeanList retour = new FactorRepartitionBeanList();
        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
        ActionPlanBean ap = actionPlanSvc.getCompleteActionPlan(ea, idBline, false, request);
        ActionPlanElementBeanCollection<ActionPlanFactorBean> factors = ap.getFactors();

        for (ActionPlanFactorBean factor : factors) {
            int nb = 0;
            for (ActionPlanCriterionBean criterion : factor.getAssociatedCriterions()) {
                if (!criterion.isCorrected()) {
                    nb += criterion.getNumberElt();
                }
            }
            FactorRepartitionBean frb = new FactorRepartitionBean(factor.getId(), nb);
            retour.add(frb);
        }
        return retour;
    }

    public FactorBeanCollection computeKiviatScoresForReport(
            ElementBean eltBean, String idBline, ActionPlanPriority maxPriority) {
        ActionPlanBean ap = ActionPlanSvc.getInstance().getCompleteActionPlan(eltBean, idBline, false, null);
        this.computeKiviatScores(ap, maxPriority);
        return ActionPlanKiviatUtils.getInstance().convertFromActionPlan(ap.getFactors());
    }

    /**
     * computes kiviat scores for all goals for the selected action plan
     * @param eltBean element for which scores have to be computed
     * @param idBline baseline for which scores have to be computed
     * @param maxPriority maximum priority to take into account
     * @param userId user id
     * @param request request
     * @return simulated factors
     */
    public ActionPlanElementBeanCollection<ActionPlanFactorBean> computeProjectKivatSimulatedScores(ElementBean eltBean, String idBline,
            ActionPlanPriority maxPriority,
            String userId, HttpServletRequest request) {
        ActionPlanElementBeanCollection<ActionPlanFactorBean> retour = null;
        ActionPlanBean ap = ActionPlanSvc.getInstance().getCompleteActionPlan(eltBean, idBline, false, request);
        if (ap.needsSimulatedKiviatRecompute(maxPriority)) {
            //entite applicative ?
            if (ElementType.EA.equals(eltBean.getTypeElt())) {
                this.computeKiviatScores(ap, maxPriority);
                retour = ap.getFactors();
            } else {
                //non : pour chaque enfant
                List<ElementLinked> children = ElementSvc.getInstance().retrieveAllChildrenElements(eltBean.getId(), userId);
                if (children != null && !children.isEmpty()) {
                    //on calcule les plans d'action de tous les enfants
                    Map<String, Double> scores = new HashMap<String, Double>();
                    Map<String, Integer> nbs = new HashMap<String, Integer>();
                    for (ElementLinked child : children) {
                        //on recupere sa derniere baseline
                        BaselineBean bb = BaselineSvc.getInstance().getLastBaseline(child);
                        if (bb != null) {
                            child.setBaseline(bb);
                            ActionPlanElementBeanCollection<ActionPlanFactorBean> factors = this.computeProjectKivatSimulatedScores(child, bb.getId(),
                                    maxPriority, userId, request);
                            for (ActionPlanFactorBean factor : factors) {
                                Double score = scores.get(factor.getId());
                                Integer nb = nbs.get(factor.getId());
                                if (score == null) {
                                    score = Double.valueOf(0.0);
                                    nb = Integer.valueOf(0);
                                }
                                scores.put(factor.getId(), Double.valueOf(score.doubleValue()
                                        + factor.getCorrectedScore()));
                                nbs.put(factor.getId(), Integer.valueOf(nb.intValue()
                                        + 1));
                            }
                        }
                    }
                    //maintenant, on fait la moyenne
                    ActionPlanElementBeanCollection<ActionPlanFactorBean> factors = (ActionPlanElementBeanCollection<ActionPlanFactorBean>) ap.getElements();
                    for (ActionPlanFactorBean factor : factors) {
                        Double score = scores.get(factor.getId());
                        Integer nb = nbs.get(factor.getId());
                        double avg = score.doubleValue() / nb.doubleValue();
                        factor.setCorrectedScore(avg);
                    }
                    retour = factors;
                } else {
                    retour = new ActionPlanElementBeanCollection<ActionPlanFactorBean>(eltBean.getId(), idBline);
                }
            }
            ap.setSimulatedKiviatRecomputedPriority(maxPriority);
        } else {
            if (ElementType.EA.equals(eltBean.getTypeElt())) {
                retour = ap.getFactors();
            } else {
                retour = (ActionPlanElementBeanCollection<ActionPlanFactorBean>) ap.getElements();
            }
        }
        return retour;
    }

    /**
     * computes kiviat scores for all goals for the selected action plan
     * @param eltBean element for which scores have to be computed
     * @param idBline baseline for which scores have to be computed
     * @param maxPriority maximum priority to take into account
     * @param forceRecompute true to force recompute, false to recompute only if necessary
     * @param request request
     */
    public void computeKiviatScores(ElementBean eltBean,
            String idBline, ActionPlanPriority maxPriority, boolean forceRecompute,
            HttpServletRequest request) {
        ActionPlanBean ap = ActionPlanSvc.getInstance().getCompleteActionPlan(eltBean, idBline, false, request);
        if (ap != null && (forceRecompute
                || ap.needsSimulatedKiviatRecompute(maxPriority))) {
            boolean exclusionMode = this.isExclusionCalculationMode();

            for (ActionPlanFactorBean factor : ap.getFactors()) {
                double newValue = this.computeGoalScore(factor, exclusionMode, maxPriority);
                factor.setCorrectedScore(newValue);
            }
            ap.setSimulatedKiviatRecomputedPriority(maxPriority);
        }
    }

    /**
     * computes kiviat scores for a specific goal for the selected action plan
     * @param ap action plan
     * @param goal goal
     */
    public double getGoalScoreForActionPlan(ActionPlanBean ap, ActionPlanElementBean goal, ActionPlanPriority maxPriority) {
        double retour = 0.0;
        ActionPlanElementBeanCollection<ActionPlanFactorBean> factors = ap.getFactors();
        boolean exclusionMode = this.isExclusionCalculationMode();
        ActionPlanFactorBean factor = factors.get(goal.getId());
        if (factor != null) {
            retour = this.computeGoalScore(factor, exclusionMode, maxPriority);
        }
        return retour;
    }

    /**
     * computes kiviat scores for all goals for the selected action plan
     * @param ap the action plan
     * @param maxPriority maximum priority to include in the computation
     */
    public void computeKiviatScores(ActionPlanBean ap, ActionPlanPriority maxPriority) {
        if (ap.needsSimulatedKiviatRecompute(maxPriority)) {
            ActionPlanElementBeanCollection<ActionPlanFactorBean> factors = ap.getFactors();

            boolean exclusionMode = this.isExclusionCalculationMode();

            for (ActionPlanFactorBean factor : factors) {
                double newValue = this.computeGoalScore(factor, exclusionMode, maxPriority);
                factor.setCorrectedScore(newValue);
            }
            ap.setSimulatedKiviatRecomputedPriority(maxPriority);
        }
    }

    private boolean isExclusionCalculationMode() {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String result = dynProp.getProperty("calcul.aggregation.mode");
        return "exclusion".equalsIgnoreCase(result);
    }

    public double computeGoalScore(ActionPlanFactorBean fact,
            boolean exclusionMode, ActionPlanPriority maxPriority) {
        double retour = 0.0;
        double sumNote = 0.0;
        double sumWeight = 0.0;
        double minNoteCrit = 4.0;
        int nbCrit = fact.getAssociatedCriterions().size();
        for (ActionPlanCriterionBean critere : fact.getAssociatedCriterions()) {
            double poids = critere.getFactors().get(fact.getId());
            if (critere != null && poids != 0.0 && critere.getScore() > 0) {
                double note = (critere.isCorrected()
                        && critere.getPriority().hasSameOrMorePriority(maxPriority)) ? critere.getCorrectedScore() : critere.getScore();
                double adjustedWeight = getWeight(poids, nbCrit, note);
                sumNote += note * adjustedWeight;
                sumWeight += adjustedWeight;
                minNoteCrit = Math.min(minNoteCrit, note);
            }
        }
        if (sumWeight > 0) {
            retour = getScore(sumNote / sumWeight, minNoteCrit, exclusionMode);
        }
        return retour;
    }

    private double getScore(double note, double minNote, boolean exclusionMode) {
        double result = note;
        if (minNote < 3 && exclusionMode) {
            result = minNote;
        } else if (minNote < 2 && result > 3) {
            result = 2.99;
        }
        return result;
    }

    /**
     * The real weight is calculated as follow:
     * note <= 2 then weight = weight * number_of_crit / note
     * note > 4 then weight = weight
     * else weight = weight * number_of_crit / note
     * @param poids
     * @param nbCrit
     * @param note
     * @return
     */
    private double getWeight(double poids, int nbCrit, double note) {
        double result = poids;
        if (note > 3.5) {
            result = poids + (((double) nbCrit) / 10.0);
        } else if (note > 3.2) {
            result = poids + (((double) nbCrit) / 5.0);
        } else {
            result = poids + (nbCrit / note);
        }
        return result;
    }
}
