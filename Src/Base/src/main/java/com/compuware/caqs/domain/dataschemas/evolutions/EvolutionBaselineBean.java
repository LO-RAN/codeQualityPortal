package com.compuware.caqs.domain.dataschemas.evolutions;

import com.compuware.caqs.domain.dataschemas.BaselineBean;

/**
 * baselinebean avec en plus l'information concernant le nombre de criteres dans
 * le plan d'actions pour cette baseline
 * @author cwfr-dzysman
 */
public class EvolutionBaselineBean extends BaselineBean {

    private int nbCriterionsInActionsPlan;

    public int getNbCriterionsInActionsPlan() {
        return nbCriterionsInActionsPlan;
    }

    public void setNbCriterionsInActionsPlan(int nbCriterionsInActionsPlan) {
        this.nbCriterionsInActionsPlan = nbCriterionsInActionsPlan;
    }
    
}
