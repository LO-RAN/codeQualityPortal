/* Generated By:JJTree: Do not edit this line. ASTSwitchStatement.java */

package com.compuware.caqs.pmd.ast;

public class ASTSwitchStatement extends SimpleJavaNode {
    public ASTSwitchStatement(int id) {
        super(id);
    }

    public ASTSwitchStatement(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
