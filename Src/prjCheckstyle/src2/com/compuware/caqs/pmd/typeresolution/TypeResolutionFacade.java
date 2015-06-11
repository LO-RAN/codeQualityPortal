/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.typeresolution;

import com.compuware.caqs.pmd.ast.ASTCompilationUnit;
import com.compuware.caqs.pmd.ast.JavaParserVisitorAdapter;

/**
 * @author Allan Caplan
 */
public class TypeResolutionFacade extends JavaParserVisitorAdapter {

    public void initializeWith(ASTCompilationUnit node) {
        ClassTypeResolver ctr = new ClassTypeResolver();
        node.jjtAccept(ctr, null);
    }

}
