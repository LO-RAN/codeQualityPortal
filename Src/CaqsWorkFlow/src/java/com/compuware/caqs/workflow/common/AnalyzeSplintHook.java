/**
 * 
 */
package com.compuware.caqs.workflow.common;

/**
 * @author cwfr-fdubois
 *
 */
public class AnalyzeSplintHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=splint&master=false";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "splint";
	}
	
}
