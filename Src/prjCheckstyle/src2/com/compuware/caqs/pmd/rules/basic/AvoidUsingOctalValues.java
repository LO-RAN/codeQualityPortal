package com.compuware.caqs.pmd.rules.basic;

import java.util.regex.Pattern;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.PropertyDescriptor;
import com.compuware.caqs.pmd.ast.ASTLiteral;
import com.compuware.caqs.pmd.properties.BooleanProperty;

public class AvoidUsingOctalValues extends AbstractRule {

    public static final Pattern OCTAL_PATTERN = Pattern.compile("0[0-7]{2,}[lL]?");

    public static final Pattern STRICT_OCTAL_PATTERN = Pattern.compile("0[0-7]+[lL]?");

    private static final PropertyDescriptor strictMethodsDescriptor = new BooleanProperty(
            "strict", "Detect violations for 00 to 07.", false, 1.0f
            );

    public Object visit(ASTLiteral node, Object data) {
        boolean strict = getBooleanProperty(strictMethodsDescriptor);
        Pattern p = strict?STRICT_OCTAL_PATTERN:OCTAL_PATTERN;

        String img = node.getImage();
        if (img != null && p.matcher(img).matches()) {
            addViolation(data, node);
        }

        return data;
    }

}
