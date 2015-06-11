package com.compuware.caqs.presentation.applets.architecture.panels.selection;

import javax.swing.tree.DefaultMutableTreeNode;

import com.compuware.caqs.domain.architecture.serializeddata.Element;

public class ElementTreeNode extends DefaultMutableTreeNode {

    private Element elt;

    public ElementTreeNode(Element elt) {
        super(elt.getLabel());
        this.elt = elt;
    }

    public Element getElement() {
        return this.elt;
    }
}
