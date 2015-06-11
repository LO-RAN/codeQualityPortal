/*
 * Aggregation.java
 *
 * Created on 4 décembre 2003, 18:14
 */

package com.compuware.caqs.domain.calculation.rules.aggregation;

import java.util.Collection;
import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.BooleanValue;
import com.compuware.caqs.domain.calculation.rules.NumericValue;
import com.compuware.caqs.domain.calculation.rules.Operand;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class Aggregation {
    
    private String mId = null;
    private String mIdTelt = null;
    protected Map<String, Operand> mParams = null;
    
    protected static final String EXCLUS = "VALUE_EXCLUS";

    /** Creates a new instance of Aggregation */
    public Aggregation() {
    }
    
    public String getId() {
        return this.mId;
    }
    
    public void setId(String id) {
        this.mId = id;
    }
    
    public String getTypeElt() {
        return this.mIdTelt;
    }
    
    public void setTypeElt(String id_telt) {
        this.mIdTelt = id_telt;
    }
    
    public Map<String, Operand> getParameters() {
        return this.mParams;
    }
    
    public void setParameters(Map<String, Operand> params) {
        this.mParams = params;
    }
    
    protected double getParameterValue(String key, double def) {
        double result = def;
        Operand op = mParams.get(key);
        if (op != null) {
            if (op instanceof NumericValue) {
                result = ((NumericValue)op).evalNum(null);
            }
            else if (op instanceof BooleanValue) {
                result = ((BooleanValue)op).evalNum(null);
            }
        }
        return result;
    }
        
    protected boolean getParameterValue(String key, boolean def) {
        boolean result = def;
        Operand op = mParams.get(key);
        if (op != null) {
            if (op instanceof NumericValue) {
                result = ((NumericValue)op).evalBool(null);
            }
            else if (op instanceof BooleanValue) {
                result = ((BooleanValue)op).evalBool(null);
            }
        }
        return result;
    }
        
    public abstract double aggregate(String key, Collection coll);
    
    public String toString() {
    	StringBuffer result = new StringBuffer();
    	result.append("<AGGREGATION");
    	result.append(" id=\"").append(mId).append("\"");
    	result.append(" typeElt=\"").append(mIdTelt).append("\"");
    	result.append(" />\n");
    	return result.toString();
    }
    
}
