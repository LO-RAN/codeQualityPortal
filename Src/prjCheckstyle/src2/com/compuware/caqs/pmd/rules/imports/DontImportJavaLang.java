package com.compuware.caqs.pmd.rules.imports;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTImportDeclaration;
import com.compuware.caqs.pmd.ast.SimpleNode;

public class DontImportJavaLang extends AbstractRule {

    public Object visit(ASTImportDeclaration node, Object data) {
        if (node.isStatic()) {
            return data;
        }
        String img = ((SimpleNode) node.jjtGetChild(0)).getImage();
        if (img.startsWith("java.lang")) {
            if (img.startsWith("java.lang.ref")
                    || img.startsWith("java.lang.reflect")
                    || img.startsWith("java.lang.annotation")
                    || img.startsWith("java.lang.instrument")
                    || img.startsWith("java.lang.management")) {
                return data;
            }

            addViolation(data, node);
        }
        return data;
    }

}
