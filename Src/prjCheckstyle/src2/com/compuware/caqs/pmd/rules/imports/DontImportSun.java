package com.compuware.caqs.pmd.rules.imports;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTImportDeclaration;
import com.compuware.caqs.pmd.ast.SimpleNode;

public class DontImportSun extends AbstractRule {

    public Object visit(ASTImportDeclaration node, Object data) {
        String img = ((SimpleNode) node.jjtGetChild(0)).getImage();
        if (img.startsWith("sun.") && !img.startsWith("sun.misc.Signal")) {
            addViolation(data, node);
        }
        return data;
    }

}
