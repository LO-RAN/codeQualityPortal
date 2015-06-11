package com.compuware.carscode.migration.exception;

public class MigrationException extends Exception {

	private static final long serialVersionUID = -9142165223761362258L;
	
	private String projectId = null;
	
	public MigrationException(Exception e) {
		super(e);
	}

	public MigrationException(String projectId, Exception e) {
		super(e);
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
	
}
