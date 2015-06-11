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
public abstract class ProgressHook extends HttpCallHook {
	
	private static final String URL = "/message?";

	private boolean messageAfter = false;
	
	/**
	 * 
	 */
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
		accessor.getRuntimeAPI().setProcessInstanceVariable(activity.getProcessInstanceUUID(), "ACTIVITY_LABEL", "" + getMessageStep());

		if (!messageAfter) {
			setUrl(getMessageUrl(accessor, activity));
			super.execute(accessor, activity, false);
		}
		setUrl(getActionUrl(accessor, activity));
		super.execute(accessor, activity);
		if (messageAfter) {
			setUrl(getMessageUrl(accessor, activity));
			super.execute(accessor, activity, false);
		}
		System.out.println("RESULT="+accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "result"));
		System.out.println("RESULT.compareTo(false)="+((String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "result")).compareTo("false"));
	}
	
	protected abstract String getActionUrl(APIAccessor accessor, ActivityInstance activity) throws Exception;
	
	protected abstract int getMessagePercent();
	protected abstract String getMessageStep();
	protected abstract String getMessageStatus();
	
	protected String getMessageUrl(APIAccessor accessor, ActivityInstance activity) throws Exception {
		return URL + getMessageParams(accessor, activity);
	}
	
	protected String getMessageParams(APIAccessor accessor, ActivityInstance activity) throws Exception {


		
		StringBuilder result = new StringBuilder();
		result.append("step=").append(getMessageStep());
		if (isSuccessProcess(accessor, activity)) {
			result.append("&status=").append(getMessageStatus());
			result.append("&percent=").append(getMessagePercent());
		}
		else {
			result.append("&status=FAILED");
			result.append("&percent=100");
		}

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
	
	protected boolean isSuccessProcess(APIAccessor accessor, ActivityInstance activity) throws Exception {
		String result = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "result");
		return (result == null || result.equalsIgnoreCase("true"));
	}
	
	public void setMessageAfter(boolean messageAfter) {
		this.messageAfter = messageAfter;
	}
	
}
