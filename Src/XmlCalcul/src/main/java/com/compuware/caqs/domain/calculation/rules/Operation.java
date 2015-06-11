/*
 * Operation.java
 *
 * Created on 23 mai 2003, 14:51
 */

package com.compuware.caqs.domain.calculation.rules;

import java.util.Collection;
import java.util.Iterator;

/** Define an operation.
 * @author cwfr-fdubois
 */
public abstract class Operation extends com.compuware.caqs.domain.calculation.rules.Operand {
    
    private static final String[] NUMERIC_OPERATORS = {"+", "-", "*", "/", "AVG"};
    private static final String[] BOOLEAN_OPERATORS = {"EQ", "LT", "LE", "GT", "GE", "AND", "OR"};
    
    private static final short AND_OP = 5;
    private static final short OR_OP = 6;

    protected Collection<Operand> mOperands = null;
    
    protected String mId;
    protected Double mDefault = null;
    
    /** Creates a new instance of Operation
     */
    public Operation() {
    }
    
    public void setId(String id) {
        this.mId = id;
    }
    
    public void setDefault(Double val) {
        this.mDefault = val;
    }
    
    /** Initialize the operands of the Operation.
     * @param operandList The operand list for the Operation.
     */    
    public void init(Collection<Operand> operandList) {
        mOperands = operandList;
    }
    
    private boolean isAndOrOperator() {
        boolean result = false;
        if (mId != null) {
            result = (mId.equalsIgnoreCase(BOOLEAN_OPERATORS[Operation.AND_OP]) ||
                        mId.equalsIgnoreCase(BOOLEAN_OPERATORS[Operation.OR_OP]));
        }
        return result;
    }
    
    public void checkValidity(ValidityErrorCollection errors) {
        if (isNumericOperand()) {
            checkNumericValidity(errors);
        }
        if (isBooleanOperand()) {
            checkBooleanValidity(errors);
        }
    }
    
    public void checkNumericValidity(ValidityErrorCollection errors) {
        boolean isValid = false;
        int i = 0;
        while ((i < NUMERIC_OPERATORS.length) && (isValid == false)) {
            if (mId.equalsIgnoreCase(NUMERIC_OPERATORS[i])) {
                isValid = true;
            }
            i++;
        }
        if (isValid) {
            Iterator<Operand> it = this.mOperands.iterator();
            while (it.hasNext()) {
                Operand op = it.next();
                op.checkNumericValidity(errors);
            }
        }
        else {
            errors.addError(this, ValidityErrorCollection.NOT_NUMERIC_ERROR);
        }
    }
    
    public void checkBooleanValidity(ValidityErrorCollection errors) {
        boolean isValid = false;
        if (this.isNumericOperand()) {
            errors.addError(this, ValidityErrorCollection.NOT_BOOLEAN_ERROR);
        }
        int i = 0;
        while ((i < BOOLEAN_OPERATORS.length) && (isValid == false)) {
            if (mId.equalsIgnoreCase(BOOLEAN_OPERATORS[i])) {
                isValid = true;
            }
            i++;
        }
        if (isValid) {
            boolean isAndOr = this.isAndOrOperator();
            Iterator<Operand> it = this.mOperands.iterator();
            while (it.hasNext()) {
                Operand op = it.next();
                if (isAndOr) {
                    op.checkBooleanValidity(errors);
                }
                else {
                    op.checkNumericValidity(errors);
                }
            }
        }
        else {
            errors.addError(this, ValidityErrorCollection.NOT_BOOLEAN_ERROR);
        }
    }
    
    public abstract String getOperatorAsString();
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (mOperands != null) {
            Iterator<Operand> i = this.mOperands.iterator();
            if (i.hasNext()) {
                while (i.hasNext()) {
                    Operand op = i.next();
                    if (op != null) {
                    	if (op.getLevel() < this.getLevel()) {
                    		buffer.append('(');
                    	}
                        buffer.append(op);
                    	if (op.getLevel() < this.getLevel()) {
                    		buffer.append(')');
                    	}
                    	if (i.hasNext()) {
                    		buffer.append(' ').append(getOperatorAsString()).append(' ');
                    	}
                    }
                }
            }
        }
        return buffer.toString();
    }
    
}
