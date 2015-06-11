/**
 * 
 */
package com.compuware.caqs.business.calculation.exception;

import com.compuware.caqs.exception.CaqsException;

/**
 * Define a exception for pre-condition check failure.
 * @author cwfr-fdubois
 *
 */
public class CalculationPreConditionException extends CaqsException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 1911314276585211079L;
	
	/**
	 * Constructor with a given message.
	 * @param message the exception message.
	 */
	public CalculationPreConditionException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with a given message and an exception cause.
	 * @param message the exception message.
	 * @param cause the exception cause.
	 */
	public CalculationPreConditionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor with an exception cause.
	 * @param cause the exception cause.
	 */
	public CalculationPreConditionException(Throwable cause) {
		super(cause);
	}
	
}
