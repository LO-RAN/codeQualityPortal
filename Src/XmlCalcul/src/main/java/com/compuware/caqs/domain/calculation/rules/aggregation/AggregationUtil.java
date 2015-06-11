/**
 * 
 */
package com.compuware.caqs.domain.calculation.rules.aggregation;

import java.util.Collection;

/**
 * @author cwfr-fdubois
 *
 */
public class AggregationUtil {

    public static double aggregate(String key, Collection coll, String aggregType) {
        double result = 0.0;
        Aggregation aggreg = null;
        AggregationFactory aggregFactory = new AggregationFactory();
        aggreg = aggregFactory.create(aggregType);
        if (aggreg != null) {
            result = aggreg.aggregate(key, coll);
        }
        return result;
    }
	
}
