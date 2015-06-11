package com.compuware.carscode.plugin.devpartner.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public interface DevPartnerDao {

	public Map retrieveModuleSummary(String fileMdbPath) throws ClassNotFoundException, SQLException;
	public Map retrieveProcedureSummary(String fileMdbPath) throws ClassNotFoundException, SQLException;
	public Map retrieveModuleMetrics(String fileMdbPath) throws ClassNotFoundException, SQLException;
	public Map retrieveProcedureMetrics(String fileMdbPath) throws ClassNotFoundException, SQLException;
	public Map retrieveModuleRuleDetections(String fileMdbPath) throws ClassNotFoundException, SQLException;
	public Collection retrieveCallsTo(String fileMdbPath) throws ClassNotFoundException, SQLException;
	public String getMetricPrefix();
	public void setSourceDirectory(String dir);
	
}
