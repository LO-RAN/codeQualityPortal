/**
 * 
 */
package com.compuware.toolbox.exception;

/**
 * @author cwfr-fdubois
 *
 */
public class SystemIOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5386995567786247016L;

	/**
	 * @param message
	 * @param cause
	 */
	public SystemIOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public SystemIOException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SystemIOException(Throwable cause) {
		super(cause);
	}

}
