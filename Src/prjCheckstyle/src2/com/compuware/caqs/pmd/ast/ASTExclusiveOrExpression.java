/* Generated By:JJTree: Do not edit this line. ASTExclusiveOrExpression.java */

package com.compuware.caqs.pmd.ast;

public class ASTExclusiveOrExpression extends SimpleJavaNode {
    public ASTExclusiveOrExpression(int id) {
        super(id);
    }

    public ASTExclusiveOrExpression(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
