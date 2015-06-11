package com.compuware.caqs.pmd.rules.strictexception;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTCatchStatement;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceType;
import com.compuware.caqs.pmd.ast.ASTType;

/**
 * Finds <code>catch</code> statements
 * containing <code>throwable</code> as the type definition.
 * <p/>
 *
 * @author <a mailto:trondandersen@c2i.net>Trond Andersen</a>
 */
public class AvoidCatchingThrowable extends AbstractRule {

    public Object visit(ASTCatchStatement node, Object data) {
        ASTType type = (ASTType) node.findChildrenOfType(ASTType.class).get(0);
        ASTClassOrInterfaceType name = (ASTClassOrInterfaceType) type.findChildrenOfType(ASTClassOrInterfaceType.class).get(0);
        if (name.hasImageEqualTo("Throwable")) {
            addViolation(data, name);
        }
        return super.visit(node, data);
    }
}
