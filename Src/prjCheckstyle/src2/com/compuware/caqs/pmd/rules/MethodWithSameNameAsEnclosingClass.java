package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclarator;

import java.util.Iterator;
import java.util.List;

public class MethodWithSameNameAsEnclosingClass extends AbstractRule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        List methods = node.findChildrenOfType(ASTMethodDeclarator.class);
        for (Iterator i = methods.iterator(); i.hasNext();) {
            ASTMethodDeclarator m = (ASTMethodDeclarator) i.next();
            if (m.hasImageEqualTo(node.getImage())) {
                addViolation(data, m);
            }
        }
        return super.visit(node, data);
    }
}
