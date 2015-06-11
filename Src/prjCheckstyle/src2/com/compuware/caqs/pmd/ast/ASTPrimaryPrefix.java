/* Generated By:JJTree: Do not edit this line. ASTPrimaryPrefix.java */

package com.compuware.caqs.pmd.ast;

public class ASTPrimaryPrefix extends SimpleJavaNode {
    public ASTPrimaryPrefix(int id) {
        super(id);
    }

    public ASTPrimaryPrefix(JavaParser p, int id) {
        super(p, id);
    }

    private boolean usesThisModifier;
    private boolean usesSuperModifier;

    public void setUsesThisModifier() {
        usesThisModifier = true;
    }

    public boolean usesThisModifier() {
        return this.usesThisModifier;
    }

    public void setUsesSuperModifier() {
        usesSuperModifier = true;
    }

    public boolean usesSuperModifier() {
        return this.usesSuperModifier;
    }

    public String getLabel() {
        String out = getImage();
        if (usesSuperModifier) {
            return "super." + out;
        } else if (usesThisModifier) {
            if (getImage() == null) {
                return "this";
            } else {
                return "this." + out;
            }
        }
        return out;
    }

    public void dump(String prefix) {
        String out = getImage();
        if (usesSuperModifier) {
            out = "super." + out;
        } else if (usesThisModifier) {
            if (getImage() == null) {
                out = "this";
            } else {
                out = "this." + out;
            }
        }

        if (out == null) {
            System.out.println(toString(prefix));
        } else {
            System.out.println(toString(prefix) + ":" + out);
        }
        dumpChildren(prefix);
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
