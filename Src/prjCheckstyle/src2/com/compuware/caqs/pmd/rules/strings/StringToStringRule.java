/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.strings;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTVariableDeclaratorId;
import com.compuware.caqs.pmd.symboltable.NameOccurrence;

import java.util.Iterator;

public class StringToStringRule extends AbstractRule {

    public Object visit(ASTVariableDeclaratorId node, Object data) {
        if (!node.getNameDeclaration().getTypeImage().equals("String")) {
            return data;
        }
        for (Iterator i = node.getUsages().iterator(); i.hasNext();) {
            NameOccurrence occ = (NameOccurrence) i.next();
            if (occ.getNameForWhichThisIsAQualifier() != null && occ.getNameForWhichThisIsAQualifier().getImage().indexOf("toString") != -1) {
                addViolation(data, occ.getLocation());
            }
        }
        return data;
    }
}
