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
public class BooleanValue extends Operand {
    
    private boolean mValue;
    
    /** Creates a new instance of NumericValue */
    public BooleanValue(String strValue) {
        Boolean tmp = Boolean.valueOf(strValue);
        mValue = tmp.booleanValue();
        mType = Operand.BOOL_TYPE;
    }
    
    public void init(Collection<Operand> operandList) {
    }
    
    public boolean evalBool(Map<String, ValuedObject> variables) {
        return mValue;
    }
    
    public double evalNum(Map<String, ValuedObject> variables) {
        return mValue ? 1 : 0;
    }
    
    public String evalString(Map<String, ValuedObject> variables) {
        return "" + mValue;
    }
    
    public void checkValidity(ValidityErrorCollection errors) {
        checkBooleanValidity(errors);
    }
    
    public void checkNumericValidity(ValidityErrorCollection errors) {
        errors.addError(this, ValidityErrorCollection.NOT_NUMERIC_ERROR);
    }
    
    public void checkStringValidity(ValidityErrorCollection errors) {
        errors.addError(this, ValidityErrorCollection.NOT_STRING_ERROR);
    }
    
    public void checkBooleanValidity(ValidityErrorCollection errors) {
        if (!this.isBooleanOperand()) {
            errors.addError(this, ValidityErrorCollection.NOT_BOOLEAN_ERROR);
        }
    }
    
    public String toString() {
        return ""+mValue;
    }

	public int getLevel() {
		return Constants.VALUE_LEVEL;
	}
}
