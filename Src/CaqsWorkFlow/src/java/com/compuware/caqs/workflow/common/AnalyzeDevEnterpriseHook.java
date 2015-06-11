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
public class AnalyzeDevEnterpriseHook extends AnalyzeHook {

	private static final String URL_PARAMS = "tool=deventerprise&master=true";

	@Override
	protected String getUrlParams() {
		return URL_PARAMS;
	}
	
	@Override
	protected String getAnalyzerName() {
		return "deventerprise";
	}
	
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
            useAlternateServerContext=true;
            super.execute(accessor, activity);
	}
}
