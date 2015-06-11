/*
 * MultiSeuil.java
 *
 * Created on 03 mars 2005, 09:30
 */

package com.compuware.caqs.domain.calculation.rules.aggregation;

import java.util.Collection;
import java.util.Iterator;

import com.compuware.caqs.domain.calculation.rules.Aggregable;
import com.compuware.caqs.domain.calculation.rules.AggregableMap;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;

/**
 *
 * @author lizac
 */
public class MultiSeuil extends com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation {
    
    public static final String SEUIL1 = "SEUIL1";
    public static final String SEUIL2 = "SEUIL2";
    public static final String SEUIL3 = "SEUIL3";

    private static final double SEUIL1_VAL = 1.0;
    private static final double SEUIL2_VAL = 2.0;
    private static final double SEUIL3_VAL = 3.0;
    private static final double SEUIL4_VAL = 4.0;
    
    /** Creates a new instance of MultiSeuil */
    public MultiSeuil() {
    }
    
    public double aggregate(String key, Collection coll) {
        double seuil1 = getParameterValue(SEUIL1, 0.0) / Constants.HUNDRED;
        double seuil2 = getParameterValue(SEUIL2, 0.0) / Constants.HUNDRED;
        double seuil3 = getParameterValue(SEUIL3, 0.0) / Constants.HUNDRED;
        int nbExclus = 0;
        double result = 0.0;
        double valueExclus = getParameterValue(EXCLUS, Aggregable.VALUE_EXCLUS);
        Iterator<AggregableMap> i = coll.iterator();
        
        while (i.hasNext()) {
            AggregableMap map = i.next();
            Aggregable aggreg = map.getAggregable(key);
            if (aggreg != null) {
                if (aggreg.getValue() == valueExclus) {
                    nbExclus++;
                }
            }
        }

        int collSize = coll.size();

        if (nbExclus > (collSize*seuil1)) {
        	result = SEUIL1_VAL;
        }
        else if (nbExclus > (collSize*seuil2)) {
        	result = SEUIL2_VAL;
        }
        else if (nbExclus > (collSize*seuil3)) {
        	result = SEUIL3_VAL;
        }
        else {
        	result = SEUIL4_VAL;
        }
        
        return result;
    }
    
}
