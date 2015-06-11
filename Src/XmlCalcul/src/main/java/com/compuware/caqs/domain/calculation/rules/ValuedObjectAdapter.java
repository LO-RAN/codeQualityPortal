/**
 * 
 */
package com.compuware.caqs.domain.calculation.rules;

/**
 * @author cwfr-fdubois
 *
 */
public class ValuedObjectAdapter implements ValuedObject {

	private double value = 0.0;
	
	public ValuedObjectAdapter(double value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.domain.calculation.rules.ValuedObject#getValue()
	 */
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

}
