package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTadditive_expression.java */

public class ASTadditive_expression extends SimpleNode {
  public ASTadditive_expression(int id) {
    super(id);
  }

  public ASTadditive_expression(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}