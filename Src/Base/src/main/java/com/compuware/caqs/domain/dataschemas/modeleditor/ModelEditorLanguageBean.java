package com.compuware.caqs.domain.dataschemas.modeleditor;

import com.compuware.caqs.domain.dataschemas.LanguageBean;

/**
 *
 * @author cwfr-dzysman
 */
public class ModelEditorLanguageBean extends LanguageBean {
    private int nbDialectsAssociated;

    public ModelEditorLanguageBean(String i) {
        super(i);
    }

    public int getNbDialectsAssociated() {
        return nbDialectsAssociated;
    }

    public void setNbDialectsAssociated(int nbDialects) {
        this.nbDialectsAssociated = nbDialects;
    }
}
