package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTinitializer.java */

public class ASTinitializer extends SimpleNode {
  public ASTinitializer(int id) {
    super(id);
  }

  public ASTinitializer(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
