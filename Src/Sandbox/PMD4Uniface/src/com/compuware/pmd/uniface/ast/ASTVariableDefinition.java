/* Generated By:JJTree: Do not edit this line. ASTVariableDefinition.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

public class ASTVariableDefinition extends SimpleNode {
  public ASTVariableDefinition(int id) {
    super(id);
  }

  public ASTVariableDefinition(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=10946dc9a1ce205613980adc72164d41 (do not edit this line) */
