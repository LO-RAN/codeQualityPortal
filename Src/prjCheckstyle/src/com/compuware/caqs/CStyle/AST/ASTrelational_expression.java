package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTrelational_expression.java */

public class ASTrelational_expression extends SimpleNode {
  public ASTrelational_expression(int id) {
    super(id);
  }

  public ASTrelational_expression(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}