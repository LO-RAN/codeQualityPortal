/**
 * 
 */
package com.compuware.caqs.workflow.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.runtime.ActivityInstance;

/**
 * @author cwfr-fdubois
 *
 */
public class SuccessMessageHook extends HttpCallHook {

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
		result.append("status=").append("COMPLETED");
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
	        String userId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "USER_ID");

	        String baselineName = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_NAME");
	        if (baselineName == null || baselineName.length() < 1) {
				baselineName = baselineId;
			}
	        
	        baselineName=getBaselineName(baselineName);

	        EmailUtil email = new EmailUtil();

		    String subject = " Analysis complete: Project=" + projectName + ", Baseline="+baselineName+", User="+userId;
		    String text = "Project " + projectName + " was successfully analyzed.\nResults are available in portal for baseline "+baselineName+".";

			email.sendEmail(to, projectName, subject, text);
			
		} catch (Exception mex) {
			System.out.println("Error sending email\n"+ mex);
		}
	}

	// duplicated from CAQS core environment
	// @see com.compuware.caqs.dao.dbms.BaselineDbmsDao
    private String getBaselineName(String name) {
        String result = name;
        if (result.matches("^[0-9]+$") && result.length() >= 12) {
            try {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
                Date d;
                d = df.parse(result.substring(0, 12));
                df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                result = df.format(d);
            } catch (ParseException e) {
                System.out.println("Error getting baseline name\n"+ e);
            }
        }
        // replace all single quotes that would break the SQL update command
        return result.replaceAll("'", " ");
    }
	
}
