/* Generated By:JJTree: Do not edit this line. ASTStripattributes.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTStripattributes extends SimpleNode {
  public ASTStripattributes(int id) {
    super(id);
  }

  public ASTStripattributes(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=49fcf0a9747451b7894458cd6fb199ed (do not edit this line) */
