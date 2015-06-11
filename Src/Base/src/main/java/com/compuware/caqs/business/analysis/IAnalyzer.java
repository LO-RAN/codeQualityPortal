/**
 * 
 */
package com.compuware.caqs.business.analysis;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.analysis.exception.AnalysisPostConditionException;
import com.compuware.caqs.business.analysis.exception.AnalysisPreConditionException;
import com.compuware.caqs.domain.dataschemas.analysis.EA;

/**
 * Define analyzer behaviour.
 * @author cwfr-fdubois
 * 
 */
public interface IAnalyzer {

	/**
	 * Initialize the analyzer.
	 * @param config the analyzer configuration.
	 */
    public abstract void init(StaticAnalysisConfig config);
	
    /**
     * Check if the analysis is possible for the given module.
     * @param curEA the given module.
     * @return true if the analysis is possible, false else.
     */
	public abstract boolean isAnalysisPossible(EA curEA);

    /**
     * Analyze.
     * @param curEA the given module.
     */
    public abstract void analyze(EA curEA) throws AnalysisException, AnalysisPreConditionException, AnalysisPostConditionException;
    
    /**
     * Load the result data in the database.
     * @param curEA the given module.
     */
    public abstract void load(EA curEA) throws AnalysisException, AnalysisPreConditionException, AnalysisPostConditionException;
    
}
