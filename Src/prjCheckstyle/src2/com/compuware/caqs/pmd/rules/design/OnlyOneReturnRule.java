/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.design;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.ASTReturnStatement;
import com.compuware.caqs.pmd.ast.SimpleNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OnlyOneReturnRule extends AbstractRule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        return super.visit(node, data);
    }

    public Object visit(ASTMethodDeclaration node, Object data) {
        if (node.isAbstract()) {
            return data;
        }

        List returnNodes = new ArrayList();
        node.findChildrenOfType(ASTReturnStatement.class, returnNodes, false);
        if (returnNodes.size() > 1) {
            for (Iterator i = returnNodes.iterator(); i.hasNext();) {
                SimpleNode problem = (SimpleNode) i.next();
                // skip the last one, it's OK
                if (!i.hasNext()) {
                    continue;
                }
                addViolation(data, problem);
            }
        }
        return data;
    }

}
