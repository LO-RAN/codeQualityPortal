package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTcompound_statement.java */

public class ASTcompound_statement extends SimpleNode {
  public ASTcompound_statement(int id) {
    super(id);
  }

  public ASTcompound_statement(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
