/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.calcul;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;

/**
 * Define an association between a module (EA) and a baseline.
 * @author cwfr-fdubois
 *
 */
public class ModuleBaselineAssoc {

	/** The module element. */
	private ElementBean element = null;
	
	/** The associated baseline. */
	private BaselineBean baseline = null;
	
	/**
	 * Constructor for the association.
	 * @param element the module element.
	 * @param baseline the associated baseline.
	 */
	public ModuleBaselineAssoc(ElementBean element, BaselineBean baseline) {
		this.element = element;
		this.baseline = baseline;
	}

	/**
	 * @return the element
	 */
	public ElementBean getElement() {
		return element;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(ElementBean element) {
		this.element = element;
	}

	/**
	 * @return the baseline
	 */
	public BaselineBean getBaseline() {
		return baseline;
	}

	/**
	 * @param baseline the baseline to set
	 */
	public void setBaseline(BaselineBean baseline) {
		this.baseline = baseline;
	}
	
}
