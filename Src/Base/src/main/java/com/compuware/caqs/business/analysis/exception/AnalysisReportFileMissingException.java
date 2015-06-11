/**
 * 
 */
package com.compuware.caqs.business.analysis.exception;

/**
 * Define an exception when an analysis report file is missing.
 * @author cwfr-fdubois
 *
 */
public class AnalysisReportFileMissingException extends AnalysisPostConditionException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 2284820950799092564L;

	/**
	 * Constructor.
	 * @param message the exception message.
	 */
	public AnalysisReportFileMissingException(String message) {
		super(message);
	}

}
