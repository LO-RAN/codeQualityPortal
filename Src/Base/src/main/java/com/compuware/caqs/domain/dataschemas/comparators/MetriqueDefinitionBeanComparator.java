/*
 * CriterionComparator.java
 *
 * Created on 6 novembre 2002, 22:43
 */
package com.compuware.caqs.domain.dataschemas.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;

/**
 * Comparateur de metrique pour une liste d'objets de classe MetriqueDefinitionBean.
 *
 * @author cwfr-dzysman
 */
public class MetriqueDefinitionBeanComparator implements Comparator<MetriqueDefinitionBean> {

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
     * @param metrique le crit�re cible.
     * @param invorder ordre invers� ou non.
     * @param part     partie � trier.
     */
    public MetriqueDefinitionBeanComparator(boolean invorder, String part, Locale loc) {
        // Initialisation des attributs.
        this.invorder = invorder;
        this.part = part;
        this.locale = loc;
        this.collator = Collator.getInstance(this.locale);
    }

    /**
     * Compare les metriques cibles de deux objets de classe MetriqueDefinitionBean.
     * o1 premier objet.
     * o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux metriques.
     */
    public int compare(MetriqueDefinitionBean o1, MetriqueDefinitionBean o2) {
        // Variable resultat.
        int result = 0;
        if ("id".equals(this.part)) {
            result = compareId(o1, o2);
        } else if ("lib".equals(this.part)) {
            result = compareLib(o1, o2);
        } else if ("desc".equals(this.part)) {
            result = compareDesc(o1, o2);
        } else if ("tool".equals(this.part)) {
            result = compareTools(o1, o2);
        }
        return result;
    }

    /**
     * Compare les identifiants des metriques.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux libelles.
     */
    public int compareId(MetriqueDefinitionBean o1, MetriqueDefinitionBean o2) {
        // Variable resultat.
        int result = 0;
        if (this.invorder) {
            // Ordre inverse.
            result = o2.getId().compareToIgnoreCase(o1.getId());
        } else {
            // Ordre universel.
            result = o1.getId().compareToIgnoreCase(o2.getId());
        }
        return result;
    }

    /**
     * Compare les libelles des metriques.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux libelles.
     */
    public int compareLib(MetriqueDefinitionBean o1, MetriqueDefinitionBean o2) {
        // Variable resultat.
        int result = 0;
        if (this.invorder) {
            // Ordre inverse.
            result = this.collator.compare(o2.getLib(locale), o1.getLib(locale));
        } else {
            // Ordre universel.
            result = this.collator.compare(o1.getLib(locale), o2.getLib(locale));
        }
        return result;
    }

    /**
     * Compare les descriptions des metriques.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux libelles.
     */
    public int compareDesc(MetriqueDefinitionBean o1, MetriqueDefinitionBean o2) {
        // Variable resultat.
        int result = 0;
        if (this.invorder) {
            // Ordre inverse.
            result = this.collator.compare(o2.getDesc(locale), o1.getDesc(locale));
        } else {
            // Ordre universel.
            result = this.collator.compare(o1.getDesc(locale), o2.getDesc(locale));
        }
        return result;
    }

    /**
     * Compare les libelles des metriques.
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux libelles.
     */
    public int compareTools(MetriqueDefinitionBean o1, MetriqueDefinitionBean o2) {
        // Variable resultat.
        int result = 0;
        if (o1.getOutil() != null && o2.getOutil() != null) {
            if (this.invorder) {
                // Ordre inverse.
                result = this.collator.compare(o2.getOutil().getLib(locale), o1.getOutil().getLib(locale));
            } else {
                // Ordre universel.
                result = this.collator.compare(o1.getOutil().getLib(locale), o2.getOutil().getLib(locale));
            }
        } else {
            if(o1.getOutil() != null && o2.getOutil() == null) {
                result = -1;
            } else if(o1.getOutil() == null && o2.getOutil() != null) {
                result = 1;
            }
        }
        return result;
    }
}