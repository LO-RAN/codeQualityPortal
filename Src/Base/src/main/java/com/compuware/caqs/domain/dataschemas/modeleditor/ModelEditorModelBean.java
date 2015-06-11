package com.compuware.caqs.domain.dataschemas.modeleditor;

import com.compuware.caqs.domain.dataschemas.UsageBean;

/**
 *
 * @author cwfr-dzysman
 */
public class ModelEditorModelBean extends UsageBean {
    private int nbEAAssociated;

    public int getNbEAAssociated() {
        return nbEAAssociated;
    }

    public void setNbEAAssociated(int nbEAAssociated) {
        this.nbEAAssociated = nbEAAssociated;
    }

    
}
