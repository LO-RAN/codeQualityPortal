/*
 * AggregationFactory.java
 *
 * Created on 24 février 2005, 10:56
 */

package com.compuware.caqs.domain.calculation.rules.aggregation;

/**
 *
 * @author  fdubois
 */
public interface IAggregationFactory {
    
    public com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation create();
    public com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation create(String key);
    
}
