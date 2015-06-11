/**
 * 
 */
package com.compuware.caqs.business.analysis.exception;


/**
 * Define an exception for post-condition check failure.
 * @author cwfr-fdubois
 *
 */
public class AnalysisPostConditionException extends AnalysisException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -3784342514589090201L;
	
	/**
	 * @param message
	 */
	public AnalysisPostConditionException(String message) {
		super(message);
	}

}
