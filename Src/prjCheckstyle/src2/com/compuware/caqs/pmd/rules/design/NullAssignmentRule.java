/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.design;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTAssignmentOperator;
import com.compuware.caqs.pmd.ast.ASTConditionalExpression;
import com.compuware.caqs.pmd.ast.ASTEqualityExpression;
import com.compuware.caqs.pmd.ast.ASTName;
import com.compuware.caqs.pmd.ast.ASTNullLiteral;
import com.compuware.caqs.pmd.ast.ASTStatementExpression;
import com.compuware.caqs.pmd.symboltable.VariableNameDeclaration;

// Would this be simplified by using DFA somehow?

public class NullAssignmentRule extends AbstractRule {

    public Object visit(ASTNullLiteral node, Object data) {
        if (node.getNthParent(5) instanceof ASTStatementExpression) {
            ASTStatementExpression n = (ASTStatementExpression) node.getNthParent(5);

            if (isAssignmentToFinalField(n)) {
                return data;
            }

            if (n.jjtGetNumChildren() > 2 && n.jjtGetChild(1) instanceof ASTAssignmentOperator) {
                addViolation(data, node);
            }
        } else if (node.getNthParent(4) instanceof ASTConditionalExpression) {
            checkTernary((ASTConditionalExpression) node.getNthParent(4), data, node);
        } else if (node.getNthParent(5) instanceof ASTConditionalExpression) {
            checkTernary((ASTConditionalExpression) node.getNthParent(5), data, node);
        }

        return data;
    }

    private boolean isAssignmentToFinalField(ASTStatementExpression n) {
        ASTName name = (ASTName) n.getFirstChildOfType(ASTName.class);
        return name != null
                && name.getNameDeclaration() instanceof VariableNameDeclaration
                && ((VariableNameDeclaration) name.getNameDeclaration()).getAccessNodeParent().isFinal();
    }

    private void checkTernary(ASTConditionalExpression n, Object data, ASTNullLiteral node) {
        if (n.isTernary() && !(n.jjtGetChild(0) instanceof ASTEqualityExpression)) {
            addViolation(data, node);
        }
    }
}
