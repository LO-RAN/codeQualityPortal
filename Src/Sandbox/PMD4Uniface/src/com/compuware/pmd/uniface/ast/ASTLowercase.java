/* Generated By:JJTree: Do not edit this line. ASTLowercase.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTLowercase extends SimpleNode {
  public ASTLowercase(int id) {
    super(id);
  }

  public ASTLowercase(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=fad9494140c51cd19ed03e8266a22efd (do not edit this line) */
