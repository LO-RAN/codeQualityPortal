package com.compuware.caqs.pmd.symboltable;

import com.compuware.caqs.pmd.cpp.ast.ASTtranslation_unit;
import com.compuware.caqs.pmd.sourcetypehandlers.VisitorStarter;

/**
 * Symbol Facade for JSP.
 *
 * @author pieter_van_raemdonck - Application Engineers NV/SA - www.ae.be
 */
public class CppSymbolFacade implements VisitorStarter {

    /**
     * Set Scope for JSP AST.
     */
    public void start(Object rootNode) {
        ASTtranslation_unit compilationUnit = (ASTtranslation_unit) rootNode;
        new CppScopeAndDeclarationFinder().setCppScope(compilationUnit);
    }

}
