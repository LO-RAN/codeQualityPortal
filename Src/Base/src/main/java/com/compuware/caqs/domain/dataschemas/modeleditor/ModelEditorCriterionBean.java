/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.modeleditor;

import com.compuware.caqs.domain.dataschemas.CriterionDefinition;

/**
 *
 * @author cwfr-dzysman
 */
public class ModelEditorCriterionBean extends CriterionDefinition {

    private int nbModelsAssociated = 0;

    public int getNbModelsAssociated() {
        return nbModelsAssociated;
    }

    public void setNbModelsAssociated(int nbModelsAssociated) {
        this.nbModelsAssociated = nbModelsAssociated;
    }
    
}
