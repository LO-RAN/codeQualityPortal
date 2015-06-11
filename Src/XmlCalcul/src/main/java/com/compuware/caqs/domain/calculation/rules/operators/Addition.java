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

/** Implements the addition operation.
 * @author cwfr-fdubois
 */
public class Addition extends NumericOperation {
	
	private static final String ADDITION_OPERATOR_STRING = "+";

	/** Evaluate the addition numeric operation using given variables.
     * @param variables given variables to use for the addition calculation.
     * @throws InstantiationException Thrown if a variable does not exist.
     * @return The addition operation result. 0 if no operand exist.
     */    
    public double evalNum(Map<String, ValuedObject> variables) throws InstantiationException {
        double result = 0;
        int nbExceptions = 0;
        Iterator<Operand> i = mOperands.iterator();
        // Loop on addition operands
        while (i.hasNext()) {
            Operand op = i.next();
            try {
	            // Add numeric operand evaluation to the result.
	            result += op.evalNum(variables);
            }
            catch (InstantiationException e) {
            	nbExceptions++;
            }
        }
        if (result == 0 && nbExceptions == mOperands.size()) {
        	throw new InstantiationException("Variables does not exist.");
        }
        return result;
    }
    
    public String getOperatorAsString() {
    	return Addition.ADDITION_OPERATOR_STRING;
    }
    
	public int getLevel() {
		return Constants.MINUSPLUS_OPERATOR_LEVEL;
	}
	
}
