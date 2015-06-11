/*
 * CriterionComparator.java
 *
 * Created on 6 novembre 2002, 22:43
 */
package com.compuware.caqs.domain.dataschemas.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

import com.compuware.caqs.domain.dataschemas.FactorBean;

/**
 * Comparateur de critere pour une liste d'objets de classe CriterionBean.
 *
 * @author cwfr-fdubois
 */
public class FactorBeanComparator implements Comparator<FactorBean> {

    private Locale locale = null;
    /**
     * True si ordre inverse, false sinon.
     */
    private boolean invorder = false;
    /**
     * La partie a trier.
     */
    private String part = "lib";
    /** The collator for the comparison.*/
    private Collator collator = null;

    /**
     * Cree une nouvelle instance de CriterionComparator.
     *
     * @param metrique le critere cible.
     * @param invorder ordre inverse ou non.
     * @param part     partie a trier.
     */
    public FactorBeanComparator(boolean invorder, String part, Locale loc) {
        // Initialisation des attributs.
        this.invorder = invorder;
        this.part = part;
        this.locale = loc;
        this.collator = Collator.getInstance(this.locale);
    }

    /**
     * Compare les criteres cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux criteres.
     */
    public int compare(FactorBean o1, FactorBean o2) {
        // Variable resultat.
        int result = 0;
        if ("lib".equals(this.part)) {
            result = compareLib(o1, o2);
        }
        return result;
    }

    /**
     * Compare les libelles des criteres.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux libelles.
     */
    public int compareLib(FactorBean o1, FactorBean o2) {
        // Variable resultat.
        int result = 0;
        if (!this.invorder) {
            // Ordre universel.
            result = this.collator.compare(o1.getLib(locale), o2.getLib(locale));
        } else {
            // Ordre inverse.
            result = this.collator.compare(o2.getLib(locale), o1.getLib(locale));
        }
        return result;
    }
}
