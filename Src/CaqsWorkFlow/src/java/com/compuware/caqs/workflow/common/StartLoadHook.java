/**
 * 
 */
package com.compuware.caqs.workflow.common;

import org.ow2.bonita.definition.TxHook;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.runtime.ActivityInstance;

/**
 * @author cwfr-fdubois
 *
 */
public class StartLoadHook implements TxHook {
	
	/**
	 * 
	 */
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
		accessor.getRuntimeAPI().setProcessInstanceVariable(activity.getProcessInstanceUUID(), "step", "load");
	}
	
}
