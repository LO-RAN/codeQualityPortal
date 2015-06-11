/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.calcul.impl;

import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;

/**
 * Implements a calculation configuration.
 * @author cwfr-fdubois
 *
 */
public class CalculationConfig implements ICalculationConfig {

    /** Identify if the calculation needs consolidation.*/
    private boolean needConsolidation = true;
    /** Identify if the calculation needs metric calculation.*/
    private boolean needMetricCalculation = true;
    /** Identify if the calculation needs criterion calculation.*/
    private boolean needCriterionCalculation = true;
    /** Identify if the calculation needs metric calculation.*/
    private boolean needGoalCalculation = true;
    /** Identify if the calculation needs to update the trend.*/
    private boolean needTrendUpdate = false;
    /**
     * Optimization flag. The calculation will only look for justifications
     * and change associated calculation results.
     */
    private boolean targetOnlyJustification = false;

    /**
     * @{@inheritDoc }
     */
    public boolean needConsolidation() {
        return this.needConsolidation;
    }

    /**
     * @{@inheritDoc }
     */
    public boolean needCalculation() {
        return this.needMetricCalculation
                || this.needCriterionCalculation
                || this.needGoalCalculation
                || this.needTrendUpdate;
    }

    /**
     * @{@inheritDoc }
     */
    public boolean needCriterionCalculation() {
        return this.needCriterionCalculation;
    }

    /**
     * @{@inheritDoc }
     */
    public boolean needGoalCalculation() {
        return this.needGoalCalculation;
    }

    /**
     * @{@inheritDoc }
     */
    public boolean needMetricCalculation() {
        return this.needMetricCalculation;
    }

    /**
     * Set if the calculation target only justification changes or not.
     * @param value the justification target flag.
     */
    public void setTargetOnlyJustification(boolean value) {
        this.targetOnlyJustification = value;
    }

    /**
     * @{@inheritDoc }
     */
    public boolean targetOnlyJustification() {
        return this.targetOnlyJustification;
    }

    /**
     * @{@inheritDoc }
     */
    public void setNeedConsolidation(boolean needConsolidation) {
        this.needConsolidation = needConsolidation;
    }

    /**
     * @{@inheritDoc }
     */
    public void setNeedMetricCalculation(boolean needMetricCalculation) {
        this.needMetricCalculation = needMetricCalculation;
    }

    /**
     * @{@inheritDoc }
     */
    public void setNeedCriterionCalculation(boolean needCriterionCalculation) {
        this.needCriterionCalculation = needCriterionCalculation;
    }

    /**
     * @{@inheritDoc }
     */
    public void setNeedGoalCalculation(boolean needGoalCalculation) {
        this.needGoalCalculation = needGoalCalculation;
    }

    /**
     * @{@inheritDoc }
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Calculation Config: ");
        result.append("Need consolidation: " + this.needConsolidation);
        result.append(", Need calculation: " + this.needCalculation());
        result.append(", Need criterion calculation: "
                + this.needCriterionCalculation);
        result.append(", Need goal calculation: " + this.needGoalCalculation);
        result.append(", Need metric calculation: " + this.needMetricCalculation);
        return result.toString();
    }

    /**
     * @{@inheritDoc }
     */
    public boolean needTrendUpdate() {
        return this.needTrendUpdate;
    }

    /**
     * @{@inheritDoc }
     */
    public void setNeedTrendUpdate(boolean needTrendUpdate) {
        this.needTrendUpdate = needTrendUpdate;
    }
}
