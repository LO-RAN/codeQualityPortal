/* Generated By:JJTree: Do not edit this line. ASTReferenceType.java */

package com.compuware.caqs.pmd.ast;

public class ASTReferenceType extends SimpleJavaNode implements Dimensionable {
    public ASTReferenceType(int id) {
        super(id);
    }

    public ASTReferenceType(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    private int arrayDepth;

    public void bumpArrayDepth() {
        arrayDepth++;
    }

    public int getArrayDepth() {
        return arrayDepth;
    }

    public boolean isArray() {
        return arrayDepth > 0;
    }

}
