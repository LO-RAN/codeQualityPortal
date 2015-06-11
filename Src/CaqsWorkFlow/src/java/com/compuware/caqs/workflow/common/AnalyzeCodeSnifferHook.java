/**
 * 
 */
package com.compuware.caqs.workflow.common;

/**
 * @author cwfr-fdubois
 *
 */
public class AnalyzeCodeSnifferHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=phpcs&master=true";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "phpcs";
	}

}
