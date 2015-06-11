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
public abstract class RuleSetXmlFactory implements com.compuware.caqs.domain.calculation.quality.RuleSetFactory {
    
    protected org.w3c.dom.Node mNode = null;
    
    public void setNode(org.w3c.dom.Node n) {
        mNode = n;
    }
    
    public org.w3c.dom.Node getNode() {
        return mNode;
    }
    
}
