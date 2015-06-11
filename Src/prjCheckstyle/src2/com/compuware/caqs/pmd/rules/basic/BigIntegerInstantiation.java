package com.compuware.caqs.pmd.rules.basic;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.RuleContext;
import com.compuware.caqs.pmd.SourceType;
import com.compuware.caqs.pmd.ast.ASTAllocationExpression;
import com.compuware.caqs.pmd.ast.ASTArguments;
import com.compuware.caqs.pmd.ast.ASTArrayDimsAndInits;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceType;
import com.compuware.caqs.pmd.ast.ASTLiteral;
import com.compuware.caqs.pmd.ast.Node;

public class BigIntegerInstantiation extends AbstractRule {

    public Object visit(ASTAllocationExpression node, Object data) {
        Node type = node.jjtGetChild(0);
        
        if (!(type instanceof ASTClassOrInterfaceType)) {
            return super.visit(node, data);            
        }
        
        String img = ((ASTClassOrInterfaceType) type).getImage();
        if (img.startsWith("java.math.")) {
            img = img.substring(10);
        }

        boolean jdk15 = ((RuleContext) data).getSourceType().compareTo(SourceType.JAVA_15) >= 0;
        
        if (("BigInteger".equals(img) || (jdk15 && "BigDecimal".equals(img))) &&
                (node.getFirstChildOfType(ASTArrayDimsAndInits.class) == null)
        ) {
            ASTArguments args = (ASTArguments) node.getFirstChildOfType(ASTArguments.class);
            if (args.getArgumentCount() == 1) {
                ASTLiteral literal = (ASTLiteral) node.getFirstChildOfType(ASTLiteral.class);
                if (literal == null || literal.jjtGetParent().jjtGetParent().jjtGetParent().jjtGetParent().jjtGetParent() != args) {
                    return super.visit(node, data);
                }

                img = literal.getImage();
                if ((img.length() > 2 && img.charAt(0) == '"')) {
                    img = img.substring(1, img.length() - 1);
                }
                
                if ("0".equals(img) || "1".equals(img) || (jdk15 && "10".equals(img))) {
                    addViolation(data, node);
                    return data;                
                }
            }
        }
        return super.visit(node, data);
    }

}
