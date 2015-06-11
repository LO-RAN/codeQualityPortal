/**
 * 
 */
package com.compuware.caqs.workflow.common;

/**
 * @author cwfr-lizac
 *
 */
public class AnalyzeFlexPMDHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=flexpmd&master=true";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "flexpmd";
	}
	
}
