/* Generated By:JJTree: Do not edit this line. ASTJspExpressionInAttribute.java */

package com.compuware.caqs.pmd.jsp.ast;

public class ASTJspExpressionInAttribute extends SimpleNode {
    public ASTJspExpressionInAttribute(int id) {
        super(id);
    }

    public ASTJspExpressionInAttribute(JspParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JspParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}