/* Generated By:JJTree: Do not edit this line. ASTStatementExpressionList.java */

package com.compuware.caqs.pmd.ast;

public class ASTStatementExpressionList extends SimpleJavaNode {
    public ASTStatementExpressionList(int id) {
        super(id);
    }

    public ASTStatementExpressionList(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}