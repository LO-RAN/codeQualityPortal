/* Generated By:JJTree: Do not edit this line. ASTNewinstance.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTNewinstance extends SimpleNode {
  public ASTNewinstance(int id) {
    super(id);
  }

  public ASTNewinstance(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=b40f06dae860946082735c44c21f4f71 (do not edit this line) */
