/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.strings;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTBlockStatement;
import com.compuware.caqs.pmd.ast.ASTLiteral;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This rule finds the following:
 * <p/>
 * <pre>
 *         StringBuffer.append(&quot;c&quot;); // appends a
 *         single character
 * </pre>
 * <p/>
 * It is preferable to use StringBuffer.append('c'); // appends a single
 * character Implementation of PMD RFE 1373863
 */
public class AppendCharacterWithChar extends AbstractRule {

    private static final Pattern REGEX = Pattern.compile("\"[\\\\]?[\\s\\S]\"");

    public Object visit(ASTLiteral node, Object data) {
        ASTBlockStatement bs = (ASTBlockStatement) node
                .getFirstParentOfType(ASTBlockStatement.class);
        if (bs == null) {
            return data;
        }

        String str = node.getImage();
        if (str == null || str.length() < 3 || str.length() > 4) {
            return data;
        }

        Matcher matcher = REGEX.matcher(str);
        if (matcher.find()) {
            if (!InefficientStringBuffering.isInStringBufferOperation(node, 8, "append")) {
                return data;
            }
            addViolation(data, node);
        }
        return data;
    }
}
