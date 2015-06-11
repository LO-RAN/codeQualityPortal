package com.compuware.caqs.dao.interfaces;

import java.util.Collection;

import com.compuware.caqs.domain.dataschemas.StereotypeBean;

public interface StereotypeDao {

	/**
	 * Recherche d'un st�r�otype par son identifiant.
	 * @param id l'identifiant du st�r�otype.
	 * @return le st�r�otype trouv� ou null;
	 */
	public abstract StereotypeBean retrieveStereotypeById(java.lang.String id);

	/**
	 * Recherche de tous les st�r�otypes.
	 * @return la colleciotn de tous les st�r�otypes trouv�s;
	 */
	public abstract Collection retrieveAllStereotype();

	/**
	 * Sauvegarde le st�r�otype pass� en param�tre.
	 * @param stereotypeBean le st�r�otype � sauver.
	 */
	public abstract void storeStereotype(StereotypeBean stereotypeBean);

}