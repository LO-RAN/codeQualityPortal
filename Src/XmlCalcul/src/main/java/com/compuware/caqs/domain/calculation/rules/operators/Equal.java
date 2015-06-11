/*
 * OpPlus.java
 *
 * Created on 26 mars 2004, 16:01
 */

package com.compuware.caqs.domain.calculation.rules.operators;

import java.util.Iterator;
import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.Operand;
import com.compuware.caqs.domain.calculation.rules.ValidityErrorCollection;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;

/** Implements the equal operator.
 * @author cwfr-fdubois
 */
public class Equal extends BooleanOperation {
	
	private static final String EQUAL_OPERATOR_STRING = "=";
    
    public boolean evalBool(Map<String, ValuedObject> variables) throws InstantiationException {
        boolean result = false;
        Iterator<Operand> i = mOperands.iterator();
        if (i.hasNext()) {
            Operand firstOp = i.next();
            if (firstOp.isStringOperand()) {
            	String tmp = firstOp.evalString(variables);
	            if (i.hasNext()) {
	                Operand op = i.next();
	                result = (tmp.equalsIgnoreCase(op.evalString(variables)));
	            }
            }
            else {
	            double tmp = 0.0;
	            tmp = firstOp.evalNum(variables);
	            if (i.hasNext()) {
	                Operand op = i.next();
	                result = (tmp == op.evalNum(variables));
	            }
            }
        }
        return result;
    }
    
    public void checkBooleanValidity(ValidityErrorCollection errors) {
        Iterator<Operand> it = this.mOperands.iterator();
        while (it.hasNext()) {
            Operand op = it.next();
            op.checkNumericValidity(errors);
        }
    }

    public String getOperatorAsString() {
    	return Equal.EQUAL_OPERATOR_STRING;
    }
    
	public int getLevel() {
		return Constants.COMPARATOR_OPERATOR_LEVEL;
	}
	    
}
