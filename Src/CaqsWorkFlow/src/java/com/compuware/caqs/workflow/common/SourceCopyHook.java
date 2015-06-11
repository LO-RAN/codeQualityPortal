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
public class SourceCopyHook extends ProgressHook {
	
	private static final String URL = "/sourcemanager?goal=transformAndCopy";

	@Override
	protected String getActionUrl(APIAccessor accessor, ActivityInstance activity) throws Exception {
		return URL + getProjectParams(accessor, activity);
	}
	
	private String getProjectParams(APIAccessor accessor, ActivityInstance activity) throws Exception {
		StringBuilder result = new StringBuilder();
        String projectId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_ID");
        String projectName = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_NAME");
        String baselineId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_ID");
        String eaList = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "EA_LIST");
        String eaOptionList = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "EA_OPTION_LIST");
		
        result.append("&projectId=").append(projectId);
        result.append("&projectName=").append(projectName);
        result.append("&baselineId=").append(baselineId);
        result.append("&eaList=").append(eaList);
        if (eaOptionList != null && eaOptionList.length() > 0) {
        	result.append("&eaOptionList=").append(eaOptionList);
        }
        
		return result.toString();
	}
	
	protected int getMessagePercent() {
		return 65;
	}

	protected String getMessageStep() {
		return "caqs.process.step.transformandcopy";
	}

	protected String getMessageStatus() {
		return "IN_PROGRESS";
	}

}
