/* Generated By:JJTree: Do not edit this line. ASTNormalAnnotation.java */

package com.compuware.caqs.pmd.ast;

public class ASTNormalAnnotation extends SimpleJavaNode {
    public ASTNormalAnnotation(int id) {
        super(id);
    }

    public ASTNormalAnnotation(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
