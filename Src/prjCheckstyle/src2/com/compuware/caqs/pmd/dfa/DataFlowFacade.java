/*
 * Created on 10.07.2004
 */
package com.compuware.caqs.pmd.dfa;

import com.compuware.caqs.pmd.ast.ASTCompilationUnit;
import com.compuware.caqs.pmd.ast.ASTConstructorDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.JavaParserVisitorAdapter;
import com.compuware.caqs.pmd.dfa.variableaccess.VariableAccessVisitor;

/**
 * @author raik
 *         <p/>
 *         TODO What about initializers?  This only processes methods and constructors
 */
public class DataFlowFacade extends JavaParserVisitorAdapter {

    private StatementAndBraceFinder sbf;
    private VariableAccessVisitor vav;

    public void initializeWith(ASTCompilationUnit node) {
        sbf = new StatementAndBraceFinder();
        vav = new VariableAccessVisitor();
        node.jjtAccept(this, null);
    }

    public Object visit(ASTMethodDeclaration node, Object data) {
        sbf.buildDataFlowFor(node);
        vav.compute(node);
        return data;
    }

    public Object visit(ASTConstructorDeclaration node, Object data) {
        sbf.buildDataFlowFor(node);
        vav.compute(node);
        return data;
    }
}
