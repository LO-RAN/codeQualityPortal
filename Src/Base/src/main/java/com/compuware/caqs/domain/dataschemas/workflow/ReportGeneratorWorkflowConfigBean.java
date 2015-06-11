package com.compuware.caqs.domain.dataschemas.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.domain.dataschemas.LanguageBean;

/**
 * The workflow configuration bean.
 * @author cwfr-dzysman
 */
public class ReportGeneratorWorkflowConfigBean {

    /**
     * The user that started the process ID.
     */
    private String userId;

    /**
     * The entity application id.
     */
    private String eaId;

    /**
     * The project ID.
     */
    private String projectId;

    /**
     * The baseline ID.
     */
    private String baselineId;

    /**
     * the language in which the report has to be generated
     */
    private String languageId;

    /**
     * message id
     */
    private String idMess;

    /**
     * Get the language id.
     * @return the language id.
     */
    public String getLanguageId() {
        return languageId;
    }

    /**
     * Set the baseline name.
     * @param baselineName the baseline name.
     */
    public void setLanguageId(String languageId) {
        this.languageId = languageId;
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
     * Get the application entity id.
     * @return the application entity id.
     */
    public String getEaId() {
        return eaId;
    }

    /**
     * Set the application entity id.
     * @param eaid the application entity id.
     */
    public void setEaId(String eaid) {
        this.eaId = eaid;
    }

    public String getIdMess() {
        return idMess;
    }

    public void setIdMess(String idMess) {
        this.idMess = idMess;
    }

    /**
     * Get the configuration as a map that will be passed to the process as variable.
     * @return the configuration as a map that will be passed to the process as variable.
     */
    public Map<String, Object> asMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("USER_ID", this.userId);
        result.put("ID_EA", this.eaId);
        result.put("BASELINE_ID", this.baselineId);
        result.put("PROJECT_ID", this.projectId);
        result.put("LANGUAGE_ID", this.languageId);
        result.put("ID_MESS", this.idMess);
        return result;
    }

}
