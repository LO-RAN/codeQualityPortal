/*
 * FormulaFactory.java
 *
 * Created on 27 mai 2003, 17:42
 */

package com.compuware.caqs.domain.calculation.quality;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class RuleFactory {
    
    com.compuware.caqs.domain.calculation.rules.OperandFactory mOperandFactory = null;
    
    public void setOperandFactory(com.compuware.caqs.domain.calculation.rules.OperandFactory factory) {
        this.mOperandFactory = factory;
    }
    
    public com.compuware.caqs.domain.calculation.rules.OperandFactory getOperandFactory() {
        return this.mOperandFactory;
    }
    
    public abstract com.compuware.caqs.domain.calculation.quality.Rule create();
    
}
