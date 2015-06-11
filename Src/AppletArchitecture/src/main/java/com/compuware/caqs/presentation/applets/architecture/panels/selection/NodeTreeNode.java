package com.compuware.caqs.presentation.applets.architecture.panels.selection;

import javax.swing.tree.DefaultMutableTreeNode;

import com.compuware.caqs.domain.architecture.serializeddata.Node;

public class NodeTreeNode extends DefaultMutableTreeNode {

	private Node elt;
	
	public NodeTreeNode(Node elt) {
		super(elt.getLbl());
		this.elt = elt;
	}
	
	public Node getElement(){
		return this.elt;
	}
}
