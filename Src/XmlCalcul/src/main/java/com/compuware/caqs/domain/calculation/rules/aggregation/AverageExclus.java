/*
 * AverageExclus.java
 *
 * Created on 25 février 2005, 11:15
 */

package com.compuware.caqs.domain.calculation.rules.aggregation;

import java.util.Collection;
import java.util.Iterator;

import com.compuware.caqs.domain.calculation.rules.Aggregable;
import com.compuware.caqs.domain.calculation.rules.AggregableMap;

/**
 *
 * @author fdubois
 */
public class AverageExclus extends com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation {
    
    /** Creates a new instance of AverageExclus */
    public AverageExclus() {
    }
    
    public double aggregate(String key, Collection coll) {
        double result = 0.0;
        double value = 0.0;
        double weight = 0.0;
        double valueExclus = getParameterValue(Exclus.EXCLUS, Aggregable.VALUE_EXCLUS);
        Iterator<AggregableMap> i = coll.iterator();
        while (i.hasNext()) {
            AggregableMap map = i.next();
            Aggregable aggreg = map.getAggregable(key);
            if (aggreg != null) {
	            if (aggreg.getValue() > 0.0) {
	                if (aggreg.getValue() != valueExclus) {
	                    value += aggreg.getValue()*aggreg.getWeight();
	                    weight += aggreg.getWeight();
	                }
	                else {
	                    value = valueExclus;
	                    weight = 1.0;
	                    break;
	                }
	            }
            }
        }
        if (weight > 0) {
            result = value / weight;
        }
        return result;
    }
    
}
