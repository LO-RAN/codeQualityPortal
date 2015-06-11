/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.analysis;

/**
 * Contains analysis parameters.
 * @author cwfr-fdubois
 *
 */
public class AnalysisConfig {

	/** The project ID. */
	String projectId = null;
	/** The baseline ID. */
	String baselineId = null;
	/** The module array to analyze. */
	String[] moduleArray = null;
	/** The module option array to analyze. */
	String[] moduleOptionArray = null;

	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the baselineId
	 */
	public String getBaselineId() {
		return baselineId;
	}
	/**
	 * @param baselineId the baselineId to set
	 */
	public void setBaselineId(String baselineId) {
		this.baselineId = baselineId;
	}
	/**
	 * @return the moduleArray
	 */
	public String[] getModuleArray() {
		return moduleArray;
	}
	/**
	 * @param moduleArray the moduleArray to set
	 */
	public void setModuleArray(String[] moduleArray) {
		this.moduleArray = moduleArray;
	}
	/**
	 * @return the moduleOptionArray
	 */
	public String[] getModuleOptionArray() {
		return moduleOptionArray;
	}
	/**
	 * @param moduleOptionArray the moduleOptionArray to set
	 */
	public void setModuleOptionArray(String[] moduleOptionArray) {
		this.moduleOptionArray = moduleOptionArray;
	}

    @Override
	public String toString() {
        StringBuilder result = new StringBuilder("AnalysisConfig: ");
        result.append("Project id: ").append(this.projectId);
        result.append(", Baseline id: ").append(this.baselineId);
        if (this.moduleArray != null) {
            result.append(", Module array: ").append(this.moduleArray.toString());
        }
        if (this.moduleOptionArray != null) {
            result.append(", Module option array: ").append(this.moduleOptionArray.toString());
        }
        return result.toString();
    }
}
