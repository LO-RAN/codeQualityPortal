/* Generated By:JJTree: Do not edit this line. ASTClrmess.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTClrmess extends SimpleNode {
  public ASTClrmess(int id) {
    super(id);
  }

  public ASTClrmess(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=bb161eb46ac3d8bda0f4043dd9edf0fb (do not edit this line) */