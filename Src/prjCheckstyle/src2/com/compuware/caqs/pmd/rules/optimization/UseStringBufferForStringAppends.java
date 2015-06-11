package com.compuware.caqs.pmd.rules.optimization;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTAssignmentOperator;
import com.compuware.caqs.pmd.ast.ASTLocalVariableDeclaration;
import com.compuware.caqs.pmd.ast.ASTName;
import com.compuware.caqs.pmd.ast.ASTPrimaryExpression;
import com.compuware.caqs.pmd.ast.ASTStatementExpression;
import com.compuware.caqs.pmd.ast.ASTVariableDeclaratorId;
import com.compuware.caqs.pmd.ast.Node;
import com.compuware.caqs.pmd.ast.SimpleNode;
import com.compuware.caqs.pmd.symboltable.NameOccurrence;

import java.util.Iterator;

public class UseStringBufferForStringAppends extends AbstractRule {

    public Object visit(ASTVariableDeclaratorId node, Object data) {
        if (node.getTypeNameNode().jjtGetNumChildren() == 0 || !"String".equals(((SimpleNode) node.getTypeNameNode().jjtGetChild(0)).getImage())) {
            return data;
        }
        Node parent = node.jjtGetParent().jjtGetParent();
        if (!parent.getClass().equals(ASTLocalVariableDeclaration.class)) {
            return data;
        }
        for (Iterator iter = node.getUsages().iterator(); iter.hasNext();) {
            NameOccurrence no = (NameOccurrence) iter.next();
            SimpleNode name = (SimpleNode) no.getLocation();
            ASTStatementExpression statement = (ASTStatementExpression) name.getFirstParentOfType(ASTStatementExpression.class);
            if (statement == null) {
                continue;
            }
            if (statement.jjtGetNumChildren() > 0 && statement.jjtGetChild(0).getClass().equals(ASTPrimaryExpression.class)) {
                // FIXME - hm, is there a bug in those methods?
                // check that we're looking at the "left hand" node. NB:
                // no.isRightHand / no.isLeftHand doesn't look like it works
                ASTName astName = (ASTName) ((SimpleNode) statement.jjtGetChild(0)).getFirstChildOfType(ASTName.class);
                if (astName != null && astName.equals(name)) {
                    ASTAssignmentOperator assignmentOperator = (ASTAssignmentOperator) statement.getFirstChildOfType(ASTAssignmentOperator.class);
                    if (assignmentOperator != null && assignmentOperator.isCompound()) {
                        addViolation(data, assignmentOperator);
                    }
                }
            }
        }

        return data;
    }
}
