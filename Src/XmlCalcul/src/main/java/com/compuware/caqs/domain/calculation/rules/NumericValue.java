/*
 * NumericValue.java
 *
 * Created on 23 mai 2003, 16:47
 */

package com.compuware.caqs.domain.calculation.rules;

import java.util.Collection;
import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.constants.Constants;

/**
 *
 * @author  cwfr-fdubois
 */
public class NumericValue extends Operand {
    
    private double mValue;
    
    /** Creates a new instance of NumericValue */
    public NumericValue(String strValue) {
        mValue = Double.parseDouble(strValue);
        mType = Operand.NUM_TYPE;
    }
    
    public void init(Collection<Operand> operandList) {
    }
    
    public boolean evalBool(Map<String, ValuedObject> variables) {
        return mValue != 0;
    }
    
    public double evalNum(Map<String, ValuedObject> variables) {
        return mValue;
    }
    
    public String evalString(Map<String, ValuedObject> variables) {
        return "" + mValue;
    }
    
    public void checkStringValidity(ValidityErrorCollection errors) {
        errors.addError(this, ValidityErrorCollection.NOT_STRING_ERROR);
    }
    
    public void checkValidity(ValidityErrorCollection errors) {
        checkNumericValidity(errors);
    }
    
    public void checkNumericValidity(ValidityErrorCollection errors) {
        if (!this.isNumericOperand()) {
            errors.addError(this, ValidityErrorCollection.NOT_NUMERIC_ERROR);
        }
    }
    
    public void checkBooleanValidity(ValidityErrorCollection errors) {
        errors.addError(this, ValidityErrorCollection.NOT_BOOLEAN_ERROR);
    }

	public int getLevel() {
		return Constants.VALUE_LEVEL;
	}
    
    public String toString() {
        return ""+mValue;
    }
}
