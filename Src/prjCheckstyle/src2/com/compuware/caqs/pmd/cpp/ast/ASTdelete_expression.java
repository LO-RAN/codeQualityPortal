package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTdelete_expression.java */

public class ASTdelete_expression extends SimpleNode {
  public ASTdelete_expression(int id) {
    super(id);
  }

  public ASTdelete_expression(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
