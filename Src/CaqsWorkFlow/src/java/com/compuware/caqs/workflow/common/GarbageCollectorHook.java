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
public class GarbageCollectorHook extends HttpCallHook {
	
	private static final String URL = "/managetasks?action=DELETE_ELEMENTS";

	/**
	 * 
	 */
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
		setUrl(URL);
		super.execute(accessor, activity, false);
		accessor.getRuntimeAPI().startTask(activity.getUUID(), false);
		accessor.getRuntimeAPI().finishTask(activity.getUUID(), false);
	}
	
}
