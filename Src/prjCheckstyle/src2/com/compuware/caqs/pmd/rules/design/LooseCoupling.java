/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.design;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceType;
import com.compuware.caqs.pmd.ast.ASTFieldDeclaration;
import com.compuware.caqs.pmd.ast.ASTFormalParameter;
import com.compuware.caqs.pmd.ast.ASTResultType;
import com.compuware.caqs.pmd.ast.Node;
import com.compuware.caqs.pmd.util.CollectionUtil;

public class LooseCoupling extends AbstractRule {

	// TODO - these should be brought in via external properties
//    private static final Set implClassNames = CollectionUtil.asSet( new Object[] {
//    	"ArrayList", "HashSet", "HashMap", "LinkedHashMap", "LinkedHashSet", "TreeSet", "TreeMap", "Vector",
//    	"java.util.ArrayList", "java.util.HashSet", "java.util.HashMap",
//    	"java.util.LinkedHashMap", "java.util.LinkedHashSet", "java.util.TreeSet",
//    	"java.util.TreeMap", "java.util.Vector" 
//    	});

    public LooseCoupling() {
        super();
    }

    public Object visit(ASTClassOrInterfaceType node, Object data) {
        Node parent = node.jjtGetParent().jjtGetParent().jjtGetParent();
        String typeName = node.getImage();
        if (CollectionUtil.isCollectionType(typeName, false) && (parent instanceof ASTFieldDeclaration || parent instanceof ASTFormalParameter || parent instanceof ASTResultType)) {
            addViolation(data, node, typeName);
        }
        return data;
    }
}
