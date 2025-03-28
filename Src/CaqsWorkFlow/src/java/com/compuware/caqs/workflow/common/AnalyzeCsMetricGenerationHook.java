/**
 * 
 */
package com.compuware.caqs.workflow.common;


/**
 * @author cwfr-fdubois
 *
 */
public class AnalyzeCsMetricGenerationHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=csmetricgeneration&master=true";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "csmetricgeneration";
	}

}
