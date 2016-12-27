/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTMethodDeclarator;

public class MethodNamingConventions extends AbstractRule {

    public Object visit(ASTMethodDeclarator node, Object data) {
    	
    	String methodName = node.getImage();
    	
        if (Character.isUpperCase(methodName.charAt(0))) {
        	addViolationWithMessage(data, node, "Method names should not start with capital letters");
        }
        if (methodName.indexOf('_') >= 0) {
            addViolationWithMessage(data, node, "Method names should not contain underscores");
        }
        return data;
    }

}