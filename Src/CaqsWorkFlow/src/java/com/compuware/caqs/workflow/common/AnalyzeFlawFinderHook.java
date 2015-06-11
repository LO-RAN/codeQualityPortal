/**
 * 
 */
package com.compuware.caqs.workflow.common;

/**
 * @author cwfr-fdubois
 *
 */
public class AnalyzeFlawFinderHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=flawfinder&master=false";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "flawfinder";
	}
	
}
