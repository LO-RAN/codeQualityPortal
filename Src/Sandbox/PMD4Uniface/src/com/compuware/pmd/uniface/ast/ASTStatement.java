/* Generated By:JJTree: Do not edit this line. ASTStatement.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTStatement extends SimpleNode {
  public ASTStatement(int id) {
    super(id);
  }

  public ASTStatement(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=0d2c302ef9dac01e813b9f0eb3358509 (do not edit this line) */
