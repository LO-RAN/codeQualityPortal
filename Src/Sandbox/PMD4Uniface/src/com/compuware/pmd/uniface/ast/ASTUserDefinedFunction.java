/* Generated By:JJTree: Do not edit this line. ASTUserDefinedFunction.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTUserDefinedFunction extends SimpleNode {
  public ASTUserDefinedFunction(int id) {
    super(id);
  }

  public ASTUserDefinedFunction(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ed7bd1711e5cd658ccf48e102b409dfe (do not edit this line) */