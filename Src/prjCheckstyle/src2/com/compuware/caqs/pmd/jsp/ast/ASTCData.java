/* Generated By:JJTree: Do not edit this line. ASTCData.java */

package com.compuware.caqs.pmd.jsp.ast;

public class ASTCData extends SimpleNode {
    public ASTCData(int id) {
        super(id);
    }

    public ASTCData(JspParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JspParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
