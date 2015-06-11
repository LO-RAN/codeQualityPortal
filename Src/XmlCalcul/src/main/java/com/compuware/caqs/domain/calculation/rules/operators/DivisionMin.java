/*
 * DivisionMaxUnder.java
 *
 * Created on 30 mars 2004, 11:10
 */

package com.compuware.caqs.domain.calculation.rules.operators;

/** Implements the minimum division operator.
 * @author cwfr-fdubois
 */
public class DivisionMin extends Division {
    
    private static final String DIVISION_MIN_OPERATOR_STRING = "//";

	/** Evaluate the between 0 and 1 division operation on the two given argument. The
     * greater argument divide the lower argument.
     * @param op1 the first operand.
     * @param op2 the second operand.
     * @return the lower argument over the greater argument division (l/g).
     */    
    protected double evalNum(double op1, double op2) {
        double result = 0;
        if (op2 >= op1 && op2 > 0) {
            result = op1/op2;
        }
        else {
            result = op2/op1;
        }
        return result;
    }
    
	public String getOperatorAsString() {
		return DivisionMin.DIVISION_MIN_OPERATOR_STRING;
	}
}
