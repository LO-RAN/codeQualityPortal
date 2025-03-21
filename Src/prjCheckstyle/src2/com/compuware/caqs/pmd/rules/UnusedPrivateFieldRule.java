/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.symboltable.NameOccurrence;
import com.compuware.caqs.pmd.symboltable.VariableNameDeclaration;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UnusedPrivateFieldRule extends AbstractRule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        Map vars = node.getScope().getVariableDeclarations();
        for (Iterator i = vars.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            VariableNameDeclaration decl = (VariableNameDeclaration) entry.getKey();
            if (!decl.getAccessNodeParent().isPrivate() || isOK(decl.getImage())) {
                continue;
            }
            if (!actuallyUsed((List) entry.getValue())) {
                addViolation(data, decl.getNode(), decl.getImage());
            }
        }
        return super.visit(node, data);
    }

    private boolean actuallyUsed(List usages) {
        for (Iterator j = usages.iterator(); j.hasNext();) {
            NameOccurrence nameOccurrence = (NameOccurrence) j.next();
            if (!nameOccurrence.isOnLeftHandSide()) {
                return true;
            }
        }
        return false;
    }

    private boolean isOK(String image) {
        return image.equals("serialVersionUID") || image.equals("serialPersistentFields") || image.equals("IDENT");
    }
}
