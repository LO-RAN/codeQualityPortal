package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTexpression.java */

public class ASTexpression extends SimpleNode {
  public ASTexpression(int id) {
    super(id);
  }

  public ASTexpression(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}