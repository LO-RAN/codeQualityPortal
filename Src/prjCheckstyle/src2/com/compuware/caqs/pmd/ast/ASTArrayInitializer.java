/* Generated By:JJTree: Do not edit this line. ASTArrayInitializer.java */

package com.compuware.caqs.pmd.ast;

public class ASTArrayInitializer extends SimpleJavaNode {
    public ASTArrayInitializer(int id) {
        super(id);
    }

    public ASTArrayInitializer(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
