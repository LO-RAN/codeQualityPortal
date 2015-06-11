/*
 * @author laurent IZAC laurent.izac@compuware.com
*/
package com.compuware.pmd.symboltable;

import com.compuware.pmd.uniface.ast.ASTCompilationUnit;
import net.sourceforge.pmd.sourcetypehandlers.VisitorStarter;

/**
 * Symbol Facade for UNIFACE.
 *
 */
public class UnifaceSymbolFacade implements VisitorStarter {

    /**
     * Set Scope for UNIFACE AST.
     */
    public void start(Object rootNode) {
        ASTCompilationUnit compilationUnit = (ASTCompilationUnit) rootNode;
        new UnifaceScopeAndDeclarationFinder().setUnifaceScope(compilationUnit);
    }

}
