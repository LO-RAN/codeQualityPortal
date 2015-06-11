/*
 * StringValue.java
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
public class StringValue extends com.compuware.caqs.domain.calculation.rules.Operand {
    
    private String mValue;
    
    /** Creates a new instance of StringValue */
    public StringValue(String strValue) {
        mValue = strValue;
        mType = Operand.STR_TYPE;
    }
    
    public void init(Collection<Operand> operandList) {
    }
    
    public boolean evalBool(Map<String, ValuedObject> variables) {
        return Boolean.parseBoolean(mValue);
    }
    
    public double evalNum(Map<String, ValuedObject> variables) {
        return Double.parseDouble(mValue);
    }
    
    public String evalString(Map<String, ValuedObject> variables) {
        return mValue;
    }
    
    public void checkValidity(ValidityErrorCollection errors) {
        checkStringValidity(errors);
    }
    
    public void checkNumericValidity(ValidityErrorCollection errors) {
        errors.addError(this, ValidityErrorCollection.NOT_NUMERIC_ERROR);
    }
    
    public void checkStringValidity(ValidityErrorCollection errors) {
        if (!this.isStringOperand()) {
            errors.addError(this, ValidityErrorCollection.NOT_STRING_ERROR);
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
