/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTBlock;
import com.compuware.caqs.pmd.ast.ASTBlockStatement;
import com.compuware.caqs.pmd.ast.ASTBooleanLiteral;
import com.compuware.caqs.pmd.ast.ASTIfStatement;
import com.compuware.caqs.pmd.ast.ASTReturnStatement;
import com.compuware.caqs.pmd.ast.ASTStatement;
import com.compuware.caqs.pmd.ast.SimpleNode;

public class SimplifyBooleanReturns extends AbstractRule {

    public Object visit(ASTIfStatement node, Object data) {
        // only deal with if..then..else stmts
        if (node.jjtGetNumChildren() != 3) {
            return super.visit(node, data);
        }

        // don't bother if either the if or the else block is empty
        if (node.jjtGetChild(1).jjtGetNumChildren() == 0 || node.jjtGetChild(2).jjtGetNumChildren() == 0) {
            return super.visit(node, data);
        }

        // first case:
        // If
        //  Expr
        //  Statement
        //   ReturnStatement
        //  Statement
        //   ReturnStatement
        // i.e.,
        // if (foo)
        //  return true;
        // else
        //  return false;

        // second case
        // If
        // Expr
        // Statement
        //  Block
        //   BlockStatement
        //    Statement
        //     ReturnStatement
        // Statement
        //  Block
        //   BlockStatement
        //    Statement
        //     ReturnStatement
        // i.e.,
        // if (foo) {
        //  return true;
        // } else {
        //  return false;
        // }
        if (node.jjtGetChild(1).jjtGetChild(0) instanceof ASTReturnStatement && node.jjtGetChild(2).jjtGetChild(0) instanceof ASTReturnStatement && terminatesInBooleanLiteral((SimpleNode) node.jjtGetChild(1).jjtGetChild(0)) && terminatesInBooleanLiteral((SimpleNode) node.jjtGetChild(2).jjtGetChild(0))) {
            addViolation(data, node);
        } else if (hasOneBlockStmt((SimpleNode) node.jjtGetChild(1)) && hasOneBlockStmt((SimpleNode) node.jjtGetChild(2)) && terminatesInBooleanLiteral((SimpleNode) node.jjtGetChild(1).jjtGetChild(0)) && terminatesInBooleanLiteral((SimpleNode) node.jjtGetChild(2).jjtGetChild(0))) {
            addViolation(data, node);
        }

        return super.visit(node, data);
    }

    private boolean hasOneBlockStmt(SimpleNode node) {
        return node.jjtGetChild(0) instanceof ASTBlock && node.jjtGetChild(0).jjtGetNumChildren() == 1 && node.jjtGetChild(0).jjtGetChild(0) instanceof ASTBlockStatement && node.jjtGetChild(0).jjtGetChild(0).jjtGetChild(0) instanceof ASTStatement && node.jjtGetChild(0).jjtGetChild(0).jjtGetChild(0).jjtGetChild(0) instanceof ASTReturnStatement;
    }

    private boolean terminatesInBooleanLiteral(SimpleNode node) {
        return eachNodeHasOneChild(node) && (getLastChild(node) instanceof ASTBooleanLiteral);
    }

    private boolean eachNodeHasOneChild(SimpleNode node) {
        if (node.jjtGetNumChildren() > 1) {
            return false;
        }
        if (node.jjtGetNumChildren() == 0) {
            return true;
        }
        return eachNodeHasOneChild((SimpleNode) node.jjtGetChild(0));
    }

    private SimpleNode getLastChild(SimpleNode node) {
        if (node.jjtGetNumChildren() == 0) {
            return node;
        }
        return getLastChild((SimpleNode) node.jjtGetChild(0));
    }
}
