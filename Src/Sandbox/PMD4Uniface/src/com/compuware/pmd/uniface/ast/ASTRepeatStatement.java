/* Generated By:JJTree: Do not edit this line. ASTRepeatStatement.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTRepeatStatement extends SimpleNode {
  public ASTRepeatStatement(int id) {
    super(id);
  }

  public ASTRepeatStatement(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ce42102452417b7767ab22e489056aeb (do not edit this line) */