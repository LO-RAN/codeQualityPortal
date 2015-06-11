/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.optimization;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.pmd.ast.ASTLocalVariableDeclaration;
import com.compuware.caqs.pmd.symboltable.Scope;
import com.compuware.caqs.pmd.symboltable.VariableNameDeclaration;

public class LocalVariableCouldBeFinal extends AbstractOptimizationRule {

    public Object visit(ASTLocalVariableDeclaration node, Object data) {
        if (node.isFinal()) {
            return data;
        }
        Scope s = node.getScope();
        Map decls = s.getVariableDeclarations();
        for (Iterator i = decls.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            VariableNameDeclaration var = (VariableNameDeclaration) entry.getKey();
            if (var.getAccessNodeParent() != node) {
                continue;
            }
            if (!assigned((List) entry.getValue())) {
                addViolation(data, var.getAccessNodeParent(), var.getImage());
            }
        }
        return data;
    }

}
