package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTselection_statement.java */

public class ASTselection_statement extends SimpleNode {
  public ASTselection_statement(int id) {
    super(id);
  }

  public ASTselection_statement(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
