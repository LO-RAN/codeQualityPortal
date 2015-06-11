/*
 * Variable.java
 *
 * Created on 23 mai 2003, 16:27
 */

package com.compuware.caqs.domain.calculation.rules;

import java.util.Collection;
import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.constants.Constants;

/**
 *
 * @author  cwfr-fdubois
 */
public class Variable extends Operand {
    
    protected String mId;
    
    /** Creates a new instance of Variable */
    public Variable(String id, String type) {
        mId = id.toUpperCase();
        mType = type;
    }
    
    public void init(Collection<Operand> operandList) {
    }
    
    public boolean evalBool(Map<String, ValuedObject> variables) throws InstantiationException {
        checkVariable(variables);
        return (evalNum(variables) != 0);
    }
    
    public double evalNum(Map<String, ValuedObject> variables) throws InstantiationException {
        double result = 0;
        checkVariable(variables);
        if (isNumericOperand()) {
            ValuedObject val = (ValuedObject)variables.get(mId);
            if (val != null) {
                result = val.getValue();
            }
        }
        return result;
    }

    public String evalString(Map<String, ValuedObject> variables) {
        return "" + variables.get(mId);
    }
    
    public void checkStringValidity(ValidityErrorCollection errors) {
        errors.addError(this, ValidityErrorCollection.NOT_STRING_ERROR);
    }
    
    protected void checkVariable(Map<String, ValuedObject> variables) throws InstantiationException {
        if (!variables.containsKey(mId)) {
            throw new InstantiationException("Variable "+mId+" does not exist.");
        }
    }
    
    public void checkValidity(ValidityErrorCollection errors) {
    }
    
    public void checkNumericValidity(ValidityErrorCollection errors) {
        if (!this.isNumericOperand()) {
            errors.addError(this, ValidityErrorCollection.NOT_NUMERIC_ERROR);
        }
    }
    
    public void checkBooleanValidity(ValidityErrorCollection errors) {
        if (!this.isBooleanOperand()) {
            errors.addError(this, ValidityErrorCollection.NOT_BOOLEAN_ERROR);
        }
    }

	public int getLevel() {
		return Constants.VALUE_LEVEL;
	}
    
    public String toString() {
        return mId;
    }
    
}
