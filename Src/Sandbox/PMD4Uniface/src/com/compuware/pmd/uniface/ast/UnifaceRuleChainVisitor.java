/*
 * @author laurent IZAC laurent.izac@compuware.com
*/

package com.compuware.pmd.uniface.ast;

import java.util.List;

import net.sourceforge.pmd.AbstractRuleChainVisitor;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.rules.XPathRule;
import net.sourceforge.pmd.ast.CompilationUnit;

public class UnifaceRuleChainVisitor extends AbstractRuleChainVisitor {

    protected void indexNodes(List<CompilationUnit> astCompilationUnits, RuleContext ctx) {
        UnifaceParserVisitor unifaceParserVisitor = new UnifaceParserVisitorAdapter() {
            // Perform a visitation of the AST to index nodes which need
            // visiting by type
            public Object visit(SimpleNode node, Object data) {
                indexNode(node);
                return super.visit(node, data);
            }
        };

        for (int i = 0; i < astCompilationUnits.size(); i++) {
            unifaceParserVisitor.visit((ASTCompilationUnit)astCompilationUnits.get(i), ctx);
        }
    }

    protected void visit(Rule rule, net.sourceforge.pmd.ast.SimpleNode node, RuleContext ctx) {
        // Rule better either be a UnifaceParserVisitor, or a XPathRule
        if (rule instanceof UnifaceParserVisitor) {
            ((SimpleNode) node).jjtAccept((UnifaceParserVisitor) rule, ctx);
        } else {
            ((XPathRule) rule).evaluate(node, ctx);
        }
    }
}
