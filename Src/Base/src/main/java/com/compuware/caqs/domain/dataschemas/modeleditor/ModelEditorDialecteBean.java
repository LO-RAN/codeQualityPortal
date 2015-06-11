package com.compuware.caqs.domain.dataschemas.modeleditor;

import com.compuware.caqs.domain.dataschemas.DialecteBean;

/**
 *
 * @author cwfr-dzysman
 */
public class ModelEditorDialecteBean extends DialecteBean {
    private int nbEAsAssociated;

    public int getNbEAsAssociated() {
        return nbEAsAssociated;
    }

    public void setNbEAsAssociated(int nbEAsAssociated) {
        this.nbEAsAssociated = nbEAsAssociated;
    }
}
