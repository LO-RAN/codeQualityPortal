/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.design;

import com.compuware.caqs.pmd.ast.ASTStatement;
import com.compuware.caqs.pmd.ast.ASTSwitchLabel;
import com.compuware.caqs.pmd.ast.ASTSwitchStatement;
import com.compuware.caqs.pmd.stat.DataPoint;
import com.compuware.caqs.pmd.stat.StatisticalRule;

/**
 * @author dpeugh
 *         <p/>
 *         Switch Density - This is the number of statements over the
 *         number of cases within a switch.  The higher the value, the
 *         more work each case is doing.
 *         <p/>
 *         Its my theory, that when the Switch Density is high, you should
 *         start looking at Subclasses or State Pattern to alleviate the
 *         problem.
 */
public class SwitchDensityRule extends StatisticalRule {

    private static class SwitchDensity {
        private int labels = 0;
        private int stmts = 0;

        public void addSwitchLabel() {
            labels++;
        }

        public void addStatement() {
            stmts++;
        }

        public void addStatements(int stmtCount) {
            stmts += stmtCount;
        }

        public int getStatementCount() {
            return stmts;
        }

        public double getDensity() {
            if (labels == 0) {
                return 0;
            }
            return (double) stmts / (double) labels;
        }
    }

    public Object visit(ASTSwitchStatement node, Object data) {
        SwitchDensity oldData = null;

        if (data instanceof SwitchDensity) {
            oldData = (SwitchDensity) data;
        }

        SwitchDensity density = new SwitchDensity();

        node.childrenAccept(this, density);

        DataPoint point = new DataPoint();
        point.setNode(node);
        point.setScore(density.getDensity());
        point.setMessage(getMessage());

        addDataPoint(point);

        if (data instanceof SwitchDensity) {
            ((SwitchDensity) data).addStatements(density.getStatementCount());
        }
        return oldData;
    }

    public Object visit(ASTStatement statement, Object data) {
        if (data instanceof SwitchDensity) {
            ((SwitchDensity) data).addStatement();
        }

        statement.childrenAccept(this, data);

        return data;
    }

    public Object visit(ASTSwitchLabel switchLabel, Object data) {
        if (data instanceof SwitchDensity) {
            ((SwitchDensity) data).addSwitchLabel();
        }

        switchLabel.childrenAccept(this, data);
        return data;
    }
}
