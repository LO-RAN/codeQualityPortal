/* Generated By:JJTree: Do not edit this line. ASTDirection.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTDirection extends SimpleNode {
  public ASTDirection(int id) {
    super(id);
  }

  public ASTDirection(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=694f15104715c372a2f01491e095eef5 (do not edit this line) */
