/*
 * Aggregable.java
 *
 * Created on 4 décembre 2003, 17:57
 */

package com.compuware.caqs.domain.calculation.rules;

/**
 *
 * @author  cwfr-fdubois
 */
public interface Aggregable {
    
    public static final String AVG = "AVG";
    public static final String EXCLUS = "EXCLUS";    
    public static final String EXCLUS_AVG = "EXCLUS_AVG";
    public static final String AVG_ALL = "AVG_ALL";
    public static final String EXCLUS_AVG_SEUIL = "EXCLUS_AVG_SEUIL";
    public static final String MULTI_SEUIL = "MULTI_SEUIL";
    
    public static final double DEFAULT_EXCLUS = 4.0;
    public static final double VALUE_EXCLUS = 1.0;
    
    double getValue();
    
    double getWeight();
    
}
