/*
 * AverageAll.java
 *
 * Created on 25 février 2005, 10:26
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
public class AverageAll extends com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation {
    
    /** Creates a new instance of AverageAll */
    public AverageAll() {
    }
    
    public double aggregate(String key, Collection coll) {
        double result = 0.0;
        double value = 0.0;
        boolean valueExist = false;
        double weight = 0.0;
        Iterator<AggregableMap> i = coll.iterator();
        while (i.hasNext()) {
            AggregableMap map = i.next();
            Aggregable aggreg = map.getAggregable(key);
            if (aggreg != null) {
                if (aggreg.getValue() > 0) {
                    value += aggreg.getValue()*aggreg.getWeight();
                    valueExist = true;
                }
                else {
                    value += Aggregable.VALUE_EXCLUS*aggreg.getWeight();
                }
                weight += aggreg.getWeight();
            }
        }
        if (weight > 0 && valueExist) {
                result = value / weight;
        }
        return result;
    }
    
}
