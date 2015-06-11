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

import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionNoteRepartition;
import com.compuware.caqs.presentation.util.StringFormatUtil;

/**
 * Comparateur de critere pour une liste d'objets de classe CriterionBean.
 *
 * @author cwfr-fdubois
 */
public class CriterionBeanComparator implements Comparator<CriterionBean> {

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
    private Map repartition;

    /**
     * Crre une nouvelle instance de CriterionComparator.
     *
     * @param invorder ordre inverse ou non.
     * @param part     partie a trier.
     * @param rep      map de repartition.
     * @param loc     locale.
     */
    public CriterionBeanComparator(boolean invorder, String part, Map rep, Locale loc) {
        // Initialisation des attributs.
        this.invorder = invorder;
        this.part = part;
        this.locale = loc;
        this.collator = Collator.getInstance(this.locale);
        this.repartition = rep;
    }

    /**
     * Compare les criteres cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux criteres.
     */
    public int compare(CriterionBean o1, CriterionBean o2) {
        // Variable resultat.
        int result = 0;
        if ("libCrit".equals(this.part)) {
            result = compareLib(o1, o2);
        } else if ("weight".equals(this.part)) {
            result = comparePoids(o1, o2);
        } else if ("trend".equals(this.part)) {
            result = compareTrend(o1, o2);
        } else if ("note".equals(this.part)) {
            result = compareNotes(o1, o2);
        } else if ("dispatching".equals(this.part)) {
            result = compareRepartition(o1, o2);
        } else if ("telt".equals(this.part)) {
            result = compareTelt(o1, o2);
        }
        return result;
    }

    /**
     * Compare les poids des criteres.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux libelles.
     */
    public int compareTelt(CriterionBean o1, CriterionBean o2) {
        // Variable resultat.
        int result = 0;
        if (!this.invorder) {
            result = o1.getCriterionDefinition().getIdTElt().compareTo(o2.getCriterionDefinition().getIdTElt());
        } else {
            result = o2.getCriterionDefinition().getIdTElt().compareTo(o1.getCriterionDefinition().getIdTElt());
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
    public int compareLib(CriterionBean o1, CriterionBean o2) {
        // Variable resultat.
        int result = 0;
        if (!this.invorder) {
            // Ordre universel.
            result = this.collator.compare(o1.getCriterionDefinition().getLib(locale), o2.getCriterionDefinition().getLib(locale));
        } else {
            // Ordre inverse.
            result = this.collator.compare(o2.getCriterionDefinition().getLib(locale), o1.getCriterionDefinition().getLib(locale));
        }
        return result;
    }

    /**
     * Compare les poids des criteres.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux libelles.
     */
    public int comparePoids(CriterionBean o1, CriterionBean o2) {
        // Variable resultat.
        int result = 0;
        Double poids1 = new Double(o1.getWeight());
        Double poids2 = new Double(o2.getWeight());
        if (!this.invorder) {
            // Ordre universel.
            result = poids1.compareTo(poids2);
        } else {
            // Ordre inverse.
            result = poids2.compareTo(poids1);
        }
        return result;
    }

    /**
     * Compare les poids des criteres.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux libelles.
     */
    public int compareRepartition(CriterionBean o1, CriterionBean o2) {
        // Variable resultat.
        int result = 0;

        CriterionNoteRepartition nr1 = (CriterionNoteRepartition) repartition.get(o1.getCriterionDefinition().getId());
        CriterionNoteRepartition nr2 = (CriterionNoteRepartition) repartition.get(o2.getCriterionDefinition().getId());
        if (nr1 == null) {
            result = -1;
        } else if (nr2 == null) {
            result = 1;
        } else {
            Double p1 = new Double(nr1.getPct(0));
            Double p2 = new Double(nr2.getPct(0));
            result = p1.compareTo(p2);
            if (result == 0) {
                p1 = new Double(nr1.getPct(1));
                p2 = new Double(nr2.getPct(1));
                result = p1.compareTo(p2);
                if (result == 0) {
                    p1 = new Double(nr1.getPct(2));
                    p2 = new Double(nr2.getPct(2));
                    result = p1.compareTo(p2);
                    if (result == 0) {
                        p1 = new Double(nr1.getPct(3));
                        p2 = new Double(nr2.getPct(3));
                        result = p1.compareTo(p2);
                    }
                }
            }
        }
        if (this.invorder) {
            result = result * -1;
        }
        return result;
    }

    /**
     * Compare les poids des criteres.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux libelles.
     */
    public int compareTrend(CriterionBean o1, CriterionBean o2) {
        // Variable resultat.
        int result = 0;
        String tendanceLabel1 = StringFormatUtil.getTendanceLabel(o1.getTendance(), o1.getNote());
        String tendanceLabel2 = StringFormatUtil.getTendanceLabel(o2.getTendance(), o2.getNote());
        if (!this.invorder) {
            // Ordre universel.
            result = tendanceLabel1.compareTo(tendanceLabel2);
        } else {
            // Ordre inverse.
            result = tendanceLabel2.compareTo(tendanceLabel1);
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
    public int compareNotes(CriterionBean o1, CriterionBean o2) {
        // Variable resultat.
        int result = 0;
        // Note du critere 1.
        Double val1 = new Double(o1.getNoteOrJustNote());
        // Note du critere 2.
        Double val2 = new Double(o2.getNoteOrJustNote());
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
