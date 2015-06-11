/*
 * ValidityError.java
 *
 * Created on 3 juin 2003, 10:49
 */

package com.compuware.caqs.domain.calculation.rules;

/**
 *
 * @author  cwfr-fdubois
 */
public class ValidityError {
    
    private Operand mOperand = null;
    private String mErrorKey = null;
    
    /** Creates a new instance of ValidityError */
    public ValidityError(String errorKey, Operand op) {
        this.mOperand = op;
        this.mErrorKey = errorKey;
    }
    
    public String toString(java.util.Properties prop) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prop.getProperty(mErrorKey));
        buffer.append(": \n");
        buffer.append(mOperand);
        return buffer.toString();
    }
}
