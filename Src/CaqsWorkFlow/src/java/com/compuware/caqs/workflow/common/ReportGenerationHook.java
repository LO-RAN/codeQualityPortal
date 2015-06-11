/**
 * 
 */
package com.compuware.caqs.workflow.common;

import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.runtime.ActivityInstance;

/**
 * @author cwfr-dzysman
 *
 */
public class ReportGenerationHook extends HttpCallHook {
	
	private static final String URL = "/report?";
	
	/**
	 * 
	 */
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
		//setMessageAfter(false);
		//super.execute(accessor, activity);
		String url = this.getTaskParams(accessor, activity);
		setUrl(url);
		super.execute(accessor, activity, false);
		//accessor.getRuntimeAPI().startTask((TaskUUID)activity.getBody().getUUID(), false);
		//accessor.getRuntimeAPI().finishTask((TaskUUID)activity.getBody().getUUID(), false);
	}
/*
	protected String getActionUrl(APIAccessor accessor, ActivityInstance<ActivityBody> activity) throws Exception {
		return URL + getTaskParams(accessor, activity);
	}*/
	
	private String getTaskParams(APIAccessor accessor, ActivityInstance activity) throws Exception {
		StringBuilder result = new StringBuilder();
        String projectId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_ID");
        String elementId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "ID_EA");
        String baselineId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_ID");
        String userId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "USER_ID");
        String languageId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "LANGUAGE_ID");
        //String idMess = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "ID_MESS");
        result.append(URL);
        result.append("id_ea=").append(elementId);
        result.append("&id_pro=").append(projectId);
        result.append("&id_bline=").append(baselineId);
        result.append("&id_user=").append(userId);
        result.append("&language=").append(languageId);
        result.append("&showMsg=true&forceRegeneration=true");
        
		return result.toString();
	}

}
