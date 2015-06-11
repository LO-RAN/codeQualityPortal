/*
 * @author laurent IZAC laurent.izac@compuware.com
*/
package com.compuware.pmd.symboltable;

import net.sourceforge.pmd.symboltable.DummyScope;
import com.compuware.pmd.uniface.ast.ASTCompilationUnit;

/**
 * Setting the scope in the root of a UNIFACE AST.
 *
 */
public class UnifaceScopeAndDeclarationFinder {

    /**
     * Set a DummyScope as scope of the given compilationUnit.
     *
     * @param compilationUnit the ASTCompilationUnit
     */
    public void setUnifaceScope(ASTCompilationUnit compilationUnit) {
        compilationUnit.setScope(new DummyScope());
    }
}
