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
public class FailedMessageHook extends HttpCallHook {
	
	private static final String URL = "/message?";

	/**
	 * 
	 */
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
		setUrl(URL + getMessageParams(accessor, activity));
		super.execute(accessor, activity, false);
		sendEmail(accessor, activity);
	}
	
	private String getMessageParams(APIAccessor accessor, ActivityInstance activity) throws Exception {
		StringBuilder result = new StringBuilder();
		result.append("status=").append("FAILED");
		result.append("&percent=").append("100");

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
	
	private void sendEmail(APIAccessor accessor, ActivityInstance activity) {
		try {
			String to = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "USER_EMAIL");
	        String projectName = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_NAME");
	        String baselineId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_ID");
	        String step = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "step");
	        String activityLabel = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "ACTIVITY_LABEL");
	        String userId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "USER_ID");

	        String serverBase1 = (String) accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), HttpCallHook.HTTP_SERVER_CONTEXT_PATH);
          String serverBase2 = (String) accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), HttpCallHook.HTTP_SERVER_CONTEXT_PATH_2);
          String serverBase3 = (String) accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), HttpCallHook.HTTP_SERVER_CONTEXT_PATH_3);

	        EmailUtil email = new EmailUtil();

		    String subject = " Analysis failed: Project " + projectName+", User="+userId;
		    String text = "Analysis failed for project " + projectName+" in step "+activityLabel
		                 +".\nContact your administrator and check the following log file(s)) :\n"
		                 +serverBase1+"/../CAQSlog/analysis-"+baselineId+".txt";

		    if (! serverBase1.equals(serverBase2)){
		    	text=text+"\n"+serverBase2+"/../CAQSlog/analysis-"+baselineId+".txt";
		    	}
		    if (! serverBase2.equals(serverBase3)){
		    	text=text+"\n"+serverBase3+"/../CAQSlog/analysis-"+baselineId+".txt";
		    	}
		    email.sendEmail(to, projectName, subject, text);			
			} catch (Exception mex) {
				System.out.println(mex);
		}
	}	
}
