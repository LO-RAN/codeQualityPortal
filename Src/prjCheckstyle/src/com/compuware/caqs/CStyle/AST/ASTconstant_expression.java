package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTconstant_expression.java */

public class ASTconstant_expression extends SimpleNode {
  public ASTconstant_expression(int id) {
    super(id);
  }

  public ASTconstant_expression(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
