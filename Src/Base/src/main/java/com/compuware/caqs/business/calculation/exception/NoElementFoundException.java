/**
 * 
 */
package com.compuware.caqs.business.calculation.exception;

/**
 * Define an exception when no element can be found for calculation.
 * @author cwfr-fdubois
 *
 */
public class NoElementFoundException extends CalculationPreConditionException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 108209187479358183L;

	/**
	 * Constructor with a given message.
	 * @param message the exception message.
	 */
	public NoElementFoundException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with a given message and an exception cause.
	 * @param message the exception message.
	 * @param cause the exception cause.
	 */
	public NoElementFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
