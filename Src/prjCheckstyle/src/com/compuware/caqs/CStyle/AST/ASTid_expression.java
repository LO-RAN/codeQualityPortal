package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTid_expression.java */

public class ASTid_expression extends SimpleNode {
  public ASTid_expression(int id) {
    super(id);
  }

  public ASTid_expression(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
