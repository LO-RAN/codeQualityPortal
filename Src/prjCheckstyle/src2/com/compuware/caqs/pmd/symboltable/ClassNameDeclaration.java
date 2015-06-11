package com.compuware.caqs.pmd.symboltable;

import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;

public class ClassNameDeclaration extends AbstractNameDeclaration {

    public ClassNameDeclaration(ASTClassOrInterfaceDeclaration node) {
        super(node);
    }

    public String toString() {
        return "Class " + node.getImage();
    }

}
