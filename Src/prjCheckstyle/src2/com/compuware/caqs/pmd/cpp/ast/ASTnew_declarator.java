package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTnew_declarator.java */

public class ASTnew_declarator extends SimpleNode {
  public ASTnew_declarator(int id) {
    super(id);
  }

  public ASTnew_declarator(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
