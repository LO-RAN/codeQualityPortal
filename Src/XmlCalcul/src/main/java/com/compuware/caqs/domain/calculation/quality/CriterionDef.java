/*
 * Criterion.java
 *
 * Created on 27 mai 2003, 17:32
 */

package com.compuware.caqs.domain.calculation.quality;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;

/** Represent a criterion definition.
 * @author cwfr-fdubois
 */
public class CriterionDef extends com.compuware.caqs.domain.calculation.quality.RuleSet {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_CALCUL_LOGGER_KEY);
	
    /** Creates a new instance of Criterion.
     * @param id the criterion id.
     * @param rules the criterion rule set.
     * @param aggregations the criterion agregation type.
     */
    public CriterionDef(String id, Collection<Rule> rules, Rule costRule, Map<String, Aggregation> aggregations, Stereotype stereotypes) {
        /* Criterion initialisation. */
        mId = id;
        mRules = rules;
        mCostRule = costRule;
        mAggregations = aggregations;
        mStereotypes = stereotypes;
    }
    
    /** Evaluate the criterion.
     * @param variables the variable map used for the evaluation.
     * @return the criterion evaluation.
     * @throws InstantiationException if a variable has no value in the variable map.
     */
    public double eval(Map<String, ValuedObject> variables, String stereotype) throws InstantiationException {
        double result = 0.0;
        // Si la liste des stereotypes est vide (applicable à tous) ou que le stereotype est présent dans la liste
        if (isStereotypeValid(stereotype)) {
            Iterator<Rule> i = mRules.iterator();
            /* Evaluate each rule until a not null value is returned. */
            while (result == 0.0 && i.hasNext()) {
                try {
                    Rule r = i.next();
                    result = r.eval(variables);
                }
                catch (InstantiationException e) {
                    // A variable is not initialized for the actual rule.
                    // Continue with the next one.
                	logger.debug(e.toString());
                }
            }
        }
        return result;
    }
    
    /** Evaluate the cost.
     * @param variables the variable map used for the evaluation.
     * @return the cost evaluation.
     * @throws InstantiationException if a variable has no value in the variable map.
     */
    public double evalCost(Map<String, ValuedObject> variables) throws InstantiationException {
        double result = 0.0;
        if (this.mCostRule != null) {
	        try {
	            result = this.mCostRule.eval(variables);
	        }
	        catch (InstantiationException e) {
	            // A variable is not initialized for the actual rule.
	            // Continue with the next one.
	        	logger.debug(e.toString());
	        }
        }
        return result;
    }
    
    /** Check if the criterion rules are valid.
     * @param errors the error collection to fill in.
     */    
    public void checkValidity(com.compuware.caqs.domain.calculation.rules.ValidityErrorCollection errors) {
        if(mRules != null) {
            Iterator<Rule> i = mRules.iterator();
            /* Check validity of each rule. */
            while (i.hasNext()) {
                Rule r = (Rule)i.next();
                /* Check and add errors to the collection if found. */
                r.checkValidity(errors);
            }
        }
    }
    
    /** Get the string representation of the criterion.
     * @return the string representation of the criterion.
     */    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        /* Print the criterion ID */
        buffer.append("<CRIT id="+mId+">\n");
        Iterator<Rule> i = this.mRules.iterator();
        /* Print each rule. */
        while (i.hasNext()) {
            Rule r = i.next();
            /* Append the rule string representation. */
            buffer.append(r);
        }
        buffer.append("</CRIT>\n");
        return buffer.toString();
    }
    
}
