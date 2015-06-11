package com.compuware.carscode.plugin.deventreprise.dao;

import com.compuware.carscode.plugin.deventreprise.dataschemas.CobolSource;
import com.compuware.carscode.plugin.deventreprise.dataschemas.Copy;
import com.compuware.carscode.plugin.deventreprise.util.ReturnCodes;
import java.util.List;
import java.util.Map;

import com.compuware.carscode.plugin.deventreprise.dataschemas.Variable;
import com.compuware.carscode.plugin.deventreprise.dataschemas.Violation;
import java.util.Properties;

public interface DevEntrepriseDao {

	public int getIdFromDatabase(String name, String collection);
	public List<Violation> getNbArithmeticOverflow(int id, String name);
	public List<Violation> getLstComplexCompute(int id, String name);
	public int getMaxDataItemLength(int id, String name);
	
	public static final String ALPHANUMERIC_TO_NUMERIC = "alphanumerictonumeric";
	public static final String ALPHANUMERIC_TO_ALPHABETIC = "alphanumerictoalphabetic";
	public static final String ALPHABETIC_TO_NUMERIC = "alphabetictonumeric";
	public static final String NUMERIC_TO_ALPHABETIC = "alphabetictonumeric";
	public static final String SIGNED_TO_UNSIGNED = "signedtounsigned";
	public Map<String, List<Violation>> getNbMoveProblems(int id, String name);
	
	public List<Violation> getNbLowerPrecisionMoves(int id, String name);
	public int getLineNumbers(int id);
	public List<Variable> getNumericVariablesMore18Digits(int id, String name);
	public List<Violation> getNbConditionsNotUsed(int id, String name);
	
	public List<Violation> getIdentiferTooLong(int id, String name);

    public List<String> retrieveAllProgramMemberNames(String collectionName);
	
	public ReturnCodes setConnection(Properties dynProp);

    public List<String> retrieveAllIncludesForCollection(String collectionName);

    public List<String> retrieveAllSourcesForCollection(String collectionName);
	
	public void closeConnection();

    public String getLearningStatus(int programId);

    public void getMetricsFromXML(String xmlPath, CobolSource cs, List<Copy> copies);

    public String getLineFromRelDetail(String relDetail);

    public String getFileLine(String objId);

    public List<String> getProgramDependencies(int id);

    public List<String> retrieveLastCompletedPrograms(String collectionName, String timestampAsString);
    public int countLearningProgramsWithStatus(String collectionName, String status);
}
