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
public class AverageWeightByNote extends Aggregation {
    
    /** Creates a new instance of AverageExclus */
    public AverageWeightByNote() {
    }
    
    public double aggregate(String key, Collection coll) {
        double result = 0.0;
        double value = 0.0;
        double weight = 0.0;
        Iterator<AggregableMap> i = coll.iterator();
        int collSize = coll.size();
        while (i.hasNext()) {
            AggregableMap map = i.next();
            Aggregable aggreg = map.getAggregable(key);
            if (aggreg != null) {
	            if (aggreg.getValue() > 0.0) {
	                double weightTmp = aggreg.getWeight() * (collSize/Math.abs(aggreg.getValue()));
	                value += aggreg.getValue() * weightTmp;
	                weight += weightTmp;
	            }
            }
        }
        if (weight > 0) {
            result = value / weight;
        }
        return result;
    }
    
}
