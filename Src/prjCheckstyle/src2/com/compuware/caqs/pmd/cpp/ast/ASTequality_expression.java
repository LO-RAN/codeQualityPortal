package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTequality_expression.java */

public class ASTequality_expression extends SimpleNode {
  public ASTequality_expression(int id) {
    super(id);
  }

  public ASTequality_expression(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
