/* Generated By:JJTree: Do not edit this line. ASTDatatype.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTDatatype extends SimpleNode {
  public ASTDatatype(int id) {
    super(id);
  }

  public ASTDatatype(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3e1f44ccbe3aa419dd9e42c07dbc6b09 (do not edit this line) */
