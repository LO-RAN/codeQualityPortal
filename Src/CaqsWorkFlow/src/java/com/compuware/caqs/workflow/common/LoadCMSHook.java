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
public class LoadCMSHook extends ProgressHook {
	
	private static final String URL = "/sourcemanager?goal=loadFromCMS";

	@Override
	protected String getActionUrl(APIAccessor accessor, ActivityInstance activity) throws Exception {
            
            Boolean isCSharp = (Boolean)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "RUN_CS_ANALYSIS");
            Boolean isVB = (Boolean)accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), "RUN_VB_ANALYSIS");

           // if dealing with Windows languages/dialects, make sure we run the CMS Checkout task from Windows
            // (assuming here the default server context being Linux...)
            // (trying to prevent any code page issues for accented characters)
            if(isVB || isCSharp){
                useAlternateServerContext=true;
            }
                        
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
        result.append("&eaOptionList=");
        if (eaOptionList != null && eaOptionList.length() > 0) {
        	result.append(eaOptionList);
        }
        
		return result.toString();
	}
	
	protected int getMessagePercent() {
		return 5;
	}

	protected String getMessageStep() {
		return "caqs.process.step.loadfromcms";
	}

	protected String getMessageStatus() {
		return "IN_PROGRESS";
	}

	public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
             super.execute(accessor, activity);
	}
}
