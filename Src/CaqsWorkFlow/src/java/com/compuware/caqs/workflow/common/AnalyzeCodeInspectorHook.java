/**
 * 
 */
package com.compuware.caqs.workflow.common;

/**
 * @author cwfr-lizac
 *
 */
public class AnalyzeCodeInspectorHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=codeinspector&master=true";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "codeinspector";
	}
	
}
