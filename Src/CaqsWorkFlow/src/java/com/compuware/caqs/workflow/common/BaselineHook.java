/**
 * 
 */
package com.compuware.caqs.workflow.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.runtime.ActivityInstance;
import org.ow2.bonita.util.BonitaException;

/**
 * @author cwfr-fdubois
 *
 */
public class BaselineHook extends ProgressHook {
	
	private static final String URL = "/baseline?action=0";

	private static final Pattern BASELINE_ID_PATTERN = Pattern.compile("<baselineId>(.*)</baselineId>");
	
	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
		setMessageAfter(true);
		super.execute(accessor, activity);
	}

	protected String getActionUrl(APIAccessor accessor, ActivityInstance activity) throws Exception {
		return URL + getProjectParams(accessor, activity);
	}
	
	private String getProjectParams(APIAccessor accessor, ActivityInstance activity) throws Exception {
		StringBuilder result = new StringBuilder();
        String projectId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_ID");
        String projectName = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "PROJECT_NAME");
        String baselineId = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_ID");
        String baselineName = (String)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_NAME");
		
        result.append("&projectId=").append(projectId);
        result.append("&projectName=").append(projectName);
        result.append("&baselineName=").append(baselineName);
        if (baselineId != null) {
            result.append("&forcedId=").append(baselineId);
        }
        
		return result.toString();
	}
	
    /**
     * Called after http call to change variables values.
     * @param inputStream the result stream.
     * @throws BonitaException 
     */
    protected void postCallSetVariables(String inputStream, APIAccessor accessor, ActivityInstance activity) throws BonitaException {
    	String baselineId = extractBaselineId(inputStream);
    	if (baselineId != null && baselineId.length() > 0) {
    		accessor.getRuntimeAPI().setProcessInstanceVariable(activity.getProcessInstanceUUID(), "BASELINE_ID", baselineId);
    	}
    }
    
    /**
     * Get the baseline id from the http call input stream.
     * @param inputStream the given input stream.
     * @return the baseline id contained into the http call input stream <baselineId>...</baselineId>.
     */
    private String extractBaselineId(String inputStream) {
    	String result = "";
    	Matcher m = BASELINE_ID_PATTERN.matcher(inputStream);
    	if (m != null && m.find()) {
    		result = m.group(1);
    	}
    	return result;
    }

	protected int getMessagePercent() {
		return 1;
	}

	protected String getMessageStep() {
		return "caqs.process.step.initbaseline";
	}

	protected String getMessageStatus() {
		return "IN_PROGRESS";
	}

}
