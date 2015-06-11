/*
 * Aggregable.java
 *
 * Created on 4 d�cembre 2003, 17:57
 */

package com.compuware.caqs.domain.calculation.rules;

/**
 *
 * @author  cwfr-fdubois
 */
public interface AggregableMap {
    
    public Aggregable getAggregable(String key);
    
    public int getVolumetry(String type);
    
}
