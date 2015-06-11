/**
 * 
 */
package com.compuware.caqs.business.analysis.exception;


/**
 * Define an exception for pre-condition check failure.
 * @author cwfr-fdubois
 *
 */
public class AnalysisPreConditionException extends AnalysisException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 1911314276585211079L;
	
	/**
	 * @param message
	 */
	public AnalysisPreConditionException(String message) {
		super(message);
	}

}
