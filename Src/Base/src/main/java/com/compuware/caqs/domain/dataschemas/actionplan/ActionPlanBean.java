package com.compuware.caqs.domain.dataschemas.actionplan;

import com.compuware.caqs.domain.dataschemas.FactorBean;
import java.io.Serializable;

import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;

public abstract class ActionPlanBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3391765487785597180L;
    /**
     * action plan's id
     */
    protected String id;
    /**
     * element id
     */
    protected String idElt;
    /**
     * baseline id
     */
    protected String idBline;
    /**
     * comment on action plan
     */
    protected String actionPlanComment;
    /**
     * user who made the comment
     */
    protected String commentUser;
    /**
     * Selected criterions for action plan
     */
    protected ActionPlanElementBeanCollection selectedElements;
    /**
     * criterions included or commented
     */
    protected ActionPlanElementBeanCollection elements;
    private boolean hasBeenCompleted = false;

    /**
     * tells if the impacted elements list has to be recomputed
     */
    private boolean needsImpactedElementsRecompute;

    /**
     * priority used to simulate score
     */
    private ActionPlanPriority simulatedScoresPriority;

    /**
     * factors
     */
    protected ActionPlanElementBeanCollection<ActionPlanFactorBean> factors;

    protected ActionPlanBean(String idElt, String idBline) {
        super();
        this.idBline = idBline;
        this.idElt = idElt;
        this.selectedElements = new ActionPlanElementBeanCollection(idElt, idBline);
        this.factors = new ActionPlanElementBeanCollection<ActionPlanFactorBean>(idElt, idBline);
        this.initializeElementsCollection();
        this.needsImpactedElementsRecompute = true;
        this.simulatedScoresPriority = ActionPlanPriority.UNDEFINED;
    }

    public ActionPlanElementBeanCollection getElements() {
        return elements;
    }

    protected abstract void initializeElementsCollection();

    public String getIdElt() {
        return idElt;
    }

    public void setIdElt(String idElt) {
        this.idElt = idElt;
    }

    public String getIdBline() {
        return idBline;
    }

    public void setIdBline(String idBline) {
        this.idBline = idBline;
    }

    public String getActionPlanComment() {
        return actionPlanComment;
    }

    public void setActionPlanComment(String actionPlanComment) {
        this.actionPlanComment = actionPlanComment;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addElementToActionPlan(ActionPlanElementBean cb) {
        this.getElements().add(cb);
        if (cb.isCorrected() && !this.getCorrectedElements().contains(cb)) {
            this.setElementCorrected(true, cb);
        }
    }

    public ActionPlanElementBeanCollection getCorrectedElements() {
        return this.selectedElements;
    }

    public boolean hasBeenCompleted() {
        return hasBeenCompleted;
    }

    public void setHasBeenCompleted(boolean hasBeenCompleted) {
        this.hasBeenCompleted = hasBeenCompleted;
    }

    public void setElementCorrected(boolean corrected, ActionPlanElementBean element) {
        element.setCorrected(corrected);
        if (corrected) {
            this.getCorrectedElements().add(element);
        } else {
            this.getCorrectedElements().remove(element);
        }
        this.setNeedsImpactedElementsRecompute(true);
    }

    public void updateFactorsMarksWithKiviatDatas(FactorBeanCollection coll) {
        if (this.factors != null && coll != null) {
            for (ActionPlanFactorBean factor : this.factors) {
                for (FactorBean f : coll) {
                    if (f.getId().equals(factor.getId())) {
                        factor.setScore(f.getNote());
                        break;
                    }
                }
            }
        }
    }

    public ActionPlanElementBeanCollection<ActionPlanFactorBean> getFactors() {
        return factors;
    }

    public void setFactors(ActionPlanElementBeanCollection<ActionPlanFactorBean> factors) {
        this.factors = factors;
    }

    public boolean isNeedsImpactedElementsRecompte() {
        return needsImpactedElementsRecompute;
    }

    public void setNeedsImpactedElementsRecompute(boolean dirty) {
        this.needsImpactedElementsRecompute = dirty;
    }

    public boolean needsSimulatedKiviatRecompute(ActionPlanPriority priority) {
        return !this.simulatedScoresPriority.equals(priority);
    }

    public void setSimulatedKiviatRecomputedPriority(ActionPlanPriority priority) {
        this.simulatedScoresPriority = priority;
    }

    

}
