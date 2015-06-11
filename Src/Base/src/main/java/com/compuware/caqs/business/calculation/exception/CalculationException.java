/**
 * 
 */
package com.compuware.caqs.business.calculation.exception;

import com.compuware.caqs.exception.CaqsException;

/**
 * Define an exception for calculation failure.
 * @author cwfr-fdubois
 *
 */
public class CalculationException extends CaqsException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -4210826524366989365L;
	
	/**
	 * Constructor with a given message.
	 * @param message the exception message.
	 */
	public CalculationException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with a given message and an exception cause.
	 * @param message the exception message.
	 * @param cause the exception cause.
	 */
	public CalculationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor with an exception cause.
	 * @param cause the exception cause.
	 */
	public CalculationException(Throwable cause) {
		super(cause);
	}
	
}
