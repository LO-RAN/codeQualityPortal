/* Generated By:JJTree: Do not edit this line. ASTInitializer.java */

package com.compuware.caqs.pmd.ast;

public class ASTInitializer extends SimpleJavaNode {
    public ASTInitializer(int id) {
        super(id);
    }

    public ASTInitializer(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    private boolean isStatic;

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic() {
        isStatic = true;
    }

    public void dump(String prefix) {
        System.out.println(toString(prefix) + ":(" + (isStatic ? "static" : "nonstatic") + ")");
        dumpChildren(prefix);
    }

}
