/*
 * Pattern.java
 *
 * Created on 27 mai 2003, 17:07
 */

package com.compuware.caqs.domain.calculation.quality;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class RuleSet {

    public static final String STEREOTYPE_ALL = "ALL";

    Collection<Rule> mRules = null;
    Rule mCostRule = null;
    String mId = null;
    Map<String, Aggregation> mAggregations = null;
    Stereotype mStereotypes = null;

    public RuleSet() {
        this.mAggregations = new HashMap<String, Aggregation>();
    }
    
    public String getId() {
        return mId;
    }
    
    public abstract double eval(Map<String, ValuedObject> variables, String stereotype) throws InstantiationException;
    public abstract double evalCost(Map<String, ValuedObject> variables) throws InstantiationException;
    
    public abstract void checkValidity(com.compuware.caqs.domain.calculation.rules.ValidityErrorCollection errors);
    
    public Map<String, Aggregation> getAggregations() {
        return mAggregations;
    }
    
    public Aggregation getAggregation(String key) {
        return mAggregations.get(key);
    }
    
    public Stereotype getStereotypes() {
        return mStereotypes;
    }
    
    public Collection<Rule> getRules() {
    	return this.mRules;
    }

    public Rule getCostRule() {
    	return this.mCostRule;
    }

    /**
     * V�rifie si le st�r�otype pass� en param�tre est valide pour l'ensemble de r�gles.
     * La liste des �tats valides est:
     *  1- La liste des st�r�otypes de l'ensemble de r�gles est vide.
     *  2- Le st�r�otype donn� vaut la constante RuleSet.STEREOTYPE_ALL.
     *  3- La liste des st�r�otypes contient le st�r�otype donn�.
     * @param stereotype le st�r�otype donn�.
     * @return true si le st�r�otype donn� est valide, false sinon.
     */
    public boolean isStereotypeValid(String stereotype) {
        boolean result = false;
        if (mStereotypes == null || mStereotypes.isEmpty()) {
            // La liste des st�r�otypes est vide => OK.
            result = true;
        }
        else if (stereotype != null && stereotype.equals(RuleSet.STEREOTYPE_ALL)) {
                // Le st�r�otype vaut STEREOTYPE_ALL => OK.
                result = true;
        }
        else if (mStereotypes.allow(stereotype)) {
            // La liste des st�r�otypes contient le st�r�otype => OK.
            result = true;
        }
        return result;
    }
}
