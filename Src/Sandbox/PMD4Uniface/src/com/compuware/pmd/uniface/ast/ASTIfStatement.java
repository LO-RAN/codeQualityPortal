/* Generated By:JJTree: Do not edit this line. ASTIfStatement.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTIfStatement extends SimpleNode {
  public ASTIfStatement(int id) {
    super(id);
  }

  public ASTIfStatement(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=911bcc4344a4e58d161618629ddf1c7a (do not edit this line) */