package com.compuware.caqs.pmd.symboltable;

import com.compuware.caqs.pmd.cpp.ast.ASTtranslation_unit;

/**
 * Setting the scope in the root of a JSP AST.
 *
 * @author pieter_van_raemdonck - Application Engineers NV/SA - www.ae.be
 */
public class CppScopeAndDeclarationFinder {

    /**
     * Set a DummyScope as scope of the given compilationUnit.
     *
     * @param compilationUnit the ASTCompilationUnit
     */
    public void setCppScope(ASTtranslation_unit compilationUnit) {
        compilationUnit.setScope(new DummyScope());
    }
}
