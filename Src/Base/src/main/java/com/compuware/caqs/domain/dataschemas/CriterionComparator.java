/*
 * CriterionComparator.java
 *
 * Created on 6 novembre 2002, 22:43
 */

package com.compuware.caqs.domain.dataschemas;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;


/**
 * Comparateur de critère pour une liste d'objets de classe CriterionBean.
 *
 * @author cwfr-fdubois
 */
public class CriterionComparator<T> implements Comparator<T> {

	Locale locale = null;
	
    /**
     * Métrique cible.
     */
    String metrique = null;
    
    /**
     * True si ordre inverse, false sinon.
     */
    boolean invorder = false;
    
    /**
     * La partie à trier (métrique, nom, note).
     */
    CriterionComparatorFilterEnum part = CriterionComparatorFilterEnum.LIB_ELT;

    /** The collator for the comparison.*/
    Collator collator = null;
    
    /**
     * Crée une nouvelle instance de CriterionComparator.
     *
     * @param metrique le critère cible.
     * @param invorder ordre inversé ou non.
     * @param part     partie à trier.
     */
    public CriterionComparator(String metrique, boolean invorder, CriterionComparatorFilterEnum part, Locale loc) {
        // Initialisation des attributs.
        this.metrique = metrique;
        this.invorder = invorder;
        this.part = part;
        this.locale = loc;
        this.collator = Collator.getInstance(this.locale);
    }

    /**
     * Compare les critères cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxième objet.
     *
     * @return le résultat de la comparaison de deux critères.
     */
    public int compare(T o1, T o2) {
        // Variable résultat.
        int result = 0;
        switch (this.part) {
        case LIB_CRIT:
            result = compareLibCrit(o1, o2);
            break;
        case DESC_CRIT:
            result = compareDescCrit(o1, o2);
            break;
        case LIB_ELT:
            result = compareNoms(o1, o2);
            break;
        case DESC_ELT:
            result = compareDesc(o1, o2);
            break;
        case METRIC:
            result = compareMetriques(o1, o2);
            break;
        case NOTE_CRIT:
            result = compareNotes(o1, o2);
            break;
        case NOTE_DESC_ELT_REPORT:
            result = compareNoteDescEltForReport(o1, o2);
            break;
        case NOTE_LIB_CRIT:
            result = compareNoteLibCritForReport(o1, o2);
            break;
        default:
        	result = 0;
        }
        return result;
    }

    /**
     * Compare les noms cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxième objet.
     *
     * @return le résultat de la comparaison de deux noms.
     */
    public int compareDesc(Object o1, Object o2) {
        // Variable résultat.
        int result = 0;
        // Nom de l'élément 1.
        ElementBean val1 = ((CriterionBean) o1).getElement();
        // Nom de l'élément 2.
        ElementBean val2 = ((CriterionBean) o2).getElement();
        if (!this.invorder) {
        // Ordre universel.
            result = val1.compareDescTo(val2);
        }
        else {
        // Ordre inverse.
            result = val2.compareDescTo(val1);
        }
        return result;
    }

    /**
     * Compare les noms cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxième objet.
     *
     * @return le résultat de la comparaison de deux noms.
     */
    public int compareNoms(Object o1, Object o2) {
        // Variable résultat.
        int result = 0;
        // Nom de l'élément 1.
        ElementBean val1 = ((CriterionBean) o1).getElement();
        // Nom de l'élément 2.
        ElementBean val2 = ((CriterionBean) o2).getElement();
        if (!this.invorder) {
        // Ordre universel.
            result = val1.compareLibTo(val2);
        }
        else {
        // Ordre inverse.
            result = val2.compareLibTo(val1);
        }
        return result;
    }

    /**
     * Compare les libellés des critères.
     * o1 premier objet.
     * o2 deuxième objet.
     *
     * @return le résultat de la comparaison de deux libellés.
     */
    public int compareLibCrit(Object o1, Object o2) {
        // Variable résultat.
        int result = 0;
        // Definition 1.
        CriterionDefinition val1 = ((CriterionBean) o1).getCriterionDefinition();
        // Definition 2.
        CriterionDefinition val2 = ((CriterionBean) o2).getCriterionDefinition();
        if (!this.invorder) {
        // Ordre universel.
            result = this.collator.compare(val1.getLib(locale), val2.getLib(locale));
        }
        else {
        // Ordre inverse.
            result = this.collator.compare(val2.getLib(locale), val1.getLib(locale));
        }
        return result;
    }

