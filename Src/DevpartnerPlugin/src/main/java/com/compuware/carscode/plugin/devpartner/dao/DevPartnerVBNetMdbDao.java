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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.compuware.carscode.plugin.devpartner.bean.Call;
import com.compuware.carscode.plugin.devpartner.bean.ElementBean;
import com.compuware.carscode.plugin.devpartner.bean.MetricBean;
import com.compuware.toolbox.dbms.JdbcDAOUtils;

public class DevPartnerVBNetMdbDao extends DevPartnerDaoAbstract {
    
	/**
	 * Get connection with a database stored in a MDB File.
	 * @param mdbFilePath The path to the MDB file
	 * @return The Connection object linked with the database
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection getConnection(String mdbFilePath) throws ClassNotFoundException, SQLException {
    	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        // set this to a MS Access DB you have on your machine
        String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
        database+= mdbFilePath.trim() + ";DriverID=22;READONLY=true}"; // add on to the end 
        // now we can get the connection from the DriverManager
        return DriverManager.getConnection( database ,"","");
    }
    
	private static final String CALL_RETRIEVE_QUERY = 
		"Select distinct fileSrc.ProjFileFullPath, metSrc.MethodName, fileDest.ProjFileFullPath, metDest.MethodName, metSrc.CodeLine, metDest.CodeLine" +
		" From CallTree, Methods metSrc, Methods metDest, Files fileSrc, Files fileDest" +
		" Where SourceProcID=metSrc.MethodID" +
		" And TargetProcID=metDest.MethodID" +
		" And metSrc.FileID=fileSrc.ProjFileID" +
		" And metDest.FileID=fileDest.ProjFileID";

	public Collection retrieveCallsTo(String fileMdbPath)
			throws ClassNotFoundException, SQLException {
		Collection result = new TreeSet();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(CALL_RETRIEVE_QUERY);
			Call procCall = null;
			String srcFilePath = null;
			String destFilePath = null;
			String srcMethodName = null;
			String destMethodName = null;
			String srcCodeline = null;
			String destCodeline = null;
			String from = null;
			String to = null;
			while (rs.next()) {
				procCall = new Call();
				srcFilePath = getRelativePath(rs.getString(1));
				srcMethodName = rs.getString(2);
				srcCodeline = rs.getString(5);
				from = getNameFromPath(srcFilePath) + '.' + srcMethodName + this.getCleanedParamList(srcCodeline);
				
				destFilePath = getRelativePath(rs.getString(3));
				destMethodName = rs.getString(4);
				destCodeline = rs.getString(6);
				to = getNameFromPath(destFilePath) + '.' + destMethodName + this.getCleanedParamList(destCodeline);
				
				procCall.setFrom(from);
				procCall.setTo(to);
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

	private static final String CLASS_METRICS_QUERY  = "Select Files.ProjFileFullPath, MAX(Metrics.McCabeCycloComplexity)"+
	" From Metrics, Files "+
	" Where Metrics.FileID = Files.ProjFileID"+
	" Group By Files.ProjFileFullPath" +
	" Order by Files.ProjFileFullPath";
	
	public Map retrieveModuleMetrics(String fileMdbPath)
			throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(DevPartnerVBNetMdbDao.CLASS_METRICS_QUERY);
			String className = null;
			String filePath = null;
			ElementBean currentElement = null;
			Map metricMap = null;
			while (rs.next()) {
				filePath = getRelativePath(rs.getString("ProjFileFullPath"));
				className = getNameFromPath(filePath);
				currentElement = new ElementBean();
				currentElement.setName(className);
				currentElement.setFilePath(filePath);
				metricMap = new HashMap();
				metricMap.put("MAX_McCabeCycloComplexity", createMetric("MAX_McCabeCycloComplexity", rs.getDouble(2), null));
				currentElement.setMetricMap(metricMap);
				result.put(className, currentElement);
			}
		}
		finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closeStatement(stmt);
			JdbcDAOUtils.closeConnection(connection);
		}
		return result;
	}

	private static final String PROJECT_RULE_RETRIEVE_QUERY = 
		"Select Files.ProjFileFullPath, FiredRules.RuleID, FiredTriggers.LineNumber"+
		 " From FiredRules, FiredRuleTriggers, FiredTriggers, Files"+
		 " where FiredRules.FiredRuleID=FiredRuleTriggers.FiredRuleID"+
		 " And FiredRuleTriggers.FiredTriggerID=FiredTriggers.FiredTriggerID"+
		 " And FiredRules.FileID = Files.ProjFileID" +
		 " Order by Files.ProjFileFullPath";
	public Map retrieveModuleRuleDetections(String fileMdbPath)
			throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(DevPartnerVBNetMdbDao.PROJECT_RULE_RETRIEVE_QUERY);
			String elementName = null;
			String filePath = null;
			ElementBean currentElement = null;
			String ruleNumber = null;
			Map metricMap = null;
			MetricBean metric = null;
			while (rs.next()) {
				filePath = getRelativePath(rs.getString("ProjFileFullPath"));
				elementName = getNameFromPath(filePath);
				ruleNumber = rs.getString("RuleID");
				currentElement = (ElementBean)result.get(elementName);
				if (currentElement == null) {
					currentElement = new ElementBean();
					currentElement.setName(elementName);
					currentElement.setFilePath(filePath);
					result.put(elementName, currentElement);
				}
				metricMap = currentElement.getMetricMap();
				metric = (MetricBean)metricMap.get(ruleNumber);
				String lineNumber = rs.getString("LineNumber");
				if(lineNumber.equals("-1")) {
					lineNumber = "";
				}
				if (metric == null) {
					metricMap.put(ruleNumber, createMetric(ruleNumber, 1, lineNumber));
				}
				else {
					metric.incValue();
					if(!lineNumber.equals("")) {
						metric.addLine(lineNumber);							
					}
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
	
	private String getNameFromPath(String filePath) {
		String result = filePath.replaceAll(" ", "");
		result = result.replaceAll("\\.", "_");
		if (result.startsWith(this.srcDir)) {
			result = result.substring(this.srcDir.length());
		}
		else {
			int srcIdx = result.indexOf("/src/");
			if (srcIdx > 0) {
				int afterSrcIdx = result.indexOf('/', srcIdx + 1);
				result = result.substring(afterSrcIdx + 1);
			}
		}
		result = result.replaceAll("/", ".");
		return result;
	}
	
	private static final String RETRIEVE_MODULE_QUERY = 
		"Select distinct Files.ProjFileFullPath, LineCount, CountOfCodeComments, CountOfCodeLines, CountOfCommentLines" + 
		" From Summaries, Files " +
		" Where Summaries.FileName is not null" +
		" And Summaries.MethodID = 0" +
		" And Summaries.SummaryType = 0" +
		" And Summaries.FileID = Files.ProjFileID" +
		" Order by Files.ProjFileFullPath";
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.plugin.devpartner.dao.DevPartnerDao#retrieveModuleSummary(java.lang.String)
	 */
	public Map retrieveModuleSummary(String fileMdbPath)
			throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(DevPartnerVBNetMdbDao.RETRIEVE_MODULE_QUERY);
			String fileName = null;
			String filePath = null;
			ElementBean currentElement = null;
			Map metricMap = new HashMap();
			while (rs.next()) {
				filePath = getRelativePath(rs.getString("ProjFileFullPath"));
				fileName = getNameFromPath(filePath);
				currentElement = new ElementBean();
				currentElement.setName(fileName);
				currentElement.setFilePath(filePath);
				metricMap = new HashMap();
				metricMap.put("LineCount", createMetric("LineCount", rs.getDouble(2), null));
				metricMap.put("CountOfCodeComments", createMetric("CountOfCodeComments", rs.getDouble(3), null));
				metricMap.put("CountOfCodeLines", createMetric("CountOfCodeLines", rs.getDouble(4), null));
				metricMap.put("CountOfCommentLines", createMetric("CountOfCommentLines", rs.getDouble(5), null));
				currentElement.setMetricMap(metricMap);
				result.put(fileName, currentElement);
			}
		}
		finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closeStatement(stmt);
			JdbcDAOUtils.closeConnection(connection);
		}
		return result;
	}
	
	private String getCleanedParamList(String procedureParam) {
		String retour = "";
		if(procedureParam.indexOf("(")!=-1) {
			procedureParam = procedureParam.substring(procedureParam.indexOf("("));
			if(procedureParam.endsWith("\n")) {
				procedureParam = procedureParam.substring(0, procedureParam.length()-2);
			}
			if(!procedureParam.equals("()") && procedureParam.contains(")")) {
				//now we remove everything but the parameter type
				String filtre = procedureParam.substring(0,procedureParam.indexOf(")"));
				String newParams = ""; 
				if(!filtre.equals("")) {
					String[] commaTokens = filtre.split(",");
					if(commaTokens!=null) {
						for(int i=0; i<commaTokens.length; i++) {
							//for each parameter
							String paramName = commaTokens[i].substring(commaTokens[i].indexOf("As")+2, commaTokens[i].length()).trim();
							if(paramName.indexOf(" ")!=-1) {
								paramName = paramName.substring(0, paramName.indexOf(" ")+1).trim();
							}
							if(!newParams.equals("")) {
								newParams += ", ";
							}
							newParams += paramName;
						}
					}
				}
				newParams = newParams.replaceAll(" ", "");
				newParams = newParams.replaceAll("\"", "");
				newParams = newParams.replaceAll("<", "&lt;");
				retour = "("+newParams+")";
			} else {
				retour = "()";
			}
			//procedureName += procedureParam;
		}
		return retour;
	}

	private static final String PROCEDURE_METRICS_QUERY = "Select distinct Metrics.MethodID, Methods.MethodName, "+
		" Metrics.McCabeCycloComplexity, Files.ProjFileFullPath, Methods.CodeLine" +
		" From Metrics, Methods, Files "+
		" Where Metrics.MethodID=Methods.MethodID" +
		" And Metrics.FileID = Files.ProjFileID";
	public Map retrieveProcedureMetrics(String fileMdbPath)
			throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(DevPartnerVBNetMdbDao.PROCEDURE_METRICS_QUERY);
			String procedureName = null;
			String filePath = null;
			String fileName = null;
			ElementBean currentElement = null;
			Map metricMap = null;
			while (rs.next()) {
				procedureName = rs.getString("MethodName");
				if (isValidMethodName(procedureName)) {
					filePath = getRelativePath(rs.getString("ProjFileFullPath"));
					fileName = getNameFromPath(filePath);
					procedureName = fileName + "."+ procedureName;
					String procedureParam = rs.getString("CodeLine");
					
					procedureName += this.getCleanedParamList(procedureParam);
					currentElement = new ElementBean();
					currentElement.setName(procedureName);
					currentElement.setFilePath(filePath);
					currentElement.setParentName(fileName);
					
					metricMap = new HashMap();
					metricMap.put("McCabeCycloComplexity", createMetric("McCabeCycloComplexity", rs.getDouble("McCabeCycloComplexity"), null));
					currentElement.setMetricMap(metricMap);
					
					result.put(procedureName, currentElement);
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

	public static final String RETRIEVE_PROCEDURE_MAP = "Select Files.ProjFileFullPath, Methods.MethodName, Methods.CodeLine "+ 
	" From Methods, Files"+
	" Where Methods.FileID = Files.ProjFileID";

	public Map retrieveProcedureMap(String fileMdbPath)
			throws ClassNotFoundException, SQLException {
		Map result = new HashMap();
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(DevPartnerVBNetMdbDao.RETRIEVE_PROCEDURE_MAP);
			String filePath = null;
			String fileName = null;
			String methodName = null;
			ElementBean currentElement = null;
			Map metricMap = new HashMap();
			while (rs.next()) {
				methodName = rs.getString("MethodName");
				if (isValidMethodName(methodName)) {
					filePath = getRelativePath(rs.getString("ProjFileFullPath"));
					fileName = getNameFromPath(filePath);
					methodName = fileName + "."+ methodName;
					String procedureParam = rs.getString("CodeLine");
					methodName += this.getCleanedParamList(procedureParam);
		
					currentElement = new ElementBean();
					currentElement.setName(methodName);
					currentElement.setFilePath(filePath);
					currentElement.setParentName(fileName);
		
					metricMap = new HashMap();
					currentElement.setMetricMap(metricMap);
					
					result.put(methodName, currentElement);
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
	
	public static final String RETRIEVE_PROCEDURE_SUMMARY = "Select Files.ProjFileFullPath, Methods.MethodName, LineCount, CountOfCodeComments, CountOfCodeLines, CountOfCommentLines, Methods.CodeLine "+ 
		" From Summaries, Methods, Files"+
		" Where Summaries.MethodId > 0"+
		" And Summaries.MethodId = Methods.MethodID"+
		" And Summaries.FileId = Files.ProjFileID";
	
	public Map retrieveProcedureSummary(String fileMdbPath)
			throws ClassNotFoundException, SQLException {
		Map result = retrieveProcedureMap(fileMdbPath);
		Connection connection = getConnection(fileMdbPath);
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(DevPartnerVBNetMdbDao.RETRIEVE_PROCEDURE_SUMMARY);
			String filePath = null;
			String fileName = null;
			String methodName = null;
			ElementBean currentElement = null;
			Map metricMap = new HashMap();
			while (rs.next()) {
				methodName = rs.getString("MethodName");
				if (isValidMethodName(methodName)) {
					filePath = getRelativePath(rs.getString("ProjFileFullPath"));
					fileName = getNameFromPath(filePath);
					methodName = fileName + "."+ methodName;
					String procedureParam = rs.getString("CodeLine");
					methodName += this.getCleanedParamList(procedureParam);
	
					currentElement = (ElementBean)result.get(methodName);
					if (currentElement != null) {
						metricMap = currentElement.getMetricMap();
						metricMap.put("LineCount", createMetric("LineCount", rs.getDouble("LineCount"), null));
						metricMap.put("CountOfCodeComments", createMetric("CountOfCodeComments", rs.getDouble("CountOfCodeComments"), null));
						metricMap.put("CountOfCodeLines", createMetric("CountOfCodeLines", rs.getDouble("CountOfCodeLines"), null));
						metricMap.put("CountOfCommentLines", createMetric("CountOfCommentLines", rs.getDouble("CountOfCommentLines"), null));
					}
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

	private static final Pattern VALID_METHOD_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9-_]+");
	
	private boolean isValidMethodName(String methodName) {
		boolean result = false;
		if (methodName != null) {
			Matcher m = VALID_METHOD_NAME_PATTERN.matcher(methodName);
			result = m.matches();
		}
		return result;
	}
	
    /**
     * Create a MetricBean initialized with properties given in parameters
     * @param idMet The metric's id
     * @param value The metric's value
     * @param line  The metric's line number
     * @return The correctly initialized MetricBean object
     */
    protected MetricBean createMetric(String idMet, double value, String line) {
    	MetricBean result = new MetricBean();
    	result.setId(idMet);
    	result.setValue(value);
    	result.setLine(line);
    	return result;
    }
    
	
	/**
	 * @param name
	 * @return
	 */
	private boolean validName(String name) {
		boolean result = false;
		if (name != null && name.trim().length() > 0) {
			result = true;
		}
		return result;
	}

	public String getMetricPrefix() {
		return "DP8-";
	}
	
}
