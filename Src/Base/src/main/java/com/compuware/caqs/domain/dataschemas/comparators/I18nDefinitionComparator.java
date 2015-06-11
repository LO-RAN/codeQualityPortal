/*
 * CriterionComparator.java
 *
 * Created on 6 novembre 2002, 22:43
 */

package com.compuware.caqs.domain.dataschemas.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.compuware.toolbox.util.resources.Internationalizable;


/**
 * Comparateur de d�finition pour une liste d'objets impl�mentant Internationalizable.
 *
 * @author cwfr-fdubois
 */
public class I18nDefinitionComparator implements Comparator<Internationalizable> {

	Locale locale = null;
	
    /**
     * True si ordre inverse, false sinon.
     */
    boolean invorder = false;
    
    /**
     * La partie � trier (lib, desc, compl).
     */
    I18nDefinitionComparatorFilterEnum part = I18nDefinitionComparatorFilterEnum.LIB;

    /** The collator for the comparison.*/
    Collator collator = null;
    
    /**
     * Cree une nouvelle instance de I18nDefinitionComparator.
     * @param invorder ordre inverse ou non.
     * @param part     partie a trier.
     */
    public I18nDefinitionComparator(boolean invorder, I18nDefinitionComparatorFilterEnum part, Locale loc) {
        // Initialisation des attributs.
        this.invorder = invorder;
        this.part = part;
        this.locale = loc;
        this.collator = Collator.getInstance(this.locale);
    }

    /**
     * Compare les deux objets en fonction de la locale et de la partie concernee.
     * o1 premier objet.
     * o2 deuxieme objet.
     *
     * @return le resultat de la comparaison de deux objects.
     */
    public int compare(Internationalizable o1, Internationalizable o2) {
        // Variable resultat.
        int result = 0;
        String first = null;
        String second = null;
        
        switch (this.part) {
        case ID:
            first = o1.getId();
            second = o2.getId();
            break;
        case LIB:
            first = o1.getLib(locale);
            second = o2.getLib(locale);
            break;
        case DESC:
            first = o1.getDesc(locale);
            second = o2.getDesc(locale);
            break;
        case COMPL:
            first = o1.getComplement(locale);
            second = o2.getComplement(locale);
            break;
        default:
        	result = 0;
        }
        if (first != null) {
            if (!this.invorder) {
            	result = this.collator.compare(first, second);
            }
            else {
            	result = this.collator.compare(second, first);
            }
        }
        return result;
    }

}