/*
 * CriterionFactory.java
 *
 * Created on 28 mai 2003, 13:56
 */

package com.compuware.caqs.business.calculation.xmlimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.compuware.caqs.domain.calculation.quality.CostFormula;
import com.compuware.caqs.domain.calculation.quality.CriterionDef;
import com.compuware.caqs.domain.calculation.quality.Rule;
import com.compuware.caqs.domain.calculation.quality.RuleFactory;
import com.compuware.caqs.domain.calculation.quality.RuleSet;
import com.compuware.caqs.domain.calculation.quality.RuleXmlFactory;
import com.compuware.caqs.domain.calculation.quality.Stereotype;
import com.compuware.caqs.domain.calculation.quality.StereotypeFactory;
import com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation;
import com.compuware.caqs.domain.calculation.rules.aggregation.IAggregationFactory;

/**
 *
 * @author  cwfr-fdubois
 */
public class CriterionXmlFactory extends com.compuware.caqs.domain.calculation.quality.RuleSetXmlFactory {
    
    private String mId = null;
    private Stereotype mStereotypes = null;

    /** Creates a new instance of CriterionFactory */
    public CriterionXmlFactory(String id) {
        mId = id;
    }
    
    public RuleSet create(RuleFactory formulaFactory, RuleFactory costFormulaFactory, IAggregationFactory aggregFactory, StereotypeFactory stereotypeFactory) {
        return create(this.mNode, (RuleXmlFactory)formulaFactory, (RuleXmlFactory)costFormulaFactory, (AggregationXmlFactory)aggregFactory, (StereotypeXmlFactory)stereotypeFactory);
    }
    
    private RuleSet create(Node n, RuleXmlFactory formulaFactory, RuleXmlFactory costFormulaFactory, AggregationXmlFactory aggregationFactory, StereotypeXmlFactory stereotypeFactory) {
        RuleSet result = null;
        if (n != null) {
            Collection<Rule> formulas = new ArrayList<Rule>();
            Rule costFormula = null;
            Map<String, Aggregation> aggregations = new HashMap<String, Aggregation>();
            NodeList children = n.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                String nodeName = child.getNodeName();
                if ((child.getNodeType() == Node.ELEMENT_NODE) && (nodeName.equals("FORMULA"))) {
                    formulaFactory.setNode(child);
                    Rule f = formulaFactory.create();
                    if (f != null) {
                        formulas.add(f);
                    }
                }
                else if ((child.getNodeType() == Node.ELEMENT_NODE) && (nodeName.equals("COSTFORMULA"))) {
                    costFormulaFactory.setNode(child);
                    costFormula = costFormulaFactory.create();
                }
                else if ((child.getNodeType() == Node.ELEMENT_NODE) && (nodeName.equals("AGGREG"))) {
                    aggregationFactory.setNode(child);
                    Aggregation agreg = aggregationFactory.create();
                    if (agreg != null) {
                        aggregations.put(agreg.getTypeElt(), agreg);
                    }
                }
                else if ((child.getNodeType() == Node.ELEMENT_NODE) && (nodeName.equals("STEREOTYPE"))) {
                    stereotypeFactory.setNode(child);
                    mStereotypes = stereotypeFactory.create();
                }
            }
            result = new CriterionDef(mId, formulas, costFormula, aggregations, mStereotypes);
        }
        return result;
    }



}
