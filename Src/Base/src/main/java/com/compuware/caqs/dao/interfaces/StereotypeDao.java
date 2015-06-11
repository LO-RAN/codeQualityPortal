package com.compuware.caqs.dao.interfaces;

import java.util.Collection;

import com.compuware.caqs.domain.dataschemas.StereotypeBean;

public interface StereotypeDao {

	/**
	 * Recherche d'un stéréotype par son identifiant.
	 * @param id l'identifiant du stéréotype.
	 * @return le stéréotype trouvé ou null;
	 */
	public abstract StereotypeBean retrieveStereotypeById(java.lang.String id);

	/**
	 * Recherche de tous les stéréotypes.
	 * @return la colleciotn de tous les stéréotypes trouvés;
	 */
	public abstract Collection retrieveAllStereotype();

	/**
	 * Sauvegarde le stéréotype passé en paramètre.
	 * @param stereotypeBean le stéréotype à sauver.
	 */
	public abstract void storeStereotype(StereotypeBean stereotypeBean);

}