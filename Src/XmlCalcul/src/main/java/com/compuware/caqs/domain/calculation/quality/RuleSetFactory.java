/*
 * FormulaFactory.java
 *
 * Created on 27 mai 2003, 17:42
 */

package com.compuware.caqs.domain.calculation.quality;

/**
 *
 * @author  cwfr-fdubois
 */
public interface RuleSetFactory {
    
    public com.compuware.caqs.domain.calculation.quality.RuleSet create(com.compuware.caqs.domain.calculation.quality.RuleFactory factory, com.compuware.caqs.domain.calculation.quality.RuleFactory costFactory, com.compuware.caqs.domain.calculation.rules.aggregation.IAggregationFactory aggregFactory, com.compuware.caqs.domain.calculation.quality.StereotypeFactory stereotypeFactory);
    
}
