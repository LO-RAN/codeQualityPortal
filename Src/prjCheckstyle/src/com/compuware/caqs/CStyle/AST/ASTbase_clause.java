package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTbase_clause.java */

public class ASTbase_clause extends SimpleNode {
  public ASTbase_clause(int id) {
    super(id);
  }

  public ASTbase_clause(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
