/*
 * AggregationXmlFactory.java
 *
 * Created on 24 février 2005, 10:57
 */

package com.compuware.caqs.business.calculation.xmlimpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.compuware.caqs.domain.calculation.rules.BooleanValue;
import com.compuware.caqs.domain.calculation.rules.NumericValue;
import com.compuware.caqs.domain.calculation.rules.Operand;
import com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation;
import com.compuware.caqs.domain.calculation.rules.aggregation.AggregationFactory;
import com.compuware.caqs.domain.calculation.rules.aggregation.IAggregationFactory;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;
import com.compuware.toolbox.io.PropertiesReader;

/**
 *
 * @author fdubois
 */
public class AggregationXmlFactory implements IAggregationFactory {
    
    protected org.w3c.dom.Node mNode = null;
    protected Properties mProps = null;
    
    public AggregationXmlFactory() {
        mProps = PropertiesReader.getProperties(Constants.AGGREG_CONFIG_FILE_PATH, this);
    }
    
    public void setNode(org.w3c.dom.Node n) {
        mNode = n;
    }
    
    public org.w3c.dom.Node getNode() {
        return mNode;
    }

    public Aggregation create(String id) {
        AggregationFactory aggregFactory = new AggregationFactory();
        return aggregFactory.create(id);
    }
    
    public Aggregation create() {
        return create(mNode);
    }
    
    private Map<String, Operand> getParameters(Node n) {
        Map<String, Operand> result = new HashMap<String, Operand>();
        NodeList children = n.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String nodeName = child.getNodeName();
            if ((child.getNodeType() == Node.ELEMENT_NODE) && (nodeName.equals("PARAM"))) {
                NamedNodeMap attributes = child.getAttributes();
                String id = (String) attributes.getNamedItem("id").getNodeValue();
                String type = (String) attributes.getNamedItem("type").getNodeValue();
                String value = (String) attributes.getNamedItem("value").getNodeValue();
                if (type.equalsIgnoreCase("NUMERIC")) {
                    result.put(id, new NumericValue(value));
                }
                else if (type.equalsIgnoreCase("BOOLEAN")) {
                    result.put(id, new BooleanValue(value));
                }
            }
        }
        return result;
    }
    
    public Aggregation create(org.w3c.dom.Node n) {
        Aggregation result = null;
        if (n != null) {
            NamedNodeMap attributes = n.getAttributes();
            String id = (String) attributes.getNamedItem("id").getNodeValue();
            String id_telt = (String) attributes.getNamedItem("telt").getNodeValue();
            try {
                AggregationFactory aggregFactory = new AggregationFactory();
                result = aggregFactory.create(id);
                result.setId(id);
                result.setTypeElt(id_telt);
                result.setParameters(getParameters(n));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
