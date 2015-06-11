/**
 * 
 */
package com.compuware.caqs.business.analysis.exception;

/**
 * Define an exception when no source file can be found.
 * @author cwfr-fdubois
 *
 */
public class SourceFileNotFoundException extends AnalysisPreConditionException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 2284820950799092564L;

	/**
	 * Constructor.
	 * @param message the exception message.
	 */
	public SourceFileNotFoundException(String message) {
		super(message);
	}
	
}
