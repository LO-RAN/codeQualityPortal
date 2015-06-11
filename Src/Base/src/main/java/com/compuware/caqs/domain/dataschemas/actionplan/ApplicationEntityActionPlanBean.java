package com.compuware.caqs.domain.dataschemas.actionplan;

import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;

/**
 *
 * @author cwfr-dzysman
 */
public class ApplicationEntityActionPlanBean extends ActionPlanBean {

    /**
     * Number of criterions whose mark is lower than 3
     */
    private ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterionsWithProblematicElements;

    public ApplicationEntityActionPlanBean(String idElt, String idBline) {
        super(idElt, idBline);
        this.criterionsWithProblematicElements = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(idElt, idBline);
    }

    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getElementsWithProblematicElement() {
        return criterionsWithProblematicElements;
    }

    public void setElementWithProblematicElement(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterionWithMarkLowerThan3) {
        this.criterionsWithProblematicElements = criterionWithMarkLowerThan3;
    }

    @Override
    protected void initializeElementsCollection() {
        this.elements = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(this.idElt, this.idBline);
    }

    @Override
    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getElements() {
        return this.elements;
    }

    public double getCorrectionCost() {
        double retour = 0.0;
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> corrected = this.getCorrectedElements();
        for (ActionPlanCriterionBean criterion : corrected) {
            retour += criterion.getCost();
        }
        return retour;
    }

    public double getCorrectionCost(ActionPlanPriority priority) {
        double retour = 0.0;
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> corrected = this.getCorrectedElements();
        for (ActionPlanCriterionBean criterion : corrected) {
            if (criterion.getPriority().equals(priority)) {
                retour += criterion.getCost();
            }
        }
        return retour;
    }
}
