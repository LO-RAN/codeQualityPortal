/* Generated By:JJTree: Do not edit this line. ASTCommentLine.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTCommentLine extends SimpleNode {
  public ASTCommentLine(int id) {
    super(id);
  }

  public ASTCommentLine(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=896e7323ca64c34645f6d6356292b668 (do not edit this line) */