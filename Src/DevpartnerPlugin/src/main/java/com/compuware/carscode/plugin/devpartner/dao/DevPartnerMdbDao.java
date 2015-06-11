package com.compuware.carscode.plugin.devpartner.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import com.compuware.carscode.plugin.devpartner.bean.Call;
import com.compuware.carscode.plugin.devpartner.bean.ElementBean;
import com.compuware.carscode.plugin.devpartner.bean.MetricBean;
import com.compuware.toolbox.dbms.JdbcDAOUtils;

public class DevPartnerMdbDao extends DevPartnerDaoAbstract {

    private Connection getConnection(String mdbFilePath) throws ClassNotFoundException, SQLException {
    	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        // set this to a MS Access DB you have on your machine
        String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
        database+= mdbFilePath.trim() + ";DriverID=22;READONLY=true}"; // add on to the end 
        // now we can get the connection from the DriverManager
        return DriverManager.getConnection( database ,"","");
    }
    
    protected MetricBean createMetric(String idMet, double value, String line) {
    	MetricBean result = new MetricBean();
    	result.setId(idMet);
    	result.setValue(value);
    	result.setLine(line);
    	return result;
    }
    
	public Map retrieveModuleSummary(String fileMdbPath) throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		ElementBean currentElement = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("Select ModuleName, ModulePath, SUM(LineCount), SUM(CountOfCodeComments), SUM(CountOfCodeLines), SUM(CountOfCommentLines) From Summaries Group By ModuleName, ModulePath");
			String moduleName = null;
			String modulePath = null;
			Map metricMap = null;
			while (rs.next()) {
				moduleName = rs.getString("ModuleName");
				modulePath = getRelativePath(rs.getString("ModulePath"));
				currentElement = new ElementBean();
				currentElement.setName(moduleName);
				currentElement.setFilePath(modulePath);
				metricMap = new HashMap();
				metricMap.put("LineCount", createMetric("LineCount", rs.getDouble(3), null));
				metricMap.put("CountOfCodeComments", createMetric("CountOfCodeComments", rs.getDouble(4), null));
				metricMap.put("CountOfCodeLines", createMetric("CountOfCodeLines", rs.getDouble(5), null));
				metricMap.put("CountOfCommentLines", createMetric("CountOfCommentLines", rs.getDouble(6), null));
				currentElement.setMetricMap(metricMap);
				result.put(moduleName, currentElement);
			}
		}
		finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closeStatement(stmt);
			JdbcDAOUtils.closeConnection(connection);
		}
		return result;
	}

	public Map retrieveProcedureSummary(String fileMdbPath) throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		ElementBean currentElement = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("Select ModuleName, ModulePath, ProcedureName, LineCount, CountOfCodeComments, CountOfCodeLines, CountOfCommentLines From Summaries");
			String procedureName = null;
			String moduleName = null;
			String modulePath = null;
			Map metricMap = null;
			while (rs.next()) {
				moduleName = rs.getString("ModuleName");
				modulePath = getRelativePath(rs.getString("ModulePath"));
				procedureName = moduleName + "." + rs.getString("ProcedureName");
				currentElement = new ElementBean();
				currentElement.setName(procedureName);
				currentElement.setParentName(moduleName);
				currentElement.setFilePath(modulePath);
				metricMap = new HashMap();
				metricMap.put("LineCount", createMetric("LineCount", rs.getDouble("LineCount"), null));
				metricMap.put("CountOfCodeComments", createMetric("CountOfCodeComments", rs.getDouble("CountOfCodeComments"), null));
				metricMap.put("CountOfCodeLines", createMetric("CountOfCodeLines", rs.getDouble("CountOfCodeLines"), null));
				metricMap.put("CountOfCommentLines", createMetric("CountOfCommentLines", rs.getDouble("CountOfCommentLines"), null));
				currentElement.setMetricMap(metricMap);
				result.put(procedureName, currentElement);
			}
		}
		finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closeStatement(stmt);
			JdbcDAOUtils.closeConnection(connection);
		}
		return result;
	}

	public Map retrieveModuleMetrics(String fileMdbPath) throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		ElementBean currentElement = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("Select ModuleName, MAX(McCabeCycloComplexity) From Metrics Group By ModuleName");
			String moduleName = null;
			Map metricMap = null;
			while (rs.next()) {
				moduleName = rs.getString("ModuleName");
				currentElement = new ElementBean();
				currentElement.setName(moduleName);
				metricMap = new HashMap();
				metricMap.put("MAX_McCabeCycloComplexity", createMetric("MAX_McCabeCycloComplexity", rs.getDouble(2), null));
				currentElement.setMetricMap(metricMap);
				result.put(moduleName, currentElement);
			}
		}
		finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closeStatement(stmt);
			JdbcDAOUtils.closeConnection(connection);
		}
		return result;
	}

	public Map retrieveProcedureMetrics(String fileMdbPath) throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		ElementBean currentElement = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("Select ModuleName, ProcedureName, McCabeCycloComplexity From Metrics");
			String procedureName = null;
			String moduleName = null;
			Map metricMap = null;
			while (rs.next()) {
				moduleName = rs.getString("ModuleName");
				procedureName = moduleName + "." + rs.getString("ProcedureName");
				currentElement = new ElementBean();
				currentElement.setName(procedureName);
				currentElement.setParentName(moduleName);
				metricMap = new HashMap();
				metricMap.put("McCabeCycloComplexity", createMetric("McCabeCycloComplexity", rs.getDouble("McCabeCycloComplexity"), null));
				currentElement.setMetricMap(metricMap);
				result.put(procedureName, currentElement);
			}
		}
		finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closeStatement(stmt);
			JdbcDAOUtils.closeConnection(connection);
		}
		return result;
	}

	public Map retrieveModuleRuleDetections(String fileMdbPath) throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		ElementBean currentElement = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("Select ModuleName, RuleNumber, LineNumberIDE From Problems Where ModuleName IS NOT NULL And ModuleName <> ''");
			String elementName = null;
			String ruleNumber = null;
			Map metricMap = null;
			MetricBean metric = null;
			while (rs.next()) {
				elementName = rs.getString("ModuleName");
				ruleNumber = rs.getString("RuleNumber");
				currentElement = (ElementBean)result.get(elementName);
				if (currentElement == null) {
					metricMap = new HashMap();
					currentElement = new ElementBean();
					currentElement.setName(elementName);
					currentElement.setMetricMap(metricMap);
					result.put(elementName, currentElement);
				}
				metricMap = currentElement.getMetricMap();
				metric = (MetricBean)metricMap.get(ruleNumber);
				if (metric == null) {
					metricMap.put(ruleNumber, createMetric(ruleNumber, 1, rs.getString("LineNumberIDE")));
				}
				else {
					metric.incValue();
					metric.addLine(rs.getString("LineNumberIDE"));
				}
			}
		}
		finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closeStatement(stmt);
			JdbcDAOUtils.closeConnection(connection);
		}
		return result;
	}

	private boolean validName(String name) {
		boolean result = false;
		if (name != null && name.trim().length() > 0) {
			result = true;
		}
		return result;
	}

	private static final String CALL_RETRIEVE_QUERY = 
		"Select distinct msrc.ModuleName + '.' + src.ProcedureName, msrc2.ModuleName + '.' + src2.ProcedureName"
		+ " From CallTree, Procedures src, Modules msrc, Procedures src2, Modules msrc2"
		+ " Where SourceProcID=src.ProcID"
		+ " And SourceModuleID=msrc.ModuleID"
		+ " And TargetProcID=src2.ProcID"
		+ " And TargetModuleID=msrc2.ModuleID";
	
	public Collection retrieveCallsTo(String fileMdbPath) throws ClassNotFoundException, SQLException {
		Collection result = new TreeSet();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(CALL_RETRIEVE_QUERY);
			Call procCall = null;
			while (rs.next()) {
				procCall = new Call();
				procCall.setFrom(rs.getString(1));
				procCall.setTo(rs.getString(2));
				result.add(procCall);
			}
		}
		finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closeStatement(stmt);
			JdbcDAOUtils.closeConnection(connection);
		}
		return result;
	}

	public String getMetricPrefix() {
		return "DP-";
	}

}
