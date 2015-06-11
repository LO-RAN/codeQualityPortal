package com.compuware.caqs.domain.dataschemas.actionplan;

import com.compuware.caqs.domain.dataschemas.FactorDefinitionBean;

import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import java.util.Locale;

public class ActionPlanFactorBean extends ActionPlanElementBean {

    /**
     *
     */
    private static final long serialVersionUID = -7785402168989817848L;
    private double weight = 0.0;
    private ActionPlanElementBeanCollection<ActionPlanCriterionBean> associatedCriterions = null;

    public ActionPlanFactorBean(String id) {
        super(new FactorDefinitionBean(id), id);
        this.id = id;
        this.elementCorrected = false;
    }

    public ActionPlanFactorBean(String id, String idElt, String idBline) {
        super(new FactorDefinitionBean(id), id);
        this.associatedCriterions = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(idElt, idBline);
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getAssociatedCriterions() {
        return associatedCriterions;
    }

    public void setAssociatedCriterions(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> associatedCriterions) {
        this.associatedCriterions = associatedCriterions;
    }

    @Override
    public int compareSeverity(ActionPlanElementBean o, Locale loc) {
        return Double.valueOf(this.score).compareTo(Double.valueOf(o.getScore()));
    }

    @Override
    public double getCorrectedScore() {
        return this.correctedScore;
    }

}
