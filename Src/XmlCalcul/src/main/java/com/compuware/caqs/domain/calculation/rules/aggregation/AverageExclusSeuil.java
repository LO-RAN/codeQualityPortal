/*
 * AverageExclusSeuil.java
 *
 * Created on 28 février 2005, 11:04
 */

package com.compuware.caqs.domain.calculation.rules.aggregation;

import java.util.Collection;
import java.util.Iterator;

import com.compuware.caqs.domain.calculation.rules.Aggregable;
import com.compuware.caqs.domain.calculation.rules.AggregableMap;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;

/**
 *
 * @author fdubois
 */
public class AverageExclusSeuil extends com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation {
    
    public static final String SEUIL = "SEUIL";
    
    /** Creates a new instance of AverageExclusSeuil */
    public AverageExclusSeuil() {
    }
    
    public double aggregate(String key, Collection coll) {
        double seuil = getParameterValue(AverageExclusSeuil.SEUIL, 0.0) / Constants.HUNDRED;
        int nbExclus = 0;
        double result = 0.0;
        double value = 0.0;
        double weight = 0.0;
        double valueExclus = getParameterValue(EXCLUS, Aggregable.VALUE_EXCLUS);
        Iterator<AggregableMap> i = coll.iterator();

        while (i.hasNext()) {
            AggregableMap map = i.next();
            Aggregable aggreg = map.getAggregable(key);
            if (aggreg != null) {
	            if (aggreg.getValue() > 0.0) {
	                if (aggreg.getValue() == valueExclus) {
	                    nbExclus++;
	                }
	                value += aggreg.getValue()*aggreg.getWeight();
	                weight += aggreg.getWeight();
	            }
            }
        }

        int collSize = coll.size();
        if (nbExclus > (collSize*seuil) ) {
            value = valueExclus;
            weight = 1.0;
        }

        if (weight > 0) {
            result = value / weight;
        }
        return result;
    }
    
}
