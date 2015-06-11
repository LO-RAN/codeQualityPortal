/**
 *  * @author laurent IZAC laurent.izac@compuware.com
 */
package com.compuware.pmd.uniface.rules;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pmd.CommonAbstractRule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.ast.Node;
import com.compuware.pmd.uniface.ast.SimpleNode;
import com.compuware.pmd.uniface.ast.UnifaceParserVisitor;

public abstract class AbstractUnifaceRule extends CommonAbstractRule implements
		UnifaceParserVisitor {

	@Override
	public void setUsesTypeResolution() {
		// No Type resolution for UNIFACE rules?
	}

	/**
	 * Adds a violation to the report.
	 * 
	 * @param data
	 *            the RuleContext
	 * @param node
	 *            the node that produces the violation
	 */
	protected final void addViolation(Object data, SimpleNode node) {
		RuleContext ctx = (RuleContext)data;
		ctx.getReport().addRuleViolation(new RuleViolation(this, ctx, node));
	}

	/**
	 * Adds a violation to the report.
	 * 
	 * @param data
	 *            the RuleContext
	 * @param node
	 *            the node that produces the violation
	 * @param msg
	 *            specific message to put in the report
	 */
	protected final void addViolationWithMessage(Object data, SimpleNode node,
			String msg) {
		RuleContext ctx = (RuleContext)data;
		ctx.getReport().addRuleViolation(
				new RuleViolation(this, ctx, node, msg));
	}

	/**
	 * Adds a violation to the report.
	 * 
	 * @param data
	 *            the RuleContext
	 * @param node
	 *            the node that produces the violation
	 * @param embed
	 *            a variable to embed in the rule violation message
	 */
	protected final void addViolation(Object data, SimpleNode node, String embed) {
		RuleContext ctx = (RuleContext)data;
		ctx.getReport().addRuleViolation(
				new RuleViolation(this, ctx, node, MessageFormat.format(
						getMessage(), embed)));
	}

	/**
	 * Adds a violation to the report.
	 * 
	 * @param data
	 *            the RuleContext
	 * @param node
	 *            the node that produces the violation, may be null, in which
	 *            case all line and column info will be set to zero
	 * @param args
	 *            objects to embed in the rule violation message
	 */
	protected final void addViolation(Object data, Node node, Object[] args) {
		RuleContext ctx = (RuleContext)data;
		ctx.getReport().addRuleViolation(
				new RuleViolation(this, ctx, (SimpleNode)node, MessageFormat
						.format(getMessage(), args)));
	}

	public void apply(List acus, RuleContext ctx) {
		visitAll(acus, ctx);
	}

	protected void visitAll(List acus, RuleContext ctx) {
		for (Iterator i = acus.iterator(); i.hasNext();) {
			SimpleNode node = (SimpleNode)i.next();
			visit(node, ctx);
		}
	}


}
