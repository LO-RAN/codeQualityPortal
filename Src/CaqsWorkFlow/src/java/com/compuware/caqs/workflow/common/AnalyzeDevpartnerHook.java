/**
 * 
 */
package com.compuware.caqs.workflow.common;

import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.runtime.ActivityInstance;

/**
 * @author cwfr-fdubois
 *
 */
public class AnalyzeDevpartnerHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=devpartner&master=true";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "devpartner";
	}
	
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
            useAlternateServerContext=true;
            useSecondAlternateServerContext=true;
            super.execute(accessor, activity);
	}
}
