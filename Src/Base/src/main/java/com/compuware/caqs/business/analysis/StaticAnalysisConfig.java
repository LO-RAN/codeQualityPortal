package com.compuware.caqs.business.analysis;

import com.compuware.caqs.domain.dataschemas.analysis.AnalysisConfig;

public class StaticAnalysisConfig  extends AnalysisConfig {

	private String toolId;
	private boolean analyse;
	private boolean load;
	private boolean masterTool;
	
	
	/**
	 * @return Returns the toolId.
	 */
	public String getToolId() {
		return toolId;
	}

	/**
	 * @param toolId The toolId to set.
	 */
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}

	/**
	 * @return Returns the analyse.
	 */
	public boolean isAnalyse() {
		return analyse;
	}
	
	/**
	 * @param analyse The analyse to set.
	 */
	public void setAnalyse(boolean analyse) {
		this.analyse = analyse;
	}
	
	/**
	 * @return Returns the load.
	 */
	public boolean isLoad() {
		return load;
	}
	
	/**
	 * @param load The load to set.
	 */
	public void setLoad(boolean load) {
		this.load = load;
	}
	
	/**
	 * @return Returns the masterTool.
	 */
	public boolean isMasterTool() {
		return masterTool;
	}
	
	/**
	 * @param masterTool The masterTool to set.
	 */
	public void setMasterTool(boolean masterTool) {
		this.masterTool = masterTool;
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer(super.toString());
		result.append(',').append("Tool=").append(getToolId());
		result.append(',').append("Analyse=").append(isAnalyse());
		result.append(',').append("Load=").append(isLoad());
		result.append(',').append("Master=").append(isMasterTool());
		return result.toString();
	}
	
}
