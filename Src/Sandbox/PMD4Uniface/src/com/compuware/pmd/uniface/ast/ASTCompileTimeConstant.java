/* Generated By:JJTree: Do not edit this line. ASTCompileTimeConstant.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTCompileTimeConstant extends SimpleNode {
  public ASTCompileTimeConstant(int id) {
    super(id);
  }

  public ASTCompileTimeConstant(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=29098bb6feaf028946132f0f1f92d7f0 (do not edit this line) */
