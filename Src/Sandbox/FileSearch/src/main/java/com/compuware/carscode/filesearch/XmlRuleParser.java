package com.compuware.carscode.filesearch;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 9 févr. 2006
 * Time: 16:21:49
 * To change this template use File | Settings | File Templates.
 */
public class XmlRuleParser {

    public static final String RULE = "SEARCH";
    public static final String RULE_NAME = "name";
    public static final String RULE_FILE_FILTER = "filter";
    public static final String RULE_CONTENT = "RULE";
    public static final String RULE_CONTENT_REGEXP = "regexp";

    private List ruleSet = new ArrayList();

    public XmlRuleParser() {
    }

    /**
     * Initialize the the parser from an xml config file.
     * @param filePath the xml config file path.
     */
    public void init(String filePath) {
        Document xmlDocument = parseDefinition(filePath);
        if (xmlDocument != null) {
            NodeList ruleList = xmlDocument.getElementsByTagName(XmlRuleParser.RULE);
            for (int i = 0; i < ruleList.getLength(); i++) {
                SearchRule searchRule = new SearchRule();
                Node rule = ruleList.item(i);
                NamedNodeMap attributes = rule.getAttributes();
                String ruleName = (String) attributes.getNamedItem(XmlRuleParser.RULE_NAME).getNodeValue();
                searchRule.setName(ruleName);
                String fileFilter = (String) attributes.getNamedItem(XmlRuleParser.RULE_FILE_FILTER).getNodeValue();
                searchRule.setFileFilterRegexp(fileFilter);
                NodeList compList = rule.getChildNodes();
                parseRuleContent(compList, searchRule);
                ruleSet.add(searchRule);
            }
        }
    }

    /**
     * Parse the content of a search rule.
     * @param compList the content nodes.
     * @param searchRule the search rule to fill in.
     */
    private void parseRuleContent(NodeList compList, SearchRule searchRule) {
        if (compList != null && searchRule != null) {
            List regexpList = new ArrayList();
            for (int i = 0; i < compList.getLength(); i++) {
                Node comp = compList.item(i);
                if (comp.getNodeType() == Node.ELEMENT_NODE) {
                    if (comp.getNodeName().equals(RULE_CONTENT)) {
                        NamedNodeMap attributes = comp.getAttributes();
                        String regexp = (String) attributes.getNamedItem(RULE_CONTENT_REGEXP).getNodeValue();
                        regexpList.add(regexp);
                    }
                }
            }
            searchRule.setRegexpList(regexpList);
        }
    }

    /**
     * Parse the xml file to extract an Xml document.
     * @param file the xml file path.
     * @return the Xml document.
     */
    private Document parseDefinition(String file) {
        Document xmlDocument = null;
        DOMParser usageParser= new DOMParser();
        try {
            usageParser.parse(file);
            xmlDocument = usageParser.getDocument();
        }
        catch (SAXException e) {
            System.out.println(e.getMessage());
        }
        catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
        return xmlDocument;
    }

    /**
     * Get the search rule set.
     * @return the search rule set.
     */
    public List getRuleSet() {
        return ruleSet;
    }

}
