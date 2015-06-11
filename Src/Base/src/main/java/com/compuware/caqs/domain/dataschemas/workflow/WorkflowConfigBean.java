package com.compuware.caqs.domain.dataschemas.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.domain.dataschemas.DialecteBean;

/**
 * The workflow configuration bean.
 * @author cwfr-fdubois
 */
public class WorkflowConfigBean {

    /**
     * The user that started the process ID.
     */
    private String userId;

    /**
     * The user that started the process email.
     */
    private String userEmail;

    /**
     * The project ID.
     */
    private String projectId;

    /**
     * The project name.
     */
    private String projectName;

    /**
     * The baseline ID.
     */
    private String baselineId;

    /**
     * The baseline name.
     */
    private String baselineName="";

    /**
     * The list of module.
     */
    private String eaList;

    /**
     * The list of module option.
     */
    private String eaOptionList="";

    /**
     * The list of distinct languages used by the modules.
     */
    private List<DialecteBean> dialects;

    /**
     * Get the baseline name.
     * @return the baseline name.
     */
    public String getBaselineName() {
        return baselineName;
    }

    /**
     * Set the baseline name.
     * @param baselineName the baseline name.
     */
    public void setBaselineName(String baselineName) {
        this.baselineName = getNotNullString(baselineName);
    }

    /**
     * Get the baseline ID.
     * @return the baseline ID.
     */
    public String getBaselineId() {
        return baselineId;
    }

    /**
     * Set the baseline ID.
     * @param baselineId the baseline ID.
     */
    public void setBaselineId(String baselineId) {
        this.baselineId = baselineId;
    }

    /**
     * Get the list of module as a csv style String. Separator is ','.
     * @return the list of module.
     */
    public String getEaList() {
        return eaList;
    }

    /**
     * Set the list of module.
     * @param eaList the list of module as a csv style String. Separator is ','.
     */
    public void setEaList(String eaList) {
        this.eaList = eaList;
    }

    /**
     * Get the list of module option as a csv style String. Separator is ','.
     * @return the list of module option.
     */
    public String getEaOptionList() {
        return eaOptionList;
    }

    /**
     * Set the list of module option.
     * @param eaOptionList the list of module option as a csv style String. Separator is ','.
     */
    public void setEaOptionList(String eaOptionList) {
        this.eaOptionList = getNotNullString(eaOptionList);
    }

    /**
     * Get the project ID.
     * @return the project ID.
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Set the project ID.
     * @param projectId the project ID.
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * Get the project name.
     * @return the project name.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Set the project name.
     * @param projectName the project name.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Get the user that started the process ID.
     * @return the user that started the process ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set the user that started the process ID.
     * @param userId the user that started the process ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get the user that started the process email.
     * @return the user that started the process email.
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Set the user that started the process email.
     * @param userEmail the user that started the process email.
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Get the list of distinct dialects used by the modules.
     * @return the list of distinct dialects used by the modules.
     */
    public List<DialecteBean> getDialects() {
        return dialects;
    }

    /**
     * Set the list of distinct languages used by the modules.
     * @param languages the list of distinct languages used by the modules.
     */
    public void setDialects(List<DialecteBean> dialects) {
        this.dialects = dialects;
    }

    private String getNotNullString(String value) {
        String result = value;
        if (result == null) {
            result = "";
        }
        return result;
    }

    /**
     * Get the configuration as a map that will be passed to the process as variable.
     * @return the configuration as a map that will be passed to the process as variable.
     */
    public Map<String, Object> asMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("USER_ID", this.userId);
        result.put("USER_EMAIL", this.userEmail);
        result.put("BASELINE_NAME", this.baselineName);
        result.put("BASELINE_ID", this.baselineId);
        result.put("EA_LIST", this.eaList);
        result.put("EA_OPTION_LIST", this.eaOptionList);
        result.put("PROJECT_ID", this.projectId);
        result.put("PROJECT_NAME", this.projectName);
        if (this.dialects != null) {
            String language;
            for (DialecteBean dialect: this.dialects) {

                language=dialect.getId();
                if(language.indexOf("_")>=0){
                    language=language.substring(0, language.indexOf("_"));
                }
               
                result.put("RUN_" + language.toUpperCase() + "_ANALYSIS", Boolean.TRUE);
            }
        }
        return result;
    }

}
