/**
 * 
 */
package com.compuware.caqs.business.calculation.exception;

import com.compuware.caqs.exception.CaqsException;

/**
 * Define a exception for post-condition check failure.
 * @author cwfr-fdubois
 *
 */
public class CalculationPostConditionException extends CaqsException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -3784342514589090201L;

	/**
	 * Constructor with a given message.
	 * @param message the exception message.
	 */
	public CalculationPostConditionException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with a given message and an exception cause.
	 * @param message the exception message.
	 * @param cause the exception cause.
	 */
	public CalculationPostConditionException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
