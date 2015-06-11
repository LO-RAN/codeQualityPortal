/*
 * OpPlus.java
 *
 * Created on 26 mars 2004, 16:01
 */

package com.compuware.caqs.domain.calculation.rules.operators;

import java.util.Iterator;
import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.Operand;
import com.compuware.caqs.domain.calculation.rules.Operation;
import com.compuware.caqs.domain.calculation.rules.ValidityErrorCollection;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.calculation.rules.WeightObject;

/** Represent a numeric operation.
 * @author cwfr-fdubois
 */
public abstract class NumericOperation extends Operation implements WeightObject {
    
    protected double mWeight = 1.0;
    
    public double getWeight() {
        return this.mWeight;
    }
    
    public void setWeight(double weight) {
        this.mWeight = weight;
    }
    
    public boolean evalBool(Map<String, ValuedObject> variables) throws InstantiationException {
        boolean result = false;
        double value = evalNum(variables);
        if (value > 0) {
            result = true;
        }
        return result;
    }
    
    public String evalString(Map<String, ValuedObject> variables) throws InstantiationException {
        return "" + this.evalNum(variables);
    }
    
    public abstract double evalNum(Map<String, ValuedObject> variables) throws InstantiationException;

    public void checkNumericValidity(ValidityErrorCollection errors) {
        Iterator<Operand> it = this.mOperands.iterator();
        while (it.hasNext()) {
            Operand op = it.next();
            op.checkNumericValidity(errors);
        }
    }
    
    public void checkBooleanValidity(ValidityErrorCollection errors) {
        errors.addError(this, ValidityErrorCollection.NOT_BOOLEAN_ERROR);
    }
    
}
