/*
 * Created on Jan 11, 2005 
 *
 * $Id: AbstractOptimizationRule.java,v 1.14 2006/11/30 02:29:13 xlv Exp $
 */
package com.compuware.caqs.pmd.rules.optimization;

import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.Rule;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.symboltable.NameOccurrence;

/**
 * Base class with utility methods for optimization rules
 *
 * @author mgriffa
 */
public class AbstractOptimizationRule extends AbstractRule implements Rule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        return super.visit(node, data);
    }

    protected boolean assigned(List usages) {
        for (Iterator j = usages.iterator(); j.hasNext();) {
            NameOccurrence occ = (NameOccurrence) j.next();
            if (occ.isOnLeftHandSide() || occ.isSelfAssignment()) {
                return true;
            }
            continue;
        }
        return false;
    }

}
