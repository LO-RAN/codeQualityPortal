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
import com.compuware.caqs.domain.calculation.rules.WVariable;
import com.compuware.caqs.domain.calculation.rules.WeightObject;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;

/**
 * Implements the average operation.
 * @author  cwfr-fdubois
 */
public class Average extends NumericOperation {
    
    private static final String AVERAGE_OPERATION_STRING = "AVG";

	/** Evaluate the numeric operation using given variables.
     * @param variables given variables to use for the operation calculation.
     * @return the average operation.
     * @throws InstantiationException Thrown if a variable does not exist.
     */
    public double evalNum(Map<String, ValuedObject> variables) throws InstantiationException {
        double result = 0;
        double sumWeight = 0;
        /* Parcours des opérandes de l'opération. */
        Iterator<Operand> i = mOperands.iterator();
        while (i.hasNext()) {
            Operand op = i.next();
            /* Evaluation de l'opérande. */
            double tmp = op.evalNum(variables);
            if (op instanceof WeightObject) {
                /* L'opérande possède un poids. */
                sumWeight += getWeight(op, tmp);
                /* On pondère l'opération en utilisant le poids associé. */
                result += tmp*((WeightObject)op).getWeight();
            }
            else {
                /* L'opérande ne possède pas de poids. */
                result += tmp;
                /* Le poids par défaut est 1. Equivalent d'une moyenne non pondérée. */
                sumWeight += 1;
            }
        }
        if (sumWeight == 0) {
            sumWeight = 1;
        }
        if (result == 0) {
            /* Le résultat de la moyenne est nul, la valeur par défaut est retournée. */
            result = mDefault.doubleValue();
        }
        else {
            /* Résultat = Somme(valeur*poids)/Somme(poids). */
            result = result/sumWeight;
        }
        return result;
    }
    
    /** Retrieve the weight of a given operand. Return 1 if the operand is not weighted.
     * If the operand is a variable and the attribute varisweight=true, then the weight is the value of the variable.
     * @param op The operand to get the weight from.
     * @param val A variable value if the weight is in fact the value of the variable (attribute
     * varisweight).
     * @return The weight of the operand.
     */    
    private double getWeight(Operand op, double val) {
        double result = 1.0;
        if ((op instanceof WVariable) && ((WVariable)op).getVarIsWeight()) {
            // The operand is a variable and its varisweight attribute is true.
            result = val;
        }
        else {
            // Retrieve the weight of the weighted operand.
            result = ((WeightObject)op).getWeight();
        }
        return result;
    }
    
	public int getLevel() {
		return Constants.FUNCTION_OPERATOR_LEVEL;
	}

	public String getOperatorAsString() {
		return Average.AVERAGE_OPERATION_STRING;
	}
	    
}
