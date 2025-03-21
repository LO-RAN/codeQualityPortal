/* Generated By:JJTree: Do not edit this line. ASTLiteral.java */

package com.compuware.caqs.pmd.ast;

public class ASTLiteral extends SimpleJavaNode {
    public ASTLiteral(int id) {
        super(id);
    }

    public ASTLiteral(JavaParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public boolean isStringLiteral() {
        return getImage() != null && getImage().startsWith("\"") && getImage().endsWith("\"");
    }
}
