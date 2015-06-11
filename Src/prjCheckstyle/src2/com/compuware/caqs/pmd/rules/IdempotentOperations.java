/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTAssignmentOperator;
import com.compuware.caqs.pmd.ast.ASTExpression;
import com.compuware.caqs.pmd.ast.ASTName;
import com.compuware.caqs.pmd.ast.ASTPrimaryExpression;
import com.compuware.caqs.pmd.ast.ASTPrimarySuffix;
import com.compuware.caqs.pmd.ast.ASTStatementExpression;
import com.compuware.caqs.pmd.ast.Node;
import com.compuware.caqs.pmd.ast.SimpleNode;

public class IdempotentOperations extends AbstractRule {

    public Object visit(ASTStatementExpression node, Object data) {
        if (node.jjtGetNumChildren() != 3
                || !(node.jjtGetChild(0) instanceof ASTPrimaryExpression)
                || !(node.jjtGetChild(1) instanceof ASTAssignmentOperator)
                || (((ASTAssignmentOperator) (node.jjtGetChild(1))).isCompound())
                || !(node.jjtGetChild(2) instanceof ASTExpression)
                || node.jjtGetChild(0).jjtGetChild(0).jjtGetNumChildren() == 0
                || node.jjtGetChild(2).jjtGetChild(0).jjtGetChild(0).jjtGetNumChildren() == 0
        ) {
            return super.visit(node, data);
        }

        SimpleNode lhs = (SimpleNode) node.jjtGetChild(0).jjtGetChild(0).jjtGetChild(0);
        if (!(lhs instanceof ASTName)) {
            return super.visit(node, data);
        }

        SimpleNode rhs = (SimpleNode) node.jjtGetChild(2).jjtGetChild(0).jjtGetChild(0).jjtGetChild(0);
        if (!(rhs instanceof ASTName)) {
            return super.visit(node, data);
        }

        if (!lhs.hasImageEqualTo(rhs.getImage())) {
            return super.visit(node, data);
        }

        if (lhs.jjtGetParent().jjtGetParent().jjtGetNumChildren() > 1) {
            Node n = lhs.jjtGetParent().jjtGetParent().jjtGetChild(1);
            if (n instanceof ASTPrimarySuffix && ((ASTPrimarySuffix) n).isArrayDereference()) {
                return super.visit(node, data);
            }
        }

        if (rhs.jjtGetParent().jjtGetParent().jjtGetNumChildren() > 1) {
            Node n = rhs.jjtGetParent().jjtGetParent().jjtGetChild(1);
            if (n instanceof ASTPrimarySuffix && ((ASTPrimarySuffix) n).isArguments() || ((ASTPrimarySuffix) n).isArrayDereference()) {
                return super.visit(node, data);
            }
        }

        if (lhs.findChildrenOfType(ASTPrimarySuffix.class).size() != rhs.findChildrenOfType(ASTPrimarySuffix.class).size()) {
            return super.visit(node, data);
        }

        addViolation(data, node);
        return super.visit(node, data);
    }
}
