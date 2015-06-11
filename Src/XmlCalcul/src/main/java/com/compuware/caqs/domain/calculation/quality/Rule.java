/*
 * Rule.java
 *
 * Created on 27 mai 2003, 17:11
 */

package com.compuware.caqs.domain.calculation.quality;

import java.util.Map;

import com.compuware.caqs.domain.calculation.rules.ValuedObject;

/**
 *
 * @author  cwfr-fdubois
 */
public interface Rule {
    
    public double eval(Map<String, ValuedObject> variables) throws InstantiationException;
    
    public void checkValidity(com.compuware.caqs.domain.calculation.rules.ValidityErrorCollection errors);
    
}
