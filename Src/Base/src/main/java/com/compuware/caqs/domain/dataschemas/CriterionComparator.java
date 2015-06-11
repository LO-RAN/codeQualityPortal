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
 * Comparateur de crit�re pour une liste d'objets de classe CriterionBean.
 *
 * @author cwfr-fdubois
 */
public class CriterionComparator<T> implements Comparator<T> {

	Locale locale = null;
	
    /**
     * M�trique cible.
     */
    String metrique = null;
    
    /**
     * True si ordre inverse, false sinon.
     */
    boolean invorder = false;
    
    /**
     * La partie � trier (m�trique, nom, note).
     */
    CriterionComparatorFilterEnum part = CriterionComparatorFilterEnum.LIB_ELT;

    /** The collator for the comparison.*/
    Collator collator = null;
    
    /**
     * Cr�e une nouvelle instance de CriterionComparator.
     *
     * @param metrique le crit�re cible.
     * @param invorder ordre invers� ou non.
     * @param part     partie � trier.
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
     * Compare les crit�res cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxi�me objet.
     *
     * @return le r�sultat de la comparaison de deux crit�res.
     */
    public int compare(T o1, T o2) {
        // Variable r�sultat.
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
     * o2 deuxi�me objet.
     *
     * @return le r�sultat de la comparaison de deux noms.
     */
    public int compareDesc(Object o1, Object o2) {
        // Variable r�sultat.
        int result = 0;
        // Nom de l'�l�ment 1.
        ElementBean val1 = ((CriterionBean) o1).getElement();
        // Nom de l'�l�ment 2.
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
     * o2 deuxi�me objet.
     *
     * @return le r�sultat de la comparaison de deux noms.
     */
    public int compareNoms(Object o1, Object o2) {
        // Variable r�sultat.
        int result = 0;
        // Nom de l'�l�ment 1.
        ElementBean val1 = ((CriterionBean) o1).getElement();
        // Nom de l'�l�ment 2.
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
     * Compare les libell�s des crit�res.
     * o1 premier objet.
     * o2 deuxi�me objet.
     *
     * @return le r�sultat de la comparaison de deux libell�s.
     */
    public int compareLibCrit(Object o1, Object o2) {
        // Variable r�sultat.
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
     * Compare les libell�s des crit�res.
     * o1 premier objet.
     * o2 deuxi�me objet.
     *
     * @return le r�sultat de la comparaison de deux libell�s.
     */
    public int compareDescCrit(Object o1, Object o2) {
        // Variable r�sultat.
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
     * Compare les crit�res cibles de deux objets de classe CriterionBean.
     * o1 premier objet.
     * o2 deuxi�me objet.
     *
     * @return le r�sultat de la comparaison de deux crit�res.
     */
    public int compareMetriques(Object o1, Object o2) {
        // Variable r�sultat.
        int result = 0;
        // Valeur brute associ�e � la m�trique 1.
        Double val1 = ((CriterionBean) o1).getValbrute(this.metrique);
        // Valeur brute associ�e � la m�trique 2.
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
     * o2 deuxi�me objet.
     *
     * @return le r�sultat de la comparaison de deux notes.
     */
    public int compareNotes(Object o1, Object o2) {
        // Variable r�sultat.
        int result = 0;
        // Note du crit�re 1.
        Double val1 = new Double(((CriterionBean) o1).getNote());
        // Note du crit�re 1.
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
     * Compare les notes puis les descriptions des �l�ments pour les rapports.
     * Les descriptions et les notes sont tri�s par ordre croissant.
     * o1 premier objet.
     * o2 deuxi�me objet.
     *
     * @return le r�sultat de la comparaison.
     */
    public int compareNoteDescEltForReport(Object o1, Object o2) {
        // Variable r�sultat.
        int result = 0;
        result = Double.compare(((CriterionBean) o1).getNote(), ((CriterionBean)o2).getNote());
        if (result == 0) {
            result = ((CriterionBean) o1).getElement().compareDescTo(((CriterionBean)o2).getElement());
        }
        return result;
    }

    /**
     * Compare les notes puis les libell�s des crit�res pour les rapports.
     * Les libell�s et les notes sont tri�s par ordre croissant.
     * o1 premier objet.
     * o2 deuxi�me objet.
     *
     * @return le r�sultat de la comparaison de deux libell�s.
     */
    public int compareNoteLibCritForReport(Object o1, Object o2) {
        // Variable r�sultat.
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