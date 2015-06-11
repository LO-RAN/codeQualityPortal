/*
 * XmlParser.java
 *
 * Created on 20 avril 2003, 18:53
 */
package com.compuware.caqs.business.calculation.xmlimpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.NamingException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.compuware.caqs.domain.calculation.quality.Rule;
import com.compuware.caqs.domain.calculation.quality.RuleSet;
import com.compuware.caqs.domain.calculation.rules.ValidityErrorCollection;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation;
import com.compuware.caqs.domain.calculation.rules.aggregation.Average;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 *
 * @author  cwfr-fdubois
 */
public class UsageCalculator {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_CALCUL_LOGGER_KEY);
    private static final String CONFIG_DIR_KEY = "conf.dir";
    private Map<String, Map<String, RuleSet>> mUsages = new HashMap<String, Map<String, RuleSet>>();
    private Map<String, String> mUsagesIFPUGElementType = new HashMap<String, String>();
    private Map<String, Rule> mUsagesIFPUGRule = new HashMap<String, Rule>();
    private String mConfigDir = null;

    /** Creates a new instance of XmlParser */
    public UsageCalculator() {
        try {
            mConfigDir = System.getProperty(CONFIG_DIR_KEY);
            if (mConfigDir == null) {
                javax.naming.Context ctx = new javax.naming.InitialContext();
                mConfigDir = (String) ctx.lookup("java:comp/env/" +
                        CONFIG_DIR_KEY);
            }
        } catch (NamingException e) {
            logger.error(e.getMessage());
            mConfigDir = "/conf/";
        }
        if (!mConfigDir.endsWith("/") && !mConfigDir.endsWith("\\")) {
            mConfigDir += "/";
        }
    }

    private Document parseDefinition(String file) {
        Document xmlDocument = null;
        DOMParser usageParser = new DOMParser();
        try {
            usageParser.parse(mConfigDir + file);
            xmlDocument = usageParser.getDocument();
        } catch (SAXException e) {
            logger.error(e.getMessage());
        } catch (java.io.IOException e) {
            logger.error(e.getMessage());
        }
        return xmlDocument;
    }

    public void init(String file) {
        Document xmlDocument = parseDefinition(file);
        if (xmlDocument != null) {
            NodeList usageList = xmlDocument.getElementsByTagName("USAGE");

            for (int i = 0; i < usageList.getLength(); i++) {
                Node usage = usageList.item(i);
                initUsage(usage);
            }
        }
    }

    private void initUsage(Node usage) {
        NamedNodeMap attributes = usage.getAttributes();
        String idUsage = (String) attributes.getNamedItem("id").getNodeValue();
        NodeList compList = usage.getChildNodes();
        Map<String, RuleSet> components = initUsageComponent(compList);
        if (!components.isEmpty()) {
            this.mUsages.put(idUsage, components);
        }
        this.initUsageIFPUGComponent(idUsage, compList);
    }

    private void initUsageIFPUGComponent(String idUsa, NodeList compList) {
        if (compList != null) {
            for (int i = 0; i < compList.getLength(); i++) {
                Node comp = compList.item(i);
                if (comp.getNodeType() == Node.ELEMENT_NODE) {
                    if (comp.getNodeName().equals("IFPUG")) {
                        IFPUGXmlFactory ifpugFactory = new IFPUGXmlFactory();
                        ifpugFactory.setOperandFactory(new OperationXmlFactory());
                        ifpugFactory.setNode(comp);
                        Rule f = ifpugFactory.create();
                        NamedNodeMap attributes = comp.getAttributes();
                        String idTElt = attributes.getNamedItem("telt").getNodeValue();
                        this.mUsagesIFPUGElementType.put(idUsa, idTElt);
                        this.mUsagesIFPUGRule.put(idUsa, f);
                    }
                }
            }
        }
    }

    private Map<String, RuleSet> initUsageComponent(NodeList compList) {
        Map<String, RuleSet> result = new HashMap<String, RuleSet>();
        if (compList != null) {
            for (int i = 0; i < compList.getLength(); i++) {
                Node comp = compList.item(i);
                if (comp.getNodeType() == Node.ELEMENT_NODE) {
                    if (comp.getNodeName().equals("CRITDEF")) {
                        NamedNodeMap attributes = comp.getAttributes();
                        String idCrit = (String) attributes.getNamedItem("id").getNodeValue();
                        CriterionXmlFactory critFactory = new CriterionXmlFactory(idCrit);
                        critFactory.setNode(comp);
                        FormulaXmlFactory formulaFactory = new FormulaXmlFactory();
                        formulaFactory.setOperandFactory(new OperationXmlFactory());
                        CostFormulaXmlFactory costFormulaFactory = new CostFormulaXmlFactory();
                        costFormulaFactory.setOperandFactory(new OperationXmlFactory());
                        AggregationXmlFactory aggregationFactory = new AggregationXmlFactory();
                        StereotypeXmlFactory stereotypeFactory = new StereotypeXmlFactory();
                        RuleSet rules = critFactory.create(formulaFactory, costFormulaFactory, aggregationFactory, stereotypeFactory);
                        if (rules != null) {
                            result.put(idCrit, rules);
                        }
                    }
                }
            }
        }
        return result;
    }

    public ValidityErrorCollection checkValidity(String usageKey, Properties prop) {
        TxtValidityErrorCollection errors = new TxtValidityErrorCollection(prop);
        Map<String, RuleSet> usage = mUsages.get(usageKey);
        Collection<RuleSet> criterions = usage.values();
        Iterator<RuleSet> i = criterions.iterator();
        while (i.hasNext()) {
            RuleSet crit = i.next();
            crit.checkValidity(errors);
        }
        return errors;
    }

    public double evalIFPUG(String usageKey, Map<String, ValuedObject> var) {
        double result = 0.0;
        Rule rule = this.mUsagesIFPUGRule.get(usageKey);
        if (rule != null) {
            try {
                result = rule.eval(var);
            } catch (InstantiationException e) {
                logger.error("IFPUG " + usageKey + ": " + e.getMessage());
            }
        }
        return result;
    }

    public double eval(String usageKey, String idCrit, Map<String, ValuedObject> var, String stereotype) {
        double result = 0;
        Map<String, RuleSet> usage = mUsages.get(usageKey);
        RuleSet crit = usage.get(idCrit);
        try {
            if (crit != null) {
                result = crit.eval(var, stereotype);
            }
        } catch (InstantiationException e) {
            logger.error("CRIT " + crit.getId() + ": " + e.getMessage());
        }
        return result;
    }

    public double evalCost(String usageKey, String idCrit, Map<String, ValuedObject> var) {
        double result = 0;
        Map<String, RuleSet> usage = mUsages.get(usageKey);
        RuleSet crit = usage.get(idCrit);
        try {
            if (crit != null) {
                result = crit.evalCost(var);
            }
        } catch (InstantiationException e) {
            logger.error("CRIT " + crit.getId() + ": " + e.getMessage());
        }
        return result;
    }

    public Aggregation getAggregation(String usageKey, String idCrit, String idTelt) {
        Aggregation result = null;
        Map<String, RuleSet> usage = mUsages.get(usageKey);
        RuleSet crit = (RuleSet) usage.get(idCrit);
        if (crit != null) {
            result = crit.getAggregation(idTelt);
        }
        if (crit != null && result == null) {
            result = crit.getAggregation("ALL");
        }
        if (result == null) {
            result = new Average();
        }
        return result;
    }

    public String toString(String usageKey) {
        StringBuffer buffer = new StringBuffer();
        Map<String, RuleSet> usage = mUsages.get(usageKey);
        Collection<RuleSet> criterions = usage.values();
        Iterator<RuleSet> i = criterions.iterator();
        while (i.hasNext()) {
            RuleSet crit = i.next();
            buffer.append(crit + "\n");
        }
        return buffer.toString();
    }

    public String getCriterionDefintionAsString(String usageKey, String idCrit) {
        StringBuffer result = new StringBuffer();
        Map<String, RuleSet> usage = mUsages.get(usageKey);
        RuleSet crit = (RuleSet) usage.get(idCrit);
        if (crit != null) {
            Map<String, Aggregation> aggregations = crit.getAggregations();
            Set<String> aggregKeys = aggregations.keySet();
            Iterator<String> i = aggregKeys.iterator();
            while (i.hasNext()) {
                String aggregKey = i.next();
                Aggregation aggreg = aggregations.get(aggregKey);
                result.append(aggreg.toString());
            }
            Collection<Rule> rules = crit.getRules();
            Iterator<Rule> ruleIter = rules.iterator();
            while (ruleIter.hasNext()) {
                Rule r = ruleIter.next();
                result.append(r.toString());
            }
            Rule cost = crit.getCostRule();
            if(cost!=null) {
                result.append(cost.toString());
            }
        }
        return result.toString();
    }

    public String getUsageIFPUGElementType(String idUsa) {
        return this.mUsagesIFPUGElementType.get(idUsa);
    }
}
