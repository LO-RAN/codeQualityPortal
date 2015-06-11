/*
 * CriterionComparator.java
 *
 * Created on 6 novembre 2002, 22:43
 */

package com.compuware.caqs.domain.dataschemas.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.CriterionDefinition;



/**
 * Comparateur de critere pour une liste d'objets de classe CriterionBean.
 *
 * @author cwfr-fdubois
 */
public class CriterionDefinitionComparator implements Comparator<CriterionDefinition> {

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
	public CriterionDefinitionComparator(boolean invorder, String part, Locale loc) {
		// Initialisation des attributs.
		this.invorder = invorder;
		this.part = part;
		this.locale = loc;
		this.collator = Collator.getInstance(this.locale);
	}

	/**
	 * Compare les criteres cibles de deux objets de classe CriterionDefinition.
	 * o1 premier objet.
	 * o2 deuxi�me objet.
	 * @return le resultat de la comparaison de deux crit�res.
	 */
	public int compare(CriterionDefinition o1, CriterionDefinition o2) {
		// Variable r�sultat.
		int result = 0;
		if("lib".equals(this.part)) {
			result = compareLib(o1, o2);
		} else if("weight".equals(this.part)) {
			result = comparePoids(o1, o2);
		} else if("telt".equals(this.part)) {
			result = compareTelt(o1, o2);
		} else if("id".equals(this.part)) {
			result = compareId(o1, o2);
		}
		return result;
	}
	
	/**
	 * Compare les poids des crit�res.
	 * @param o1 premier objet.
	 * @param o2 deuxieme objet.
	 *
	 * @return le resultat de la comparaison de deux libelles.
	 */
	public int compareTelt(CriterionDefinition o1, CriterionDefinition o2) {
		// Variable r�sultat.
		int result = 0;
		if (!this.invorder) {
			result = o1.getElementType().getId().compareTo(o2.getElementType().getId());
		} else {
			result = o2.getElementType().getId().compareTo(o1.getElementType().getId());
		}
		return result;
	}

	/**
	 * Compare les id des crit�res.
	 * @param o1 premier objet.
	 * @param o2 deuxieme objet.
	 *
	 * @return le resultat de la comparaison de deux ids.
	 */
	public int compareId(CriterionDefinition o1, CriterionDefinition o2) {
		// Variable r�sultat.
		int result = 0;
		if (!this.invorder) {
			result = o1.getId().compareTo(o2.getId());
		} else {
			result = o2.getId().compareTo(o1.getId());
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
	public int compareLib(CriterionDefinition o1, CriterionDefinition o2) {
		// Variable resultat.
		int result = 0;
		if (!this.invorder) {
			// Ordre universel.
			result = this.collator.compare(o1.getLib(locale), o2.getLib(locale));
		}
		else {
			// Ordre inverse.
			result = this.collator.compare(o2.getLib(locale), o1.getLib(locale));
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
	public int comparePoids(CriterionDefinition o1, CriterionDefinition o2) {
		int result = 0;
		Double poids1 = new Double(o1.getWeight());
		Double poids2 = new Double(o2.getWeight());
		if (!this.invorder) {
			// Ordre universel.
			result = poids1.compareTo(poids2);
		}
		else {
			// Ordre inverse.
			result = poids2.compareTo(poids1);
		}
		return result;
        }

}