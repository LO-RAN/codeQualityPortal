/*
 * FormulaFactory.java
 *
 * Created on 28 mai 2003, 13:58
 */

package com.compuware.caqs.business.calculation.xmlimpl;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.compuware.caqs.domain.calculation.quality.CostFormula;
import com.compuware.caqs.domain.calculation.quality.Rule;
import com.compuware.caqs.domain.calculation.rules.Operand;
import com.compuware.caqs.domain.calculation.rules.OperandXmlFactory;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;

/**
 *
 * @author  cwfr-fdubois
 */
public class IFPUGXmlFactory extends com.compuware.caqs.domain.calculation.quality.RuleXmlFactory {
    
    /** Creates a new instance of FormulaFactory */
    public IFPUGXmlFactory() {
    }
    
    public Rule create() {
        return create(this.mNode, (OperandXmlFactory)this.getOperandFactory());
    }
    
    private Rule create(Node n, OperandXmlFactory operandFactory) {
        Rule result = null;
        String nodeName = n.getNodeName();
        if ((n.getNodeType() == Node.ELEMENT_NODE) && (nodeName.equals(Constants.IFPUG_NODE_NAME))) {
            NodeList children = n.getChildNodes();
            operandFactory.setNode(children.item(1));
            Operand oper = operandFactory.create();
            if (oper != null) {
                result = new CostFormula(oper);
            }
        }
        return result;
    }
    
}
