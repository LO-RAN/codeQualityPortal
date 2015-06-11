/*
 * Formula.java
 *
 * Created on 27 mai 2003, 17:23
 */

package com.compuware.caqs.domain.calculation.quality;

import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.Operand;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;

/**
 * A formula definition.
 * @author  cwfr-fdubois
 */
public class Formula implements com.compuware.caqs.domain.calculation.quality.Rule {
    
    /** the operand used to evaluate the formula. */
    private Operand mOperand = null;
    
    /** the value the formula has if verified. */
    private double mVerifiedValue = 0;
    
    public Formula() {
    	
    }
    
    /** Creates a new instance of Formula
     * @param value the value the formula has if verified.
     * @param op the operand used to evaluate the formula.
     */
    public Formula(double value, Operand op) {
        /* Formula initialisation. */
        mVerifiedValue = value;
        mOperand = op;
    }
    
    /** Evaluate the formula.
     * @param variables the variable map used for the evaluation.
     * @return the formula evaluation.
     * @throws InstantiationException if a variable has no value in the variable map.
     */    
    public double eval(Map<String, ValuedObject> variables) throws InstantiationException {
        double result = 0;
        if(mOperand != null) {
            if (mOperand.isBooleanOperand()) {
                /* The operand is a boolean one. */
                /* If the result is true, the formula is verified. */
                if (mOperand.evalBool(variables)) {
                    result = mVerifiedValue;
                }
            }
            else {
                /* The operand is a numeric one. */
                /* The result of the operand operation give its value to the formula. */
                result = mOperand.evalNum(variables);
            }
        }
        return result;
    }
    
    /** Check if the formula is valid.
     * @param errors the error collection to fill in.
     */    
    public void checkValidity(com.compuware.caqs.domain.calculation.rules.ValidityErrorCollection errors) {
        if(mOperand != null) {
            /* Check the operand validity. */
            mOperand.checkValidity(errors);
        }
    }
    
    /** Get the string representation of the formula.
     * @return the string representation of the formula.
     */    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        /* Print the formula's definition. */
        buffer.append("<FORMULA value=\""+mVerifiedValue+"\">\n");
        if(mOperand != null) {
            /* Append the operand representation. */
        	buffer.append("<RULE>");
            buffer.append(mOperand);
        	buffer.append("</RULE>");
        }
        buffer.append("\n</FORMULA>\n");
        return buffer.toString();
    }
    
}
