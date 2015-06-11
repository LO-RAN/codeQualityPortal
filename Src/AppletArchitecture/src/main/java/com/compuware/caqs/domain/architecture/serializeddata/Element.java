/*
 * Element.java
 *
 * Created on 4 novembre 2002, 16:38
 */
package com.compuware.caqs.domain.architecture.serializeddata;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author  cwfr-fxalbouy
 */
public class Element implements Serializable {

    protected String lbl;
    protected String id;
    protected Element parentElement;
    protected Vector<Element> children = new Vector<Element>();
    /**
     * not needed to save models as the affected elements are accessed thru assigned node
     * this variable is used to compute real links
     **/
    protected Node assignedToNode = null;

    /** Creates a new instance of Element */
    public Element(String lbl, String id) {
        this.lbl = lbl;
        this.id = id;
    }

    public void addChild(Element element) {
        this.children.add(element);
    }

    public Vector<Element> getChildren() {
        return this.children;
    }

    public void addParent(Element parentElement) {
        this.parentElement = parentElement;
    }

    public Element getParent() {
        return this.parentElement;
    }

    public String getLabel() {
        return this.lbl;
    }

    public String getId() {
        return this.id;
    }

    public void setAssignedToNode(Node n) {
        this.assignedToNode = n;
    }

    public boolean isAssigned() {
        return (this.assignedToNode != null);
    }

    public Node getAssignedToNode() {
        return this.assignedToNode;
    }

    public Element getParentElement() {
        return this.parentElement;
    }
}
