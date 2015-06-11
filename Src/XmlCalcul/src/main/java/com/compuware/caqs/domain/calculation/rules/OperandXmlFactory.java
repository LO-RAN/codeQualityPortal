/*
 * FormulaFactory.java
 *
 * Created on 27 mai 2003, 17:42
 */

package com.compuware.caqs.domain.calculation.rules;

import java.util.Properties;

import com.compuware.caqs.domain.calculation.rules.constants.Constants;
import com.compuware.toolbox.io.PropertiesReader;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class OperandXmlFactory implements com.compuware.caqs.domain.calculation.rules.OperandFactory {
    
    protected org.w3c.dom.Node mNode = null;
    protected Properties mProps = null;
    
    public OperandXmlFactory() {
        mProps = PropertiesReader.getProperties(Constants.OPERATOR_CONFIG_FILE_PATH, this);
    }
    
    public void setNode(org.w3c.dom.Node n) {
        mNode = n;
    }
    
    public org.w3c.dom.Node getNode() {
        return mNode;
    }
    
}
