/* Generated By:JJTree: Do not edit this line. ASTTryStatement.java */

package com.compuware.caqs.pmd.ast;


public class ASTTryStatement extends SimpleJavaNode {

    public ASTTryStatement(int id) {
        super(id);
    }

    public ASTTryStatement(JavaParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public boolean hasFinally() {
        for (int i = 0; i < this.jjtGetNumChildren(); i++) {
            if (jjtGetChild(i) instanceof ASTFinallyStatement) {
                return true;
            }
        }
        return false;
    }

    public ASTFinallyStatement getFinally() {
        for (int i = 0; i < this.jjtGetNumChildren(); i++) {
            if (jjtGetChild(i) instanceof ASTFinallyStatement) {
                return (ASTFinallyStatement) jjtGetChild(i);
            }
        }
        throw new RuntimeException("ASTTryStatement.getFinally called but this try stmt doesn't contain a finally block");
    }

}
