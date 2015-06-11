/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.symboltable;

import com.compuware.caqs.pmd.ast.SimpleNode;

public interface NameDeclaration {
    SimpleNode getNode();

    String getImage();

    Scope getScope();
}
