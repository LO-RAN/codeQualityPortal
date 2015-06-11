/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.junit;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.Rule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.ASTName;
import com.compuware.caqs.pmd.ast.ASTPrimaryExpression;
import com.compuware.caqs.pmd.ast.ASTPrimaryPrefix;
import com.compuware.caqs.pmd.ast.ASTResultType;
import com.compuware.caqs.pmd.ast.ASTStatementExpression;
import com.compuware.caqs.pmd.ast.ASTTypeParameters;
import com.compuware.caqs.pmd.ast.Node;

public class JUnitTestsShouldContainAsserts extends AbstractRule implements Rule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        return super.visit(node, data);
    }

    public Object visit(ASTMethodDeclaration method, Object data) {
        if (!method.isPublic() || method.isAbstract() || method.isNative() || method.isStatic()) {
            return data; // skip various inapplicable method variations
        }

        Node node = method.jjtGetChild(0);
        if (node instanceof ASTTypeParameters) {
            node = method.jjtGetChild(1);
        }
        if (((ASTResultType)node).isVoid() && method.getMethodName().startsWith("test"))  {
            if (!containsAssert(method.getBlock(), false)) {
                addViolation(data, method);
            }
        }
		return data;
	}

    private boolean containsAssert(Node n, boolean assertFound) {
        if (!assertFound) {
            if (n instanceof ASTStatementExpression) {
                if (isAssertOrFailStatement((ASTStatementExpression)n)) {
                    return true;
                }
            }
            if (!assertFound) {
                for (int i=0;i<n.jjtGetNumChildren() && ! assertFound;i++) {
                    Node c = n.jjtGetChild(i);
                    if (containsAssert(c, assertFound)) 
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Tells if the expression is an assert statement or not.
     */
    private boolean isAssertOrFailStatement(ASTStatementExpression expression) {
        if (expression!=null 
                && expression.jjtGetNumChildren()>0
                && expression.jjtGetChild(0) instanceof ASTPrimaryExpression
                ) {
            ASTPrimaryExpression pe = (ASTPrimaryExpression) expression.jjtGetChild(0);
            if (pe.jjtGetNumChildren()> 0 && pe.jjtGetChild(0) instanceof ASTPrimaryPrefix) {
                ASTPrimaryPrefix pp = (ASTPrimaryPrefix) pe.jjtGetChild(0);
                if (pp.jjtGetNumChildren()>0 && pp.jjtGetChild(0) instanceof ASTName) {
                    ASTName n = (ASTName) pp.jjtGetChild(0);
                    if (n.getImage()!=null && (n.getImage().startsWith("assert") || n.getImage().startsWith("fail") )) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
