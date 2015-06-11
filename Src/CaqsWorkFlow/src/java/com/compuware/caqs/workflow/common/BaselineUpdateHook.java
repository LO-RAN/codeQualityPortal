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
public class BaselineUpdateHook extends ProgressHook {
	
	private static final String URL = "/baseline?action=1";

	@Override
	protected String getActionUrl(APIAccessor accessor, ActivityInstance activity) throws Exception {
		return URL + getProjectParams(accessor, activity);
	}
	
	private String getProjectParams(APIAccessor accessor, ActivityInstance activity) throws Exception {
		StringBuilder result = new StringBuilder();
        String projectId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_ID");
        String projectName = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_NAME");
        String baselineId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_ID");
        String baselineName = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_NAME");
		if (baselineName == null || baselineName.length() < 1) {
			baselineName = baselineId;
		}
        
        result.append("&projectId=").append(projectId);
        result.append("&projectName=").append(projectName);
        result.append("&baselineId=").append(baselineId);
        result.append("&baselineName=").append(baselineName);
        
		return result.toString();
	}

	protected int getMessagePercent() {
		return 90;
	}

	protected String getMessageStep() {
		return "caqs.process.step.updatebaseline";
	}

	protected String getMessageStatus() {
		return "IN_PROGRESS";
	}

}
