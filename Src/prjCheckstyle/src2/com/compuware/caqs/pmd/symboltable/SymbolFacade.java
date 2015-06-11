/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.symboltable;

import com.compuware.caqs.pmd.ast.ASTCompilationUnit;

public class SymbolFacade {
    public void initializeWith(ASTCompilationUnit node) {
        ScopeAndDeclarationFinder sc = new ScopeAndDeclarationFinder();
        node.jjtAccept(sc, null);
        OccurrenceFinder of = new OccurrenceFinder();
        node.jjtAccept(of, null);
    }
}
