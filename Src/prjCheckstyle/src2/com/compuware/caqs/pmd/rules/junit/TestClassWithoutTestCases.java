/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.junit;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclarator;

import java.util.Iterator;
import java.util.List;

public class TestClassWithoutTestCases extends AbstractRule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isAbstract() || node.isInterface() || node.isNested()) {
            return data;
        }

        String className = node.getImage();
        if (className.endsWith("Test")) {
            List m = node.findChildrenOfType(ASTMethodDeclarator.class);
            boolean testsFound = false;
            if (m != null) {
                for (Iterator it = m.iterator(); it.hasNext() && !testsFound;) {
                    ASTMethodDeclarator md = (ASTMethodDeclarator) it.next();
                    if (!isInInnerClassOrInterface(md)
                            && md.getImage().startsWith("test")) {
                        testsFound = true;
                    }
                }
            }

            if (!testsFound) {
                addViolation(data, node);
            }

        }
        return data;
    }

    private boolean isInInnerClassOrInterface(ASTMethodDeclarator md) {
        ASTClassOrInterfaceDeclaration p = (ASTClassOrInterfaceDeclaration) md.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        return p != null && p.isNested();
    }
}
