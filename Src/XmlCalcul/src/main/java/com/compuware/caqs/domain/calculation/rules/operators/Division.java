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

/** Implements the division operator.
 * @author cwfr-fdubois
 */
public class Division extends NumericOperation {
    
	private static final String DIVISION_OPERATOR_STRING = "/";

    /** Evaluate the division for the operation operands.
     * @param variables given variables to use for the division calculation.
     * @throws InstantiationException Thrown if a variable does not exist.
     * @return op1/op2.
     */    
    public double evalNum(Map<String, ValuedObject> variables) throws InstantiationException {
        double result = 0;
        Iterator<Operand> i = mOperands.iterator();
        Operand firstOp = null;
        Operand secondOp = null;
        if (i.hasNext()) {
            firstOp = i.next();
        }
        if (i.hasNext()) {
            secondOp = i.next();
        }
        if (firstOp != null && secondOp != null) {
            result = evalNum(firstOp.evalNum(variables), secondOp.evalNum(variables));
        }
        return result;
    }
    
    /** Evaluate the division on the two given argument (op1/op2).
     * @param op1 the first operand.
     * @param op2 the second operand.
     * @return op1/op2.
     */    
    protected double evalNum(double op1, double op2) {
        double result = 0;
        if (op2 != 0) {
            result = op1/op2;
        }
        return result;
    }
    
	public int getLevel() {
		return Constants.MULTDIV_OPERATOR_LEVEL;
	}

	public String getOperatorAsString() {
		return Division.DIVISION_OPERATOR_STRING;
	}
	    
}
