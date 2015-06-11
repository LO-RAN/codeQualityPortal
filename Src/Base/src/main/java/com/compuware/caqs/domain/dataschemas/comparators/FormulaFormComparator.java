/*
 * FormulaFormComparator.java
 *
 * Created on 6 novembre 2002, 22:43
 */
package com.compuware.caqs.domain.dataschemas.comparators;

import java.util.Comparator;

import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;

/**
 * Comparateur de critere pour une liste d'objets de classe CriterionBean.
 *
 * @author cwfr-fdubois
 */
public class FormulaFormComparator implements Comparator<FormuleForm> {

    /**
     * True si ordre inverse, false sinon.
     */
    private boolean invorder = false;
    /**
     * La partie a trier.
     */
    private String part = "score";

    /**
     * Cree une nouvelle instance de FormulaFormComparator.
     *
     * @param invorder ordre inverse ou non.
     * @param part     partie a trier.
     */
    public FormulaFormComparator(boolean invorder, String part) {
        // Initialisation des attributs.
        this.invorder = invorder;
        this.part = part;
    }

    /**
     * Compare les criteres cibles de deux objets de classe FormuleForm.
     * o1 premier objet.
     * o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux criteres.
     */
    public int compare(FormuleForm o1, FormuleForm o2) {
        // Variable resultat.
        int result = 0;
        if ("score".equals(this.part)) {
            result = compareScore(o1, o2);
        }
        return result;
    }

    /**
     * Compare les notes cibles de deux objets de classe CriterionPerFactorBean.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     * @param note la note a comparer
     * @return le resultat de la comparaison de deux notes.
     */
    public int compareScore(FormuleForm o1, FormuleForm o2) {
        // Variable resultat.
        int result = 0;
        // Note 1.
        Integer val1 = new Integer(o1.getScore());
        // Note 2.
        Integer val2 = new Integer(o2.getScore());
        if (!this.invorder) {
            // Ordre universel.
            result = val1.compareTo(val2);
        } else {
            // Ordre inverse.
            result = val2.compareTo(val1);
        }
        return result;
    }
}
