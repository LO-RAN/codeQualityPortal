/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.optimization;

import com.compuware.caqs.pmd.ast.ASTAllocationExpression;
import com.compuware.caqs.pmd.ast.ASTDoStatement;
import com.compuware.caqs.pmd.ast.ASTForStatement;
import com.compuware.caqs.pmd.ast.ASTReturnStatement;
import com.compuware.caqs.pmd.ast.ASTThrowStatement;
import com.compuware.caqs.pmd.ast.ASTWhileStatement;

public class AvoidInstantiatingObjectsInLoops extends AbstractOptimizationRule {

    public Object visit(ASTAllocationExpression node, Object data) {
        if (insideLoop(node) && fourthParentNotThrow(node) && fourthParentNotReturn(node)) {
            addViolation(data, node);
        }
        return data;
    }

    private boolean fourthParentNotThrow(ASTAllocationExpression node) {
        return !(node.jjtGetParent().jjtGetParent().jjtGetParent().jjtGetParent() instanceof ASTThrowStatement);
    }

    private boolean fourthParentNotReturn(ASTAllocationExpression node) {
        return !(node.jjtGetParent().jjtGetParent().jjtGetParent().jjtGetParent() instanceof ASTReturnStatement);
    }

    private boolean insideLoop(ASTAllocationExpression node) {
        if (node.getFirstParentOfType(ASTDoStatement.class) != null) {
            return true;
        }
        if (node.getFirstParentOfType(ASTWhileStatement.class) != null) {
            return true;
        }
        if (node.getFirstParentOfType(ASTForStatement.class) != null) {
            return true;
        }
        return false;
    }
}
