/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.imports;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTImportDeclaration;

public class ImportFromSamePackageRule extends AbstractRule {

    public Object visit(ASTImportDeclaration importDecl, Object data) {
        String packageName = importDecl.getScope().getEnclosingSourceFileScope().getPackageName();

        if (packageName != null && packageName.equals(importDecl.getPackageName())) {
            addViolation(data, importDecl);
        }

        // special case
        if (packageName == null && importDecl.getPackageName().equals("")) {
            addViolation(data, importDecl);
        }
        return data;
    }
}
