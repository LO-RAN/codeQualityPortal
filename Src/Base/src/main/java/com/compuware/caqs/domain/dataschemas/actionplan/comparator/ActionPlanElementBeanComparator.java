package com.compuware.caqs.domain.dataschemas.actionplan.comparator;

import java.util.Comparator;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanElementBean;

public class ActionPlanElementBeanComparator implements Comparator<ActionPlanElementBean> {

    protected Locale locale = null;
    protected String sort = null;
    protected boolean asc = true;

    public ActionPlanElementBeanComparator(Locale l, String sort, boolean asc) {
        this.locale = l;
        this.sort = sort;
        this.asc = asc;
    }

    public int compare(ActionPlanElementBean apcb, ActionPlanElementBean apcb2) {
        int retour = 0;
        if ("indiceGravite".equals(this.sort)) {
            retour = Integer.valueOf(apcb.getIndiceGravite()).compareTo(Integer.valueOf(apcb2.getIndiceGravite()));
        } else if ("severitySort".equals(this.sort)) {
            //utilise pour trier la premiere fois la severite d'un critere
            retour = apcb.compareSeverity(apcb2, this.locale);
        } else if ("lib".equals(this.sort)) {
            retour = apcb.getInternationalizableProperties().getLib(this.locale).compareTo(apcb2.getInternationalizableProperties().getLib(this.locale));
        } else if ("note".equals(this.sort)) {
            retour = Double.valueOf(apcb.getScore()).compareTo(Double.valueOf(apcb2.getScore()));
        }
        if (!this.isAsc()) {
            retour = retour * -1;
        }
        return retour;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
