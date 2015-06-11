/**
 * 
 */
package com.compuware.caqs.workflow.common;

/**
 * @author cwfr-fdubois
 *
 */
public class AnalyzeGenericParserHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=genericparser&master=false";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "genericparser";
	}

}
