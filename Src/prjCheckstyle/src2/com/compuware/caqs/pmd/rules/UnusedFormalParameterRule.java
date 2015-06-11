/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTConstructorDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.Node;
import com.compuware.caqs.pmd.ast.SimpleNode;
import com.compuware.caqs.pmd.symboltable.NameOccurrence;
import com.compuware.caqs.pmd.symboltable.VariableNameDeclaration;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UnusedFormalParameterRule extends AbstractRule {

    public Object visit(ASTConstructorDeclaration node, Object data) {
        check(node, data);
        return data;
    }

    public Object visit(ASTMethodDeclaration node, Object data) {
        if (!node.isPrivate() && !hasProperty("checkall")) {
            return data;
        }
        if (!node.isNative()) {
            check(node, data);
        }
        return data;
    }

    private void check(SimpleNode node, Object data) {
        Node parent = node.jjtGetParent().jjtGetParent().jjtGetParent();
        if (parent instanceof ASTClassOrInterfaceDeclaration && !((ASTClassOrInterfaceDeclaration) parent).isInterface()) {
            Map vars = node.getScope().getVariableDeclarations();
            for (Iterator i = vars.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                VariableNameDeclaration nameDecl = (VariableNameDeclaration) entry.getKey();
                if (actuallyUsed(nameDecl, (List) entry.getValue())) {
                    continue;
                }
                addViolation(data, node, new Object[]{node instanceof ASTMethodDeclaration ? "method" : "constructor", nameDecl.getImage()});
            }
        }
    }

    private boolean actuallyUsed(VariableNameDeclaration nameDecl, List usages) {
        for (Iterator j = usages.iterator(); j.hasNext();) {
            NameOccurrence occ = (NameOccurrence) j.next();
            if (occ.isOnLeftHandSide()) {
                if (nameDecl.isArray() && occ.getLocation().jjtGetParent().jjtGetParent().jjtGetNumChildren() > 1) {
                    // array element access
                    return true;
                }
                continue;
            } else {
                return true;
            }
        }
        return false;
    }


}
