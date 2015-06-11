/**
 * 
 */
package com.compuware.caqs.business.analysis.exception;

import com.compuware.caqs.exception.CaqsException;

/**
 * Define an exception for analysis failure.
 * @author cwfr-fdubois
 *
 */
public class AnalysisException extends CaqsException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -4210826524366989365L;
	
	/**
	 * @param message
	 */
	public AnalysisException(String message) {
		super(message);
	}

	/**
	 * @param message
	 */
	public AnalysisException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * @param t
	 */
	public AnalysisException(Throwable t) {
		super(t);
	}

}
