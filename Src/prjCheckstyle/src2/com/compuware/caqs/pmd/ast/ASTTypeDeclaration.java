/* Generated By:JJTree: Do not edit this line. ASTTypeDeclaration.java */

package com.compuware.caqs.pmd.ast;

import com.compuware.caqs.pmd.Rule;

public class ASTTypeDeclaration extends SimpleJavaNode implements CanSuppressWarnings {
    public ASTTypeDeclaration(int id) {
        super(id);
    }

    public ASTTypeDeclaration(JavaParser p, int id) {
        super(p, id);
    }


    public boolean hasSuppressWarningsAnnotationFor(Rule rule) {
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            if (jjtGetChild(i) instanceof ASTAnnotation) {
                ASTAnnotation a = (ASTAnnotation) jjtGetChild(i);
                if (a.suppresses(rule)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
