/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class SimpleNode extends net.sourceforge.pmd.ast.SimpleNode implements Node {
 protected UnifaceParser parser;

    public SimpleNode(int i) {
        super(i);
    }

    public SimpleNode(UnifaceParser p, int i) {
        this(i);
        parser = p;
    }

    public void jjtOpen() {
        if (beginLine == -1 && parser.token.next != null) {
            beginLine = parser.token.next.beginLine;
            beginColumn = parser.token.next.beginColumn;
        }
    }

    public void jjtClose() {
        if (beginLine == -1 && (children == null || children.length == 0)) {
            beginColumn = parser.token.beginColumn;
        }
        if (beginLine == -1) {
            beginLine = parser.token.beginLine;
        }
        endLine = parser.token.endLine;
        endColumn = parser.token.endColumn;
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /**
     * Accept the visitor. *
     */
    public Object childrenAccept(UnifaceParserVisitor visitor, Object data) {
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                ((Node) children[i]).jjtAccept(visitor, data);
            }
        }
        return data;
    }

    public String toString() {
        return UnifaceParserTreeConstants.jjtNodeName[id];
    }

   
}

/* JavaCC - OriginalChecksum=d252c5f9e0d14081564c17cec47f3813 (do not edit this line) */