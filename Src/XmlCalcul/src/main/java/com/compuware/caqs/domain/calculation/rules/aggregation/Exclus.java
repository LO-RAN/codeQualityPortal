/*
 * Exclus.java
 *
 * Created on 24 février 2005, 13:10
 */

package com.compuware.caqs.domain.calculation.rules.aggregation;

import java.util.Collection;
import java.util.Iterator;

import com.compuware.caqs.domain.calculation.rules.Aggregable;
import com.compuware.caqs.domain.calculation.rules.AggregableMap;

/**
 *
 * @author  fdubois
 */
public class Exclus extends com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation {
    
    public static final String DEFAULT = "DEFAULT";

    /** Creates a new instance of Exclus */
    public Exclus() {
    }
    
    public double aggregate(String key, Collection coll) {
        double result = getParameterValue(DEFAULT, Aggregable.DEFAULT_EXCLUS);
        double valueExclus = getParameterValue(EXCLUS, Aggregable.VALUE_EXCLUS);
        Iterator<AggregableMap> i = coll.iterator();
        
        while (i.hasNext() && result != valueExclus) {
            AggregableMap map = i.next();
            Aggregable aggreg = map.getAggregable(key);
            
            if (aggreg != null) {
                if (aggreg.getValue() == valueExclus) {
                    result = valueExclus;
                    break;
                }
            }
        }
        return result;
    }
    
}
