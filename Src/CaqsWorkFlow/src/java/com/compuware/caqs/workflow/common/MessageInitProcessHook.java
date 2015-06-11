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
public class MessageInitProcessHook extends HttpCallHook {
	
	private static final String URL = "/message?";

	/**
	 * 
	 */
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
		setUrl(getMessageUrl(accessor, activity));
		super.execute(accessor, activity, false);
	}
	
	protected String getMessageUrl(APIAccessor accessor, ActivityInstance activity) throws Exception {
		return URL + getMessageParams(accessor, activity);
	}
	
	protected String getMessageParams(APIAccessor accessor, ActivityInstance activity) throws Exception {
		StringBuilder result = new StringBuilder();
		result.append("step=caqs.process.step.wait");
		result.append("&status=IN_PROGRESS");
		result.append("&percent=0");

		String projectId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_ID");
        String projectName = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_NAME");
        String baselineId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_ID");
        String userId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "USER_ID");
		
        result.append("&projectId=").append(projectId);
        result.append("&projectName=").append(projectName);
        result.append("&baselineId=").append(baselineId);
        result.append("&userId=").append(userId);
        
		return result.toString();
	}
	
}
