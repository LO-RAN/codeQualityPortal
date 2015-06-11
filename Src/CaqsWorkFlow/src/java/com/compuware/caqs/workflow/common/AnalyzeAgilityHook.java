/**
 * 
 */
package com.compuware.caqs.workflow.common;


/**
 * @author cwfr-lizac
 *
 */
public class AnalyzeAgilityHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=agility&master=false";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "agility";
	}
	
}
