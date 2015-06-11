/*
 * CriterionComparator.java
 *
 * Created on 6 novembre 2002, 22:43
 */

package com.compuware.caqs.domain.dataschemas.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.BottomUpSummaryBean;



/**
 * Comparateur de crit�re pour une liste d'objets de classe CriterionBean.
 *
 * @author cwfr-fdubois
 */
public class BottomUpSummaryBeanComparator implements Comparator<BottomUpSummaryBean> {

	Locale locale = null;

	/**
	 * True si ordre inverse, false sinon.
	 */
	boolean invorder = false;

	/**
	 * La partie � trier.
	 */
	String part = "lib";

	/** The collator for the comparison.*/
	Collator collator = null;

	/**
	 * Cr�e une nouvelle instance de CriterionComparator.
	 *
	 * @param metrique le crit�re cible.
	 * @param invorder ordre invers� ou non.
	 * @param part     partie � trier.
	 */
	public BottomUpSummaryBeanComparator(boolean invorder, String part, Locale loc) {
		// Initialisation des attributs.
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
	public int compare(BottomUpSummaryBean o1, BottomUpSummaryBean o2) {
		// Variable r�sultat.
		int result = 0;
		if("lib".equals(this.part)) {
			result = compareLib(o1, o2);
		} else if("note1".equals(this.part)) {
			result = compareNotes(o1, o2, 0);
		} else if("note2".equals(this.part)) {
			result = compareNotes(o1, o2, 1);
		} else if("note3".equals(this.part)) {
			result = compareNotes(o1, o2, 2);
		} else if("note4".equals(this.part)) {
			result = compareNotes(o1, o2, 3);
		}
		return result;
	}

	/**
	 * Compare les libell�s des crit�res.
	 * @param o1 premier objet.
	 * @param o2 deuxi�me objet.
	 *
	 * @return le r�sultat de la comparaison de deux libell�s.
	 */
	public int compareLib(BottomUpSummaryBean o1, BottomUpSummaryBean o2) {
		// Variable r�sultat.
		int result = 0;
		if (!this.invorder) {
			// Ordre universel.
			result = this.collator.compare(o1.getElement().getLib(), o2.getElement().getLib());
		}
		else {
			// Ordre inverse.
			result = this.collator.compare(o1.getElement().getLib(), o2.getElement().getLib());
		}
		return result;
	}

	/**
	 * Compare les notes cibles de deux objets de classe CriterionPerFactorBean.
	 * @param o1 premier objet.
	 * @param o2 deuxi�me objet.
	 * @param note la note � comparer
	 * @return le r�sultat de la comparaison de deux notes.
	 */
	public int compareNotes(BottomUpSummaryBean o1, BottomUpSummaryBean o2, int note) {
		// Variable r�sultat.
		int result = 0;
		// Note du crit�re 1.
		Double val1 = new Double(o1.getNote(note));
		// Note du crit�re 1.
		Double val2 = new Double(o2.getNote(note));
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

}