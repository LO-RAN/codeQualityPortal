/*
 * CriterionComparator.java
 *
 * Created on 6 novembre 2002, 22:43
 */

package com.compuware.caqs.domain.dataschemas.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.CriterionPerFactorBean;
import com.compuware.caqs.presentation.util.StringFormatUtil;



/**
 * Comparateur de critère pour une liste d'objets de classe CriterionBean.
 *
 * @author cwfr-fdubois
 */
public class CriterionPerFactorBeanComparator implements Comparator<CriterionPerFactorBean> {

	Locale locale = null;

	/**
	 * True si ordre inverse, false sinon.
	 */
	boolean invorder = false;

	/**
	 * La partie à trier.
	 */
	String part = "lib";

	/** The collator for the comparison.*/
	Collator collator = null;

	/**
	 * Crée une nouvelle instance de CriterionComparator.
	 *
	 * @param metrique le critère cible.
	 * @param invorder ordre inversé ou non.
	 * @param part     partie à trier.
	 */
	public CriterionPerFactorBeanComparator(boolean invorder, String part, Locale loc) {
		// Initialisation des attributs.
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
	public int compare(CriterionPerFactorBean o1, CriterionPerFactorBean o2) {
		// Variable résultat.
		int result = 0;
		if("lib".equals(this.part)) {
			result = compareLibCrit(o1, o2);
		} else if("tendance".equals(this.part)) {
			result = compareTendanceCrit(o1, o2);
		} else if("note".equals(this.part)) {
			result = compareNotes(o1, o2);
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
	public int compareTendanceCrit(CriterionPerFactorBean o1, CriterionPerFactorBean o2) {
		// Variable résultat.
		int result = 0;
		double note1 = (o1.getNote() != null) ? o1.getNote().doubleValue() : 0.0;
		double tendance1 = o1.getTendance();

		String tendanceLabel1 = StringFormatUtil.getTendanceLabel(tendance1, note1);

		double note2 = (o2.getNote() != null) ? o2.getNote().doubleValue() : 0.0;
		double tendance2 = o2.getTendance();

		String tendanceLabel2 = StringFormatUtil.getTendanceLabel(tendance2, note2);

		if (!this.invorder) {
			// Ordre universel.
			result = tendanceLabel1.compareTo(tendanceLabel2);
		}
		else {
			// Ordre inverse.
			result = tendanceLabel2.compareTo(tendanceLabel1);
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
	public int compareLibCrit(CriterionPerFactorBean o1, CriterionPerFactorBean o2) {
		// Variable résultat.
		int result = 0;
		if (!this.invorder) {
			// Ordre universel.
			result = this.collator.compare(o1.getLib(locale), o2.getLib(locale));
		}
		else {
			// Ordre inverse.
			result = this.collator.compare(o1.getLib(locale), o2.getLib(locale));
		}
		return result;
	}

	/**
	 * Compare les notes cibles de deux objets de classe CriterionPerFactorBean.
	 * o1 premier objet.
	 * o2 deuxième objet.
	 *
	 * @return le résultat de la comparaison de deux notes.
	 */
	public int compareNotes(CriterionPerFactorBean o1, CriterionPerFactorBean o2) {
		// Variable résultat.
		int result = 0;
		// Note du critère 1.
		Double val1 = (o1.getNote()!=null) ? new Double(o1.getNote()) : new Double(0.0);
		// Note du critère 1.
		Double val2 = (o2.getNote()!=null) ? new Double(o2.getNote()) : new Double(0.0);
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