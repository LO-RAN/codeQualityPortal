/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;

public class ClassNamingConventions extends AbstractRule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (Character.isLowerCase(node.getImage().charAt(0))) {
            addViolation(data, node);
        }
        return data;
    }
}
