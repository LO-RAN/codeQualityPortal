/**
 * 
 */
package com.compuware.caqs.workflow.common;

/**
 * @author cwfr-lizac
 *
 */
public class AnalyzeCppCheckHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=cppcheck&master=false";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "cppcheck";
	}
	
}
