/* Generated By:JJTree: Do not edit this line. ASTArgumentList.java */

package com.compuware.caqs.pmd.ast;

public class ASTArgumentList extends SimpleJavaNode {
    public ASTArgumentList(int id) {
        super(id);
    }

    public ASTArgumentList(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
