package com.compuware.caqs.domain.architecture.serializeddata;

public abstract class NodeAffectable extends Node {

    public NodeAffectable() {
        super();
    }

    public NodeAffectable(String label) {
        super(label);
    }

    public NodeAffectable(String label, double x, double y, int z, int width, int height) {
        super(label, x, y, z, width, height);
    }
}
