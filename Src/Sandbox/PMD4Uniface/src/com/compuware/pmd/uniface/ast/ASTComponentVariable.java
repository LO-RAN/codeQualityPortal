/* Generated By:JJTree: Do not edit this line. ASTComponentVariable.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTComponentVariable extends SimpleNode {
  public ASTComponentVariable(int id) {
    super(id);
  }

  public ASTComponentVariable(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=b74fe6b5a6d199957c3befcbbda2f0be (do not edit this line) */
