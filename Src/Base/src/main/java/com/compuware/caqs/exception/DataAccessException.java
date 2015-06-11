/**
 * 
 */
package com.compuware.caqs.exception;

/**
 * @author cwfr-fdubois
 *
 */
public class DataAccessException extends CaqsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -134603506907574599L;

	/**
	 * @param message
	 */
	public DataAccessException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DataAccessException(Throwable cause) {
		super(cause);
	}

}
