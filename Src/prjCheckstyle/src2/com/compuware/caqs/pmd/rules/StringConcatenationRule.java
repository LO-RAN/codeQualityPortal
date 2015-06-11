/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTBlockStatement;
import com.compuware.caqs.pmd.ast.ASTForStatement;
import com.compuware.caqs.pmd.ast.Node;

public class StringConcatenationRule extends AbstractRule {

    public Object visit(ASTForStatement node, Object data) {
        Node forLoopStmt = null;
        for (int i = 0; i < 4; i++) {
            forLoopStmt = node.jjtGetChild(i);
            if (forLoopStmt instanceof ASTBlockStatement) {
                break;
            }
        }
        if (forLoopStmt == null) {
            return data;
        }


        return data;
    }
}
