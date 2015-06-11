package com.compuware.caqs.domain.dataschemas.actionplan;

import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;

/**
 *
 * @author cwfr-dzysman
 */
public class ProjectActionPlanBean extends ActionPlanBean {

    /**
     * Number of criterions whose mark is lower than 3
     */
    private ActionPlanElementBeanCollection<ActionPlanFactorBean> factorsWithProblematicElements;

    public ProjectActionPlanBean(String idElt, String idBline) {
        super(idElt, idBline);
        this.factorsWithProblematicElements = new ActionPlanElementBeanCollection<ActionPlanFactorBean>(idElt, idBline);
    }

    @Override
    protected void initializeElementsCollection() {
        this.elements = new ActionPlanElementBeanCollection<ActionPlanFactorBean>(this.idElt, this.idBline);
    }

    public ActionPlanElementBeanCollection<ActionPlanFactorBean> getFactorsWithProblematicElements() {
        return factorsWithProblematicElements;
    }

    public void setFactorsWithProblematicElements(ActionPlanElementBeanCollection<ActionPlanFactorBean> factorsWithProblematicElements) {
        this.factorsWithProblematicElements = factorsWithProblematicElements;
    }

    
}
