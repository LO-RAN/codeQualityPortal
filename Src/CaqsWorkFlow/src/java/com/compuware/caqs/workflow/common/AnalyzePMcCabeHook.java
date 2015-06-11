/**
 * 
 */
package com.compuware.caqs.workflow.common;

/**
 * @author cwfr-fdubois
 *
 */
public class AnalyzePMcCabeHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=pmccabe&master=true";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "pmccabe";
	}

}
