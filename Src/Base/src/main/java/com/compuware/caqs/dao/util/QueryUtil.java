package com.compuware.caqs.dao.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * Utility class for database SQL queries.
 * @author cwfr-fdubois
 */
public final class QueryUtil {

    /** The IN clause text to replace in the query. */
	private static final String DEFAULT_IN_CLAUSE_KEY = "@INCLAUSE@";

    /** The unique instance of the class. */
	private static QueryUtil instance = new QueryUtil();

    /** The private constructor for the unique instance. */
	private QueryUtil() {
	}

    /**
     * Get the unique instance of the class.
     * @return the unique instance of the class.
     */
	public static QueryUtil getInstance() {
		return instance;
	}

    /**
     * Create a SQL IN Clause from  a string array.
     * @param array the array of string values to set in the IN clause.
     * @return the SQL IN Clause created from the string array.
     */
	public String getInClause(String[] array) {
		StringBuffer result = new StringBuffer("('");
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (i > 0) {
					result.append("','");
				}
				result.append(array[i]);
			}
		}
    	result.append("')");
		return result.toString();
	}

    /**
     * Create a SQL IN Clause from  a string collection.
     * @param stringColl the collection of string values to set in the IN clause.
     * @return the SQL IN Clause created from the string collection.
     */
    public String getInClause(Collection<String> stringColl) {
    	StringBuffer result = new StringBuffer("('");
    	if (stringColl != null && stringColl.size() > 0) {
    		Iterator<String> stringIter = stringColl.iterator();
    		while(stringIter.hasNext()) {
    			result.append(stringIter.next());
    			if (stringIter.hasNext()) {
    				result.append("','");
    			}
    		}
    	}
    	result.append("')");
    	return result.toString();
    }

    /**
     * Replace in the given query the string defined in the DEFAULT_IN_CLAUSE_KEY
     * by the IN clause created from  the given array of string.
     * @param query the query to change.
     * @param inClauseArray the array of string values.
     * @return the new query with the IN clause inserted.
     */
    public String replaceInClause(String query, String[] inClauseArray) {
    	return query.replaceFirst(DEFAULT_IN_CLAUSE_KEY, getInClause(inClauseArray));
    }

    /**
     * Replace in the given query the string defined in the DEFAULT_IN_CLAUSE_KEY
     * by the IN clause created from  the given collection of string.
     * @param query the query to change.
     * @param stringColl the collection of string values.
     * @return the new query with the IN clause inserted.
     */
    public String replaceInClause(String query, Collection<String> stringColl) {
    	return query.replaceFirst(DEFAULT_IN_CLAUSE_KEY, getInClause(stringColl));
    }

}
