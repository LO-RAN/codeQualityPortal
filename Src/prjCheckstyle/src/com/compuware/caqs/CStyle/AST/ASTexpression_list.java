package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTexpression_list.java */

public class ASTexpression_list extends SimpleNode {
  public ASTexpression_list(int id) {
    super(id);
  }

  public ASTexpression_list(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
