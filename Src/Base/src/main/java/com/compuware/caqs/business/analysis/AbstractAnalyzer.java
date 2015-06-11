/**
 * 
 */
package com.compuware.caqs.business.analysis;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.analysis.exception.AnalysisPostConditionException;
import com.compuware.caqs.business.analysis.exception.AnalysisPreConditionException;
import com.compuware.caqs.domain.dataschemas.analysis.EA;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class AbstractAnalyzer implements IAnalyzer {

	/**
	 * The analysis configuration.
	 */
	protected StaticAnalysisConfig config = null;
	
	/**
	 * Check environment and data for analysis pre-requisit.
	 */
	public abstract void preAnalyzeCheck(EA curEA) throws AnalysisPreConditionException;
	
	/**
	 * Check environment and data for analysis ending.
	 */
	public abstract void postAnalyzeCheck(EA curEA) throws AnalysisPostConditionException;
	
	/**
	 * Realize the analysis.
	 */
	public abstract void doAnalyze(EA curEA) throws AnalysisException;
	
	/** {@inheritDoc}
	 */
	public void analyze(EA curEA) throws AnalysisException, AnalysisPreConditionException, AnalysisPostConditionException {
		preAnalyzeCheck(curEA);
		doAnalyze(curEA);
		postAnalyzeCheck(curEA);
	}
	
	/** {@inheritDoc}
	 */
	public void init(StaticAnalysisConfig pConfig) {
		this.config = pConfig;
	}

	/**
	 * Check environment and data for load pre-requisit.
	 */
	public abstract void preLoadCheck(EA curEA) throws AnalysisPreConditionException;
	
	/**
	 * Check environment and data for load ending.
	 */
	public abstract void postLoadCheck(EA curEA) throws AnalysisPostConditionException;
	
	/**
	 * Realize the data load.
	 */
	public abstract void doLoad(EA curEA) throws AnalysisException;
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.analysis.IAnalyzer#load()
	 */
	public void load(EA curEA) throws AnalysisException, AnalysisPreConditionException, AnalysisPostConditionException {
		preLoadCheck(curEA);
		doLoad(curEA);
		postLoadCheck(curEA);
	}

}
