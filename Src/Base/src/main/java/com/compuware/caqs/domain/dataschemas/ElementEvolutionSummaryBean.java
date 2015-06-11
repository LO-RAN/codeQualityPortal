/**
 * 
 */
package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

/**
 * @author cwfr-fdubois
 *
 */
public class ElementEvolutionSummaryBean extends ElementBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -52661887130747515L;
	
	int nbCriterions = 0;
	
	/**
	 * 
	 */
	public ElementEvolutionSummaryBean() {
		super();
	}

	/**
	 * @param baseline
	 * @param id
	 * @param idTelt
	 * @param lib
	 */
	public ElementEvolutionSummaryBean(BaselineBean baseline, 
            String id, String idTelt, String lib) {
		super(baseline, id, idTelt, lib);
	}

	/**
	 * @return Returns the nbCriterions.
	 */
	public int getNbCriterions() {
		return nbCriterions;
	}

	/**
	 * @param nbCriterions The nbCriterions to set.
	 */
	public void setNbCriterions(int nbCriterions) {
		this.nbCriterions = nbCriterions;
	}

}
