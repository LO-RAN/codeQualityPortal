package com.compuware.caqs.domain.dataschemas.actionplan.comparator;

import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;

public class ActionPlanCriterionBeanComparator extends ActionPlanElementBeanComparator {

    public ActionPlanCriterionBeanComparator(Locale l, String sort, boolean desc) {
        super(l, sort, desc);
    }

    public int compare(ActionPlanCriterionBean apcb, ActionPlanCriterionBean apcb2) {
        int retour = 0;
        if ("aggregation".equals(this.sort)) {
            retour = apcb.compareAggregation(apcb2);
        } else if ("telt".equals(this.sort)) {
            retour = apcb.getTypeElt().compareToIgnoreCase(apcb2.getTypeElt());
        } else if ("nbElts".equals(this.sort)) {
            retour = Integer.valueOf(apcb.getRepartition().getValue(0) +
                    apcb.getRepartition().getValue(1)).compareTo(Integer.valueOf(apcb2.getRepartition().getValue(0) +
                    apcb2.getRepartition().getValue(1)));
        } else if ("repartition".equals(this.sort)) {
            retour = Integer.valueOf(apcb.getRepartition().getValue(0)).compareTo(Integer.valueOf(apcb2.getRepartition().getValue(0)));
            if (retour == 0) {
                retour = Integer.valueOf(apcb.getRepartition().getValue(1)).compareTo(Integer.valueOf(apcb2.getRepartition().getValue(1)));
            }
            if (retour == 0) {
                retour = Integer.valueOf(apcb.getRepartition().getValue(2)).compareTo(Integer.valueOf(apcb2.getRepartition().getValue(2)));
            }
            if (retour == 0) {
                retour = Integer.valueOf(apcb.getRepartition().getValue(3)).compareTo(Integer.valueOf(apcb2.getRepartition().getValue(3)));
            }
        } else if ("nbCorrected".equals(this.sort)) {
            retour = Integer.valueOf(apcb.getCorrectedElts().size()).compareTo(Integer.valueOf(apcb2.getCorrectedElts().size()));
        } else if ("nbStables".equals(this.sort)) {
            retour = Integer.valueOf(apcb.getStablesElts().size()).compareTo(Integer.valueOf(apcb2.getStablesElts().size()));
        } else if ("nbPartiallyCorrected".equals(this.sort)) {
            retour = Integer.valueOf(apcb.getPartiallyCorrectedElts().size()).compareTo(Integer.valueOf(apcb2.getPartiallyCorrectedElts().size()));
        } else if ("nbDeteriorated".equals(this.sort)) {
            retour = Integer.valueOf(apcb.getDeterioratedElts().size()).compareTo(Integer.valueOf(apcb2.getDeterioratedElts().size()));
        } else if ("priority".equals(this.sort)) {
            retour = apcb.getPriority().compareTo(apcb2.getPriority());
        } else if ("cost".equals(this.sort)) {
            retour = Double.valueOf(apcb.getCost()).compareTo(Double.valueOf(apcb2.getCost()));
        } else if ("idTelt".equals(this.sort)) {
            String elt1 = ((new ElementType(apcb.getTypeElt())).getLib(this.locale));
            String elt2 = ((new ElementType(apcb2.getTypeElt())).getLib(this.locale));
            retour = elt1.compareTo(elt2);
        } else {
            retour = super.compare(apcb, apcb2);
        }

        if (!this.isAsc()) {
            retour = retour * -1;
        }
        return retour;
    }
}
