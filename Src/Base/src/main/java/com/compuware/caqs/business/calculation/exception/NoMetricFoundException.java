/**
 * 
 */
package com.compuware.caqs.business.calculation.exception;

/**
 * Define an exception when no metric can be found for calculation.
 * @author cwfr-fdubois
 *
 */
public class NoMetricFoundException extends CalculationPreConditionException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 4639672900060377744L;

	/**
	 * Constructor with a given message.
	 * @param message the exception message.
	 */
	public NoMetricFoundException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with a given message and an exception cause.
	 * @param message the exception message.
	 * @param cause the exception cause.
	 */
	public NoMetricFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
