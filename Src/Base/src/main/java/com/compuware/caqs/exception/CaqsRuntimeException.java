/**
 * 
 */
package com.compuware.caqs.exception;

/**
 * @author cwfr-fdubois
 *
 */
public class CaqsRuntimeException extends CaqsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6984697525518562989L;

	/**
	 * 
	 */
	public CaqsRuntimeException() {
		super();
	}

	/**
	 * @param message
	 */
	public CaqsRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CaqsRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public CaqsRuntimeException(Throwable cause) {
		super(cause);
	}

}
