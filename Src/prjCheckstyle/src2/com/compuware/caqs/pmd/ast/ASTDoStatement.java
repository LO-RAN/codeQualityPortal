/* Generated By:JJTree: Do not edit this line. ASTDoStatement.java */

package com.compuware.caqs.pmd.ast;

public class ASTDoStatement extends SimpleJavaNode {
    public ASTDoStatement(int id) {
        super(id);
    }

    public ASTDoStatement(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
