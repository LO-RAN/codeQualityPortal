/**
 * 
 */
package com.compuware.caqs.workflow.common;


/**
 * @author cwfr-lizac
 *
 */
public class AnalyzeJspParserHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=jspanalyzer&master=true";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "csmetricgeneration";
	}

}
