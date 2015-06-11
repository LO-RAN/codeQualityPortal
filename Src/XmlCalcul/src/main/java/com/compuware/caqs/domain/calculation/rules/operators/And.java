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

/** Implements the AND logical operator.
 * @author cwfr-fdubois
 */
public class And extends BooleanOperation {
    
	private static final String AND_OPERATOR_STRING = "&amp;&amp;";

	/** Evaluate the logical AND operation using given variables.
     * @param variables given variables to use for the AND calculation.
     * @throws InstantiationException Thrown if a variable does not exist.
     * @return The result of the AND operation.
     */    
    public boolean evalBool(Map<String, ValuedObject> variables) throws InstantiationException {
        boolean result = false;
        Iterator<Operand> i = mOperands.iterator();
        if (i.hasNext()) {
            // Initialize the result using the first operand.
            Operand firstOp = i.next();
            result = firstOp.evalBool(variables);
            // For each other operands
            while (i.hasNext() && result) {
                Operand op = i.next();
                // Evaluate the operand boolean value
                // and make a logical AND operation with the result.
                result = (result && op.evalBool(variables));
            }
        }
        return result;
    }
    
    /** Check the AND operation for boolean invalid syntaxe.
     * @param errors A collection of error used to store validity errors.
     */    
    public void checkBooleanValidity(ValidityErrorCollection errors) {
        Iterator<Operand> it = this.mOperands.iterator();
        // Loop on operands.
        while (it.hasNext()) {
            Operand op = it.next();
            // Validate operand and add errors to the collection.
            op.checkBooleanValidity(errors);
        }
    }

    public String getOperatorAsString() {
    	return And.AND_OPERATOR_STRING;
    }
    
	public int getLevel() {
		return Constants.BOOLEAN_OPERATOR_LEVEL;
	}
	    
}
