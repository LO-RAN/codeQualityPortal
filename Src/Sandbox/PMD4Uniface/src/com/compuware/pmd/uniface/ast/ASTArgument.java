/* Generated By:JJTree: Do not edit this line. ASTArgument.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTArgument extends SimpleNode {
  public ASTArgument(int id) {
    super(id);
  }

  public ASTArgument(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=dc7ee949cbc979c14e6b70aef7acb9a7 (do not edit this line) */