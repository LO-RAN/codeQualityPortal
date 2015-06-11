package com.compuware.caqs.pmd.rules.design;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTCatchStatement;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceType;
import com.compuware.caqs.pmd.ast.ASTFormalParameter;
import com.compuware.caqs.pmd.ast.ASTThrowStatement;
import com.compuware.caqs.pmd.ast.ASTTryStatement;
import com.compuware.caqs.pmd.ast.ASTType;

import java.util.Iterator;
import java.util.List;

/**
 * Catches the use of exception statements as a flow control device.
 *
 * @author Will Sargent
 */
public class ExceptionAsFlowControl extends AbstractRule {

    public Object visit(ASTThrowStatement node, Object data) {
        ASTTryStatement parent = (ASTTryStatement) node.getFirstParentOfType(ASTTryStatement.class);
        if (parent == null) {
            return data;
        }
        for (parent = (ASTTryStatement) parent.getFirstParentOfType(ASTTryStatement.class)
                ; parent != null
                ; parent = (ASTTryStatement) parent.getFirstParentOfType(ASTTryStatement.class)) {

            List list = parent.findChildrenOfType(ASTCatchStatement.class);
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                ASTCatchStatement catchStmt = (ASTCatchStatement) iter.next();
                ASTFormalParameter fp = (ASTFormalParameter) catchStmt.jjtGetChild(0);
                ASTType type = (ASTType) fp.findChildrenOfType(ASTType.class).get(0);
                ASTClassOrInterfaceType name = (ASTClassOrInterfaceType) type.findChildrenOfType(ASTClassOrInterfaceType.class).get(0);
                if (node.getFirstClassOrInterfaceTypeImage() != null && node.getFirstClassOrInterfaceTypeImage().equals(name.getImage())) {
                    addViolation(data, name);
                }
            }
        }
        return data;
    }

}
