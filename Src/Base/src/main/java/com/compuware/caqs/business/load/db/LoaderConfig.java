package com.compuware.caqs.business.load.db;

import com.compuware.caqs.domain.dataschemas.analysis.EA;

public class LoaderConfig {
    protected boolean updateMetricIfAlreadyExist = false;
    protected boolean createIfDoesNotExists = true;
    protected boolean peremptElementsIfNotUsed = false;
    protected String projectId = null;
    protected EA ea = null;
    protected String baseLineId = null;
    private boolean filePathAndLineActive = false;
    private int filePathColumn = 1;
    
    public LoaderConfig(String projectId, EA ea, String baselineId) {
    	this.projectId = projectId;
    	this.ea = ea;
    	this.baseLineId = baselineId;
    }
    
	/**
	 * @return Returns the baseLineId.
	 */
	public String getBaseLineId() {
		return baseLineId;
	}
	/**
	 * @param baseLineId The baseLineId to set.
	 */
	public void setBaseLineId(String baseLineId) {
		this.baseLineId = baseLineId;
	}
	/**
	 * @return Returns the createIfDoesNotExists.
	 */
	public boolean isCreateIfDoesNotExists() {
		return createIfDoesNotExists;
	}
	/**
	 * @param createIfDoesNotExists The createIfDoesNotExists to set.
	 */
	public void setCreateIfDoesNotExists(boolean createIfDoesNotExists) {
		this.createIfDoesNotExists = createIfDoesNotExists;
	}
	/**
	 * @return Returns the ea.
	 */
	public EA getEa() {
		return ea;
	}
	/**
	 * @param ea The ea to set.
	 */
	public void setEa(EA ea) {
		this.ea = ea;
	}
	/**
	 * @return Returns the filePathAndLineActive.
	 */
	public boolean isFilePathAndLineActive() {
		return filePathAndLineActive;
	}
	/**
	 * @param filePathAndLineActive The filePathAndLineActive to set.
	 */
	public void setFilePathAndLineActive(boolean filePathAndLineActive) {
		this.filePathAndLineActive = filePathAndLineActive;
	}
	/**
	 * @return Returns the peremptElementsIfNotUsed.
	 */
	public boolean isPeremptElementsIfNotUsed() {
		return peremptElementsIfNotUsed;
	}
	/**
	 * @param peremptElementsIfNotUsed The peremptElementsIfNotUsed to set.
	 */
	public void setPeremptElementsIfNotUsed(boolean peremptElementsIfNotUsed) {
		this.peremptElementsIfNotUsed = peremptElementsIfNotUsed;
	}
	/**
	 * @return Returns the projectId.
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return Returns the updateMetricIfAlreadyExist.
	 */
	public boolean isUpdateMetricIfAlreadyExist() {
		return updateMetricIfAlreadyExist;
	}
	/**
	 * @param updateMetricIfAlreadyExist The updateMetricIfAlreadyExist to set.
	 */
	public void setUpdateMetricIfAlreadyExist(boolean updateMetricIfAlreadyExist) {
		this.updateMetricIfAlreadyExist = updateMetricIfAlreadyExist;
	}

	/**
	 * @return Returns the filePathColumn.
	 */
	public int getFilePathColumn() {
		return filePathColumn;
	}

	/**
	 * @param filePathColumn The filePathColumn to set.
	 */
	public void setFilePathColumn(int filePathColumn) {
		this.filePathColumn = filePathColumn;
	}
	
}
