/* Generated By:JJTree: Do not edit this line. ASTCommentTag.java */

package com.compuware.caqs.pmd.jsp.ast;

public class ASTCommentTag extends SimpleNode {
    public ASTCommentTag(int id) {
        super(id);
    }

    public ASTCommentTag(JspParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JspParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
