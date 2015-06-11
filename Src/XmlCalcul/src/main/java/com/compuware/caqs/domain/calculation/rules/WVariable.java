/*
 * WVariable.java
 *
 * Created on 29 mars 2004, 10:46
 */

package com.compuware.caqs.domain.calculation.rules;

/**
 *
 * @author  cwfr-fdubois
 */
public class WVariable extends com.compuware.caqs.domain.calculation.rules.Variable implements com.compuware.caqs.domain.calculation.rules.WeightObject {
    
    protected double mWeight = 1.0;
    protected boolean mVarIsWeight = false;
    
    /** Creates a new instance of WVariable */
    public WVariable(String id, String type) {
        super(id, type);
    }
    
    public double getWeight() {
        return this.mWeight;
    }
    
    public void setWeight(double weight) {
        this.mWeight = weight;
    }
    
    public boolean getVarIsWeight() {
        return this.mVarIsWeight;
    }
    
    public void setVarIsWeight(boolean varisweight) {
        this.mVarIsWeight = varisweight;
    }
    
}
