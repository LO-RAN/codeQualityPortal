/* Generated By:JJTree: Do not edit this line. ASTIndirection.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTIndirection extends SimpleNode {
  public ASTIndirection(int id) {
    super(id);
  }

  public ASTIndirection(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7f6c2ca97b20305d2922ec7fa9688103 (do not edit this line) */
