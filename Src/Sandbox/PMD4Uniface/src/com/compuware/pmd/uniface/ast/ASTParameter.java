/* Generated By:JJTree: Do not edit this line. ASTParameter.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTParameter extends SimpleNode {
  public ASTParameter(int id) {
    super(id);
  }

  public ASTParameter(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=de8f0af635e137e36a4907e88c5245b1 (do not edit this line) */
