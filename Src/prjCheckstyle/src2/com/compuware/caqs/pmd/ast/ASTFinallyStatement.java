/* Generated By:JJTree: Do not edit this line. ASTFinallyStatement.java */

package com.compuware.caqs.pmd.ast;

public class ASTFinallyStatement extends SimpleJavaNode {
    public ASTFinallyStatement(int id) {
        super(id);
    }

    public ASTFinallyStatement(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
