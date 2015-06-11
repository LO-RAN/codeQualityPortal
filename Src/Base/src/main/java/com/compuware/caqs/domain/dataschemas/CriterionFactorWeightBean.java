package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 25 avr. 2005
 * Time: 13:38:50
 * To change this template use File | Settings | File Templates.
 */
public class CriterionFactorWeightBean extends FactorDefinitionBean implements Serializable {

	private static final long serialVersionUID = 3381031591596355577L;

	protected double mWeight = 0.0;
    protected double mSumWeight = 0.0;


    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        this.mWeight = weight;
    }

    public double getSumWeight() {
        return mSumWeight;
    }

    public void setSumWeight(double sumWeight) {
        this.mSumWeight = sumWeight;
    }

    public double getPctWeight() {
        double result = 0.0;
        if (this.mWeight >= 0) {
            result = this.mSumWeight/this.mWeight;
        }
        return result;
    }

    public void setSumWeight(Double sumWeight) {
        if (sumWeight != null) {
            this.mSumWeight = sumWeight.doubleValue();
        }
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("[FACTWEIGHT ");
        result.append(super.toString());
        result.append("WEIGHT=").append(mWeight);
        result.append("SUM_WEIGHT=").append(mSumWeight);
        result.append("]");
        return result.toString();
    }
}
