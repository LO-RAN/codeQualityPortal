/* Generated By:JJTree: Do not edit this line. ASTForUpdate.java */

package com.compuware.caqs.pmd.ast;

public class ASTForUpdate extends SimpleJavaNode {
    public ASTForUpdate(int id) {
        super(id);
    }

    public ASTForUpdate(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
