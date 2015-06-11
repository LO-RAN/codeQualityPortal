package com.compuware.caqs.pmd.rules.naming;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclarator;
import com.compuware.caqs.pmd.ast.ASTPrimitiveType;
import com.compuware.caqs.pmd.ast.ASTResultType;
import com.compuware.caqs.pmd.ast.SimpleJavaNode;

public class SuspiciousHashcodeMethodName extends AbstractRule {

    public Object visit(ASTMethodDeclaration node, Object data) {
        /* original XPath rule was
         //MethodDeclaration
        [ResultType
        //PrimitiveType
        [@Image='int']
        [//MethodDeclarator
        [@Image='hashcode' or @Image='HashCode' or @Image='Hashcode']
        [not(FormalParameters/*)]]]
         */

        ASTResultType type = (ASTResultType) node.getFirstChildOfType(ASTResultType.class);
        ASTMethodDeclarator decl = (ASTMethodDeclarator) node.getFirstChildOfType(ASTMethodDeclarator.class);
        String name = decl.getImage();
        if (name.equalsIgnoreCase("hashcode") && !name.equals("hashCode")
                && decl.jjtGetChild(0).jjtGetNumChildren() == 0
                && type.jjtGetNumChildren() != 0) {
            SimpleJavaNode t = (SimpleJavaNode) type.jjtGetChild(0).jjtGetChild(0);
            if (t instanceof ASTPrimitiveType && "int".equals(t.getImage())) {
                addViolation(data, node);
                return data;
            }
        }
        return super.visit(node, data);
    }

}
