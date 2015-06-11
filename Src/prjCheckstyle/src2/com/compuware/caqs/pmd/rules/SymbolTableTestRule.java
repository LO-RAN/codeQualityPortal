/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.Rule;
import com.compuware.caqs.pmd.ast.ASTFieldDeclaration;
import com.compuware.caqs.pmd.ast.ASTVariableDeclaratorId;
import com.compuware.caqs.pmd.ast.SimpleNode;
import com.compuware.caqs.pmd.symboltable.NameOccurrence;

import java.util.Iterator;

public class SymbolTableTestRule extends AbstractRule implements Rule {

    public Object visit(ASTFieldDeclaration node,Object data) {
        ASTVariableDeclaratorId declaration = (ASTVariableDeclaratorId)node.findChildrenOfType(ASTVariableDeclaratorId.class).get(0);
        for (Iterator iter = declaration.getUsages().iterator();iter.hasNext();) {
            NameOccurrence no = (NameOccurrence)iter.next();
            SimpleNode location = no.getLocation();
            System.out.println(declaration.getImage() + " is used here: " + location.getImage());
        }
        return data;
    }
}
