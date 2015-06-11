/*
 * Average.java
 *
 * Created on 24 février 2005, 13:02
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
public class Average extends com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation {
    
    /** Creates a new instance of Average */
    public Average() {
    }
    
    public double aggregate(String key, Collection coll) {
        double result = 0.0;
        double value = 0.0;
        double weight = 0.0;
        Iterator<AggregableMap> i = coll.iterator();
        while (i.hasNext()) {
            AggregableMap map = i.next();
            Aggregable aggreg = map.getAggregable(key);
            if (aggreg != null) {
                if (aggreg.getValue() > 0.0) {
                    value += aggreg.getValue() * aggreg.getWeight() * map.getVolumetry(key);
                    weight += aggreg.getWeight() * map.getVolumetry(key);
                }
            }
        }
        if (weight > 0) {
            result = value / weight;
        }
        return result;
    }
    
}
