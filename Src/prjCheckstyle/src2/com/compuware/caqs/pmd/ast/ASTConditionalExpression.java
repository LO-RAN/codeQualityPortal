/* Generated By:JJTree: Do not edit this line. ASTConditionalExpression.java */

package com.compuware.caqs.pmd.ast;

public class ASTConditionalExpression extends SimpleJavaNode {
    public ASTConditionalExpression(int id) {
        super(id);
    }

    public ASTConditionalExpression(JavaParser p, int id) {
        super(p, id);
    }

    private boolean isTernary;

    public void setTernary() {
        isTernary = true;
    }

    public boolean isTernary() {
        return this.isTernary;
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
