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

/** Implements the logical OR operation.
 * @author cwfr-fdubois
 */
public class Or extends BooleanOperation {
    
    private static final String OR_OPERATOR_STRING = "||";

	public boolean evalBool(Map<String, ValuedObject> variables) throws InstantiationException {
        boolean result = false;
        Iterator<Operand> i = mOperands.iterator();
        if (i.hasNext()) {
            Operand firstOp = i.next();
            result = evalBool(firstOp, variables);
            while (i.hasNext() && !result) {
                Operand op = i.next();
                result = (result || evalBool(op, variables));
            }
        }
        return result;
    }
	
	private boolean evalBool(Operand op, Map<String, ValuedObject> variables) {
		boolean result = false;
		try {
			result = op.evalBool(variables);
		}
		catch (InstantiationException e) {
			result = false;
		}
		return result;
	}
    
    public void checkBooleanValidity(ValidityErrorCollection errors) {
        Iterator<Operand> it = this.mOperands.iterator();
        while (it.hasNext()) {
            Operand op = it.next();
            op.checkBooleanValidity(errors);
        }
    }

	public String getOperatorAsString() {
		return Or.OR_OPERATOR_STRING;
	}
	    
	public int getLevel() {
		return Constants.BOOLEAN_OPERATOR_LEVEL;
	}
	    
}
