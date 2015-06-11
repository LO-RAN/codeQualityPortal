/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.modeleditor;

import com.compuware.caqs.domain.dataschemas.ElementType;

/**
 *
 * @author cwfr-dzysman
 */
public class ElementTypeBean extends ElementType {
    private int nbCriterionsAssociated = 0;

    public int getNbCriterionsAssociated() {
        return nbCriterionsAssociated;
    }

    public void setNbCriterionsAssociated(int nbCriterionsAssociated) {
        this.nbCriterionsAssociated = nbCriterionsAssociated;
    }
    
}
