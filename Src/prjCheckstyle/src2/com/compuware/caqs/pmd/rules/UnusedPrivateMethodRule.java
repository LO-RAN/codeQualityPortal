/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTConstructorDeclaration;
import com.compuware.caqs.pmd.ast.ASTInitializer;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclarator;
import com.compuware.caqs.pmd.ast.AccessNode;
import com.compuware.caqs.pmd.ast.SimpleNode;
import com.compuware.caqs.pmd.symboltable.ClassScope;
import com.compuware.caqs.pmd.symboltable.MethodNameDeclaration;
import com.compuware.caqs.pmd.symboltable.NameOccurrence;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UnusedPrivateMethodRule extends AbstractRule {


    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }

        Map methods = ((ClassScope) node.getScope()).getMethodDeclarations();
        for (Iterator i = findUnique(methods).iterator(); i.hasNext();) {
            MethodNameDeclaration mnd = (MethodNameDeclaration) i.next();
            List occs = (List) methods.get(mnd);
            if (!privateAndNotExcluded(mnd)) {
                continue;
            }
            if (occs.isEmpty()) {
                addViolation(data, mnd.getNode(), mnd.getImage() + mnd.getParameterDisplaySignature());
            } else {
                if (calledFromOutsideItself(occs, mnd)) {
                    addViolation(data, mnd.getNode(), mnd.getImage() + mnd.getParameterDisplaySignature());
                }

            }
        }
        return data;
    }

    private Set findUnique(Map methods) {
        // some rather hideous hackery here
        // to work around the fact that PMD does not yet do full type analysis
        // when it does, delete this
        Set unique = new HashSet();
        Set sigs = new HashSet();
        for (Iterator i = methods.keySet().iterator(); i.hasNext();) {
            MethodNameDeclaration mnd = (MethodNameDeclaration) i.next();
            String sig = mnd.getImage() + mnd.getParameterCount();
            if (!sigs.contains(sig)) {
                unique.add(mnd);
            }
            sigs.add(sig);
        }
        return unique;
    }

    private boolean calledFromOutsideItself(List occs, MethodNameDeclaration mnd) {
        int callsFromOutsideMethod = 0;
        for (Iterator i = occs.iterator(); i.hasNext();) {
            NameOccurrence occ = (NameOccurrence) i.next();
            SimpleNode occNode = occ.getLocation();
            ASTConstructorDeclaration enclosingConstructor = (ASTConstructorDeclaration) occNode.getFirstParentOfType(ASTConstructorDeclaration.class);
            if (enclosingConstructor != null) {
                callsFromOutsideMethod++;
                break; // Do we miss unused private constructors here?
            }
            ASTInitializer enclosingInitializer = (ASTInitializer) occNode.getFirstParentOfType(ASTInitializer.class);
            if (enclosingInitializer != null) {
                callsFromOutsideMethod++;
                break;
            }

            ASTMethodDeclaration enclosingMethod = (ASTMethodDeclaration) occNode.getFirstParentOfType(ASTMethodDeclaration.class);
            if ((enclosingMethod == null) || (enclosingMethod != null && !mnd.getNode().jjtGetParent().equals(enclosingMethod))) {
                callsFromOutsideMethod++;
            }
        }
        return callsFromOutsideMethod == 0;
    }

    private boolean privateAndNotExcluded(MethodNameDeclaration mnd) {
        ASTMethodDeclarator node = (ASTMethodDeclarator) mnd.getNode();
        return ((AccessNode) node.jjtGetParent()).isPrivate() && !node.hasImageEqualTo("readObject") && !node.hasImageEqualTo("writeObject") && !node.hasImageEqualTo("readResolve") && !node.hasImageEqualTo("writeReplace");
    }
}
