/*
 * OpPlus.java
 *
 * Created on 26 mars 2004, 16:01
 */

package com.compuware.caqs.domain.calculation.rules.operators;

import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.Operation;
import com.compuware.caqs.domain.calculation.rules.ValidityErrorCollection;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;

/** Represent a boolean operation.
 * @author cwfr-fdubois
 */
public abstract class BooleanOperation extends Operation {
    
    /** Greater Than (>) operator. */    
    protected static final String GT_OP = "gt";
    /** Greater or Equal (>=) operator. */    
    protected static final String GE_OP = "ge";
    /** Lower Than (<) operator. */    
    protected static final String LT_OP = "lt";
    /** Lower or Equal (<=) operator. */    
    protected static final String LE_OP = "le";
    /** Equal (=) operator. */    
    protected static final String EQ_OP = "eq";
    
    public double evalNum(Map<String, ValuedObject> variables) throws InstantiationException {
        double result = 0.0;
        boolean value = evalBool(variables);
        if (value == true) {
            result = 1.0;
        }
        return result;
    }
    
    public String evalString(Map<String, ValuedObject> variables) throws InstantiationException {
        return "" + this.evalBool(variables);
    }
    
    public abstract boolean evalBool(Map<String, ValuedObject> variables) throws InstantiationException;
    
    public void checkNumericValidity(ValidityErrorCollection errors) {
        errors.addError(this, ValidityErrorCollection.NOT_NUMERIC_ERROR);
    }
    
}
