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

/** Implements the Lower Than operator.
 * @author cwfr-fdubois
 */
public class LowerThan extends BooleanOperation {
    
	private static final String LOWER_THAN_OPERATOR_STRING = "&lt;";
	private static final String LOWER_THAN_EQUAL_OPERATOR_STRING = "&lt;=";

    public boolean evalBool(Map<String, ValuedObject> variables) throws InstantiationException {
        boolean result = false;
        double tmp = 0.0;
        Iterator<Operand> i = mOperands.iterator();
        if (i.hasNext()) {
            Operand firstOp = i.next();
            tmp = firstOp.evalNum(variables);
            if (i.hasNext()) {
                Operand op = i.next();
                result = evalBool(tmp, op.evalNum(variables));
            }
        }
        return result;
    }
    
    private boolean evalBool(double v1, double v2) {
        boolean result = false;
        if (mId.equals(BooleanOperation.LT_OP)) {
            result = v1 < v2;
        }
        else if (mId.equals(BooleanOperation.LE_OP)) {
            result = v1 <= v2;
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
		String result = "";
		if (mId.equals(BooleanOperation.LT_OP)) {
            result = LowerThan.LOWER_THAN_OPERATOR_STRING;
        }
        else if (mId.equals(BooleanOperation.LE_OP)) {
            result = LowerThan.LOWER_THAN_EQUAL_OPERATOR_STRING;
        }
		return result;
	}
	    
	public int getLevel() {
		return Constants.COMPARATOR_OPERATOR_LEVEL;
	}
	    
}
