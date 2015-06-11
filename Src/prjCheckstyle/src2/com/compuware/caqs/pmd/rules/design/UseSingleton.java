/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.design;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceType;
import com.compuware.caqs.pmd.ast.ASTCompilationUnit;
import com.compuware.caqs.pmd.ast.ASTConstructorDeclaration;
import com.compuware.caqs.pmd.ast.ASTFieldDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.ASTResultType;

public class UseSingleton extends AbstractRule {

    private boolean isOK;
    private int methodCount;

    public Object visit(ASTCompilationUnit cu, Object data) {
        methodCount = 0;
        isOK = false;
        Object result = cu.childrenAccept(this, data);
        if (!isOK && methodCount > 0) {
            addViolation(data, cu);
        }

        return result;
    }

    public Object visit(ASTFieldDeclaration decl, Object data) {
        if (!decl.isStatic()) {
            isOK = true;
        }
        return data;
    }

    public Object visit(ASTConstructorDeclaration decl, Object data) {
        if (decl.isPrivate()) {
            isOK = true;
        }
        return data;
    }

    public Object visit(ASTClassOrInterfaceDeclaration decl, Object data) {
        if (decl.isAbstract()) {
            isOK = true;
        }
        return super.visit(decl, data);
    }

    public Object visit(ASTMethodDeclaration decl, Object data) {
        methodCount++;

        if (!isOK && !decl.isStatic()) {
            isOK = true;
        }

        // TODO use symbol table
        if (decl.getMethodName().equals("suite")) {
            ASTResultType res = (ASTResultType) decl.getFirstChildOfType(ASTResultType.class);
            ASTClassOrInterfaceType c = (ASTClassOrInterfaceType) res.getFirstChildOfType(ASTClassOrInterfaceType.class);
            if (c != null && c.hasImageEqualTo("Test")) {
                isOK = true;
            }
        }

        return data;
    }

}
