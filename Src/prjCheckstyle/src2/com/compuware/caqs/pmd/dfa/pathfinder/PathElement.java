package com.compuware.caqs.pmd.dfa.pathfinder;

import com.compuware.caqs.pmd.dfa.IDataFlowNode;

public class PathElement {

    public int currentChild;
    public IDataFlowNode node;
    public IDataFlowNode pseudoRef;

    PathElement(IDataFlowNode node) {
        this.node = node;
    }

    PathElement(IDataFlowNode node, IDataFlowNode ref) {
        this.node = node;
        this.pseudoRef = ref;
    }

    public boolean isPseudoPathElement() {
        return pseudoRef != null;
    }
}

