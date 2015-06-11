package com.compuware.caqs.pmd.rules.design;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.RuleContext;
import com.compuware.caqs.pmd.ast.ASTArgumentList;
import com.compuware.caqs.pmd.ast.ASTCastExpression;
import com.compuware.caqs.pmd.ast.ASTCatchStatement;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceType;
import com.compuware.caqs.pmd.ast.ASTName;
import com.compuware.caqs.pmd.ast.ASTPrimaryExpression;
import com.compuware.caqs.pmd.ast.ASTPrimaryPrefix;
import com.compuware.caqs.pmd.ast.ASTThrowStatement;
import com.compuware.caqs.pmd.ast.SimpleNode;
import com.compuware.caqs.pmd.symboltable.VariableNameDeclaration;
import org.jaxen.JaxenException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PreserveStackTrace extends AbstractRule {

	public Object visit(ASTCatchStatement node, Object data) {
        String target = (((SimpleNode) node.jjtGetChild(0).jjtGetChild(1)).getImage());
        List lstThrowStatements = node.findChildrenOfType(ASTThrowStatement.class);
        for (Iterator iter = lstThrowStatements.iterator(); iter.hasNext();) {
            ASTThrowStatement throwStatement = (ASTThrowStatement) iter.next();
            SimpleNode sn = (SimpleNode) throwStatement.jjtGetChild(0).jjtGetChild(0);
            if (sn.getClass().equals(ASTCastExpression.class)) {
                ASTPrimaryExpression expr = (ASTPrimaryExpression) sn.jjtGetChild(1);
                if (expr.jjtGetNumChildren() > 1 && expr.jjtGetChild(1).getClass().equals(ASTPrimaryPrefix.class)) {
                    RuleContext ctx = (RuleContext) data;
                    addViolation(ctx, throwStatement);
                }
                continue;
            }
            ASTArgumentList args = (ASTArgumentList) throwStatement.getFirstChildOfType(ASTArgumentList.class);

            if (args != null) {
                ck(data, target, throwStatement, args);
            } else if (args == null) {
                SimpleNode child = (SimpleNode) throwStatement.jjtGetChild(0);
                while (child != null && child.jjtGetNumChildren() > 0
                        && !child.getClass().equals(ASTName.class)) {
                    child = (SimpleNode) child.jjtGetChild(0);
                }
                if (child != null){
                    if( child.getClass().equals(ASTName.class) && (!target.equals(child.getImage()) && !child.hasImageEqualTo(target + ".fillInStackTrace"))) {
	                    Map vars = ((ASTName) child).getScope().getVariableDeclarations();
	                    for (Iterator i = vars.keySet().iterator(); i.hasNext();) {
	                        VariableNameDeclaration decl = (VariableNameDeclaration) i.next();
	                        args = (ASTArgumentList) ((SimpleNode) decl.getNode().jjtGetParent())
	                                .getFirstChildOfType(ASTArgumentList.class);
	                        if (args != null) {
	                            ck(data, target, throwStatement, args);
	                        }
	                    }
                    } else if(child.getClass().equals(ASTClassOrInterfaceType.class)){
        				addViolation((RuleContext) data, throwStatement);
                    }
                }
            }
        }
        return super.visit(node, data);
    }

	private void ck(Object data, String target,
			ASTThrowStatement throwStatement, ASTArgumentList args) {
		try {
			List lst = args.findChildNodesWithXPath("//Name[@Image='" + target
					+ "']");
			if (lst.isEmpty()) {
				RuleContext ctx = (RuleContext) data;
				addViolation(ctx, throwStatement);
			}
		} catch (JaxenException e) {
			e.printStackTrace();
		}
	}
}
