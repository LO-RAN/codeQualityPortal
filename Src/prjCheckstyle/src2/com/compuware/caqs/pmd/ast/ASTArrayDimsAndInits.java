/* Generated By:JJTree: Do not edit this line. ASTArrayDimsAndInits.java */

package com.compuware.caqs.pmd.ast;

public class ASTArrayDimsAndInits extends SimpleJavaNode {
    public ASTArrayDimsAndInits(int id) {
        super(id);
    }

    public ASTArrayDimsAndInits(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
