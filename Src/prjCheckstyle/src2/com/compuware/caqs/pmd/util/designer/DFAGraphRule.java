package com.compuware.caqs.pmd.util.designer;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTCompilationUnit;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;

import java.util.List;

public class DFAGraphRule extends AbstractRule {

    private List methods;
    private List constructors;

    public DFAGraphRule() {
        super.setUsesDFA();
    }

    public List getMethods() {
        return this.methods;
    }

    public List getConstructors() {
        return this.constructors;
    }

    public Object visit(ASTCompilationUnit acu, Object data) {
        methods = acu.findChildrenOfType(ASTMethodDeclaration.class);
        constructors = acu.findChildrenOfType(ASTMethodDeclaration.class);
        return data;
    }
}