    /**
     * Compare les libellés des critères.
     * o1 premier objet.
     * o2 deuxième objet.
     *
     * @return le résultat de la comparaison de deux libellés.
     */
    public int compareDescCrit(Object o1, Object o2) {
        // Variable résultat.
        int result = 0;
        // Definition 1.
        CriterionDefinition val1 = ((CriterionBean) o1).getCriterionDefinition();
        // Definition 2.
        CriterionDefinition val2 = ((CriterionBean) o2).getCriterionDefinition();
        if (!this.invorder) {
        // Ordre universel.
            result = this.collator.compare(val1.getDesc(locale), val2.getDesc(locale));
        }
        else {
        // Ordre inverse.
            result = this.collator.compare(val2.getDesc(locale), val1.getDesc(locale));
        }
        return result;
    }

    /**
     * Compare les critères cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxième objet.
     *
     * @return le résultat de la comparaison de deux critères.
     */
    public int compareMetriques(Object o1, Object o2) {
        // Variable résultat.
        int result = 0;
        // Valeur brute associée à la métrique 1.
        Double val1 = ((CriterionBean) o1).getValbrute(this.metrique);
        // Valeur brute associée à la métrique 2.
        Double val2 = ((CriterionBean) o2).getValbrute(this.metrique);
        if (!this.invorder) {
        // Ordre universel.
        	if (val1 == null) {
        		result = -1;
        	}
        	else {
        		result = val1.compareTo(val2);
        	}
        }
        else {
        // Ordre inverse.
        	if (val2 == null) {
        		result = -1;
        	}
        	else {
        		result = val2.compareTo(val1);
        	}
        }
        return result;
    }

    /**
     * Compare les notes cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxième objet.
     *
     * @return le résultat de la comparaison de deux notes.
     */
    public int compareNotes(Object o1, Object o2) {
        // Variable résultat.
        int result = 0;
        // Note du critère 1.
        Double val1 = new Double(((CriterionBean) o1).getNote());
        // Note du critère 1.
        Double val2 = new Double(((CriterionBean) o2).getNote());
        if (!this.invorder) {
        // Ordre universel.
            result = val1.compareTo(val2);
        }
        else {
        // Ordre inverse.
            result = val2.compareTo(val1);
        }
        return result;
    }

    /**
     * Compare les notes puis les descriptions des éléments pour les rapports.
     * Les descriptions et les notes sont triés par ordre croissant.
     * o1 premier objet.
     * o2 deuxième objet.
     *
     * @return le résultat de la comparaison.
     */
    public int compareNoteDescEltForReport(Object o1, Object o2) {
        // Variable résultat.
        int result = 0;
        result = Double.compare(((CriterionBean) o1).getNote(), ((CriterionBean)o2).getNote());
        if (result == 0) {
            result = ((CriterionBean) o1).getElement().compareDescTo(((CriterionBean)o2).getElement());
        }
        return result;
    }

    /**
     * Compare les notes puis les libellés des critères pour les rapports.
     * Les libellés et les notes sont triés par ordre croissant.
     * o1 premier objet.
     * o2 deuxième objet.
     *
     * @return le résultat de la comparaison de deux libellés.
     */
    public int compareNoteLibCritForReport(Object o1, Object o2) {
        // Variable résultat.
        int result = 0;
        result = Double.compare(((CriterionBean) o1).getNote(), ((CriterionBean)o2).getNote());
        if (result == 0) {
        	CriterionDefinition def1 = ((CriterionBean) o1).getCriterionDefinition();
        	CriterionDefinition def2 = ((CriterionBean) o2).getCriterionDefinition();
            result = this.collator.compare(def1.getLib(locale), def2.getLib(locale));
        }
        return result;
    }


}