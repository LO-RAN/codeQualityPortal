/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.imports;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceType;
import com.compuware.caqs.pmd.ast.ASTCompilationUnit;
import com.compuware.caqs.pmd.ast.ASTImportDeclaration;
import com.compuware.caqs.pmd.ast.ASTName;
import com.compuware.caqs.pmd.ast.SimpleJavaNode;
import com.compuware.caqs.pmd.ast.SimpleNode;
import com.compuware.caqs.pmd.rules.ImportWrapper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UnusedImportsRule extends AbstractRule {

    private Set imports = new HashSet();

    public Object visit(ASTCompilationUnit node, Object data) {
        imports.clear();
        super.visit(node, data);
        for (Iterator i = imports.iterator(); i.hasNext();) {
            ImportWrapper wrapper = (ImportWrapper) i.next();
            addViolation(data, wrapper.getNode(), wrapper.getFullName());
        }
        return data;
    }

    public Object visit(ASTImportDeclaration node, Object data) {
        if (!node.isImportOnDemand()) {
            ASTName importedType = (ASTName) node.jjtGetChild(0);
            String className;
            if (isQualifiedName(importedType)) {
                int lastDot = importedType.getImage().lastIndexOf('.') + 1;
                className = importedType.getImage().substring(lastDot);
            } else {
                className = importedType.getImage();
            }
            imports.add(new ImportWrapper(importedType.getImage(), className, node));
        }

        return data;
    }

    public Object visit(ASTClassOrInterfaceType node, Object data) {
        check(node);
        return super.visit(node, data);
    }

    public Object visit(ASTName node, Object data) {
        check(node);
        return data;
    }

    private void check(SimpleNode node) {
        String name;
        if (!isQualifiedName(node)) {
            name = node.getImage();
        } else {
            name = node.getImage().substring(0, node.getImage().indexOf('.'));
        }
        ImportWrapper candidate = new ImportWrapper(node.getImage(), name, new SimpleJavaNode(-1));
        if (imports.contains(candidate)) {
            imports.remove(candidate);
        }
    }
}
