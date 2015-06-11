package com.compuware.caqs.pmd.rules.migration;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTCastExpression;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceType;
import com.compuware.caqs.pmd.ast.ASTFieldDeclaration;
import com.compuware.caqs.pmd.ast.ASTLocalVariableDeclaration;
import com.compuware.caqs.pmd.ast.ASTName;
import com.compuware.caqs.pmd.ast.ASTVariableDeclaratorId;
import com.compuware.caqs.pmd.ast.SimpleNode;
import com.compuware.caqs.pmd.symboltable.NameOccurrence;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UnnecessaryCast extends AbstractRule {

    private static Set implClassNames = new HashSet();

    static {
        implClassNames.add("List");
        implClassNames.add("Set");
        implClassNames.add("Map");
        implClassNames.add("java.util.List");
        implClassNames.add("java.util.Set");
        implClassNames.add("java.util.Map");
        implClassNames.add("ArrayList");
        implClassNames.add("HashSet");
        implClassNames.add("HashMap");
        implClassNames.add("LinkedHashMap");
        implClassNames.add("LinkedHashSet");
        implClassNames.add("TreeSet");
        implClassNames.add("TreeMap");
        implClassNames.add("Vector");
        implClassNames.add("java.util.ArrayList");
        implClassNames.add("java.util.HashSet");
        implClassNames.add("java.util.HashMap");
        implClassNames.add("java.util.LinkedHashMap");
        implClassNames.add("java.util.LinkedHashSet");
        implClassNames.add("java.util.TreeSet");
        implClassNames.add("java.util.TreeMap");
        implClassNames.add("java.util.Vector");
    }

    public Object visit(ASTLocalVariableDeclaration node, Object data) {
        return process(node, data);
    }

    public Object visit(ASTFieldDeclaration node, Object data) {
        return process(node, data);
    }

    private Object process(SimpleNode node, Object data) {
        ASTClassOrInterfaceType cit = (ASTClassOrInterfaceType) node.getFirstChildOfType(ASTClassOrInterfaceType.class);
        if (cit == null || !implClassNames.contains(cit.getImage())) {
            return data;
        }
        cit = (ASTClassOrInterfaceType) cit.getFirstChildOfType(ASTClassOrInterfaceType.class);
        if (cit == null) {
            return data;
        }
        ASTVariableDeclaratorId decl = (ASTVariableDeclaratorId) node
                .getFirstChildOfType(ASTVariableDeclaratorId.class);
        List usages = decl.getUsages();
        for (Iterator iter = usages.iterator(); iter.hasNext();) {
            NameOccurrence no = (NameOccurrence) iter.next();
            ASTName name = (ASTName) no.getLocation();
            SimpleNode n = (SimpleNode) name.jjtGetParent().jjtGetParent().jjtGetParent();
            if (ASTCastExpression.class.equals(n.getClass())) {
                addViolation(data, n);
            }
        }
        return null;
    }
}
