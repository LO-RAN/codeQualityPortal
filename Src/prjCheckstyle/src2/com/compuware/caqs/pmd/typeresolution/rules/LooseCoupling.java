/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.typeresolution.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceType;
import com.compuware.caqs.pmd.ast.ASTFieldDeclaration;
import com.compuware.caqs.pmd.ast.ASTFormalParameter;
import com.compuware.caqs.pmd.ast.ASTResultType;
import com.compuware.caqs.pmd.ast.Node;
import com.compuware.caqs.pmd.util.CollectionUtil;

/**
 * This is a separate rule, uses the type resolution facade
 */
public class LooseCoupling extends AbstractRule {

	public LooseCoupling() {
		super();
	}

	public Object visit(ASTClassOrInterfaceType node, Object data) {
		Node parent = node.jjtGetParent().jjtGetParent().jjtGetParent();
		Class clazzType = node.getType();
		boolean isType = CollectionUtil.isCollectionType(clazzType, false);
		if (isType
				&& (parent instanceof ASTFieldDeclaration || parent instanceof ASTFormalParameter || parent instanceof ASTResultType)) {
			addViolation(data, node, node.getImage());
		}
		return data;
	}
}
