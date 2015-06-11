/**
 * 
 */
package com.compuware.caqs.business.calculation;

import com.compuware.caqs.business.calculation.exception.CalculationException;
import com.compuware.caqs.business.calculation.exception.CalculationPostConditionException;
import com.compuware.caqs.business.calculation.exception.CalculationPreConditionException;
import com.compuware.caqs.domain.dataschemas.analysis.AnalysisConfig;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;


/**
 * Define what a calculator should do.
 * @author cwfr-fdubois
 *
 */
public interface ICalculator {

	/**
	 * Init the element according to the given configuration.
	 * @param analysisParameters the analysis parameters.
	 * @param config the given configuration.
	 */
	public void init(AnalysisConfig analysisParameters, ICalculationConfig config) throws CalculationPreConditionException;
	
	/**
	 * Calculate the element according to the given configuration.
	 * @param config the given configuration.
	 */
	public void calculate() throws CalculationException, CalculationPostConditionException;
	
}
