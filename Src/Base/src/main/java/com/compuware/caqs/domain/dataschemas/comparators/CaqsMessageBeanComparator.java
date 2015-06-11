/*
 * CriterionComparator.java
 *
 * Created on 6 novembre 2002, 22:43
 */
package com.compuware.caqs.domain.dataschemas.comparators;

import java.util.Comparator;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import java.sql.Timestamp;

/**
 * Comparateur de critere pour une liste d'objets de classe CriterionBean.
 *
 * @author cwfr-fdubois
 */
public class CaqsMessageBeanComparator implements Comparator<CaqsMessageBean> {

    private Locale locale = null;
    /**
     * True si ordre inverse, false sinon.
     */
    private boolean invorder = false;
    /**
     * La partie a trier.
     */
    private String part = "endDate";

    /**
     * Cree une nouvelle instance de CriterionComparator.
     *
     * @param metrique le message cible.
     * @param invorder ordre inverse ou non.
     * @param part     partie a trier.
     */
    public CaqsMessageBeanComparator(boolean invorder, String part, Locale loc) {
        // Initialisation des attributs.
        this.invorder = invorder;
        this.part = part;
        this.locale = loc;
    }

    /**
     * Compare les criteres cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux criteres.
     */
    public int compare(CaqsMessageBean o1, CaqsMessageBean o2) {
        int result = 0;
        if ("dates".equals(this.part)) {
            result = compareByDates(o1, o2);
        }
        return result;
    }

    /**
     * Compare les messages par date de finition
     * @param o1 premier objet.
     * @param o2 deuxieme objet.
     * @return le resultat de la comparaison des deux messages.
     */
    private int compareByDates(CaqsMessageBean o1, CaqsMessageBean o2) {
        int result = 0;
        Timestamp endDate1 = o1.getEndDate();
        Timestamp endDate2 = o2.getEndDate();
        if (endDate1 != null && endDate2 != null) {
            result = endDate1.compareTo(endDate2);
        } else if(endDate1==null && endDate2==null) {
            result = o1.getBeginDate().compareTo(o2.getBeginDate());
        } else {
            result = (endDate1 == null && endDate2 != null) ? -1 : (endDate1 != null && endDate2 == null) ? 1 : 0;
        }
        if (this.invorder) {
            result = result * -1;
        }
        return result;
    }
}