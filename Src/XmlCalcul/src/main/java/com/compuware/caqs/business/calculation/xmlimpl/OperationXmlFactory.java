/*
 * OperationXmlFactory.java
 *
 * Created on 27 mai 2003, 10:52
 */

package com.compuware.caqs.business.calculation.xmlimpl;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.compuware.caqs.domain.calculation.rules.BooleanValue;
import com.compuware.caqs.domain.calculation.rules.NumericValue;
import com.compuware.caqs.domain.calculation.rules.Operand;
import com.compuware.caqs.domain.calculation.rules.OperandXmlFactory;
import com.compuware.caqs.domain.calculation.rules.Operation;
import com.compuware.caqs.domain.calculation.rules.StringValue;
import com.compuware.caqs.domain.calculation.rules.Variable;
import com.compuware.caqs.domain.calculation.rules.WVariable;
import com.compuware.caqs.domain.calculation.rules.WeightObject;

/**
 *
 * @author  cwfr-fdubois
 */
public class OperationXmlFactory extends OperandXmlFactory {
    
    public Operand create() {
        return create(this.mNode);
    }
    
    private Operand create(Node n) {
        Operand result = null;
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            String nodeName = n.getNodeName();
            NamedNodeMap attributes = n.getAttributes();
            if (nodeName.equals("OP")) {
                result = createOperation(attributes);
            }
            if (nodeName.equals("VAR")) {
                result = createVariable(attributes);
            }
            if (nodeName.equals("VAL")) {
                result = createValue(attributes);
            }
            create(result, n.getChildNodes());
        }
        return result;
    }

    private void create(Operand oper, NodeList children) {
        if (oper != null) {
            Collection<Operand> operands = new ArrayList<Operand>();
            if (children != null) {
                for (int i = 0; i < children.getLength(); i++) {
                    Operand child = create(children.item(i));
                    if (child != null)
                        operands.add(child);
                }
            }
            oper.init(operands);
        }
    }
    
    private Operand createOperation(NamedNodeMap attributes) {
        Operation result = null;
        String id = (String) attributes.getNamedItem("id").getNodeValue();
        String type = (String) attributes.getNamedItem("type").getNodeValue();
        Node n_default = attributes.getNamedItem("default");
        try {
            String className = mProps.getProperty(id);
            Class cls = Class.forName(className);
            result = (Operation)cls.newInstance();
            result.setId(id);
            result.setType(type);
            if (n_default != null) {
                String sDefault = (String) n_default.getNodeValue();
                result.setDefault(Double.valueOf(sDefault));
            }
            if (result instanceof WeightObject) {
                Node n_weight = attributes.getNamedItem("weight");
                if (n_weight != null) {
                    String weight = (String)n_weight.getNodeValue();
                    ((WeightObject)result).setWeight(Double.parseDouble(weight));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private Operand createVariable(NamedNodeMap attributes) {
        Operand result = null;
        String id = (String) attributes.getNamedItem("id").getNodeValue();
        String type = (String) attributes.getNamedItem("type").getNodeValue();
        Node n_weight = attributes.getNamedItem("weight");
        if (n_weight != null) {
            String weight = (String)n_weight.getNodeValue();
            result = new WVariable(id, type);
            ((WVariable)result).setWeight(Double.parseDouble(weight));
            Node n_varisweight = attributes.getNamedItem("varisweight");
            if (n_varisweight != null) {
                String varisweight = (String)n_varisweight.getNodeValue();
                ((WVariable)result).setVarIsWeight(varisweight.equalsIgnoreCase("true"));
            }
        }
        else
            result = new Variable(id, type);
        return result;
    }

    private Operand createValue(NamedNodeMap attributes) {
        Operand result = null;
        String strValue = (String) attributes.getNamedItem("value").getNodeValue();
        String type = (String) attributes.getNamedItem("type").getNodeValue();
        if (type.equalsIgnoreCase("NUMERIC")) {
            result = new NumericValue(strValue);
        }
        else if (type.equalsIgnoreCase("BOOLEAN")) {
            result = new BooleanValue(strValue);
        }
        else if (type.equalsIgnoreCase("STRING")) {
            result = new StringValue(strValue);
        }
        return result;
    }
    
}
