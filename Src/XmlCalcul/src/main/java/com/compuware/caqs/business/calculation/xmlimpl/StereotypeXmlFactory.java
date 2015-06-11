/*
 * AggregationXmlFactory.java
 *
 * Created on 24 février 2005, 10:57
 */

package com.compuware.caqs.business.calculation.xmlimpl;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.compuware.caqs.domain.calculation.quality.Stereotype;
import com.compuware.toolbox.util.StringUtils;

/**
 * Crée un stéréotype à partir d'un flux XML.
 * Exemple:
 * <STEREOTYPE>
 *    <include list="TEST2,TEST3" />
 *    <exclude list="TEST1" />
 * </STEREOTYPE>
 * @author fdubois
 */
public class StereotypeXmlFactory implements com.compuware.caqs.domain.calculation.quality.StereotypeFactory {
    
    private static final String STEREOTYPE_SEPARATOR = ",";

    protected Node mNode = null;

    public StereotypeXmlFactory() {
    }
    
    public void setNode(Node n) {
        mNode = n;
    }
    
    public Node getNode() {
        return mNode;
    }
        
    public Stereotype create() {
        return create(mNode);
    }
    
    private void setParameters(Node n, Stereotype s) {
        NodeList children = n.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String nodeName = child.getNodeName();
            if ((child.getNodeType() == Node.ELEMENT_NODE) && (nodeName.equals("include"))) {
                NamedNodeMap attributes = child.getAttributes();
                String list = (String) attributes.getNamedItem("list").getNodeValue();
                s.addIncludedStereotypes(StringUtils.getCollectionFromString(list, STEREOTYPE_SEPARATOR));
            }
            if ((child.getNodeType() == Node.ELEMENT_NODE) && (nodeName.equals("exclude"))) {
                NamedNodeMap attributes = child.getAttributes();
                String list = (String) attributes.getNamedItem("list").getNodeValue();
                s.addExcludedStereotypes(StringUtils.getCollectionFromString(list, STEREOTYPE_SEPARATOR));
            }
        }
    }
    
    public Stereotype create(Node n) {
        Stereotype result = null;
        if (n != null) {
            try {
                result = new Stereotype();
                setParameters(n, result);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
