/* Generated By:JJTree: Do not edit this line. ASTRelationalExpression.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTRelationalExpression extends SimpleNode {
  public ASTRelationalExpression(int id) {
    super(id);
  }

  public ASTRelationalExpression(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2c7fe548b2a36e989c2b9d7266135e8d (do not edit this line) */
