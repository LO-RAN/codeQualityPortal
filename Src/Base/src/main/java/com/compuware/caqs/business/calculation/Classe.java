/*
 * Programme.java
 *
 * Created on 3 octobre 2002, 11:24
 */

package com.compuware.caqs.business.calculation;

import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;

/**
 * D�finit une classe.
 * @author  cwfr-fdubois
 */
public class Classe extends Leaf {
    
    /** Creates a new instance of Classe.
     * @param idElt Identifiant de l'�l�ment classe.
     * @param poids le poids associ� � l'�l�ment.
     */
    public Classe(String idElt, String idTElt, double poids, String stereotype, ProjectBean projet, Baseline baseline, UsageBean usage) {
        // Initialisation des attributs de la classe.
        super(idElt, idTElt, poids, stereotype, projet, baseline, usage);
    }
    
}
