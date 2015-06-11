/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.calcul;

/**
 * @author cwfr-fdubois
 *
 */
public interface ICalculationConfig {

	/**
	 * Needs the calculation to (re-)calculate metrics, criterions and goals?
	 * Default : false.
	 * @return true if the calculation needs to (re-)calculate metrics, criterions and goals, false else.
	 */
	public boolean needCalculation();
	
	/**
	 * Needs the calculation to (re-)calculate the consolidated metrics?
	 * Default : false.
	 * @return true if the calculation needs to (re-)calculate the consolidated metrics, false else.
	 */
	public boolean needMetricCalculation();
	
	/**
	 * Needs the calculation to (re-)calculate the criterions?
	 * Default : false.
	 * @return true if the calculation needs to (re-)calculate the criterions, false else.
	 */
	public boolean needCriterionCalculation();
	
	/**
	 * Needs the calculation to (re-)calculate the goals?
	 * Default : false.
	 * @return true if the calculation needs to (re-)calculate the goals, false else.
	 */
	public boolean needGoalCalculation();
	
	/**
	 * Needs the calculation to (re-)consolidate the results?
	 * Default : false.
	 * @return true if the calculation needs to (re-)consolidate the results, false else.
	 */
	public boolean needConsolidation();
	
        /**
         * Needs the update of the elements' trend
         * Default : false.
         * @return true if the calculation needs to update the trends, false else.
         */
        public boolean needTrendUpdate();

	/**
	 * Should the calculation target only justification changes?
	 * @return true if the calculation should target only justification changes, false else.
	 */
	public boolean targetOnlyJustification();
	
	/**
	 * @param needConsolidation the needConsolidation to set
	 */
	public void setNeedConsolidation(boolean needConsolidation);
	
	/**
	 * @param needMetricCalculation the needMetricCalculation to set
	 */
	public void setNeedMetricCalculation(boolean needMetricCalculation);
	
	/**
	 * @param needCriterionCalculation the needCriterionCalculation to set
	 */
	public void setNeedCriterionCalculation(boolean needCriterionCalculation);
	
	/**
	 * @param needGoalCalculation the needGoalCalculation to set
	 */
	public void setNeedGoalCalculation(boolean needGoalCalculation);

        /**
         * @param needTrendUpdate the needTrendUpdate to set
         */
        public void setNeedTrendUpdate(boolean needTrendUpdate);

}
