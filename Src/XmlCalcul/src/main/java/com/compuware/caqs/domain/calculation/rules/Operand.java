/*
 * Operand.java
 *
 * Created on 23 mai 2003, 14:30
 */

package com.compuware.caqs.domain.calculation.rules;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class Operand {
    
    protected static final String STR_TYPE = "STRING";
    
    protected static final String NUM_TYPE = "NUMERIC";
    
    protected static final String BOOL_TYPE = "BOOLEAN";
    
    protected String mType = null;
    
    /** Creates a new instance of Operand */
    public Operand() {
    }
    
    public abstract int getLevel();

	public String getType() {
        return mType;
    }
    
    public void setType(String type) {
        mType = type;
    }
    
    public boolean isStringOperand() {
        boolean result = false;
        if (mType != null) {
            result = mType.equalsIgnoreCase(STR_TYPE);
        }
        return result;
    }
    
    public boolean isNumericOperand() {
        boolean result = false;
        if (mType != null) {
            result = mType.equalsIgnoreCase(NUM_TYPE);
        }
        return result;
    }
    
    public boolean isBooleanOperand() {
        boolean result = false;
        if (mType != null) {
            result = mType.equalsIgnoreCase(BOOL_TYPE);
        }
        return result;
    }
    
    public abstract boolean evalBool(Map<String, ValuedObject> variables) throws InstantiationException;
    
    public abstract double evalNum(Map<String, ValuedObject> variables) throws InstantiationException;
    
    public abstract String evalString(Map<String, ValuedObject> variables) throws InstantiationException;

    public abstract void init(Collection<Operand> operandList);
    
    public abstract void checkValidity(ValidityErrorCollection errors);
    
    public abstract void checkNumericValidity(ValidityErrorCollection errors);
    
    public abstract void checkBooleanValidity(ValidityErrorCollection errors);
    
}
