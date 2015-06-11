package com.compuware.caqs.pmd.rules.basic;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTBlock;
import com.compuware.caqs.pmd.ast.ASTBlockStatement;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.ASTReturnStatement;
import com.compuware.caqs.pmd.ast.ASTStatement;

public class UnnecessaryReturn extends AbstractRule {

    public Object visit(ASTMethodDeclaration node, Object data) {

        if (node.getResultType().isVoid()) {
            super.visit(node, data);
        }
        return data;
    }

    public Object visit(ASTReturnStatement node, Object data) {
        if (node.jjtGetParent().getClass().equals(ASTStatement.class) && node.jjtGetParent().jjtGetParent().getClass().equals(ASTBlockStatement.class) && node.jjtGetParent().jjtGetParent().jjtGetParent().getClass().equals(ASTBlock.class)
                && node.jjtGetParent().jjtGetParent().jjtGetParent().jjtGetParent().getClass().equals(ASTMethodDeclaration.class)) {
            addViolation(data, node);
        }
        return data;
    }

}
