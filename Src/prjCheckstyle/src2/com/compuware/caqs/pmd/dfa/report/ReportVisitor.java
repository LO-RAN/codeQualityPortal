package com.compuware.caqs.pmd.dfa.report;

public abstract class ReportVisitor {

    public void visit(AbstractReportNode node) {
        node.childrenAccept(this);
    }

}
