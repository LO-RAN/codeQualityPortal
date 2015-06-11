package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTAssignmentOperator;
import com.compuware.caqs.pmd.ast.ASTExpression;
import com.compuware.caqs.pmd.ast.ASTIfStatement;
import com.compuware.caqs.pmd.ast.ASTWhileStatement;
import com.compuware.caqs.pmd.ast.Node;

public class AssignmentInOperand extends AbstractRule {

    public Object visit(ASTExpression node, Object data) {
        Node parent = node.jjtGetParent();
        if ((parent instanceof ASTWhileStatement || parent instanceof ASTIfStatement) &&
                node.containsChildOfType(ASTAssignmentOperator.class))
        {
            addViolation(data, node);
            return data;
        }
        return super.visit(node, data);
    }

}
