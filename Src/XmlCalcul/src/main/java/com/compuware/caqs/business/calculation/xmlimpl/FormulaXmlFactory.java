/*
 * FormulaFactory.java
 *
 * Created on 28 mai 2003, 13:58
 */

package com.compuware.caqs.business.calculation.xmlimpl;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.compuware.caqs.domain.calculation.rules.Operand;
import com.compuware.caqs.domain.calculation.rules.OperandXmlFactory;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;
import com.compuware.caqs.domain.calculation.quality.Formula;
import com.compuware.caqs.domain.calculation.quality.Rule;

/**
 *
 * @author  cwfr-fdubois
 */
public class FormulaXmlFactory extends com.compuware.caqs.domain.calculation.quality.RuleXmlFactory {
    
    /** Creates a new instance of FormulaFactory */
    public FormulaXmlFactory() {
    }
    
    public Rule create() {
        return create(this.mNode, (OperandXmlFactory)this.getOperandFactory());
    }
    
    private Rule create(Node n, OperandXmlFactory operandFactory) {
        Rule result = null;
        String nodeName = n.getNodeName();
        if ((n.getNodeType() == Node.ELEMENT_NODE) && (nodeName.equals(Constants.FORMULA_NODE_NAME))) {
            NamedNodeMap attributes = n.getAttributes();
            NodeList children = n.getChildNodes();
            operandFactory.setNode(children.item(1));
            Operand oper = operandFactory.create();
            if (oper != null) {
                String verifiedValue = (String) attributes.getNamedItem("value").getNodeValue();
                result = new Formula(Double.parseDouble(verifiedValue), oper);
            }
        }
        return result;
    }
    
}
