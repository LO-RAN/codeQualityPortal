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
public abstract class AnalyzeHook extends ProgressHook {
	
	private static final String URL = "/analysestatique?";
	private boolean isLoad = false;

	/**
	 * 
	 */
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
		setIsLoad(accessor, activity);
		super.execute(accessor, activity);
	}
	
	@Override
	protected String getActionUrl(APIAccessor accessor, ActivityInstance activity) throws Exception {
		return URL + getUrlParams() + getProjectParams(accessor, activity);
	}
	
	protected abstract String getUrlParams();
	
	private String getProjectParams(APIAccessor accessor, ActivityInstance activity) throws Exception {
		StringBuilder result = new StringBuilder();
        String projectId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_ID");
        String projectName = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_NAME");
        String baselineId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_ID");
        String step = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "step");
        String eaList = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "EA_LIST");
        String eaOptionList = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "EA_OPTION_LIST");
		
        result.append("&projectId=").append(projectId);
        result.append("&projectName=").append(projectName);
        result.append("&baselineId=").append(baselineId);
        result.append("&step=").append(step);
        result.append("&eaList=").append(eaList);
        if (eaOptionList != null && eaOptionList.length() > 0) {
        	result.append("&eaOptionList=").append(eaOptionList);
        }
        
		return result.toString();
	}
	
	protected abstract String getAnalyzerName();
	
	@Override
	protected String getMessageStep() {
		String result = "caqs.process.step.";
		if (isLoad) {
			result += "load.";
		}
		else {
			result += "analysis.";
		}
		result += getAnalyzerName();
		return result;
	}

	@Override
	protected String getMessageStatus() {
		return "IN_PROGRESS";
	}
	
	@Override
	protected int getMessagePercent() {
		int result = 20;
		if (isLoad()) {
			result = 50;
		}
		return result;
	}
	
	public boolean isLoad() {
		return this.isLoad;
	}
	
	protected void setIsLoad(APIAccessor accessor, ActivityInstance activity) throws Exception {
        String step = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "step");
        this.isLoad = step.equalsIgnoreCase("load");
	}
	
}
