/*
 * OpPlus.java
 *
 * Created on 26 mars 2004, 16:01
 */

package com.compuware.caqs.domain.calculation.rules.operators;

import java.util.Iterator;
import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.Operand;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;

/** Implements the multiplication operator.
 * @author cwfr-fdubois
 */
public class Multiplication extends NumericOperation {
    
    private static final String MULTIPLICATION_OPERATOR_STRING = "*";

	public double evalNum(Map<String, ValuedObject> variables) throws InstantiationException {
        double result = 0;
        Iterator<Operand> i = mOperands.iterator();
        if (i.hasNext()) {
            Operand firstOp = i.next();
            result = firstOp.evalNum(variables);
            while (i.hasNext()) {
                Operand op = i.next();
                result *= op.evalNum(variables);
            }
        }
        return result;
    }
    
	public String getOperatorAsString() {
		return Multiplication.MULTIPLICATION_OPERATOR_STRING;
	}
	    
	public int getLevel() {
		return Constants.MULTDIV_OPERATOR_LEVEL;
	}
	    
}
