package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTshift_expression.java */

public class ASTshift_expression extends SimpleNode {
  public ASTshift_expression(int id) {
    super(id);
  }

  public ASTshift_expression(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
