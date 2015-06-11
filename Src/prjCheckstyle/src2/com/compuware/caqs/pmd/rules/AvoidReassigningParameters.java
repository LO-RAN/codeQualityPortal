/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTMethodDeclarator;
import com.compuware.caqs.pmd.symboltable.NameOccurrence;
import com.compuware.caqs.pmd.symboltable.VariableNameDeclaration;

public class AvoidReassigningParameters extends AbstractRule {

    public Object visit(ASTMethodDeclarator node, Object data) {
        Map params = node.getScope().getVariableDeclarations();
        for (Iterator i = params.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
 
            VariableNameDeclaration decl = (VariableNameDeclaration) entry.getKey();
            List usages = (List) entry.getValue();
            for (Iterator j = usages.iterator(); j.hasNext();) {
                NameOccurrence occ = (NameOccurrence) j.next();
                if ((occ.isOnLeftHandSide() || occ.isSelfAssignment()) && 
                    occ.getNameForWhichThisIsAQualifier() == null &&
                    (!decl.isArray() || occ.getLocation().jjtGetParent().jjtGetParent().jjtGetNumChildren() == 1))
                {
                    // not an array or no primary suffix to access the array values
                    addViolation(data, decl.getNode(), decl.getImage());
                }
            }
        }
        return super.visit(node, data);
    }
}
