/* Generated By:JJTree: Do not edit this line. ASTUnaryExpression.java */

package com.compuware.caqs.pmd.ast;

public class ASTUnaryExpression extends SimpleJavaNode {
    public ASTUnaryExpression(int id) {
        super(id);
    }

    public ASTUnaryExpression(JavaParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

}
