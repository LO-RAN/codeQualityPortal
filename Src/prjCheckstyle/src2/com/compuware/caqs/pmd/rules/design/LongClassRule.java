/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.design;

import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;

/**
 * This rule detects when a class exceeds a certain
 * threshold.  i.e. if a class has more than 1000 lines
 * of code.
 */
public class LongClassRule extends ExcessiveLengthRule {
    public LongClassRule() {
        super(ASTClassOrInterfaceDeclaration.class);
    }
}
