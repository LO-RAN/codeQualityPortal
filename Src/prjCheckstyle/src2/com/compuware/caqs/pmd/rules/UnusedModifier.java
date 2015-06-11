package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTFieldDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.Node;
import com.compuware.caqs.pmd.ast.SimpleNode;

public class UnusedModifier extends AbstractRule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (!node.isInterface() && node.isNested() && (node.isPublic() || node.isStatic())) {
            ASTClassOrInterfaceDeclaration parent = (ASTClassOrInterfaceDeclaration) node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
            if (parent != null && parent.isInterface()) {
                addViolation(data, node, getMessage());
            }
        } else if (node.isInterface() && node.isNested() && (node.isPublic() || node.isStatic())) {
            ASTClassOrInterfaceDeclaration parent = (ASTClassOrInterfaceDeclaration) node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
            if (parent.isInterface() || (!parent.isInterface() && node.isStatic())) {
                addViolation(data, node, getMessage());
            }
        }
        return super.visit(node, data);
    }

    public Object visit(ASTMethodDeclaration node, Object data) {
        if (node.isSyntacticallyPublic() || node.isSyntacticallyAbstract()) {
            check(node, data);
        }
        return super.visit(node, data);
    }

    public Object visit(ASTFieldDeclaration node, Object data) {
        if (node.isSyntacticallyPublic() || node.isSyntacticallyStatic() || node.isSyntacticallyFinal()) {
            check(node, data);
        }
        return super.visit(node, data);
    }

    private void check(SimpleNode fieldOrMethod, Object data) {
        // third ancestor could be an AllocationExpression
        // if this is a method in an anonymous inner class
        Node parent = fieldOrMethod.jjtGetParent().jjtGetParent().jjtGetParent();
        if (parent instanceof ASTClassOrInterfaceDeclaration && ((ASTClassOrInterfaceDeclaration) parent).isInterface()) {
            addViolation(data, fieldOrMethod);
        }
    }

}
