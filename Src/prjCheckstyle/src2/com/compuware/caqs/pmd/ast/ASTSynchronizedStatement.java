/* Generated By:JJTree: Do not edit this line. ASTSynchronizedStatement.java */

package com.compuware.caqs.pmd.ast;

public class ASTSynchronizedStatement extends SimpleJavaNode {
    public ASTSynchronizedStatement(int id) {
        super(id);
    }

    public ASTSynchronizedStatement(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
