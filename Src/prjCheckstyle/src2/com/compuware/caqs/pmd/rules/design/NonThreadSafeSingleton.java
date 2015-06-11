/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.design;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.PropertyDescriptor;
import com.compuware.caqs.pmd.ast.ASTAssignmentOperator;
import com.compuware.caqs.pmd.ast.ASTCompilationUnit;
import com.compuware.caqs.pmd.ast.ASTFieldDeclaration;
import com.compuware.caqs.pmd.ast.ASTIfStatement;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.ASTName;
import com.compuware.caqs.pmd.ast.ASTNullLiteral;
import com.compuware.caqs.pmd.ast.ASTPrimaryExpression;
import com.compuware.caqs.pmd.ast.ASTPrimaryPrefix;
import com.compuware.caqs.pmd.ast.ASTPrimarySuffix;
import com.compuware.caqs.pmd.ast.ASTStatementExpression;
import com.compuware.caqs.pmd.ast.ASTSynchronizedStatement;
import com.compuware.caqs.pmd.properties.BooleanProperty;

public class NonThreadSafeSingleton extends AbstractRule {

    private Map fieldDecls = new HashMap();

    private boolean checkNonStaticMethods = true;
    private boolean checkNonStaticFields = true;

    private static final PropertyDescriptor checkNonStaticMethodsDescriptor = new BooleanProperty(
    	"checkNonStaticMethods", "Check for non-static methods.", true, 1.0f
    	);
    private static final PropertyDescriptor checkNonStaticFieldsDescriptor = new BooleanProperty(
    	"checkNonStaticFields", "Check for non-static fields.",	true, 2.0f
    	);
    
    private static final Map propertyDescriptorsByName = asFixedMap(new PropertyDescriptor[] {
    	checkNonStaticMethodsDescriptor, checkNonStaticFieldsDescriptor
    	});
    
//    public NonThreadSafeSingleton() {
//        checkNonStaticMethods = super.getBooleanProperty("checkNonStaticMethods");
//        checkNonStaticFields = super.getBooleanProperty("checkNonStaticFields");
//    }

    public Object visit(ASTCompilationUnit node, Object data) {
        fieldDecls.clear();
        checkNonStaticMethods = getBooleanProperty(checkNonStaticMethodsDescriptor);
        checkNonStaticFields = getBooleanProperty(checkNonStaticFieldsDescriptor);        
        return super.visit(node, data);
    }

    public Object visit(ASTFieldDeclaration node, Object data) {
        if (checkNonStaticFields || node.isStatic()) {
            fieldDecls.put(node.getVariableName(), node);
        }
        return super.visit(node, data);
    }

    public Object visit(ASTMethodDeclaration node, Object data) {

        if ((checkNonStaticMethods && !node.isStatic()) || node.isSynchronized()) {
            return super.visit(node, data);
        }

        List ifStatements = node.findChildrenOfType(ASTIfStatement.class);
        for (Iterator iter = ifStatements.iterator(); iter.hasNext();) {
            ASTIfStatement ifStatement = (ASTIfStatement) iter.next();
            if (ifStatement.getFirstParentOfType(ASTSynchronizedStatement.class) == null) {
                ASTNullLiteral NullLiteral = (ASTNullLiteral) ifStatement.getFirstChildOfType(ASTNullLiteral.class);

                if (NullLiteral == null) {
                    continue;
                }
                ASTName Name = (ASTName) ifStatement.getFirstChildOfType(ASTName.class);
                if (Name == null || !fieldDecls.containsKey(Name.getImage())) {
                    continue;
                }
                List assigmnents = ifStatement.findChildrenOfType(ASTAssignmentOperator.class);
                boolean violation = false;
                for (int ix = 0; ix < assigmnents.size(); ix++) {
                    ASTAssignmentOperator oper = (ASTAssignmentOperator) assigmnents.get(ix);
                    if (!oper.jjtGetParent().getClass().equals(ASTStatementExpression.class)) {
                        continue;
                    }
                    ASTStatementExpression expr = (ASTStatementExpression) oper.jjtGetParent();
                    if (expr.jjtGetChild(0).getClass().equals(ASTPrimaryExpression.class) && ((ASTPrimaryExpression) expr.jjtGetChild(0)).jjtGetChild(0).getClass().equals(ASTPrimaryPrefix.class)) {
                        ASTPrimaryPrefix pp = (ASTPrimaryPrefix) ((ASTPrimaryExpression) expr.jjtGetChild(0)).jjtGetChild(0);
                        String name = null;
                        if (pp.usesThisModifier()) {
                        	ASTPrimarySuffix priSuf = (ASTPrimarySuffix)expr.getFirstChildOfType(ASTPrimarySuffix.class); 
                        	name = priSuf.getImage();
						} else {
							ASTName astName = (ASTName) pp.jjtGetChild(0);
							name = astName.getImage();
                        }
						if (fieldDecls.containsKey(name)) {
							violation = true;
						}
                    }
                }
                if (violation) {
                    addViolation(data, ifStatement);
                }
            }
        }
        return super.visit(node, data);
    }
    
    /**
     * @return Map
     */
    protected Map propertiesByName() {
    	return propertyDescriptorsByName;
    }
}
