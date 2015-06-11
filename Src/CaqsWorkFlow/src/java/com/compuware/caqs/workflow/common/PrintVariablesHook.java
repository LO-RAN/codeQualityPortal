package com.compuware.caqs.workflow.common;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ow2.bonita.definition.Hook;
import org.ow2.bonita.facade.QueryAPIAccessor;
import org.ow2.bonita.facade.runtime.ActivityInstance;
import org.ow2.bonita.facade.runtime.ProcessInstance;

/**
 * Print all variable values.
 * @author cwfr-fdubois
 *
 */
public class PrintVariablesHook implements Hook {

	/**
	 * 
	 */
	public void execute(QueryAPIAccessor accessor, ActivityInstance activity) throws Exception {
		StringBuilder sbuilder = new StringBuilder("Activity ID: ");
		sbuilder.append(activity.getActivityName()).append('\n');
		
		ProcessInstance processInstance = accessor.getQueryRuntimeAPI().getProcessInstance(activity.getProcessInstanceUUID());
		sbuilder.append("Process ID: ").append(processInstance.getProcessInstanceUUID()).append('\n');
		
		Map<String, Object> variableMap = processInstance.getLastKnownVariableValues();
		if (variableMap != null) {
			Set<String> keySet = variableMap.keySet();
			Iterator<String> keyIter = keySet.iterator();
			while(keyIter.hasNext()) {
				String key = keyIter.next();
				sbuilder.append(key).append(':').append(variableMap.get(key)).append('\n');
			}
		}
		System.out.println(sbuilder.toString());
		Thread.sleep(10000);
	}

}
