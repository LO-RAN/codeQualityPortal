/**
 * 
 */
package com.compuware.caqs.workflow.common;

/**
 * @author cwfr-lizac
 *
 */
public class AnalyzeCodeAnalyzerProHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=codeanalyzerpro&master=false";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "codeanalyzerpro";
	}
	
}
