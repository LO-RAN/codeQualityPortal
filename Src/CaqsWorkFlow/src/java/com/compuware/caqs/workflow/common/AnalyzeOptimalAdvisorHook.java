/**
 * 
 */
package com.compuware.caqs.workflow.common;


/**
 * @author cwfr-fdubois
 *
 */
public class AnalyzeOptimalAdvisorHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=optimaladvisor&master=false";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}

	@Override
	protected String getAnalyzerName() {
		return "optimaladvisor";
	}
	
}
