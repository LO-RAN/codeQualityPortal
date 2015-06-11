package com.compuware.caqs.pmd.rules.optimization;

import java.util.Set;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.RuleContext;
import com.compuware.caqs.pmd.SourceType;
import com.compuware.caqs.pmd.ast.ASTName;
import com.compuware.caqs.pmd.ast.ASTPrimaryExpression;
import com.compuware.caqs.pmd.ast.ASTPrimaryPrefix;
import com.compuware.caqs.pmd.ast.ASTPrimarySuffix;
import com.compuware.caqs.pmd.ast.Node;
import com.compuware.caqs.pmd.util.CollectionUtil;

public class UnnecessaryWrapperObjectCreation extends AbstractRule {

    private static final Set prefixSet = CollectionUtil.asSet(new String[] {
        "Byte.valueOf",
        "Short.valueOf",
        "Integer.valueOf",
        "Long.valueOf",
        "Float.valueOf",
        "Double.valueOf",
        "Character.valueOf"
    });

    private static final Set suffixSet = CollectionUtil.asSet(new String[] {
        "byteValue",
        "shortValue",
        "intValue",
        "longValue",
        "floatValue",
        "doubleValue",
        "charValue"
    });

    public Object visit(ASTPrimaryPrefix node, Object data) {
        if (node.jjtGetNumChildren() == 0 || !node.jjtGetChild(0).getClass().equals(ASTName.class)) {
            return super.visit(node, data);
        }

        String image = ((ASTName) node.jjtGetChild(0)).getImage();
        if (image.startsWith("java.lang.")) {
            image = image.substring(10);
        }

        boolean checkBoolean = ((RuleContext) data).getSourceType().compareTo(SourceType.JAVA_15) >= 0;

        if (prefixSet.contains(image)||(checkBoolean && "Boolean.valueOf".equals(image))) {
            ASTPrimaryExpression parent = (ASTPrimaryExpression) node.jjtGetParent();
            if (parent.jjtGetNumChildren() >= 3) {
                Node n = parent.jjtGetChild(2);
                if (n instanceof ASTPrimarySuffix) {
                    ASTPrimarySuffix suffix = (ASTPrimarySuffix) n;
                    image = suffix.getImage();

                    if (suffixSet.contains(image)||(checkBoolean && "booleanValue".equals(image))) {
                        super.addViolation(data, node);
                        return data;
                    }
                }
            }
        }
        return super.visit(node, data);
    }

}
