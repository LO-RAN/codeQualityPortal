/* Generated By:JJTree: Do not edit this line. ASTCompilationUnit.java */

package com.compuware.carscode.jspparser;

import net.sourceforge.pmd.ast.CompilationUnit;

public class ASTCompilationUnit extends SimpleNode implements CompilationUnit {
    public ASTCompilationUnit(int id) {
        super(id);
    }

    public ASTCompilationUnit(JspParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JspParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
