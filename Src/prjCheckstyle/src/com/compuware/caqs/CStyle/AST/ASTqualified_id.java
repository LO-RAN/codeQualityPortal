package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTqualified_id.java */

public class ASTqualified_id extends SimpleNode {
  public ASTqualified_id(int id) {
    super(id);
  }

  public ASTqualified_id(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
