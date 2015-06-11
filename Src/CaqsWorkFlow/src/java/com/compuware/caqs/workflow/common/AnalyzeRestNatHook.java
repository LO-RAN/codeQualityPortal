/**
 * 
 */
package com.compuware.caqs.workflow.common;

import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.runtime.ActivityInstance;

/**
 * @author cwfr-lizac
 *
 */
public class AnalyzeRestNatHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=restnat&master=true";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "restnat";
	}
	
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
            useOnlySecondAlternateServerContext=true;
            super.execute(accessor, activity);
	}
}
